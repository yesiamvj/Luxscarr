package com.ulgebra.getscar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class checkAct2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle buns=getIntent().getExtras();

        String booking_idd= buns.getString("booking_idd");

        Intent intent=new Intent(getApplicationContext(),SingleBookingDetails.class);

        intent.putExtra("booking_idd",booking_idd);

        setContentView(R.layout.activity_chek);
        finish();
        startActivity(intent);

    }
}
