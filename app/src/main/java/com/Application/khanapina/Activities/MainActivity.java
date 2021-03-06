package com.Application.khanapina.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.Application.khanapina.Adapters.BannerAdapter;
import com.Application.khanapina.Adapters.Restaurant_RecyclerView;
import com.Application.khanapina.BottomSheetView;
import com.Application.khanapina.GoogleMaps.UserLocation;
import com.Application.khanapina.ModelClass.Banner;
import com.Application.khanapina.ModelClass.Restaurant_info;
import com.Application.khanapina.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements Restaurant_RecyclerView.ResturantView_holder.OnNoteListener, BottomSheetView {


    //for search option
    SearchView searchView;


    //for location
    private TextView fullAddress, Locality;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LinearLayout locationService;


    List<Banner> sliderItems;
    FirebaseFirestore firestore;
    //for ViewPager
    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();

    DatabaseReference reference;
    RecyclerView restaurant_list_recyvlerview;
    ArrayList<Restaurant_info> mresturantinformation;
    //for adapter
    Restaurant_RecyclerView restaurant_recyclerView;

    SharedPreferences sharedPreferences, addressSharedPreference;
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // for searchview
        searchView = findViewById(R.id.searchoption);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchLayout.class));
            }
        });


        //for Location
        locationService = findViewById(R.id.Location_service);
        Locality = findViewById(R.id.tv_Address);
        fullAddress = findViewById(R.id.tv_full_location);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //for save the login status once login don't have to login again this is step2
        //step 1 is in Login page
        sharedPreferences = getSharedPreferences("user_number", MODE_PRIVATE);
        addressSharedPreference = getSharedPreferences("ADDRESS", MODE_PRIVATE);

        if (addressSharedPreference.contains("ADDRESS")) {
            String fullAdress = addressSharedPreference.getString("ADDRESS", null);
            String[] splitAddress = fullAdress.split(",", 2);
            Locality.setText(splitAddress[0]);
            fullAddress.setText(splitAddress[1]);
        } else {
            Toast.makeText(this, "Select Location", Toast.LENGTH_SHORT).show();
        }

        locationService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserLocation.class));
            }
        });

        //for Banner
        Imagesliderfunction();


        //implementation of recyclerview for showing top restaurants
        toprestaurants();


        //initialize the navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.home);

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
                    case R.id.editlocation:
                        startActivity(new Intent(getApplicationContext(), location.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), profile_activity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        return true;
                }
                return false;
            }
        });

    }


    public void toprestaurants() {
        restaurant_list_recyvlerview = findViewById(R.id.list_of_top_restaurant);
        restaurant_list_recyvlerview.setLayoutManager(new LinearLayoutManager(this));
        mresturantinformation = new ArrayList<>();


        reference = FirebaseDatabase.getInstance().getReference().child("Restaurants");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Restaurant_info mrest = new Restaurant_info();
                    mrest.setName(Objects.requireNonNull(dataSnapshot1.getValue(Restaurant_info.class)).getName());
                    mrest.setCuisines(Objects.requireNonNull(dataSnapshot1.getValue(Restaurant_info.class)).getCuisines());
                    mrest.setAggregate_rating(Objects.requireNonNull(dataSnapshot1.child("user_rating").getValue(Restaurant_info.class)).getAggregate_rating());
                    mrest.setRating_text(Objects.requireNonNull(dataSnapshot1.child("user_rating").getValue(Restaurant_info.class)).getRating_text());
                    mrest.setVotes(Objects.requireNonNull(dataSnapshot1.child("user_rating").getValue(Restaurant_info.class)).getVotes());
                    mrest.setRating_color(Objects.requireNonNull(dataSnapshot1.child("user_rating").getValue(Restaurant_info.class)).getRating_color());
                    mrest.setPhotos_url(Objects.requireNonNull(dataSnapshot1.getValue(Restaurant_info.class)).getPhotos_url());
                    mresturantinformation.add(mrest);

                }
                restaurant_recyclerView = new Restaurant_RecyclerView(MainActivity.this, mresturantinformation, MainActivity.this);
                restaurant_list_recyvlerview.setAdapter(restaurant_recyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("msg", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    public void Imagesliderfunction() {
        firestore = FirebaseFirestore.getInstance();
        viewPager2 = findViewById(R.id.bannerslider);
        sliderItems = new ArrayList<>();

        firestore.collection("Banner").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        firestore.collection("Banner").document(document.getId())
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Banner_image_url(documentSnapshot.getString("imageurl"));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("mymessage", "Error getting documents: " + e.getMessage());
                            }
                        });
                    }

                } else {
                    Log.d("mymessage", "Error getting documents: ", task.getException());
                }
            }
        });

        //implementation of motion of image sliding
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        //implementation of auto sliding
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000); //slider duration for 2 seconds
            }
        });
    }

    public void Banner_image_url(String url) {
        sliderItems.add(new Banner(url));
        viewPager2.setAdapter(new BannerAdapter(sliderItems, viewPager2));
        viewPager2.setClipToPadding(true);
        viewPager2.setClipChildren(true);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

    public void OpenDessertLayout(View view) {
        startActivity(new Intent(this, DessertLayout.class));
    }


    public void OpenDrinksLayout(View view) {
        startActivity(new Intent(this, DrinksLayout.class));
    }

    public void OpenBurgerLayout(View view) {
        startActivity(new Intent(this, BurgerLayout.class));
    }

    public void OpenPizzaLayout(View view) {
        startActivity(new Intent(this, PizzaLayout.class));
    }

    public void OpenMainCourseLayout(View view) {
        startActivity(new Intent(this, MainCourseLayout.class));
    }


    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(this, EachRestaurant.class);
        intent.putExtra("selected_one", mresturantinformation.get(position));
        startActivity(intent);
    }

    @Override
    public void onAddClick(int position, int item_count) {

    }

    @Override
    public void onIncrementClick(int position, int item_count) {

    }

    @Override
    public void onRemovingItem(int position, int item_count) {

    }
}
