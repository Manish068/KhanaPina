package com.Application.khanapina.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.Application.khanapina.ModelClass.Banner;
import com.Application.khanapina.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder>{
    private List<Banner> slideritems;
    private ViewPager2 viewPager2;
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            slideritems.addAll(slideritems);
            notifyDataSetChanged();
        }
    };

    public BannerAdapter(List<Banner> slideritems, ViewPager2 viewPager2) {
        this.slideritems = slideritems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slide,parent,false);
        return new BannerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        holder.setImage(slideritems.get(position));
        if(position == slideritems.size()-2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return slideritems.size();
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.bannerContainer);
        }

        public void setImage(Banner banner){
            //use picasso
           // imageView.setImageResource(banner.getImageurl());
            //Picasso.get().load(mrestaurant_info.get(position).getPhotos_url()).into(holder.restaurant_pic);
            Picasso.get().load(banner.getImageurl()).into(imageView);
            imageView.setClipToOutline(true);
        }
    }


}
