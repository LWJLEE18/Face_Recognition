package com.example.academic_festival;

import android.app.ProgressDialog;

import android.content.ContentResolver;

import android.content.Intent;

import android.content.pm.PackageManager;
import android.net.Uri;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.webkit.MimeTypeMap;

import android.widget.Button;

import android.widget.EditText;
import android.widget.Toast;



import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.academic_festival.R;
import com.google.android.gms.tasks.OnFailureListener;

import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FirebaseStorage;

import com.google.firebase.storage.OnProgressListener;

import com.google.firebase.storage.StorageReference;

import com.google.firebase.storage.UploadTask;



import java.util.HashMap;
import java.util.List;


public class Record extends AppCompatActivity {



    Button uploadv;

    ProgressDialog progressDialog;
    EditText editText;

    private DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference();



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.record);
        editText = findViewById(R.id.uploadname);



        // initialise layout

        uploadv = findViewById(R.id.uploadv);

        uploadv.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                checkSelfPermission();
                // Code for showing progressDialog while uploading
                if (editText.getText() != null){
                    progressDialog = new ProgressDialog(Record.this);
                    choosevideo();
                    databaseReference2.child("newusername").setValue(editText.getText().toString());

                }else {
                    Toast.makeText(Record.this,"파일 이름이 작성되지 않았습니다",Toast.LENGTH_SHORT);
                }


            }

        });

    }



    // choose a video from phone storage

    private void choosevideo() {

        Intent intent = new Intent();

        intent.setType("video/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, 5);

    }
    public void checkSelfPermission() {

        String temp = "";

        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += android.Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }

        //파일 쓰기 권한 확인
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += android.Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }


        if (TextUtils.isEmpty(temp) == false) {
            // 권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "),1);
        }else {
            // 모두 허용 상태
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }
    }




    Uri videouri;
    // startActivityForResult is used to receive the result, which is the selected video.
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            videouri = data.getData();

            progressDialog.setTitle("Uploading...");

            progressDialog.show();

            uploadvideo();

        }

    }



    private String getfiletype(Uri videouri) {

        ContentResolver r = getContentResolver();

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

                    Toast.makeText(Record.this, "Video Uploaded!!", Toast.LENGTH_SHORT).show();

                }

            }).addOnFailureListener(new OnFailureListener() {

                @Override

                public void onFailure(@NonNull Exception e) {

                    // Error, Image not uploaded

                    progressDialog.dismiss();

                    Toast.makeText(Record.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

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