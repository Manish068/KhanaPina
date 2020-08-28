package com.Application.khanapina.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.Application.khanapina.Adapters.GroupAdapter;
import com.Application.khanapina.ModelClass.ItemGroup;

import com.Application.khanapina.ModelClass.Menu_item;
import com.Application.khanapina.ModelClass.Restaurant_info;
import com.Application.khanapina.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EachRestaurant extends AppCompatActivity {

    //initialize variable

    ImageView RestaurantImage;
    TextView RestaurantName, RatingNumber, RatingText, CousineText, LocationText;
    RatingBar ratingBar;
    Intent intent;


    RecyclerView rv_CousineGroup;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_restaurant);


        RestaurantImage = findViewById(R.id.Restaurant_image);
        RestaurantName = findViewById(R.id.Restaurant_name);
        RatingNumber = findViewById(R.id.rating_number);
        RatingText = findViewById(R.id.rating_text);
        CousineText = findViewById(R.id.Cousine_text);
        LocationText = findViewById(R.id.location_text);
        ratingBar = findViewById(R.id.rating_bar);
        intent = getIntent();

        Restaurant_info restaurant_info = intent.getParcelableExtra("selected_one");
        assert restaurant_info != null;
        final String restaurantName = restaurant_info.getName();
        String imageres = restaurant_info.getPhotos_url();
        String cousine = restaurant_info.getCuisines();
        String rating = restaurant_info.getRating_text();
        String ratingNumber = restaurant_info.getAggregate_rating();

        Picasso.get().load(imageres).into(RestaurantImage);
        RestaurantName.setText(restaurantName);
        CousineText.setText(cousine);
        RatingNumber.setText(ratingNumber);
        RatingText.setText(rating);


        reference = FirebaseDatabase.getInstance().getReference("Restaurants");
        rv_CousineGroup = findViewById(R.id.rv_cousinegroup);
        rv_CousineGroup.setLayoutManager(new LinearLayoutManager(this));


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ItemGroup> itemGroups = new ArrayList<>();
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    if (restaurantName.equals(Objects.requireNonNull(dataSnapshot.child(String.valueOf(i)).getValue(Restaurant_info.class)).getName())) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.child(i + "/menu").getChildren()) {
                            ItemGroup itemGroup = new ItemGroup();
                            itemGroup.setKey(dataSnapshot1.getKey());
                            ArrayList<Menu_item> items = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot1.getChildren()) {
                                Log.d("trial", "onDataChange: " + snapshot.toString());
                                Menu_item item_category = snapshot.getValue(Menu_item.class);
                                items.add(item_category);
                                itemGroup.setMenu(items);
                            }
                            itemGroups.add(itemGroup);
                        }
                        GroupAdapter adapter = new GroupAdapter(EachRestaurant.this, itemGroups);
                        rv_CousineGroup.setAdapter(adapter);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}