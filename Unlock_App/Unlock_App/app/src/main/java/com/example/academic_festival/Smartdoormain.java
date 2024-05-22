package com.example.academic_festival;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class Smartdoormain extends AppCompatActivity {

    private Smartdoor_signin1 smartdoor_signin1;
    private Smartdoor_register smartdoor_register;
    private Smartdoor_register_succes smartdoor_register_succes;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    public long backkeyPressedTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smartdoor);

        FragmentManager fragmentManager = getSupportFragmentManager();

        FirebaseApp.initializeApp(this);

        smartdoor_register = new Smartdoor_register();
        smartdoor_register_succes = new Smartdoor_register_succes();
        smartdoor_signin1 = new Smartdoor_signin1();
        fragmentChange(0);
        getToken();

        onBackPressed();
    }
    @Override
    public void onBackPressed(){
        if (System.currentTimeMillis() > backkeyPressedTime+2000) {
        backkeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this,"\'뒤로\' 버튼을 한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
            return;
        }
        if (System.currentTimeMillis() <= backkeyPressedTime+2000) {
        finish();
        }
    }
    public void fragmentChange(int index) {
        if (index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, smartdoor_signin1).commit();

        } else if (index == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, smartdoor_register).commit();

        } else if (index == 2){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main,smartdoor_register_succes).commit();

        }
    }
    public void getToken(){

    }
}
