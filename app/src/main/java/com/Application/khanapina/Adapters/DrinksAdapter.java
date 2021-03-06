package com.Application.khanapina.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Application.khanapina.BottomSheetView;
import com.Application.khanapina.ModelClass.Menu_item;
import com.Application.khanapina.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

public class DrinksAdapter extends RecyclerView.Adapter<DrinksAdapter.DrinksViewHolder> {

    private Context context;
    private ArrayList<Menu_item> drinks_items;
    int item_count;
    private BottomSheetView bottomSheetView;

    public DrinksAdapter(Context context, ArrayList<Menu_item> drinks_items, BottomSheetView bottomSheetView) {
        this.context = context;
        this.drinks_items = drinks_items;
        this.bottomSheetView = bottomSheetView;
    }

    @NonNull
    @Override
    public DrinksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.foodmenu_itemsdetailcontainer, parent, false);
        return new DrinksViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final DrinksViewHolder holder, int position) {

        holder.itemName.setText(drinks_items.get(position).getItem_name());
        Picasso.get().load(drinks_items.get(position).getItem_url()).into(holder.itemImage);
        //Get current locale information
        Locale currentLocale = new Locale("en", "IN");

        //Get currency instance from locale; This will have all currency related information
        Currency currentCurrency = Currency.getInstance(currentLocale);

        //Currency Formatter specific to locale
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);
        holder.itemPrice.setText(currencyFormatter.format(drinks_items.get(position).getItem_price()));

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

    }

    @Override
    public int getItemCount() {
        return drinks_items.size();
    }

    public class DrinksViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice;
        ImageView itemImage, favoriteItem;
        TextView AddItem, Increment, Decrement, itemcount;

        public DrinksViewHolder(@NonNull View itemView) {
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
