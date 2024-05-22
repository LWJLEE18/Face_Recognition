package com.example.academic_festival;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Nullable;

public class Smartdoor_info_id extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseUser info_usernamestr;
    String string,string1;
    Smartdoormain2 smartdoormain2;
    ImageView infoid_prev;
    TextView infoid_username, infoid_pw, infoid_delete;
    Button infoid_logout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        smartdoormain2 = (Smartdoormain2) getActivity();
        FirebaseApp.initializeApp(getActivity());

    }
    @Override
    public void onDetach() {
        super.onDetach();
        smartdoormain2 = null;
    }
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.smartdoor_info_id, container, false);
        infoid_prev = view.findViewById(R.id.infoid_prev);
        infoid_username = view.findViewById(R.id.infoid_username1);
        infoid_logout = view.findViewById(R.id.infoid_logout);
        infoid_pw = view.findViewById(R.id.infoid_pw);
        infoid_delete = view.findViewById(R.id.infoid_delet);

        mAuth = FirebaseAuth.getInstance();
        infoid_username.setText(ID());
        infoid_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smartdoormain2.fragmentChange(2);
            }
        });
        infoid_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), Smartdoormain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);;
                startActivity(intent);
            }
        });
        infoid_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("비밀번호를 변경하시겠습니까?\n비밀번호 변경을 수락하시면 가입하신 메일로\n비밀번호 변경 문자가 발송됩니다.");
                builder.setPositiveButton("아니오",null);
                builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.sendPasswordResetEmail(string)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        mAuth.signOut();
                                        smartdoormain2.fragmentChange(0);
                                        builder.setMessage("메일 발송에는 시간이 소요될 수도 있습니다");
                                        builder.setPositiveButton("닫기",null);
                                        builder.create().show();
                                    }
                                });
                    }
                });
                builder.create().show();

            }
        });

        infoid_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("정말로 회원 탈퇴를 하시겠습니까?");
                builder.setPositiveButton("아니오",null);
                builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.getCurrentUser().delete();
                        Intent intent = new Intent(getActivity(), Smartdoormain.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
                builder.create().show();

            }
        });

        return view;
    }
    private String ID(){
        info_usernamestr = FirebaseAuth.getInstance().getCurrentUser();
        string=info_usernamestr.getEmail();
        string1=string.substring(0,string.indexOf("@"));
        return string1;
    }

}
