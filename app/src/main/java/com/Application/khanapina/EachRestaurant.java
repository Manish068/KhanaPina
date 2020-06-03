package com.Application.khanapina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Application.khanapina.Adapters.GroupAdapter;
import com.Application.khanapina.ModelClass.Restaurant_info;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EachRestaurant extends AppCompatActivity {

    //initialize variable

    ImageView RestaurantImage;
    TextView RestaurantName, RatingNumber, RatingText, CousineText, LocationText;
    RatingBar ratingBar;
    Intent intent;
    RecyclerView rv_CousineGroup;
    ArrayList<String> stringArrayList;
    LinearLayoutManager layoutManagerGroup;
    GroupAdapter groupAdapter;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_restaurant);

        rv_CousineGroup = findViewById(R.id.rv_cousinegroup);
        RestaurantImage = findViewById(R.id.Restaurant_image);
        RestaurantName = findViewById(R.id.Restaurant_name);
        RatingNumber = findViewById(R.id.rating_number);
        RatingText = findViewById(R.id.rating_text);
        CousineText = findViewById(R.id.Cousine_text);
        LocationText = findViewById(R.id.location_text);
        ratingBar = findViewById(R.id.rating_bar);
        intent = getIntent();

        Restaurant_info restaurant_info = intent.getParcelableExtra("selected_one");
        final String restaurantName = restaurant_info.getName();
        String imageres = restaurant_info.getPhotos_url();
        String cousine = restaurant_info.getCuisines();
        String ratingtext = restaurant_info.getRating_text();
        String ratingNumber = restaurant_info.getAggregate_rating();

        Picasso.get().load(imageres).into(RestaurantImage);
        RestaurantName.setText(restaurantName);
        CousineText.setText(cousine);
        RatingNumber.setText(ratingNumber);
        RatingText.setText(ratingtext);

        stringArrayList = new ArrayList<>();


        reference = FirebaseDatabase.getInstance().getReference().child("Restaurants");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    if (restaurantName.equals(dataSnapshot.child(String.valueOf(i)).getValue(Restaurant_info.class).getName())) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.child(i + "/menu").getChildren()) {
                            stringArrayList.add(dataSnapshot1.getKey());
                        }
                        break;
                    } else {
                        Log.d("myrule", "onDataChange: " + "noData");
                    }
                }
                groupAdapter = new GroupAdapter(EachRestaurant.this, stringArrayList);
                layoutManagerGroup = new LinearLayoutManager(EachRestaurant.this);
                rv_CousineGroup.setLayoutManager(layoutManagerGroup);
                rv_CousineGroup.setAdapter(groupAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}