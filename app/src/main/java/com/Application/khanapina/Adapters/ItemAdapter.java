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

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Menu_item> arrayListMember;
    private Context context;

    public ItemAdapter(List<Menu_item> arrayListMember, Context context) {
        this.arrayListMember = arrayListMember;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menuitem_activity, parent, false);
        return new ItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {
        holder.itemName.setText(arrayListMember.get(position).getItem_name());
        //Get current locale information
        Locale currentLocale = new Locale("en", "IN");

        //Get currency instance from locale; This will have all currency related information
        Currency currentCurrency = Currency.getInstance(currentLocale);

        //Currency Formatter specific to locale
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);
        holder.itemPrice.setText(currencyFormatter.format(arrayListMember.get(position).getItem_price()));
    }

    @Override
    public int getItemCount() {
        return (arrayListMember != null ? arrayListMember.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemPrice;
        ImageView Increment, Decrement;
        TextView AddItem, itemcount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.tv_itemname);
            itemPrice = itemView.findViewById(R.id.item_price);
            AddItem = itemView.findViewById(R.id.addItemButton);
            Increment = itemView.findViewById(R.id.increment_item);
            Decrement = itemView.findViewById(R.id.decrement_item);
            itemcount = itemView.findViewById(R.id.item_count);

            Increment.setVisibility(View.GONE);
            itemcount.setVisibility(View.GONE);
            Decrement.setVisibility(View.GONE);
        }
    }
}
