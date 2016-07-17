package com.ulgebra.luxscarr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView textView=(TextView)findViewById(R.id.styleText);
        textView.setText(Html.fromHtml("We need two forms of identification to verify about you.<br/> Take a look at the list below and  <br/> provide <b> <i> any one of </i> the document </b> under each category:"));


    }
    public void makeAcall(View view) {
        String phnno = (String) view.getTag();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(phnno));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        startActivity(intent);
    }
    public void goToMapp(View view){
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/place/Gets+Rent-A-Car/@12.980512,80.218189,14z/data=!4m5!3m4!1s0x0:0x3a75dd9b32600e72!8m2!3d12.9805116!4d80.2181886?hl=en-US"));
        startActivity(intent);
    }
    public void goToMaill(View view){


        Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "info@luxscar.com"));
        startActivity(intent);


    }

}
