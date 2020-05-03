package com.Application.khanapina;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    TextView txtv;

    DatabaseReference reference;
    RecyclerView restaurant_list_recyvlerview;
    ArrayList<Restaurant_info> mresturantinformation;
    //for adapter
    Restaurant_RecyclerView restaurant_recyclerView;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //for save the login status once login don't have to login again this is step2
        //step 1 is in Login page
        sharedPreferences=getSharedPreferences("user_number",MODE_PRIVATE);

        restaurant_list_recyvlerview=findViewById(R.id.list_of_top_restaurant);
        restaurant_list_recyvlerview.setLayoutManager(new LinearLayoutManager(this));
        mresturantinformation=new ArrayList<>();
        //initialize the navigation view
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.home);


        reference= FirebaseDatabase.getInstance().getReference().child("Restaurants");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    Restaurant_info mrest = new Restaurant_info();
                    mrest.setName(dataSnapshot1.getValue(Restaurant_info.class).getName());
                    mrest.setCuisines(dataSnapshot1.getValue(Restaurant_info.class).getCuisines());
                    mrest.setAggregate_rating(dataSnapshot1.child("user_rating").getValue(Restaurant_info.class).getAggregate_rating());
                    mrest.setRating_text(dataSnapshot1.child("user_rating").getValue(Restaurant_info.class).getRating_text());
                    mrest.setVotes(dataSnapshot1.child("user_rating").getValue(Restaurant_info.class).getVotes());
                    mrest.setRating_color(dataSnapshot1.child("user_rating").getValue(Restaurant_info.class).getRating_color());
                    mrest.setPhotos_url(dataSnapshot1.getValue(Restaurant_info.class).getPhotos_url());
                    mresturantinformation.add(mrest);

                }
                restaurant_recyclerView = new Restaurant_RecyclerView(MainActivity.this, mresturantinformation);
                restaurant_list_recyvlerview.setAdapter(restaurant_recyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("msg", "onCancelled: "+databaseError.getMessage());
            }
        });


        //get the item selected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_favorites:
                        startActivity(new Intent(getApplicationContext(),Favdish.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.cart:
                        startActivity(new Intent(getApplicationContext(),cart.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.editlocation:
                        startActivity(new Intent(getApplicationContext(),location.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),profile_activity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;
                }
                return false;
            }
        });


    }

}
