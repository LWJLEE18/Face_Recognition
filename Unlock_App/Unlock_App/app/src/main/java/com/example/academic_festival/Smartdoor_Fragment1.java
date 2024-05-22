package com.example.academic_festival;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Smartdoor_Fragment1 extends Fragment {

    TimeZone timeZone;
    DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA) {};

    ImageView doorcircle ;
    TextView doorcircletext ;

    int doorstatus;
    Smartdoormain2 smartdoormain2;
    LottieAnimationView lottieAnimationView, lottieAnimationView2;




    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();



    Date date = new Date();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        smartdoormain2 = (Smartdoormain2) getActivity();
        doorstatus_test();

    }
    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        smartdoormain2 = null;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.smartdoor_fragment1, container, false);

        doorcircle = view.findViewById(R.id.doorcircle);
        doorcircletext = view.findViewById(R.id.doortext);
        lottieAnimationView = view.findViewById(R.id.lottieAnimationView);
        lottieAnimationView2 = view.findViewById(R.id.lottieAnimation_loading);
        lottieAnimationView2.setVisibility(View.GONE);
        doorcircle.bringToFront();
        doorcircletext.bringToFront();


        doorcircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doorstatus_loading();
            }
        });
        return view;
    }

    private void doorstatus_test(){
        databaseReference.child("Door Status").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long value = snapshot.getValue(Long.class);
                doorstatus = value.intValue();
                if (doorstatus == 1){
                    doorcircletext.setText("잠금되었습니다");
                }
                else if (doorstatus == 0){
                    doorcircletext.setText("해제되었습니다");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }
    private void doorstatus_loading(){

            lottieAnimationView.setVisibility(View.GONE);
            lottieAnimationView2.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    timeZone = TimeZone.getTimeZone("Asia/Seoul");
                    dateFormat.setTimeZone(timeZone);
                    databaseReference.child("app_Btn").setValue(1);
                    UserData.Commet userData = new UserData.Commet();
                    String data = dateFormat.format(date);
                    String time = getTime();
                    //int doorstatus = 0;
                    userData.date = data;
                    userData.time = time;
                    userData.doorstatus = doorstatus;
                    databaseReference.child("time").push().setValue(userData);
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    lottieAnimationView2.setVisibility(View.GONE);
                    }
                },3000);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    databaseReference.child("app_Btn").setValue(0);
                }
            },1000);



        } /*else if (doorstatus == 0){
            doorcircletext.setText("잠금중");
            lottieAnimationView.setVisibility(View.GONE);
            lottieAnimationView2.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    timeZone = TimeZone.getTimeZone("Asia/Seoul");
                    dateFormat.setTimeZone(timeZone);
                    databaseReference.child("Door Status").setValue(1);
                    UserData.Commet userData = new UserData.Commet();
                    String data = dateFormat.format(date);
                    String time = getTime();
                    //int doorstatus = 1;
                    userData.date = data;
                    userData.time = time;
                    userData.doorstatus = doorstatus;
                    databaseReference.child("time").push().setValue(userData);
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    lottieAnimationView2.setVisibility(View.GONE);
                }
                },3000);
                }
                */




    private String getTime() {
        Date mDate;
        TimeZone timeZone;
        DateFormat dateFormat= new SimpleDateFormat("kk:mm ss", Locale.KOREAN) {
        };
        timeZone = TimeZone.getTimeZone("Asia/Seoul");
        dateFormat.setTimeZone(timeZone);
        mDate = new Date();
        return dateFormat.format(mDate);
    }




}
