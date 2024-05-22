package com.example.academic_festival;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Smartdoor_timelineAdapter extends RecyclerView.Adapter<Smartdoor_timelineAdapter.Smartdoor_timelineViweHolder>{

    ArrayList<UserData.Commet> userDatalist;

    public Smartdoor_timelineAdapter(){
        userDatalist = new ArrayList<>();
        getDate();

    }

    @NonNull
    @Override
    public Smartdoor_timelineViweHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()) // 날짜
                .inflate(R.layout.timeline_itemcard, parent, false);

        return new Smartdoor_timelineViweHolder(itemview);

    }

    @Override
    public void onBindViewHolder(@NonNull Smartdoor_timelineViweHolder holder, int position) {


        holder.itemcard_date.setText(userDatalist.get(position).date);
        holder.itemcard_time.setText(userDatalist.get(position).time);
        if (userDatalist.get(position).doorstatus == 1){
            holder.itemcard_status.setText("문 잠김");
            holder.itemcard_view.setBackgroundColor(Color.parseColor("#ec5353"));
        }else {
            holder.itemcard_status.setText("문 열림");
            holder.itemcard_view.setBackgroundColor(Color.parseColor("#0067a3"));
        }


    }

    @Override
    public int getItemCount() {
        return userDatalist.size();
    }



    class Smartdoor_timelineViweHolder extends RecyclerView.ViewHolder {

        TextView itemcard_date;
        TextView itemcard_time;
        TextView itemcard_status;
        ImageView itemcard_view;



        public Smartdoor_timelineViweHolder(View itemview) {
            super(itemview);
            itemcard_view = itemview.findViewById(R.id.timeline_itemcardview);
            itemcard_date = itemview.findViewById(R.id.timeline_itemcarddate);
            itemcard_time = itemview.findViewById(R.id.timeline_itemcardtext);
            itemcard_status = itemview.findViewById(R.id.timeline_itemcardstatus);



        }
    }
    public void getDate(){

        FirebaseDatabase database;
        DatabaseReference databaseReference;
        database = FirebaseDatabase.getInstance();
        database.getReference("time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                userDatalist.clear();
                for (DataSnapshot snapshot : datasnapshot.getChildren()){
                    userDatalist.add(snapshot.getValue(UserData.Commet.class));
                }

                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
