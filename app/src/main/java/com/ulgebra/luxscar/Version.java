package com.ulgebra.luxscar;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Version extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);
    }
    public void goToUlgebra(View view){
        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse("http://www.fb.com/ulgebra"));
        startActivity(browse);
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        finish();
        startActivity(intent);
    }
    public void viewDevs(View view){

        Intent intentq=new Intent(getApplicationContext(),Developers.class);
        startActivity(intentq);

    }
}
