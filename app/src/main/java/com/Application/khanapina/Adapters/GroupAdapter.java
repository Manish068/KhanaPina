package com.Application.khanapina.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Application.khanapina.R;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    //Initialize Activity and arraylist

    ArrayList<String> arrayListGroup;
    private Activity activity;

    public GroupAdapter(Activity activity, ArrayList<String> arrayListGroup) {
        this.activity = activity;
        this.arrayListGroup = arrayListGroup;
    }


    @NonNull
    @Override
    public GroupAdapter.GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cousine_activity, parent, false);
        return new GroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.GroupViewHolder holder, int position) {
        //Set group name on Textview
        holder.cousine_text.setText(arrayListGroup.get(position));


        //Initialize member ArrayList
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            arrayList.add("Member " + i);
        }

        ItemAdapter itemAdapter = new ItemAdapter(arrayList);
        //Initialize LayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        //set Adapter and layout manager
        holder.rv_items.setLayoutManager(linearLayoutManager);
        holder.rv_items.setAdapter(itemAdapter);

    }

    @Override
    public int getItemCount() {
        return arrayListGroup.size();
    }


    public class GroupViewHolder extends RecyclerView.ViewHolder {

        TextView cousine_text;
        RecyclerView rv_items;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);

            //Assign Variable
            cousine_text = itemView.findViewById(R.id.cousine_name);
            rv_items = itemView.findViewById(R.id.rv_items);

        }
    }
}
