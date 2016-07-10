package com.ulgebra.luxscarr;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    AutoCompleteTextView username,email,mob_no,password,lic_no;
    Button regis_btn;
    String u_name,email_id,mobil_no,pass_word,lcnc_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //no user =>

        SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
        String user_idd = myPrefs.getString("MEM1","");
        //String user_id = sharedPref.getString(getString(R.string.user_id),null);
        Toast.makeText(getApplicationContext()," user id "+user_idd,Toast.LENGTH_LONG).show();

        if(user_idd!=null){

            Toast.makeText(getApplicationContext()," user id "+user_idd,Toast.LENGTH_LONG).show();
           Intent intent=new Intent(this,Welcome.class);
            intent.putExtra("needTab",0);
            this.finish();
            startActivity(intent);
       }

        username=(AutoCompleteTextView)findViewById(R.id.reg_username);
        email=(AutoCompleteTextView)findViewById(R.id.reg_email);
        mob_no=(AutoCompleteTextView)findViewById(R.id.reg_mobile);
        password=(AutoCompleteTextView)findViewById(R.id.reg_password);
        lic_no=(AutoCompleteTextView)findViewById(R.id.reg_licence_no);
        regis_btn=(Button)findViewById(R.id.signup_btn);

        // final String url_data="http://luxscar.com/luxscar_app/user_login.html";
        regis_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean check_all=false;
                    if(TextUtils.isEmpty(username.getText())){
                        username.setError("Required");
                        username.requestFocus();
                        check_all=true;
                    }
                    if(TextUtils.isEmpty(email.getText())){
                        email.setError("Required");
                        check_all=true;
                    }
                    if(TextUtils.isEmpty(mob_no.getText())){
                        mob_no.setError("Required");
                        mob_no.requestFocus();
                        check_all=true;
                    }
                    if(TextUtils.isEmpty(password.getText())){
                        password.setError("Required");
                        password.requestFocus();
                        check_all=true;
                    }
                    if(TextUtils.isEmpty(lic_no.getText()) ){
                        lic_no.setError("Required");
                        lic_no.requestFocus();
                        check_all=true;
                    }


                if(!check_all){
                    try{
                        u_name=username.getText().toString();
                        email_id=email.getText().toString();
                        mobil_no=mob_no.getText().toString();
                        pass_word=password.getText().toString();
                        lcnc_no=lic_no.getText().toString();

                        String my_data="http://luxscar.com/luxscar_app/register_user.php?";
                        // String my_data="";
                        try {
                            my_data+="&"+URLEncoder.encode("username", "UTF-8") + "="+u_name;
                            my_data+="&"+URLEncoder.encode("email","UTF-8")+"="+email_id;
                            my_data+="&"+URLEncoder.encode("mobile_no","UTF-8")+"="+mobil_no;
                            my_data+="&"+URLEncoder.encode("password","UTF-8")+"="+pass_word;
                            my_data+="&"+URLEncoder.encode("lic_no","UTF-8")+"="+lcnc_no;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        final String regis_url=my_data;
                        new LongOperation().execute(regis_url);

                    }catch(Exception e){

                            }
                }else{
                    Snackbar.make(v, "Please fill in all Fields", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                           }
        });




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(this,BookDate.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void goToLogin(View view){
        Intent intent=new Intent(this,Login_user.class);
        startActivity(intent);

    }
    public void goToBook(View view){
        Intent intent=new Intent(this,BookDate.class);
        startActivity(intent);

    }
    public void goToOTP(View view){
        Intent intent=new Intent(this,OtpRegistration.class);
        startActivity(intent);
    }

    public class LongOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
        String data ="";
        String otpt="";



        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)

            Dialog.setMessage("Please wait..");
            Dialog.show();



        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            /************ Make Post Call To Web Server ***********/
            BufferedReader reader=null;

            // Send data
            try
            {
                // Defined URL  where to send data
                URL url = new URL(urls[0]);

                // Send POST data request

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( "" );
                wr.flush();
                // Get the server response
try{
    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    // StringBuilder sb = new StringBuilder();
    String line = null;


    // Read Server Response
    while((line = reader.readLine()) != null)
    {
        // Append server response in string
        // sb.append(line + "\n");
        otpt+=line;

    }

    // Append Server Response To Content String
    Content = otpt;
    Log.i("my_err","output="+otpt);
}catch (Exception e){

}



            }
            catch(Exception ex)
            {
                Error = ex.getMessage();
            }
            finally
            {
                try
                {

                    reader.close();
                }

                catch(Exception ex) {}
            }

            /*****************************************************/
            return null;
        }


        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();


            if(otpt.hashCode()==("Successfully Registered").hashCode()){


                Bundle bun=new Bundle();
                bun.putString("email_ids",email_id);
                bun.putString("mob_no",mobil_no);

                Intent in=new Intent(getApplicationContext(),OtpRegistration.class);
                in.putExtras(bun);
                startActivity(in);
            }else {

                if(otpt.hashCode()==0){
                    Toast.makeText(getApplicationContext(),"Check your Internet Connection",Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(getApplicationContext(),otpt,Toast.LENGTH_LONG).show();

                }
             }

            if (Error != null) {

               // uiUpdate.setText("Output : "+Error);
        //Log.i("my_err",Content);
            } else {

                Log.i("my_err", "some err");
                // Show Response Json On Screen (activity)
               // uiUpdate.setText( Content );

                /****************** Start Parse Response JSON Data *************/

                String OutputData = "";
                JSONObject jsonResponse;





            }
        }

    }

}
