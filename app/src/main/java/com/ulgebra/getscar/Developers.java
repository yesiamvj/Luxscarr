package com.ulgebra.getscar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Developers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);
    }
    public void goToUlgebra(View view){
        Intent browse = new Intent( Intent.ACTION_VIEW ,Uri.parse("http://www.fb.com/ulgebra"));
        startActivity(browse);
    }
    public void goToCCS(View view){
        Intent browse = new Intent( Intent.ACTION_VIEW ,Uri.parse("http://www.chennaicreativesolutions.com"));
        startActivity(browse);
    }
    public void makeAcall(View view) {
        String phnno = (String) view.getTag();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(phnno));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        startActivity(intent);
    }
    public void goToMaill(View view){


        Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "thevjcode@gmail.com"));
        startActivity(intent);


    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        finish();
        startActivity(intent);
    }

}
