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

public class CustomersAdapters extends RecyclerView.Adapter<CustomersAdapters.MyViewHolder> {
    private ArrayList<Customers> customersArrayList;
    private static final String TAG = "CourseAdapter";
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView CustomerName,Time,PhoneNo,Absent,Total,Percentage;
        public CardView mCardView;
        public MyViewHolder(View v) {
            super(v);
            CustomerName = v.findViewById(R.id.customer_name);
            Time = v.findViewById(R.id.time);
            PhoneNo = v.findViewById(R.id.phoneNo);
            mCardView = v.findViewById(R.id.cardView3);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CustomersAdapters(Context context, ArrayList<Customers> customersArrayList) {
        this.customersArrayList = customersArrayList;
        mContext =context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customers_list_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Customers customer = customersArrayList.get(position);
        holder.CustomerName.setText(customer.getName());
        holder.Time.setText(customer.getPurchaseTime());
        holder.PhoneNo.setText(customer.getPhone());


        //starting the sms activity
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(mContext,ItemsActivity.class);
                intent.putExtra("phone_no",customer.getPhone());
                intent.putExtra("time",customer.getPurchaseTime());
                Log.d("Successful",customer.getName());

                mContext.startActivity(intent);



            }
        });

    }





    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return customersArrayList.size();
    }
}






