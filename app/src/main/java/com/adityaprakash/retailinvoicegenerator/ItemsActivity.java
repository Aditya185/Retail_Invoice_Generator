package com.adityaprakash.retailinvoicegenerator;


import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class ItemsActivity extends AppCompatActivity {


    private ArrayList<Items> itemsArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ItemsAdapter mAdapter;
    private Button sendBillButton;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private static final String TAG = ItemsActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    public SQLiteDatabase mInvoicedb;
    public TextView totalBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        mInvoicedb = this.openOrCreateDatabase("Invoice.db",MODE_PRIVATE,null);
        mInvoicedb.execSQL("CREATE TABLE IF NOT EXISTS ITEMS(ID INTEGER PRIMARY KEY AUTOINCREMENT,ITEM_NAME VARCHAR,QUANTITY VARCHAR,PRICE VARCHAR,PHONE VARCHAR,TIME VARCHAR)");

        initializer();
        updateItemList();

        FloatingActionButton fab = findViewById(R.id.fab4);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewItem();


                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        checkForSmsPermission();
        sendBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = getBillMessage();

                smsSendMessage(v,message);
            }
        });

    }


    private void checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, getString(R.string.permission_not_granted));
            // Permission not yet granted. Use requestPermissions().
            // MY_PERMISSIONS_REQUEST_SEND_SMS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            // Permission already granted. Enable the SMS button.
            //enableSmsButton();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        // For the requestCode, check if permission was granted or not.
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (permissions[0].equalsIgnoreCase(Manifest.permission.SEND_SMS)
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted. Enable sms button.
                    //enableSmsButton();
                } else {
                    // Permission denied.
                    Log.d(TAG, getString(R.string.failure_permission));
                    Toast.makeText(this, getString(R.string.failure_permission),
                            Toast.LENGTH_LONG).show();
                    // Disable the sms button.
                   //disableSmsButton();
                }
            }
        }
    }


    public void smsSendMessage(View view,String message) {
       // EditText editText = (EditText) findViewById(R.id.editText_main);
        // Set the destination phone number to the string in editText.
        Bundle extras = getIntent().getExtras();
        String destinationAddress = extras.getString("phone_no");
        Log.d("phone no",destinationAddress);




        // Find the sms_message view.
       // EditText smsEditText = (EditText) findViewById(R.id.sms_message);
        // Get the text of the sms message.
        String smsMessage = "Store Name\n"+message;
        // Set the service center address if needed, otherwise null.
        String scAddress = null;
        // Set pending intents to broadcast
        // when message sent and when delivered, or set to null.
        PendingIntent sentIntent = null, deliveryIntent = null;
        // Check for permission first.
        checkForSmsPermission();
        // Use SmsManager.
        SmsManager smsManager = SmsManager.getDefault();

        smsManager.sendTextMessage(destinationAddress, scAddress, smsMessage,
                sentIntent, deliveryIntent);
        Toast.makeText(ItemsActivity.this,"Bill Sent Successfully",Toast.LENGTH_SHORT).show();
    }

    private void addNewItem() {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.items_dialog_box, null);

        final TextInputEditText eItemName = alertLayout.findViewById(R.id.item_name);
        final TextInputEditText price = alertLayout.findViewById(R.id.price);
        final TextInputEditText quantity = alertLayout.findViewById(R.id.quantity);

        AlertDialog.Builder builder = new AlertDialog.Builder(ItemsActivity.this);
        builder.setTitle("Add Item");
        builder.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        builder.setCancelable(false);

        //final EditText courseNameField = new EditText(CustomerActivity.this);


        builder.setPositiveButton("Add Item", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String firstEntry = eItemName.getText().toString();
                String secondEntry = quantity.getText().toString();
                String thirdEntry = price.getText().toString();


                if (TextUtils.isEmpty(firstEntry))
                {
                    Toast.makeText(ItemsActivity.this, "Please Enter Item Name...", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(secondEntry))
                {
                    Toast.makeText(ItemsActivity.this, "Please Enter Quantity...", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(thirdEntry))
                {
                    Toast.makeText(ItemsActivity.this, "Please Enter Price of one Item...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //fetching the PHONE NO
                    Bundle extras = getIntent().getExtras();
                    String phone = extras.getString("phone_no");
                    String time = extras.getString("time");
                    Log.d("phone_no",phone);


                    int pri = Integer.parseInt(thirdEntry);
                    int qty = Integer.parseInt(secondEntry);
                    int total = pri*qty;
                    String totalPrice = new Integer(total).toString();


                    String sql = "INSERT INTO ITEMS (ITEM_NAME,QUANTITY,PRICE,PHONE,TIME) VALUES (?,?,?,?,?)";
                    SQLiteStatement statement = mInvoicedb.compileStatement(sql);
                    statement.bindString(1,firstEntry);
                    statement.bindString(2,secondEntry);
                    statement.bindString(3,totalPrice);
                    statement.bindString(4,phone);
                    statement.bindString(5,time);

                    try {
                        statement.execute();
                    }catch(Exception e){
                        Toast.makeText(ItemsActivity.this,"Some error occurs",Toast.LENGTH_SHORT).show();
                    }

                    updateItemList();


                    //itemsArrayList.add(new Items(firstEntry,"Qty: "+secondEntry,"Total Price: "+totalPrice));
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });

        builder.show();





    }

    private void updateItemList() {

        //fetching the date
        Bundle extras = getIntent().getExtras();
        String phoneNo = extras.getString("phone_no");
        String timeNo = extras.getString("time");
        Log.d("update", phoneNo);

        //update array list
        String query = "SELECT * FROM ITEMS WHERE PHONE =? AND TIME = ?;";
        Cursor c = mInvoicedb.rawQuery(query, new String[]{phoneNo,timeNo});
        int nameIndex = c.getColumnIndex("ITEM_NAME");
        int quantityIndex = c.getColumnIndex("QUANTITY");
        int priceIndex = c.getColumnIndex("PRICE");
        int phoneIndex = c.getColumnIndex("PHONE");
        int totalPri = 0;
        String topa = "";
        String message = "";

        if (c.moveToFirst()) {
            itemsArrayList.clear();
            do {

                itemsArrayList.add(new Items(c.getString(nameIndex), c.getString(quantityIndex), c.getString(priceIndex)));
                totalPri += Integer.parseInt(c.getString(priceIndex));
               message+= c.getString(nameIndex)+" "+ c.getString(quantityIndex)+" "+ c.getString(priceIndex)+"\n";


            } while (c.moveToNext());
        }
        topa = "Total: "+new Integer(totalPri).toString();
        message += topa;
        totalBill.setText(topa);
        mAdapter.notifyItemInserted(itemsArrayList.size());
        totalPri = 0;

    }
    private String getBillMessage(){

        //fetching the date
        Bundle extras = getIntent().getExtras();
        String phoneNo = extras.getString("phone_no");
        String timeNo = extras.getString("time");
        Log.d("update", phoneNo);

        //update array list
        String query = "SELECT * FROM ITEMS WHERE PHONE =? AND TIME = ?;";
        Cursor c = mInvoicedb.rawQuery(query, new String[]{phoneNo,timeNo});
        int nameIndex = c.getColumnIndex("ITEM_NAME");
        int quantityIndex = c.getColumnIndex("QUANTITY");
        int priceIndex = c.getColumnIndex("PRICE");
        int phoneIndex = c.getColumnIndex("PHONE");
        int totalPri1 = 0;
        String topa = "";
        String message = "";

        if (c.moveToFirst()) {
            //itemsArrayList.clear();
            do {

                //itemsArrayList.add(new Items(c.getString(nameIndex), c.getString(quantityIndex), c.getString(priceIndex)));
                totalPri1 += Integer.parseInt(c.getString(priceIndex));
                message+= c.getString(nameIndex)+" qty-"+ c.getString(quantityIndex)+" tot_price="+ c.getString(priceIndex)+"\n";


            } while (c.moveToNext());
        }
        topa = "Total Amount: "+new Integer(totalPri1).toString();
        String fin_message = message+topa;
        //totalBill.setText(topa);
        //mAdapter.notifyItemInserted(itemsArrayList.size());
        totalPri1 = 0;
        return fin_message;

    }

    private void initializer() {



        recyclerView = (RecyclerView) findViewById(R.id.rec_view);
        mAdapter = new ItemsAdapter(ItemsActivity.this, itemsArrayList);
        sendBillButton = (Button) findViewById(R.id.sen_bill_btn);
        totalBill = (TextView)findViewById(R.id.title_view);
    }


    private void disableSmsButton() {
        Toast.makeText(this, R.string.sms_disabled, Toast.LENGTH_LONG).show();

        sendBillButton.setVisibility(View.INVISIBLE);
        Button retryButton = (Button) findViewById(R.id.button_retry);
        retryButton.setVisibility(View.VISIBLE);
    }

    private void enableSmsButton() {
        ImageButton smsButton = (ImageButton) findViewById(R.id.message_icon);
        smsButton.setVisibility(View.VISIBLE);
    }


    public void retryApp(View view) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        startActivity(intent);
    }

}


