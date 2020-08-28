package com.Application.khanapina.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Application.khanapina.ModelClass.Locations;
import com.Application.khanapina.R;

import java.sql.Struct;
import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Locations> locationsArrayList;

    public LocationAdapter(Context context, ArrayList<Locations> locationsArrayList) {
        this.context = context;
        this.locationsArrayList = locationsArrayList;
    }


    @NonNull
    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.saved_location_activity, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.ViewHolder holder, int position) {
        holder.textAddressline1.setText(locationsArrayList.get(position).getAddress());
        holder.textLandmark.setText(locationsArrayList.get(position).getLandmark());
    }

    @Override
    public int getItemCount() {
        return locationsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textAddressline1, textLandmark;
        private ImageView editLocationButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textAddressline1 = itemView.findViewById(R.id.textAddressline1);
            textLandmark = itemView.findViewById(R.id.textLandmark);
            editLocationButton = itemView.findViewById(R.id.editLocationButton);

        }
    }
}
