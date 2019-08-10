package com.adityaprakash.retailinvoicegenerator;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class CustomerActivity extends AppCompatActivity {


    private ArrayList<Customers> customersArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomersAdapters mAdapter;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public SQLiteDatabase mInvoicedb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        mInvoicedb = this.openOrCreateDatabase("Invoice.db",MODE_PRIVATE,null);
        mInvoicedb.execSQL("CREATE TABLE IF NOT EXISTS CUSTOMERS(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR,DATES VARCHAR,TIME VARCHAR,PHONE VARCHAR)");


        initializer();



        updateCustomerList();
        FloatingActionButton fab = findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewcustomer();


                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }


    private void addNewcustomer() {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.customer_dialog_box, null);

        final TextInputEditText eCoursename = alertLayout.findViewById(R.id.customer_name);
        final TextInputEditText eCourseCode = alertLayout.findViewById(R.id.phoneNo);

        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerActivity.this);
        builder.setTitle("Add Customer");
        builder.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        builder.setCancelable(false);

        //final EditText courseNameField = new EditText(CustomerActivity.this);


        builder.setPositiveButton("Add Customer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String firstEntry = eCoursename.getText().toString();
                String secondEntry = eCourseCode.getText().toString();
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                String hour = Integer.toString(mHour);
                String minute = Integer.toString(mMinute);
                String time = hour+":"+minute;
                Log.d("time",time);
                String thirdEntry = "Time "+time;

                if (TextUtils.isEmpty(firstEntry))
                {
                    Toast.makeText(CustomerActivity.this, "Please Enter Customer Name...", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(secondEntry))
                {
                    Toast.makeText(CustomerActivity.this, "Please Enter PhoneNo...", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    //fetching the date
                    Bundle extras = getIntent().getExtras();
                    String date = extras.getString("dateId");
                    Log.d("date",date);



                    String sql = "INSERT INTO CUSTOMERS (NAME,DATES,TIME,PHONE) VALUES (?,?,?,?)";
                    SQLiteStatement statement = mInvoicedb.compileStatement(sql);
                    statement.bindString(1,firstEntry);
                    statement.bindString(2,date);
                    statement.bindString(3,thirdEntry);
                    statement.bindString(4,secondEntry);

                    try {
                        statement.execute();
                    }catch(Exception e){
                        Toast.makeText(CustomerActivity.this,"Some error occurs",Toast.LENGTH_SHORT).show();
                    }

                    updateCustomerList();


                   // customersArrayList.add(new Customers(firstEntry,secondEntry,thirdEntry));
                    mAdapter.notifyItemInserted(customersArrayList.size());
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

    private void updateCustomerList() {
        //fetching the date
        Bundle extras = getIntent().getExtras();
        String date1 = extras.getString("dateId");
        Log.d("update",date1);

        //update array list
        String query = "SELECT * FROM CUSTOMERS WHERE DATES =?;";
        Cursor c = mInvoicedb.rawQuery(query,new String[] {date1});
        int nameIndex = c.getColumnIndex("NAME");
        int dateIndex = c.getColumnIndex("DATE");
        int timeIndex = c.getColumnIndex("TIME");
        int phoneIndex = c.getColumnIndex("PHONE");


        if(c.moveToFirst()){
            customersArrayList.clear();
            do{

                customersArrayList.add(new Customers(c.getString(nameIndex),c.getString(phoneIndex),c.getString(timeIndex)));

            }while(c.moveToNext());
        }


        mAdapter.notifyItemInserted(customersArrayList.size());



    }




    private void initializer() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_customer_view);
        mAdapter = new CustomersAdapters(CustomerActivity.this, customersArrayList);
    }

}