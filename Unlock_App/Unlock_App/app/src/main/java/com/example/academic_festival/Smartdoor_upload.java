package com.example.academic_festival;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Smartdoor_upload extends Fragment {
    private DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference();
    Smartdoormain2 smartdoormain2;
    Button uploadv;
    ProgressDialog progressDialog;
    EditText editText;
    TextView knowusername;
    Uri videouri;


    Map<Integer,String> username = new HashMap<>();
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        smartdoormain2 = (Smartdoormain2) getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        smartdoormain2 = null;
    }
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.record, container, false);
        editText = view.findViewById(R.id.uploadname);
        uploadv = view.findViewById(R.id.uploadv);



        uploadv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText() != null){
                    progressDialog = new ProgressDialog(getContext());
                    choosevideo();
                    databaseReference2.child("newusername").setValue(editText.getText().toString());
                }else {
                    Toast.makeText(getContext(),"파일 이름이 작성되지 않았습니다",Toast.LENGTH_SHORT);
                }
            }
        });

        return view;
    }



    private void choosevideo() {

        Intent intent = new Intent();

        intent.setType("video/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, 5);

    }

    public void checkSelfPermission() {

        String temp = "";

        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += android.Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }

        //파일 쓰기 권한 확인
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += android.Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }


        if (TextUtils.isEmpty(temp) == false) {
            // 권한 요청
            ActivityCompat.requestPermissions(getActivity(), temp.trim().split(" "),1);
        }else {
            // 모두 허용 상태
            Toast.makeText(getContext(), "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }
    }
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            videouri = data.getData();

            progressDialog.setTitle("업로드중 ...");

            progressDialog.show();

            uploadvideo();

        }

    }
    private String getfiletype(Uri videouri) {

        ContentResolver r = getContext().getContentResolver();

        // get the file type ,in this case its mp4

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(r.getType(videouri));

    }



    private void uploadvideo() {

        if (videouri != null) {

            // save the selected video in Firebase storage

            final StorageReference reference = FirebaseStorage.getInstance().getReference("newuser/" + editText.getText() + "." + getfiletype(videouri));

            reference.putFile(videouri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override

                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                    while (!uriTask.isSuccessful()) ;

                    // get the link of video

                    progressDialog.dismiss();

                    Toast.makeText(getContext(), "업로드 완료!!", Toast.LENGTH_SHORT).show();

                }

            }).addOnFailureListener(new OnFailureListener() {

                @Override

                public void onFailure(@NonNull Exception e) {

                    // Error, Image not uploaded

                    progressDialog.dismiss();

                    Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                // Progress Listener for loading

                // percentage on the dialog box

                @Override

                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    // show the progress bar

                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                    progressDialog.setMessage("Uploaded " + (int) progress + "%");

                }

            });

        }

    }
}
