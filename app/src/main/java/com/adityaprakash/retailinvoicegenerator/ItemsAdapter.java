package com.adityaprakash.retailinvoicegenerator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {
    private ArrayList<Items> itemsArrayList;
    private static final String TAG = "CourseAdapter";
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView ItemsName,Quantity,Price,Absent,Total,Percentage;
        public CardView mCardView;
        public MyViewHolder(View v) {
            super(v);
            ItemsName = v.findViewById(R.id.item_name);
            Quantity = v.findViewById(R.id.qty);
            Price = v.findViewById(R.id.price);
            mCardView = v.findViewById(R.id.cardView4);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ItemsAdapter(Context context, ArrayList<Items> itemsArrayList) {
        this.itemsArrayList = itemsArrayList;
        mContext =context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_list_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Items item = itemsArrayList.get(position);
        holder.ItemsName.setText(item.getItemName());
        holder.Price.setText("Total Price: "+item.getPrice());
        holder.Quantity.setText("Qty: "+item.getQuantity());


        //starting the sms activity
//        holder.mCardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//                Intent intent = new Intent(mContext,SMSActivity.class);
//
//
//                mContext.startActivity(intent);
//
//
//
//            }
//        });

    }





    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }
}




