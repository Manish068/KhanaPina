package com.Application.khanapina;

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

import com.Application.khanapina.Adapters.DessertAdapter;
import com.Application.khanapina.ModelClass.Menu_item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
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

public class DessertLayout extends AppCompatActivity implements BottomSheetView {


    RecyclerView dessert_recyclerview;
    DessertAdapter dessertAdapter;
    ArrayList<Menu_item> dessertItems;
    DatabaseReference reference;
    ImageView backbutton;
    TextView No_of_items;
    private LinearLayout linearLayout;

    Boolean openBanner = false;
    ExtendedFloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dessert_layout);


        linearLayout = findViewById(R.id.bottom_sheet);
        // No_of_items = findViewById(R.id.total_items);
        backbutton = findViewById(R.id.back_button);
        floatingActionButton = findViewById(R.id.gotoCartButton);


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(DessertLayout.this, MainActivity.class));
            }
        });

        getMenuItems();


    }

    private void getMenuItems() {
        dessert_recyclerview = findViewById(R.id.dessert_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        dessert_recyclerview.setLayoutManager(gridLayoutManager);
        dessertItems = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Restaurants");

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
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        savecurrentdate = currentDate.format(calfordate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a", Locale.US);
        savecurrenttime = currentTime.format(calfordate.getTime());

        String str = savecurrentdate;
        String strNew = str.replace("/", ""); //strNew is 'HelloWorldJavaUsers'


        final DatabaseReference cartlistref = FirebaseDatabase.getInstance().getReference().child("cartlist").child(strNew);

        final HashMap<String, Object> cartmap = new HashMap<>();
        cartmap.put("item_name", ((TextView) Objects.requireNonNull(dessert_recyclerview.findViewHolderForAdapterPosition(position)).
                itemView.findViewById(R.id.item_name)).getText().toString());
        cartmap.put("item_price", ((TextView) Objects.requireNonNull(dessert_recyclerview.findViewHolderForAdapterPosition(position)).
                itemView.findViewById(R.id.item_price)).getText().toString());
        cartmap.put("item_quantity", item_count);
        cartmap.put("order_date", savecurrentdate);
        cartmap.put("order_time", savecurrenttime);
        cartlistref.child(String.valueOf(position)).updateChildren(cartmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(DessertLayout.this, "Item Added", Toast.LENGTH_SHORT).show();
                }
            }
        });


        DatabaseReference cartlist = FirebaseDatabase.getInstance().getReference().child("cartlist").child(strNew);
        cartlist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren() && floatingActionButton.getVisibility() == View.VISIBLE) {
                    Toast.makeText(DessertLayout.this,
                            "has children",
                            Toast.LENGTH_SHORT).show();
                } else {
                    floatingActionButton.show();
                    Animation animation = AnimationUtils.loadAnimation(DessertLayout.this, R.anim.fadein);
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
            Objects.requireNonNull(dessert_recyclerview.findViewHolderForAdapterPosition(position)).itemView.findViewById(R.id.addItemButton).setVisibility(View.VISIBLE);
            Objects.requireNonNull(dessert_recyclerview.findViewHolderForAdapterPosition(position)).itemView.findViewById(R.id.increment_item).setVisibility(View.GONE);
            Objects.requireNonNull(dessert_recyclerview.findViewHolderForAdapterPosition(position)).itemView.findViewById(R.id.decrement_item).setVisibility(View.GONE);
            Objects.requireNonNull(dessert_recyclerview.findViewHolderForAdapterPosition(position)).itemView.findViewById(R.id.item_count).setVisibility(View.GONE);

            DatabaseReference cartlist = FirebaseDatabase.getInstance().getReference().child("cartlist").child(strNew);
            cartlist.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        Toast.makeText(DessertLayout.this,
                                "has children",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Animation animation = AnimationUtils.loadAnimation(DessertLayout.this, R.anim.fadeout);
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

    public void OpenCart(View view) {
        startActivity(new Intent(this, cart.class));
        overridePendingTransition(0, 0);
        finish();
    }
    /*private void showBanner(final int item_count) {

        final ArrayList<Integer> itemQuantity=new ArrayList<>();
        DatabaseReference cartlistRef = FirebaseDatabase.getInstance().getReference().child("cartlist");
        cartlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (item_count == 0 ) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    itemQuantity.clear();
                } else {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String item = dataSnapshot1.child("item_quantity").getValue().toString();
                            Toast.makeText(DessertLayout.this, item, Toast.LENGTH_SHORT).show();
                            itemQuantity.add(Integer.parseInt(item));
                            Log.d("ItemQuantity", "onDataChange: "+itemQuantity.size());
                            //No_of_items.setText(String.valueOf(totalQuantity(itemQuantity)));
                        }
                    No_of_items.setText(String.valueOf(totalQuantity(itemQuantity)));
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getMessage());
            }
        });

    }

    private int totalQuantity(ArrayList<Integer> itemQuantity) {
            int sum = 0;
            for (int i = 0; i < itemQuantity.size(); i++)
                sum += itemQuantity.get(i);
            return sum;
    }*/
}

