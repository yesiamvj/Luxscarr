package com.ulgebra.getscar;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.format;

/**
 * Created by Vijayakumar on 08/07/2016.
 */

public class FragmentBookingDate extends Fragment {
    private int CalendarHour, CalendarMinute;
    DatePicker frm_dat_pic,to_date_pic;
    int fromHour,toHour,fromMin,toMin;
    Boolean pickTSet=false;
    Boolean toTimeSet=false;
    TimePickerDialog timepickerdialog,timepickerdialog2;
    Calendar calendar;
    Button prcd_btn,fromTimeBtn,toTimeBtn;
    String fromFormat,toFormat,fromTime,toTime,minstr;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.activity_book_date, container, false);


        frm_dat_pic=(DatePicker)rootView.findViewById(R.id.from_date);
        frm_dat_pic.setMinDate(System.currentTimeMillis() - 1000);
        to_date_pic=(DatePicker)rootView.findViewById(R.id.to_date);
        to_date_pic.setMinDate(System.currentTimeMillis() - 1000);
        prcd_btn=(Button)rootView.findViewById(R.id.prcd_btn);
        fromTimeBtn=(Button)rootView.findViewById(R.id.BtnSelctFromTime);
        toTimeBtn=(Button)rootView.findViewById(R.id.BtnSelctToTime);




        calendar = Calendar.getInstance();
        CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
        CalendarMinute = calendar.get(Calendar.MINUTE);

        fromTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timepickerdialog = new TimePickerDialog(rootView.getContext(),
                 new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                fromHour=hourOfDay;
                                fromMin=minute;
                                pickTSet=true;

                                if (hourOfDay == 0) {

                                    hourOfDay += 12;

                                    fromFormat = "AM";
                                }
                                else if (hourOfDay == 12) {

                                    fromFormat = "PM";

                                }
                                else if (hourOfDay > 12) {

                                    hourOfDay -= 12;

                                    fromFormat = "PM";

                                }
                                else {

                                    fromFormat = "AM";
                                }
                                if(minute<10){
                                    minstr="0"+minute;
                                }else {
                                    minstr=minute+"";
                                }

                                fromTime=hourOfDay + ":" + minstr +" "+ fromFormat;

                               fromTimeBtn.setText(hourOfDay + ":" + minstr +" "+ fromFormat);
                            }
                        }, CalendarHour, CalendarMinute, false);

                timepickerdialog.show();

            }
        });

        toTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timepickerdialog2 = new TimePickerDialog(rootView.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                toHour=hourOfDay;
                                toMin=minute;
                                toTimeSet=true;
                                if (hourOfDay == 0) {

                                    hourOfDay += 12;

                                    toFormat = "AM";
                                }
                                else if (hourOfDay == 12) {

                                    toFormat = "PM";

                                }
                                else if (hourOfDay > 12) {

                                    hourOfDay -= 12;

                                    toFormat = "PM";

                                }
                                else {

                                    toFormat = "AM";
                                }
                                if(minute<10){
                                    minstr="0"+minute;
                                }
                                else {
                                    minstr=minute+"";
                                }
                                toTime=hourOfDay + ":" + minstr +" "+ toFormat;

                                toTimeBtn.setText(hourOfDay + ":" + minstr +" "+ toFormat);
                            }
                        }, CalendarHour, CalendarMinute, false);
                timepickerdialog2.show();

            }
        });


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



                int timeDiff= 1;

                if(toHour>fromHour){
                    timeDiff=1;
                }
                if(toHour==fromHour){
                    if(toMin>fromMin){
                        timeDiff=1;
                    }
                    else {
                        timeDiff=0;
                    }
                }
                if(toHour<fromHour){
                    timeDiff=0;
                }



                int diff_day=calc_diff(frm_day,frm_month,frm_year,to_day,tomonth,to_year);
                diff_day=diff_day+timeDiff;



                Log.v("dif",diff_day+" diff day , tohour "+toHour+" from hour "+fromHour);


                Intent intsz=new Intent(getActivity().getApplicationContext(),BrowseCars.class);
                intsz.putExtra("frm_day",frm_day);
                intsz.putExtra("frm_mnth",frm_month);
                intsz.putExtra("frm_year",frm_year);
                intsz.putExtra("to_day",to_day);
                intsz.putExtra("to_mnth",tomonth);
                intsz.putExtra("to_year",to_year);
                intsz.putExtra("from_time",fromTime);
                intsz.putExtra("to_time",toTime);

                if(diff_day==0){
                    diff_day=1;
                }

                intsz.putExtra("from_to_diff",diff_day);


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

                String dateq = new SimpleDateFormat("dd").format(new Date());
                String monq = new SimpleDateFormat("MM").format(new Date());
                String yearq = new SimpleDateFormat("yyyy").format(new Date());

                Log.v("bookdatetimes","date q "+dateq+" monq "+monq+" yearq "+yearq);


                int chkk=1;
                String frmMon=frm_month+"",frmDay=frm_day+"";
                if(frm_month<10){
                    frmMon="0"+frm_month;
                }
                if(frm_day<10){
                    frmDay="0"+frm_day;
                }
                Log.v("diff_ady",diff_day+" is d");
                if(diff_day<=2 && (frmDay.equals(dateq)) && (frmMon.equals(monq)) && ((frm_year+"").equals(yearq)) ){

                    chkk=1;

                }
                else {



                    if(diff_day<2){
                        chkk=0;
                        new AlertDialog.Builder(rootView.getContext())
                                .setTitle("Sorry !")
                                .setMessage("Minimum booking period is 2 days")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }




                }

                if(!pickTSet && toTimeSet){
                    chkk=0;
                    new AlertDialog.Builder(rootView.getContext())
                            .setTitle(" Alert !")
                            .setMessage("Please Select Pick Up Time")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                if(!toTimeSet && pickTSet){
                    chkk=0;
                    new AlertDialog.Builder(rootView.getContext())
                            .setTitle(" Alert !")
                            .setMessage("Please Select Ride end time ")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                if(!toTimeSet && !pickTSet){
                    chkk=0;
                    new AlertDialog.Builder(rootView.getContext())
                            .setTitle(" Alert !")
                            .setMessage("Please Select Pick up time and End time ")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

                if(grt==1){
                    Snackbar.make(v,"Please Select valid dates",Snackbar.LENGTH_LONG).show();
                }else {
                    if(chkk==1){
                        startActivity(intsz);
                    }
                }





            }
        });


        return rootView;
    }
    public int calc_diff(int f_d,int f_m,int f_y,int to_d,int to_m,int to_y){




        int cur_yr=f_y;
        int  cur_mnth=f_m;
        int  cur_date=f_d;



        int alm_yr=to_y;
        int alm_mnth=to_m;
        int alm_date=to_d;


        int y,m,d,tot_day=0,diff_mnt,frm_mnth,to_mnth,from_day,to_day,from_yr,en=0;

        int fin_yr=alm_yr;
        int fin_mnth=alm_mnth;
        int fin_day=alm_date;

        from_yr=cur_yr;

        for(y=from_yr;y<=fin_yr;y++){
//alert(y);
            en=en+1;
            //  alert(en);
            if(en==1){
                frm_mnth=cur_mnth;
                from_day=cur_date;
                if(y==fin_yr){
                    to_mnth=fin_mnth;
                }else{
                    to_mnth=12;
                }

            }else{

                if(y==fin_yr){

                    to_mnth=fin_mnth;
                    to_day=fin_day;

                    if(en==1){
                        frm_mnth=f_m;
                    }else{
                        frm_mnth=1;
                    }
                    //alert(to_mnth+ " ti "+to_day +" "+frm_mnth);
                }else{
                    to_mnth=12;
                    frm_mnth=1;
                }

            }


            for(m=frm_mnth;m<=to_mnth;m++){
                //  alert(frm_mnth+" "+to_mnth);
                switch(m){
                    case 1:
                        diff_mnt=31;
                        break;
                    case 2:
                        if(y%4==0){

                            diff_mnt=29;
                        }else
                        {
                            diff_mnt=28;
                        }

                        break;
                    case 3:
                        diff_mnt=31;
                        break;
                    case 4:
                        diff_mnt=30;
                        break;
                    case 5:
                        diff_mnt=31;
                        break;
                    case 6:
                        diff_mnt=30;
                        break;
                    case 7:
                        diff_mnt=31;
                        break;
                    case 8:
                        diff_mnt=31;
                        break;
                    case 9:
                        diff_mnt=30;
                        break;
                    case 10:
                        diff_mnt=31;
                        break;
                    case 11:
                        diff_mnt=30;
                        break;
                    case 12:
                        diff_mnt=31;
                        break;

                    default:
                        diff_mnt=30;
                        break;

                }


                if(y==fin_yr && m==fin_mnth){
                    to_day=fin_day;

                }else{
                    to_day=diff_mnt;

                }

                if(en==1 && m==frm_mnth){

                    if(frm_mnth!=to_mnth){
                        to_day=diff_mnt - f_d;
                    }else{


                        to_day=(fin_day-f_d);
                    }
                }else{
                    from_day=1;
                }


                if(y==fin_yr && m==fin_mnth){
                    tot_day+=to_day;
                }else{
                    tot_day+=to_day;

                }



            }
            // console.log("year day="+tot_day);
        }
        // tot_day=tot_day-1;
        return tot_day;


    }

}
