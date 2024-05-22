package com.example.academic_festival;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Nullable;

public class Smartdoor_signin1 extends Fragment {

    TextView register_btn,signin_logo,signin_logo2,textView4,textView2;
    Button signin_loginbtn;
    Smartdoormain smartdoormain;
    Smartdoormain2 smartdoormain2;
    EditText signin_ID,signin_PW;
    CheckBox checkBox;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    LinearLayout design1,design2;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        smartdoormain = (Smartdoormain) getActivity();
        FirebaseApp.initializeApp(getActivity());

    }
    @Override
    public void onDetach() {
        super.onDetach();
        smartdoormain = null;
    }
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.smartdoor_signin, container, false);
        register_btn = view.findViewById(R.id.register_btn);
        signin_loginbtn = view.findViewById(R.id.signin_loginbtn);
        signin_logo = view.findViewById(R.id.register_logo);
        signin_logo2 = view.findViewById(R.id.register_logo2);
        textView2 = view.findViewById(R.id.textView2);
        textView4 = view.findViewById(R.id.textView4);
        signin_ID = view.findViewById(R.id.signin_ID);
        signin_PW = view.findViewById(R.id.signin_PW);
        checkBox = view.findViewById(R.id.checkBox);
        mAuth = FirebaseAuth.getInstance();
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //autoLogin();
            }
        });
        //autoLogin();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidekeyboard();
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smartdoormain.fragmentChange(1);
            }
        });

        signin_loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });

        return view;
    }
    public void signin(){
        String id = signin_ID.getText().toString();
        String pw = signin_PW.getText().toString();
        if (id.length()>0 && pw.length()>0){
            mAuth.signInWithEmailAndPassword(id,pw)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getActivity(), "로그인에 성공했습니다", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), Smartdoormain2.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(getActivity(),"로그인에 실패하였습니다",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }else {

        }
    }
    public void autoLogin(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()){
                    String idToken = task.getResult().getToken();
                    Intent intent = new Intent(getActivity(), Smartdoormain2.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else task.isCanceled();

            }
        });
    }

    private void hidekeyboard(){
        if (getActivity() !=null & getActivity().getCurrentFocus()!=null){
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }



}
