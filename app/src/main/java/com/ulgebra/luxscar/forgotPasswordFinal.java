package com.ulgebra.luxscar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class forgotPasswordFinal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_final);
    }
    public void goToLogin(View view){
        Intent intent=new Intent(this,Login_user.class);
        startActivity(intent);
    }
}
