package com.ulgebra.luxscarr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class BrowseCars extends AppCompatActivity {


    TextView from_to_inp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_cars);

        Bundle buns=getIntent().getExtras();
       int frm_day= buns.getInt("frm_day");
       int frm_month= buns.getInt("frm_mnth");
       int frm_year= buns.getInt("frm_year");
       int to_day= buns.getInt("to_day");
        int tomonth=buns.getInt("to_mnth");
        int to_year=buns.getInt("to_year");

        from_to_inp=(TextView)findViewById(R.id.BrowseCarsDateText);

        String book_date=frm_day+"-"+frm_month+"-"+frm_year+" to "+to_day+"-"+tomonth+"-"+to_year;
        from_to_inp.setText(book_date);

        final String sel_url="http://luxscar.com/luxscar_app/view_avl_cars.php";










    }
}
