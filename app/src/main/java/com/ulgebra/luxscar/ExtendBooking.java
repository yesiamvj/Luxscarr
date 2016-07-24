package com.ulgebra.luxscar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ExtendBooking extends AppCompatActivity {
    DatePicker frm_dat_pic,to_date_pic;
    int frm_day,frm_month,frm_year;
    Button prcd_btn;
    Date date1=null;
    String dateTo;
    String yeear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intentz=getIntent();
        setContentView(R.layout.activity_extend_booking);
        String format = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        final String dateString_From = intentz.getStringExtra("booking_from_date");
        String dateString_To = intentz.getStringExtra("booking_to_date");
        dateTo = dateString_From;
        if (dateTo.charAt(1) == '-'){
            dateTo = "0" + dateTo;
        }
        if (dateTo.charAt(4) == '-'){
            dateTo = dateTo.substring(0,3) + "0" + dateTo.substring(3);
        }
        yeear=dateTo.substring(dateTo.length()- 4);
        Log.v("dateea",dateTo);
        Log.v("dateea",yeear);
        final String booking_idd = intentz.getStringExtra("booking_idd");
        TextView exFromDt=(TextView)findViewById(R.id.exFromDt);
        TextView exBookId=(TextView)findViewById(R.id.exBookId);
        exFromDt.setText(dateString_From);
        exBookId.setText(booking_idd);
        Date date = null;
        try {
            date = sdf.parse(dateString_To);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            date1 = sdf.parse(dateTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Calendar myCalendar2=Calendar.getInstance();
        myCalendar2.setTime(date1);

        Calendar myCalendar = Calendar.getInstance();
        to_date_pic=(DatePicker)findViewById(R.id.to_date);
        myCalendar.setTime(date);
        to_date_pic.setMinDate(date.getTime());
       // Log.v("to daa",myCalendar.get(Calendar.DAY_OF_MONTH)+"");

        prcd_btn=(Button)findViewById(R.id.prcd_btn);


        prcd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int to_day = to_date_pic.getDayOfMonth();
                int tomonth = to_date_pic.getMonth() + 1;
                int to_year = to_date_pic.getYear();
                Intent intsz=new Intent(getApplicationContext(),UpdateExtendBooking.class);
                intsz.putExtra("frm_day",date1.getDate());
                intsz.putExtra("frm_mnth",date1.getMonth()+1);
                intsz.putExtra("frm_year",yeear);
                intsz.putExtra("froom",dateTo.toString());
                intsz.putExtra("to_day",to_day);
                intsz.putExtra("to_mnth",tomonth);
                intsz.putExtra("to_year",to_year);
                intsz.putExtra("booking_idd",booking_idd);
                startActivity(intsz);



            }
        });



    }
}
