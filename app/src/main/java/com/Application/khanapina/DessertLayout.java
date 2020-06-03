package com.Application.khanapina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Application.khanapina.Adapters.DessertAdapter;
import com.Application.khanapina.ModelClass.Menu_item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

public class DessertLayout extends AppCompatActivity implements BottomSheetView {

    RecyclerView dessert_recyclerview;
    DessertAdapter dessertAdapter;
    ArrayList<Menu_item> dessertItems;
    DatabaseReference reference;
    ImageView backbutton;
    TextView No_of_items;
    private LinearLayout linearLayout;
    private BottomSheetBehavior bottomSheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dessert_layout);


        linearLayout = findViewById(R.id.bottom_sheet);
        No_of_items = findViewById(R.id.total_items);
        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        backbutton = findViewById(R.id.back_button);


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(DessertLayout.this, MainActivity.class));
            }
        });

        dessert_recyclerview = findViewById(R.id.dessert_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        dessert_recyclerview.setLayoutManager(gridLayoutManager);
        dessertItems = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Restaurants");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot1.child("menu").child("Desserts").getChildren()) {
                        Menu_item item = snapshot.getValue(Menu_item.class);
                        dessertItems.add(item);
                    }
                    dessertAdapter = new DessertAdapter(DessertLayout.this, dessertItems, DessertLayout.this);
                    dessert_recyclerview.setAdapter(dessertAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("dessertDatabaseError", "onCancelled: ", databaseError.toException());
            }
        });

    }

    @Override
    public void onAddClick(final int position, int item_count) {

        String savecurrenttime, savecurrentdate;

        Calendar calfordate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        savecurrentdate = currentDate.format(calfordate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime = currentTime.format(calfordate.getTime());

        final DatabaseReference cartlistref = FirebaseDatabase.getInstance().getReference().child("cartlist");

        final HashMap<String, Object> cartmap = new HashMap<>();
        final HashMap<String, Object> bannermap = new HashMap<>();
        cartmap.put("item_name", ((TextView) dessert_recyclerview.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.item_name)).getText().toString());
        cartmap.put("item_price", ((TextView) dessert_recyclerview.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.item_price)).getText().toString());
        cartmap.put("item_quantity", item_count);
        cartmap.put("order_date", savecurrentdate);
        cartmap.put("order_time", savecurrenttime);
        bannermap.put("total_item", item_count);
        cartlistref.child(String.valueOf(position)).updateChildren(cartmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(DessertLayout.this, "Item Added", Toast.LENGTH_SHORT).show();
                    cartlistref.updateChildren(bannermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("checking", "onComplete: " + "successful");
                            }
                        }
                    });
                }
            }
        });

        showBanner(position, item_count);
    }


    @Override
    public void onRemovingItem(int position, int item_count) {
        if (item_count == 0) {
            DatabaseReference cartlistRef = FirebaseDatabase.getInstance().getReference().child("cartlist").child(String.valueOf(position));
            cartlistRef.removeValue();
            dessert_recyclerview.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.addItemButton).setVisibility(View.VISIBLE);
            dessert_recyclerview.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.increment_item).setVisibility(View.GONE);
            dessert_recyclerview.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.decrement_item).setVisibility(View.GONE);
            dessert_recyclerview.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.item_count).setVisibility(View.GONE);

            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            String savecurrenttime, savecurrentdate;

            Calendar calfordate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
            savecurrentdate = currentDate.format(calfordate.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            savecurrenttime = currentTime.format(calfordate.getTime());

            DatabaseReference cartlistRef = FirebaseDatabase.getInstance().getReference().child("cartlist").child(String.valueOf(position));
            cartlistRef.child("item_quantity").setValue(item_count);
            cartlistRef.child("order_date").setValue(savecurrentdate);
            cartlistRef.child("order_time").setValue(savecurrenttime);

            showBanner(position, item_count);

        }
    }

    private void showBanner(int position, final int item_count) {
        DatabaseReference cartlistRef = FirebaseDatabase.getInstance().getReference().child("cartlist").child(String.valueOf(position));
        cartlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (item_count == 0) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        Log.d("TAG", "onDataChange: " + dataSnapshot.getKey());
                        String item = dataSnapshot.child("item_quantity").getValue().toString();
                        Toast.makeText(DessertLayout.this, item, Toast.LENGTH_SHORT).show();
                        No_of_items.setText(item);
                    } else {
                        Toast.makeText(DessertLayout.this, "mymsg", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getMessage());
            }
        });
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}

