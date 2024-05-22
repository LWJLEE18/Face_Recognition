package com.example.academic_festival;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Nullable;

public class Smartdoor_info extends Fragment {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    Smartdoormain2 smartdoormain2;
    NavigationBarView navigationBarView;
    TextView info_username1,doorstatus, useradd;
    FirebaseUser info_usernamestr;
    String string;

    int doorstatus_int;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        smartdoormain2 = (Smartdoormain2) getActivity();

    }
    @Override
    public void onDetach() {
        super.onDetach();
        smartdoormain2 = null;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.smartdoor_info, container, false);
        navigationBarView = view.findViewById(R.id.bottom_navi);
        info_username1 = view.findViewById(R.id.info_username1);
        doorstatus = view.findViewById(R.id.doorstatus);
        useradd = view.findViewById(R.id.useraddbtn);

        info_username1.setText(ID());
        info_username1.setOnClickListener(new View.OnClickListener() {
            //사용자 관리 창으로 이동
            @Override
            public void onClick(View v) {
                smartdoormain2.fragmentChange(3);
            }
        });

        useradd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smartdoormain2.fragmentChange(7);
            }
        });



        databaseReference.child("Door Status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long value = snapshot.getValue(Long.class);
                doorstatus_int = value.intValue();
                if (doorstatus_int == 1){
                    doorstatus.setText("문닫힘");
                } else if (doorstatus_int == 0){
                    doorstatus.setText("문열림");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        doorstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smartdoormain2.fragmentChange(5);
            }
        });
        return view;
    }

    //현재 로그인된 아이디를  FirebaseAuth를 사용하여 받아옴
    private String ID(){
        info_usernamestr = FirebaseAuth.getInstance().getCurrentUser();
        string=info_usernamestr.getEmail();
        //이메일 형식으로 되어있으니 @ 문자 이전까지만 출력
        string=string.substring(0,string.indexOf("@"));
        return string;
    }


}
