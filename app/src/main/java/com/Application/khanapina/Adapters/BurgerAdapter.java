package com.Application.khanapina.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Application.khanapina.ModelClass.Menu_item;
import com.Application.khanapina.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

public class BurgerAdapter extends RecyclerView.Adapter<BurgerAdapter.BurgerViewHolder> {

    private Context context;
    private ArrayList<Menu_item> burger_items;

    public BurgerAdapter(Context context, ArrayList<Menu_item> burger_items) {
        this.context = context;
        this.burger_items = burger_items;
    }

    @NonNull
    @Override
    public BurgerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.foodmenu_itemsdetailcontainer, parent, false);
        return new BurgerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BurgerViewHolder holder, int position) {
        holder.itemName.setText(burger_items.get(position).getItem_name());
        Picasso.get().load(burger_items.get(position).getItem_url()).into(holder.itemImage);
        //Get current locale information
        Locale currentLocale = new Locale("en", "IN");

        //Get currency instance from locale; This will have all currency related information
        Currency currentCurrency = Currency.getInstance(currentLocale);

        //Currency Formatter specific to locale
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);
        holder.itemPrice.setText(String.valueOf(currencyFormatter.format(burger_items.get(position).getItem_price())));
    }

    @Override
    public int getItemCount() {
        return burger_items.size();
    }


    public class BurgerViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice;
        ImageView itemImage;
        TextView decrementItem, incrementItem, AddItem, itemCount;

        public BurgerViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemImage = itemView.findViewById(R.id.item_image);
            itemPrice = itemView.findViewById(R.id.item_price);
            decrementItem = itemView.findViewById(R.id.decrement_item);
            incrementItem = itemView.findViewById(R.id.increment_item);
            AddItem = itemView.findViewById(R.id.addItemButton);
            itemCount = itemView.findViewById(R.id.item_count);

            decrementItem.setVisibility(View.GONE);
            incrementItem.setVisibility(View.GONE);
            itemCount.setVisibility(View.GONE);

            AddItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddItem.setVisibility(View.GONE);
                    decrementItem.setVisibility(View.VISIBLE);
                    incrementItem.setVisibility(View.VISIBLE);
                    itemCount.setVisibility(View.VISIBLE);
                }
            });

            decrementItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    decrementItem.setVisibility(View.GONE);
                    incrementItem.setVisibility(View.GONE);
                    itemCount.setVisibility(View.GONE);
                    AddItem.setVisibility(View.VISIBLE);
                }
            });
        }

    }

}