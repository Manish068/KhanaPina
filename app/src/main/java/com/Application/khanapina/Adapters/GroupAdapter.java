package com.Application.khanapina.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Application.khanapina.ModelClass.ItemGroup;
import com.Application.khanapina.ModelClass.Menu_item;
import com.Application.khanapina.R;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    //Initialize Activity and arraylist

    private List<ItemGroup> arrayListGroup;
    private Context context;

    public GroupAdapter(Context context, List<ItemGroup> arrayListGroup) {
        this.context = context;
        this.arrayListGroup = arrayListGroup;
    }


    @NonNull
    @Override
    public GroupAdapter.GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cousine_activity, parent, false);
        return new GroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupAdapter.GroupViewHolder holder, int position) {
        holder.cousine_text.setText(arrayListGroup.get(position).getKey());

        //Set group name on Textview

        //String name=holder.cousine_text.getText().toString();
        Log.d("database", "onBindViewHolder: " + arrayListGroup.size());


        //Initialize member ArrayList
        List<Menu_item> menu_items = arrayListGroup.get(position).getMenu();

        //Initialize LayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        //set Adapter and layout manager
        ItemAdapter itemAdapter = new ItemAdapter(menu_items, context);
        holder.rv_items.setHasFixedSize(true);
        holder.rv_items.setLayoutManager(linearLayoutManager);
        holder.rv_items.setNestedScrollingEnabled(false);
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
