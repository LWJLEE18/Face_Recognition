package com.example.academic_festival;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.Nullable;

public class Smartdoor_register extends Fragment {

    private FirebaseAuth mAuth;

    View register_layout;

    Smartdoormain smartdoormain;
    Button register_btn;

    EditText register_ID, register_PW, register_PWrecon;
    ImageView signin1_prev;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        smartdoormain = (Smartdoormain) getActivity();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        smartdoormain = null;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.smartdoor_register, container, false);
        register_btn = view.findViewById(R.id.register_btn1);
        signin1_prev = view.findViewById(R.id.signin1_prev);
        register_ID = view.findViewById(R.id.register_ID);
        register_PW = view.findViewById(R.id.register_PW);
        register_PWrecon = view.findViewById(R.id.register_PWrecon);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidekeyboard();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
                // smartdoormain.fragmentChange(2);
            }
        });

        signin1_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smartdoormain.fragmentChange(0);
            }
        });

        return view;
    }

    public void register() {
        String id = register_ID.getText().toString();
        String pw = register_PW.getText().toString();
        String pwrecon = register_PWrecon.getText().toString();
        if (id.length() > 0 && pw.length() > 0) {
            if (pwrecon.equals(pw)) {
                mAuth.createUserWithEmailAndPassword(id, pw)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getActivity(),"success",Toast.LENGTH_SHORT).show();
                                    smartdoormain.fragmentChange(4);
                                } else {
                                    if (task.getException().toString() != null){
                                        Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
            else {
                Toast.makeText(getActivity(),"비밀번호가 일치하지 않습니다 ",Toast.LENGTH_SHORT).show();
              }
        }
        else {
            Toast.makeText(getActivity(),"아이디와 비밀번호가 빈칸입니다",Toast.LENGTH_SHORT).show();
        }

    }
    private void hidekeyboard(){
        if (getActivity() !=null & getActivity().getCurrentFocus()!=null){
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
