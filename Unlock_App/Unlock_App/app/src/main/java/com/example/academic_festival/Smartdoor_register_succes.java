package com.example.academic_festival;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.Nullable;

public class Smartdoor_register_succes extends Fragment {

    Smartdoormain smartdoormain;

    Button regist_succes_btn;

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
        View view = inflater.inflate(R.layout.smartdoor_register_succes, container, false);
        regist_succes_btn = view.findViewById(R.id.regist_succes_btn);

        regist_succes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smartdoormain.fragmentChange(0);
            }
        });
        return view;
    }

}
