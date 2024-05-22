package com.example.academic_festival;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    private TextView dialog_text;
    private TextView dialog_close;
    private ImageView dialog_img;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    static BottomSheetFragment newInstance(){
        return new BottomSheetFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShowsDialog(true);

    }
    public void onStart(){
        super.onStart();
        try {
            //bottomsheetfragment의 여백이없도록  deviceSize를 가져와서 설정해줌
            WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point deviceSize = new Point();
            display.getSize(deviceSize);
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = deviceSize.x;
            params.horizontalMargin = 0.0f;
            getDialog().getWindow().setAttributes(params);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("RestrictedApi")
    public void setupDialog(Dialog dialog, int style){
        super.setupDialog(dialog,style);
        // 바깥부분 터지할때와 뒤로가기 버튼을 누르면 창이 닫히지 않도록 막음
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        // dialog 바깥부분의 밝기를 50%로 설정
        dialog.getWindow().setDimAmount(0.5f);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.smartdoor_pop,container,false);
        dialog_text = v.findViewById(R.id.pop_date);
        dialog_close = v.findViewById(R.id.pop_close);
        dialog_img = v.findViewById(R.id.pop_img);


        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 창을 닫고 fragment를 삭제
                dismiss();
                // 닫기 버튼을 누를경우 DB의 Unknown의 값을 0(감지되지않음)으로 변경하여 지속적으로 실행되는 것을 막음
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference();
                databaseReference.child("Unknown").setValue(0);
            }
        });
        databaseReference.child("Unknown_img").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 감지된 날짜, 시간을 알려주기위해 DB에서 "Unknown_img"값을 받아옴
                String text = snapshot.getValue(String.class);
                // 중복 파일명을 막기위해서 초단의 까지 저장하므로 초단위부분은 버림
                String text2 = text.substring(0,12);
                // EX) "20231121100" 년 월 일 시간 분 으로 나누기위해 StringBuffer함수를 사용
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(text2);
                stringBuffer.insert(4,"/");
                stringBuffer.insert(7,"/");
                stringBuffer.insert(10," ");
                stringBuffer.insert(13,"시");
                stringBuffer.insert(16,"분");
                //stringBuffer.insert()
                dialog_text.setText(stringBuffer);
                // FirebaseStorage에 접근
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                // 위에서 가져온 "Unknown_ing"를 활용하여 photo디렉토리 밑에 사진을 받아옴
                StorageReference fileRef = storageRef.child("photo/"+text+".jpeg");
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //glide를 사용하여 사진을 받아옴과 동시에 dialog_img ImageView에 입력
                        Glide.with(getActivity()).load(uri).into(dialog_img);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return v;
    }


    /*public Smartdoor_CustomDialog(){

    }
    public static Smartdoor_CustomDialog getInstance(){
        Smartdoor_CustomDialog smartdoorCustomDialog = new Smartdoor_CustomDialog();
        return smartdoorCustomDialog;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.smartdoor_pop, container, false);
        Bundle mArgs = getArguments();
        String mValue = mArgs.getString("key");

        dialog_text = view.findViewById(R.id.pop_text);
        dialog_close = view.findViewById(R.id.pop_close);
        dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        dialog_text.setText(mValue);
        setCancelable(false);

        return view;
    }*/
}
