package com.adityaprakash.retailinvoicegenerator;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.DatePicker;
import android.widget.Toast;

import com.adityaprakash.retailinvoicegenerator.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Date> dateList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DateAdapter mAdapter;
    public  SQLiteDatabase Invoicedb;
    private int mYear, mMonth, mDay, mHour, mMinute;
    DatabaseHelper myDb;
    private int id = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Invoicedb = this.openOrCreateDatabase("Invoice.db",MODE_PRIVATE,null);
        Invoicedb.execSQL("CREATE TABLE IF NOT EXISTS DATES(ID INTEGER PRIMARY KEY AUTOINCREMENT,DATE VARCHAR,UNIQUE(DATE))");


        initializer();
        updateDateList();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewDate();


                Snackbar.make(view, "Please select the date", Snackbar.LENGTH_LONG)
                        .setAction("Date is added successfully", null).show();

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


    }

    private void addNewDate() {

        // Get Current Date
        final Calendar cl = Calendar.getInstance();
        mYear = cl.get(Calendar.YEAR);
        mMonth = cl.get(Calendar.MONTH);
        mDay = cl.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        String sql = "INSERT INTO DATES (DATE) VALUES (?)";
                        SQLiteStatement statement = Invoicedb.compileStatement(sql);
                        statement.bindString(1,date);
                        try {
                            statement.execute();
                        }catch(Exception e){
                            Toast.makeText(MainActivity.this,"Date is already added",Toast.LENGTH_SHORT).show();
                        }

                          updateDateList();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();





    }

    private void updateDateList(){
        //update array list
        Cursor c = Invoicedb.rawQuery("SELECT * FROM DATES",null);
        int nameIndex = c.getColumnIndex("DATE");
        if(c.moveToFirst()){
            dateList.clear();
            do{

                dateList.add(new Date(c.getString(nameIndex)));

            }while(c.moveToNext());
        }


        mAdapter.notifyItemInserted(dateList.size());

    }

    private void initializer() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new DateAdapter(MainActivity.this,dateList);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
