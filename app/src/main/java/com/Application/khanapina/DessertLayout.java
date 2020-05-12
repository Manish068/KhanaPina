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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DessertLayout extends AppCompatActivity {

    RecyclerView dessert_recyclerview;
    DessertAdapter dessertAdapter;
    ArrayList<Dessert_item> dessertItems;
    DatabaseReference reference;
    ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dessert_layout);

        backbutton=findViewById(R.id.back_button);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(DessertLayout.this,MainActivity.class));
            }
        });

        dessert_recyclerview=findViewById(R.id.dessert_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        dessert_recyclerview.setLayoutManager(gridLayoutManager);
        dessertItems = new ArrayList<>();
        reference= FirebaseDatabase.getInstance().getReference().child("Restaurants");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    for (DataSnapshot snapshot: dataSnapshot1.child("menu").child("Desserts").getChildren()){
                        Dessert_item item=new Dessert_item();
                        item.setItem_name(snapshot.getValue(Dessert_item.class).getItem_name());
                        item.setItem_url(snapshot.getValue(Dessert_item.class).getItem_url());
                        dessertItems.add(item);
                    }
                    dessertAdapter=new DessertAdapter(DessertLayout.this,dessertItems);
                    dessert_recyclerview.setAdapter(dessertAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("dessertDatabaseError", "onCancelled: ",databaseError.toException() );
            }
        });
    }
}
