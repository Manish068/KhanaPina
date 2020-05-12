package com.Application.khanapina;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DessertAdapter extends RecyclerView.Adapter<DessertAdapter.DessertViewHolder> {

    Context context;
    ArrayList<Dessert_item> dessert_items;

    public DessertAdapter(Context context, ArrayList<Dessert_item> dessert_items) {
        this.context = context;
        this.dessert_items = dessert_items;
    }

    @NonNull
    @Override
    public DessertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.dessert_itemsdetail, parent, false);
        DessertViewHolder dessertViewHolder = new DessertViewHolder(v);
        return dessertViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DessertViewHolder holder, int position) {
        holder.itemName.setText(dessert_items.get(position).getItem_name());
        Picasso.get().load(dessert_items.get(position).item_url).into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return dessert_items.size();
    }


    public class DessertViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        ImageView itemImage;

        public DessertViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName=itemView.findViewById(R.id.item_name);
            itemImage=itemView.findViewById(R.id.item_image);
        }
    }

}


