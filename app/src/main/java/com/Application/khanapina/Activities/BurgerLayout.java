package com.Application.khanapina.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Application.khanapina.Adapters.BurgerAdapter;
import com.Application.khanapina.BottomSheetView;
import com.Application.khanapina.ModelClass.Menu_item;
import com.Application.khanapina.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class BurgerLayout extends AppCompatActivity implements BottomSheetView {

    RecyclerView burger_recyclerview;
    BurgerAdapter burgerAdapter;
    ArrayList<Menu_item> burgerItems;
    DatabaseReference reference;
    ImageView backbutton;

    Boolean openBanner = false;
    ExtendedFloatingActionButton floatingActionButton;
    private LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.burger_layout);


        linearLayout = findViewById(R.id.bottom_sheet);

        backbutton = findViewById(R.id.back_button);
        floatingActionButton = findViewById(R.id.gotoCartButton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(BurgerLayout.this, MainActivity.class));
            }
        });

        getMenuItems();


    }

    private void getMenuItems() {
        burger_recyclerview = findViewById(R.id.burger_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        burger_recyclerview.setLayoutManager(gridLayoutManager);
        burgerItems = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Restaurants");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot1.child("menu/Burger").getChildren()) {
                        Menu_item item = snapshot.getValue(Menu_item.class);
                        burgerItems.add(item);
                    }
                    burgerAdapter = new BurgerAdapter(BurgerLayout.this, burgerItems, BurgerLayout.this);
                    burger_recyclerview.setAdapter(burgerAdapter);
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
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        savecurrentdate = currentDate.format(calfordate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a", Locale.US);
        savecurrenttime = currentTime.format(calfordate.getTime());

        String str = savecurrentdate;
        String strNew = str.replace("/", ""); //strNew is 'HelloWorldJavaUsers'


        final DatabaseReference cartlistref = FirebaseDatabase.getInstance().getReference().child("cartlist").child(strNew);

        final HashMap<String, Object> cartmap = new HashMap<>();
        cartmap.put("item_name", ((TextView) Objects.requireNonNull(burger_recyclerview.findViewHolderForAdapterPosition(position)).
                itemView.findViewById(R.id.item_name)).getText().toString());
        cartmap.put("item_price", ((TextView) Objects.requireNonNull(burger_recyclerview.findViewHolderForAdapterPosition(position)).
                itemView.findViewById(R.id.item_price)).getText().toString());
        cartmap.put("item_quantity", item_count);
        cartmap.put("order_date", savecurrentdate);
        cartmap.put("order_time", savecurrenttime);
        cartlistref.child(String.valueOf(position)).updateChildren(cartmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(BurgerLayout.this, "Item Added", Toast.LENGTH_SHORT).show();
                }
            }
        });


        DatabaseReference cartlist = FirebaseDatabase.getInstance().getReference().child("cartlist").child(strNew);
        cartlist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren() && floatingActionButton.getVisibility() == View.VISIBLE) {
                    Toast.makeText(BurgerLayout.this,
                            "has children",
                            Toast.LENGTH_SHORT).show();
                } else {
                    floatingActionButton.show();
                    Animation animation = AnimationUtils.loadAnimation(BurgerLayout.this, R.anim.fadein);
                    floatingActionButton.startAnimation(animation);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DessertLayouterror", "onCancelled: " + databaseError.getMessage());
            }
        });
        //  showBanner(item_count);
    }

    @Override
    public void onIncrementClick(int position, int item_count) {
        String savecurrenttime, savecurrentdate;

        Calendar calfordate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        savecurrentdate = currentDate.format(calfordate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a", Locale.US);
        savecurrenttime = currentTime.format(calfordate.getTime());

        String str = savecurrentdate;
        String strNew = str.replace("/", "");

        DatabaseReference cartlistRef = FirebaseDatabase.getInstance().getReference().child("cartlist").child(strNew).child(String.valueOf(position));
        cartlistRef.child("item_quantity").setValue(item_count);
        cartlistRef.child("order_date").setValue(savecurrentdate);
        cartlistRef.child("order_time").setValue(savecurrenttime);
    }


    @Override
    public void onRemovingItem(int position, int item_count) {
        if (item_count == 0) {
            String savecurrentdate;

            Calendar calfordate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            savecurrentdate = currentDate.format(calfordate.getTime());

            String str = savecurrentdate;
            String strNew = str.replace("/", "");


            DatabaseReference cartlistRef = FirebaseDatabase.getInstance().getReference().child("cartlist").child(strNew).child(String.valueOf(position));
            cartlistRef.removeValue();
            Objects.requireNonNull(burger_recyclerview.findViewHolderForAdapterPosition(position)).itemView.findViewById(R.id.addItemButton).setVisibility(View.VISIBLE);
            Objects.requireNonNull(burger_recyclerview.findViewHolderForAdapterPosition(position)).itemView.findViewById(R.id.increment_item).setVisibility(View.GONE);
            Objects.requireNonNull(burger_recyclerview.findViewHolderForAdapterPosition(position)).itemView.findViewById(R.id.decrement_item).setVisibility(View.GONE);
            Objects.requireNonNull(burger_recyclerview.findViewHolderForAdapterPosition(position)).itemView.findViewById(R.id.item_count).setVisibility(View.GONE);

            DatabaseReference cartlist = FirebaseDatabase.getInstance().getReference().child("cartlist").child(strNew);
            cartlist.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        Toast.makeText(BurgerLayout.this,
                                "has children",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Animation animation = AnimationUtils.loadAnimation(BurgerLayout.this, R.anim.fadeout);
                        floatingActionButton.startAnimation(animation);
                        floatingActionButton.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("DessertLayouterror", "onCancelled: " + databaseError.getMessage());
                }
            });
            //showBanner(item_count);
        } else {
            String savecurrenttime, savecurrentdate;

            Calendar calfordate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            savecurrentdate = currentDate.format(calfordate.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a", Locale.US);
            savecurrenttime = currentTime.format(calfordate.getTime());

            String str = savecurrentdate;
            String strNew = str.replace("/", "");

            DatabaseReference cartlistRef = FirebaseDatabase.getInstance().getReference().child("cartlist").child(strNew).child(String.valueOf(position));
            cartlistRef.child("item_quantity").setValue(item_count);
            cartlistRef.child("order_date").setValue(savecurrentdate);
            cartlistRef.child("order_time").setValue(savecurrenttime);

            // showBanner(item_count);

        }
    }
}
