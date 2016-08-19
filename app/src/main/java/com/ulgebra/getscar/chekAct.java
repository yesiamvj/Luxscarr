package com.ulgebra.getscar;

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
        String from_time= buns.getString("from_time");
        String to_time= buns.getString("to_time");
        int from_to_diff=buns.getInt("from_to_diff");
        Intent intent=new Intent(getApplicationContext(),BrowseCars.class);
        intent.putExtra("frm_day",frm_day);
        intent.putExtra("frm_mnth",frm_month);
        intent.putExtra("frm_year",frm_year);
        intent.putExtra("to_day",to_day);
        intent.putExtra("to_mnth",tomonth);
        intent.putExtra("to_year",to_year);
        intent.putExtra("from_time",from_time);
        intent.putExtra("to_time",to_time);
        intent.putExtra("from_to_diff",from_to_diff);
        setContentView(R.layout.activity_chek);
        finish();
        startActivity(intent);

    }
}
