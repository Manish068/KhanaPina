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

public class PizzaLayout extends AppCompatActivity {

    RecyclerView pizza_recyclerview;
    BurgerAdapter pizzaAdapter;
    ArrayList<Menu_item> pizzaItems;
    DatabaseReference reference;
    ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza_layout);

        backbutton = findViewById(R.id.back_button);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(PizzaLayout.this, MainActivity.class));
            }
        });

        pizza_recyclerview = findViewById(R.id.pizza_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        pizza_recyclerview.setLayoutManager(gridLayoutManager);
        pizzaItems = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Restaurants");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot1.child("menu/Pizza").getChildren()) {
                        Menu_item item = snapshot.getValue(Menu_item.class);
                        pizzaItems.add(item);
                    }
                    pizzaAdapter = new BurgerAdapter(PizzaLayout.this, pizzaItems);
                    pizza_recyclerview.setAdapter(pizzaAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("dessertDatabaseError", "onCancelled: ", databaseError.toException());
            }
        });
    }
}