package com.example.academic_festival;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Smartdoormain2 extends AppCompatActivity {
    private Smartdoor_Fragment1 smartdoor_fragment1 = new Smartdoor_Fragment1();
    private Smartdoor_Fragment2 smartdoor_fragment2 = new Smartdoor_Fragment2();
    private Smartdoor_info smartdoor_info = new Smartdoor_info();
    private Smartdoor_info_id smartdoor_info_id = new Smartdoor_info_id();
    private Smartdoor_pwchange smartdoor_pwchange = new Smartdoor_pwchange();
    private Smartdoor_timeline smartdoorTimeline = new Smartdoor_timeline();
    private Smartdoor_setting smartdoor_setting = new Smartdoor_setting();
    private Smartdoor_upload smartdoorUpload = new Smartdoor_upload();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private NavigationBarView navigationBarView;

    private static final String CHANNEL_ID = "LensUnlock";
    private static final String CHANNEL_ID_2 = "LensUnlock_2";
    private static final int notificationId = 0;
    private static final int notificationId_1 = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smartdoor_2);
        navigationBarView = findViewById(R.id.bottom_navi);
        navigationBarView.bringToFront();
        createNotificationChannel();
        notification();
        FirebaseApp.initializeApp(this);
        fragmentChange(0);
        navigationBar();
        //discovered();
        doorstatus_notification2();

    }

    public void fragmentChange(int index) {
        if (index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main2, smartdoor_fragment1).commit();}
        //else if (index == 1) {
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main2, smartdoor_fragment2).commit();}
        else if (index == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main2, smartdoor_info).commit();}
        else if (index == 3) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main2, smartdoor_info_id).commit();}
        else if (index == 4) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main2, smartdoor_pwchange).commit();}
        else if (index == 5) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main2, smartdoorTimeline).commit();}
        else if (index == 6) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main2, new Smartdoor_setting()).commit();}
        else if (index == 7 ){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main2,smartdoorUpload).commit();}
    }
    public void discovered() {
        //데이터 베이스의 Unknown값이 1로 변경 되었을때만 실행됨
        databaseReference.child("Unknown").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer Unknown = snapshot.getValue(Integer.class);
                //감지된 경우에만 실행
                if (Unknown == 1) {
                    BottomSheetFragment s = BottomSheetFragment.newInstance();
                    s.show(getSupportFragmentManager(), s.getTag());
                    Unkonwn_notification();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void navigationBar() {
        //bottomnavigation 설정 부
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        fragmentChange(0);
                        return true;
                    case R.id.setting:
                        fragmentChange(6);
                        return true;
                    case R.id.info:
                        //info fragment로 이동
                        fragmentChange(2);
                        return true;
                }
                return false;
            }
        });
    }
    public void notification() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("Unknown_notifi", true)) {
            discovered();
        }
    }
    @SuppressLint("MissingPermission")
    public void Unkonwn_notification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID_2)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("LensUnlock")
                .setContentText("모르는 사람이 감지되었습니다.");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId_1,builder.build());
    }
    @SuppressLint("MissingPermission")
    public void doorstatus_notification_close() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("LensUnlock")
                .setContentText("문이 닫혀있습니다.");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId,builder.build());
    }
    @SuppressLint("MissingPermission")
    public void doorstatus_notification_open() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Lens Unlock")
                .setContentText("문이 열려있습니다.");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId,builder.build());
    }
    public void doorstatus_notification2(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("doorstatus_notifi",true)){
            databaseReference.child("Door Status").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Integer status = snapshot.getValue(Integer.class);
                    if (status == 1) {
                        doorstatus_notification_close();
                    }
                    else if (status == 0) {
                        doorstatus_notification_open();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = getString(R.string.channel_name);
            CharSequence name2 = getString(R.string.channel_name2);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name,importance);
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID_2,name2,importance);
            channel.setDescription(description);
            channel1.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            notificationManager.createNotificationChannel(channel1);
        }
    }
}






