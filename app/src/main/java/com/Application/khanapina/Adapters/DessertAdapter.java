package com.Application.khanapina.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Application.khanapina.BottomSheetView;
import com.Application.khanapina.ModelClass.Menu_item;
import com.Application.khanapina.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

public class DessertAdapter extends RecyclerView.Adapter<DessertAdapter.DessertViewHolder> {

    private Context context;
    private ArrayList<Menu_item> menu_items;
    SharedPreferences sharedPreferences;
    int item_count;
    private BottomSheetView bottomSheetView;


    public DessertAdapter(Context context, ArrayList<Menu_item> menu_items, BottomSheetView bottomSheetView) {
        this.context = context;
        this.menu_items = menu_items;
        this.bottomSheetView = bottomSheetView;
    }

    @NonNull
    @Override
    public DessertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.foodmenu_itemsdetailcontainer, parent, false);
        return new DessertViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final DessertViewHolder holder, final int position) {


        holder.itemName.setText(menu_items.get(position).getItem_name());
        Picasso.get().load(menu_items.get(position).getItem_url()).into(holder.itemImage);
        //Get current locale information
        Locale currentLocale = new Locale("en", "IN");

        //Get currency instance from locale; This will have all currency related information
        Currency currentCurrency = Currency.getInstance(currentLocale);


        //Currency Formatter specific to locale
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);
        holder.itemPrice.setText(currencyFormatter.format(menu_items.get(position).getItem_price()));


        holder.AddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_count = 1;
                holder.Increment.setVisibility(View.VISIBLE);
                holder.Decrement.setVisibility(View.VISIBLE);
                holder.itemcount.setVisibility(View.VISIBLE);
                holder.AddItem.setVisibility(View.GONE);
                holder.itemcount.setText(String.valueOf(item_count));

                bottomSheetView.onAddClick(holder.getAdapterPosition(), item_count);
            }
        });


        holder.Decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                item_count = Integer.parseInt(holder.itemcount.getText().toString()) - 1;
                holder.itemcount.setText(String.valueOf(item_count));
                bottomSheetView.onRemovingItem(holder.getAdapterPosition(), item_count);
            }
        });


        holder.Increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_count = Integer.parseInt(holder.itemcount.getText().toString()) + 1;
                holder.itemcount.setText(String.valueOf(item_count));
                bottomSheetView.onIncrementClick(holder.getAdapterPosition(), item_count);
            }
        });


        holder.favoriteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addToFavorite(menu_items.get(position), true);
                holder.favoriteItem.setImageResource(R.drawable.ic_baseline_favorite_24);
            }
        });
    }

    private void addToFavorite(Menu_item menu_item, boolean b) {
        DatabaseReference cartlistref = FirebaseDatabase.getInstance().getReference().child("FavoriteItem");
        HashMap<String, Object> favorite = new HashMap<>();
        favorite.put("item_name", menu_item.getItem_name());
        favorite.put("item_price", menu_item.getItem_price());
        favorite.put("item_favorite", b);
        favorite.put("item_url", menu_item.getItem_url());
        cartlistref.push().setValue(favorite).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Item Added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return menu_items.size();
    }


    public class DessertViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice;
        ImageView itemImage, favoriteItem;
        TextView AddItem, Increment, Decrement, itemcount;


        public DessertViewHolder(@NonNull final View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemImage = itemView.findViewById(R.id.item_image);
            itemPrice = itemView.findViewById(R.id.item_price);
            AddItem = itemView.findViewById(R.id.addItemButton);
            Increment = itemView.findViewById(R.id.increment_item);
            Decrement = itemView.findViewById(R.id.decrement_item);
            itemcount = itemView.findViewById(R.id.item_count);
            favoriteItem = itemView.findViewById(R.id.fav_button);


            Increment.setVisibility(View.GONE);
            itemcount.setVisibility(View.GONE);
            Decrement.setVisibility(View.GONE);


        }

    }

}


