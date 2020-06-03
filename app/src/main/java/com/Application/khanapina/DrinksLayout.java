package com.Application.khanapina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.Application.khanapina.Adapters.DrinksAdapter;
import com.Application.khanapina.ModelClass.Menu_item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DrinksLayout extends AppCompatActivity {

    RecyclerView drinks_recyclerview;
    DrinksAdapter drinksAdapter;
    ArrayList<Menu_item> drinksItems;
    DatabaseReference reference;
    ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drinks_layout);

        backbutton = findViewById(R.id.back_button);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(DrinksLayout.this, MainActivity.class));
            }
        });

        drinks_recyclerview = findViewById(R.id.drinks_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        drinks_recyclerview.setLayoutManager(gridLayoutManager);
        drinksItems = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Restaurants");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot1.child("menu/Drinks").getChildren()) {
                        Menu_item item = snapshot.getValue(Menu_item.class);
                        drinksItems.add(item);
                    }
                    drinksAdapter = new DrinksAdapter(DrinksLayout.this, drinksItems);
                    drinks_recyclerview.setAdapter(drinksAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("dessertDatabaseError", "onCancelled: ", databaseError.toException());
            }
        });


    }
}
