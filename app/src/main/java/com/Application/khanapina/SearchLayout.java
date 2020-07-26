package com.Application.khanapina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.Application.khanapina.Adapters.BurgerAdapter;
import com.Application.khanapina.Adapters.DessertAdapter;
import com.Application.khanapina.ModelClass.Menu_item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class SearchLayout extends AppCompatActivity implements BottomSheetView {

    ArrayList<Menu_item> menuItemArrayList;
    DessertAdapter searchAdapter;
    SearchView searchView;
    RecyclerView searchRecyclerView;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);
        searchView = findViewById(R.id.searchView);
        searchRecyclerView = findViewById(R.id.searchRecyclerview);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        searchRecyclerView.setLayoutManager(gridLayoutManager);
        menuItemArrayList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Restaurants");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot1.child("menu").getChildren()) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Menu_item item = new Menu_item();
                            item.setItem_price(Objects.requireNonNull(snapshot1.getValue(Menu_item.class)).getItem_price());
                            item.setItem_name(Objects.requireNonNull(snapshot1.getValue(Menu_item.class)).getItem_name());
                            item.setItem_url(Objects.requireNonNull(snapshot1.getValue(Menu_item.class)).getItem_url());
                            menuItemArrayList.add(item);
                        }
                    }
      /*              searchAdapter = new DessertAdapter(SearchLayout.this, menuItemArrayList, SearchLayout.this);
                    searchRecyclerView.setAdapter(searchAdapter);*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("dessertDatabaseError", "onCancelled: ", databaseError.toException());
            }
        });
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }

        Objects.requireNonNull(searchView).setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchRecyclerView.setVisibility(View.GONE);
                return true;
            }
        });


    }

    private void search(String newText) {
        ArrayList<Menu_item> itemArrayList = new ArrayList<>();
        for (Menu_item object : menuItemArrayList) {
            if (object.getItem_name().toLowerCase().contains(newText.toLowerCase())) {

                itemArrayList.add(object);
            }
        }
        searchAdapter = new DessertAdapter(SearchLayout.this, itemArrayList, SearchLayout.this);
        searchRecyclerView.setAdapter(searchAdapter);
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