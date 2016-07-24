package com.ulgebra.luxscar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class chekAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle buns=getIntent().getExtras();
        int frm_day= buns.getInt("frm_day");
        int frm_month= buns.getInt("frm_mnth");
        int frm_year= buns.getInt("frm_year");
        int to_day= buns.getInt("to_day");
        int tomonth=buns.getInt("to_mnth");
        int to_year=buns.getInt("to_year");
        Intent intent=new Intent(getApplicationContext(),BrowseCars.class);
        intent.putExtra("frm_day",frm_day);
        intent.putExtra("frm_mnth",frm_month);
        intent.putExtra("frm_year",frm_year);
        intent.putExtra("to_day",to_day);
        intent.putExtra("to_mnth",tomonth);
        intent.putExtra("to_year",to_year);
        setContentView(R.layout.activity_chek);
        finish();
        startActivity(intent);

    }
}
