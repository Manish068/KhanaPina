package com.Application.khanapina.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Application.khanapina.Activities.Favdish;
import com.Application.khanapina.Activities.MainActivity;
import com.Application.khanapina.Activities.cart;
import com.Application.khanapina.Activities.profile_activity;
import com.Application.khanapina.Adapters.LocationAdapter;
import com.Application.khanapina.ModelClass.Locations;
import com.Application.khanapina.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class location extends AppCompatActivity {

    RecyclerView savedLocation__recyclerview;
    LocationAdapter locationAdapter;
    ArrayList<Locations> locationsArrayList;
    FirebaseFirestore firestore;
    FirebaseAuth fAuth;
    String userID;
    private TextView textErrorMessage;
    private ProgressBar usersProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);


        fAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();


        savedLocation__recyclerview = findViewById(R.id.saveLocations);
        textErrorMessage = findViewById(R.id.textErrorMessage);
        usersProgressBar = findViewById(R.id.usersProgressBar);

        locationsArrayList = new ArrayList<>();
        locationAdapter = new LocationAdapter(this, locationsArrayList);
        savedLocation__recyclerview.setAdapter(locationAdapter);


        FetchSavedLocation();

        //initialize the navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.editlocation);

        //get the item selected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_favorites:
                        startActivity(new Intent(getApplicationContext(), Favdish.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.cart:
                        startActivity(new Intent(getApplicationContext(), cart.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), profile_activity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.editlocation:
                        return true;
                }
                return false;
            }
        });

    }

    private void FetchSavedLocation() {
        usersProgressBar.setVisibility(View.VISIBLE);
        firestore.collection("users")
                .document(userID)
                .collection("location")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        usersProgressBar.setVisibility(View.GONE);
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Locations locations = new Locations();
                                locations.setAddress(document.getString("Address"));
                                locations.setLandmark(document.getString("landmark"));
                                locations.setLatitude(document.getString("latitude"));
                                locations.setLongitude(document.getString("longitude"));
                                locationsArrayList.add(locations);
                            }

                            if (locationsArrayList.size() > 0) {
                                locationAdapter.notifyDataSetChanged();
                            } else {
                                textErrorMessage.setText(String.format("%s", "No location available"));
                                textErrorMessage.setVisibility(View.VISIBLE);
                            }
                        } else {
                            textErrorMessage.setText(String.format("%s", "No location available"));
                            textErrorMessage.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}
