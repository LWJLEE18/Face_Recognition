package com.example.academic_festival;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
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

public class Smartdoor_Fragment2 extends Fragment {

    Smartdoormain2 smartdoormain2;
    Smartdoor_Fragment1 smartdoor_fragment1;

    ImageView doorcircle;
    TextView doortext;
    LottieAnimationView lottieAnimationView;
    String time;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    int doorstatus;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        smartdoormain2 = (Smartdoormain2) getActivity();
        doorstatus_test();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        smartdoormain2 = null;
    }

    public void onStart() {
        super.onStart();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TimeZone timeZone;
                DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA) {
                };
                timeZone = TimeZone.getTimeZone("Asia/Seoul");
                dateFormat.setTimeZone(timeZone);
                Date date = new Date();

                if (doorstatus == 1){
                    databaseReference.child("Door Status").setValue(0);
                    UserData.Commet userData = new UserData.Commet();
                    String data = dateFormat.format(date);
                    String time = getTime();
                    int doorstatus = 0;
                    userData.date = data;
                    userData.time = time;
                    userData.doorstatus = doorstatus;

                    databaseReference.child("time").push().setValue(userData);
                }
                else if(doorstatus == 0){
                    databaseReference.child("Door Status").setValue(1);
                    UserData.Commet userData = new UserData.Commet();
                    String data = dateFormat.format(date);
                    String time = getTime();
                    int doorstatus = 1;
                    userData.date = data;
                    userData.time = time;
                    userData.doorstatus = doorstatus;
                    databaseReference.child("time").push().setValue(userData);
                }
                smartdoormain2.fragmentChange(0);

            }
        },3000);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.smartdoor_fragment2,container, false);
        doorcircle = view.findViewById(R.id.doorcircle);
        doortext = view.findViewById(R.id.doortext);
        lottieAnimationView = view.findViewById(R.id.lottieAnimationView);

        doorcircle.bringToFront();
        doortext.bringToFront();
        return view;

    }
    private void doorstatus_test(){
        databaseReference.child("Door Status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long value = snapshot.getValue(Long.class);
                doorstatus = value.intValue();
                if (doorstatus == 1){
                    doortext.setText("해제중");
                } else if (doorstatus == 0){
                    doortext.setText("잠금중");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
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
