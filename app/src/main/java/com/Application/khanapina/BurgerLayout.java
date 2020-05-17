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

import com.Application.khanapina.Adapters.BurgerAdapter;
import com.Application.khanapina.ModelClass.Menu_item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BurgerLayout extends AppCompatActivity {

    RecyclerView burger_recyclerview;
    BurgerAdapter burgerAdapter;
    ArrayList<Menu_item> burgerItems;
    DatabaseReference reference;
    ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.burger_layout);

        backbutton = findViewById(R.id.back_button);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(BurgerLayout.this, MainActivity.class));
            }
        });

        burger_recyclerview = findViewById(R.id.burger_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        burger_recyclerview.setLayoutManager(gridLayoutManager);
        burgerItems = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Restaurants");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot1.child("menu/Burger").getChildren()) {
                        Menu_item item = snapshot.getValue(Menu_item.class);
                        burgerItems.add(item);
                    }
                    burgerAdapter = new BurgerAdapter(BurgerLayout.this, burgerItems);
                    burger_recyclerview.setAdapter(burgerAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("dessertDatabaseError", "onCancelled: ", databaseError.toException());
            }
        });
    }
}