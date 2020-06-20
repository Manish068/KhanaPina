package com.Application.khanapina.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Application.khanapina.R;
import com.Application.khanapina.ModelClass.Restaurant_info;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Restaurant_RecyclerView extends RecyclerView.Adapter<Restaurant_RecyclerView.ResturantView_holder> {

    private Context mcontext;
    private ArrayList<Restaurant_info> mrestaurant_info;
    private View v;
    private ResturantView_holder.OnNoteListener mOnNoteListner;


    public Restaurant_RecyclerView(Context mcontext, ArrayList<Restaurant_info> mrestaurant_info, ResturantView_holder.OnNoteListener mOnNoteListner) {
        this.mcontext = mcontext;
        this.mrestaurant_info = mrestaurant_info;
        this.mOnNoteListner = mOnNoteListner;
    }

    @NonNull
    @Override
    public ResturantView_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(mcontext).inflate(R.layout.restaurant_card_view, parent, false);
        return new ResturantView_holder(v, mOnNoteListner);
    }

    @Override
    public void onBindViewHolder(@NonNull ResturantView_holder holder, int position) {
        //set Animation
        holder.restaurant_pic.setAnimation(AnimationUtils.loadAnimation(mcontext, R.anim.fade_transition_animation));


        holder.container.setAnimation(AnimationUtils.loadAnimation(mcontext, R.anim.fade_scale_animation));

        holder.tv_name.setText(mrestaurant_info.get(position).getName());
        holder.tv_cuisines.setText(mrestaurant_info.get(position).getCuisines());
        holder.tv_aggregate_rating.setText(mrestaurant_info.get(position).getAggregate_rating());
        holder.tv_rating_text.setText(mrestaurant_info.get(position).getRating_text());
        holder.tv_votes.setText(mrestaurant_info.get(position).getVotes());
        holder.tv_aggregate_rating.setTextColor(Color.parseColor("#" + mrestaurant_info.get(position).getRating_color()));

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

        OnNoteListener mOnNoteListener;

        LinearLayout container;


        public ResturantView_holder(@NonNull View itemView, final OnNoteListener mOnNoteListener) {
            super(itemView);
            restaurant_pic = itemView.findViewById(R.id.resturant_image);
            tv_name = itemView.findViewById(R.id.resturant_name);
            tv_cuisines = itemView.findViewById(R.id.cousines);
            tv_aggregate_rating = itemView.findViewById(R.id.rating_number);
            tv_rating_text = itemView.findViewById(R.id.rating_text);
            tv_votes = itemView.findViewById(R.id.votes_count);
            container = itemView.findViewById(R.id.container);
            this.mOnNoteListener = mOnNoteListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOnNoteListener.onNoteClick(getAdapterPosition());

                }
            });

        }

        public interface OnNoteListener {
            void onNoteClick(int position);
        }
    }
}
