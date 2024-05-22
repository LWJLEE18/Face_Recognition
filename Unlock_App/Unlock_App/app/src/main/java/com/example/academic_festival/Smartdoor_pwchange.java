package com.example.academic_festival;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.Nullable;

public class Smartdoor_pwchange extends Fragment {

    Smartdoormain smartdoormain;

    private FirebaseAuth mAuth;
    ImageView pwchange_prev;

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
        View view = inflater.inflate(R.layout.smartdoor_passwordchange, container, false);
        pwchange_prev = view.findViewById(R.id.pwchange_prev);
        mAuth = FirebaseAuth.getInstance();



        pwchange_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smartdoormain.fragmentChange(6);
            }
        });
        return view;
    }
}
