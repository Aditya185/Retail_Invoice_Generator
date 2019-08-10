package com.adityaprakash.retailinvoicegenerator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class WelcomeActivity extends AppCompatActivity {
    private Button mButton;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mButton = findViewById(R.id.btn_login);
        editText = findViewById(R.id.input_shop_name);

        final String shopName = editText.getText().toString();

        Log.d("Successful",shopName);


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                intent.putExtra("shop_name",shopName);


                startActivity(intent);
            }
        });

    }
}
