package com.ulgebra.luxscarr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

/**
 * Created by Vijayakumar on 08/07/2016.
 */

public class FragmentBookingDate extends Fragment {
    DatePicker frm_dat_pic,to_date_pic;
    Button prcd_btn;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_book_date, container, false);


        frm_dat_pic=(DatePicker)rootView.findViewById(R.id.from_date);
        frm_dat_pic.setMinDate(System.currentTimeMillis() - 1000);
        to_date_pic=(DatePicker)rootView.findViewById(R.id.to_date);
        prcd_btn=(Button)rootView.findViewById(R.id.prcd_btn);
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
                Intent intsz=new Intent(getActivity().getApplicationContext(),BrowseCars.class);
                intsz.putExtra("frm_day",frm_day);
                intsz.putExtra("frm_mnth",frm_month);
                intsz.putExtra("frm_year",frm_year);
                intsz.putExtra("to_day",to_day);
                intsz.putExtra("to_mnth",tomonth);
                intsz.putExtra("to_year",to_year);


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

                    startActivity(intsz);
                }



            }
        });


        return rootView;
    }
}
