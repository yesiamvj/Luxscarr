package com.ulgebra.luxscarr;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Date;

public class BookDate extends AppCompatActivity {


    DatePicker frm_dat_pic,to_date_pic;
    Button prcd_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_date);
        frm_dat_pic=(DatePicker)findViewById(R.id.from_date);
        frm_dat_pic.setMinDate(System.currentTimeMillis() - 1000);
        to_date_pic=(DatePicker)findViewById(R.id.to_date);
        prcd_btn=(Button)findViewById(R.id.prcd_btn);
        prcd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frm_dat_pic.setMinDate(System.currentTimeMillis() - 1000);
                int frm_day = frm_dat_pic.getDayOfMonth();
                int frm_month = frm_dat_pic.getMonth() + 1;
                int frm_year = frm_dat_pic.getYear();
                int to_day = to_date_pic.getDayOfMonth();
                int tomonth = to_date_pic.getMonth() + 1;
                int to_year = to_date_pic.getYear();
                Bundle bunsz=new Bundle();
                bunsz.putInt("frm_day",frm_day);
                bunsz.putInt("frm_mnth",frm_month);
                bunsz.putInt("frm_year",frm_year);
                bunsz.putInt("to_day",to_day);
                bunsz.putInt("to_mnth",tomonth);
                bunsz.putInt("to_year",to_year);


                int grt=0;
                if(frm_year>=to_year){
                    grt=1;
                    if(frm_month>=tomonth){
                        grt=1;
                        if(frm_day>to_day){


                            grt=1;



                        }else{
                            if(frm_month>tomonth){
                                grt=1;
                            }else{
                                grt=0;
                            }
                            if(frm_year>to_year){
                                grt=1;
                            }

                        }
                    }else{
                        grt=0;
                        if(frm_year>to_year){
                            grt=1;
                        }

                    }
                }
if(grt==1){

    Snackbar.make(v,"Please Select valid dates",Snackbar.LENGTH_LONG).show();
}else {
    Intent intsz=new Intent(getApplicationContext(),BrowseCars.class);
    intsz.putExtras(bunsz);
    startActivity(intsz);
}



            }
        });




    }
}
