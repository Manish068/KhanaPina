package com.Application.khanapina.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Application.khanapina.R;
import com.Application.khanapina.ModelClass.Restaurant_info;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Restaurant_RecyclerView extends RecyclerView.Adapter<Restaurant_RecyclerView.ResturantView_holder> {

    private Context mcontext;
    private ArrayList<Restaurant_info> mrestaurant_info;
    private View v;

    public Restaurant_RecyclerView(Context mcontext, ArrayList<Restaurant_info> mrestaurant_info) {
        this.mcontext = mcontext;
        this.mrestaurant_info = mrestaurant_info;
    }

    @NonNull
    @Override
    public ResturantView_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v= LayoutInflater.from(mcontext).inflate(R.layout.restaurant_card_view,parent,false);
        return new ResturantView_holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResturantView_holder holder, int position) {
        holder.tv_name.setText(mrestaurant_info.get(position).getName());
        holder.tv_cuisines.setText(mrestaurant_info.get(position).getCuisines());
        holder.tv_aggregate_rating.setText(mrestaurant_info.get(position).getAggregate_rating());
        holder.tv_rating_text.setText(mrestaurant_info.get(position).getRating_text());
        holder.tv_votes.setText(mrestaurant_info.get(position).getVotes());
        holder.tv_aggregate_rating.setTextColor(Color.parseColor("#"+mrestaurant_info.get(position).getRating_color()));

        Picasso.get().load(mrestaurant_info.get(position).getPhotos_url()).into(holder.restaurant_pic);
    }

    @Override
    public int getItemCount() {
        return mrestaurant_info.size();
    }


    public static class ResturantView_holder extends RecyclerView.ViewHolder{

        ImageView restaurant_pic;
        TextView tv_name;
        TextView tv_cuisines;
        TextView tv_aggregate_rating;
        TextView tv_rating_text;
        TextView tv_votes;



        public ResturantView_holder(@NonNull View itemView) {
            super(itemView);
            restaurant_pic=itemView.findViewById(R.id.resturant_image);
            tv_name=itemView.findViewById(R.id.resturant_name);
            tv_cuisines=itemView.findViewById(R.id.cousines);
            tv_aggregate_rating=itemView.findViewById(R.id.rating_number);
            tv_rating_text=itemView.findViewById(R.id.rating_text);
            tv_votes=itemView.findViewById(R.id.votes_count);

        }
    }
}
