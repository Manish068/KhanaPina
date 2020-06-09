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

import com.Application.khanapina.Adapters.MainCourseAdapter;
import com.Application.khanapina.ModelClass.Menu_item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainCourseLayout extends AppCompatActivity {

    RecyclerView maincourse_recyclerview;
    MainCourseAdapter mainCourseAdapter;
    ArrayList<Menu_item> maincourseItems;
    DatabaseReference reference;
    ImageView backbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maincourse_layout);

        backbutton = findViewById(R.id.back_button);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainCourseLayout.this, MainActivity.class));
            }
        });


        maincourse_recyclerview = findViewById(R.id.maincourse_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        maincourse_recyclerview.setLayoutManager(gridLayoutManager);
        maincourseItems = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Restaurants");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot1.child("menu/MainCourse").getChildren()) {
                        Menu_item item = snapshot.getValue(Menu_item.class);
                        maincourseItems.add(item);
                    }
                    mainCourseAdapter = new MainCourseAdapter(MainCourseLayout.this, maincourseItems);
                    maincourse_recyclerview.setAdapter(mainCourseAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("dessertDatabaseError", "onCancelled: ", databaseError.toException());
            }
        });
    }
}
