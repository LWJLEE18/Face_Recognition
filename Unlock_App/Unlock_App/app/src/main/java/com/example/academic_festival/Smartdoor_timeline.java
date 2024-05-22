package com.example.academic_festival;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Smartdoor_timeline extends Fragment {
    Smartdoormain2 smartdoormain2;
    ImageView Prev;
    RecyclerView timeline;
    RecyclerView.Adapter adapter;
    List<UserData.Commet> userDatalist;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        smartdoormain2 = (Smartdoormain2) getActivity();

    }
    @Override
    public void onStart(){
        super.onStart();
        userDatalist = new ArrayList<>();
        getDatabase();
        adapter = new Smartdoor_timelineAdapter();
        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getActivity());
        timeline.setLayoutManager(mlayoutManager);
        timeline.setAdapter(adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        smartdoormain2 = null;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.smartdoor_info_timeline, container, false);
        Prev = view.findViewById(R.id.infotimeline_prev);
        timeline = view.findViewById(R.id.timeline_recycle);



        timeline.setHasFixedSize(true);

        Prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smartdoormain2.fragmentChange(2);}
        });

        return view;
    }
    void getDatabase(){
        FirebaseDatabase.getInstance().getReference().child("time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userDatalist.clear();
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    userDatalist.add(item.getValue(UserData.Commet.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
