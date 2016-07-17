package com.ulgebra.luxscarr;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Login_user extends AppCompatActivity {

    AutoCompleteTextView user_inp,pass_inp;
    Button sign_inp,frgt_pass_btn_inp,sign_up_btn_inp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        user_inp=(AutoCompleteTextView)findViewById(R.id.login_username);
        pass_inp=(AutoCompleteTextView)findViewById(R.id.login_password);
        sign_inp=(Button)findViewById(R.id.login_btn);
        frgt_pass_btn_inp=(Button)findViewById(R.id.frgt_pass_btn);
        sign_up_btn_inp=(Button)findViewById(R.id.sign_up_btn);
        sign_up_btn_inp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ints=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(ints);
            }
        });
        frgt_pass_btn_inp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent ints=new Intent(getApplicationContext(),ForgotPassStep1.class);
                    startActivity(ints);

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"err"+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        sign_inp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean check_all=false;

                if(TextUtils.isEmpty(user_inp.getText())){
                    user_inp.setError("Required");
                    user_inp.requestFocus();
                    check_all=true;
                }
                if(TextUtils.isEmpty(pass_inp.getText())){
                    pass_inp.setError("Required");
                    pass_inp.requestFocus();
                    check_all=true;
                }

                if(!check_all){
                    try {


                        String otp_nums = "http://luxscar.com/luxscar_app/user_login.php?";

                        otp_nums += URLEncoder.encode("user_name", "UTF-8") + "=" + user_inp.getText();
                        otp_nums += "&" + URLEncoder.encode("pass_word", "UTF-8") + "=" + pass_inp.getText();

                        final String otp_url = otp_nums;
                        new LongOperation().execute(otp_url);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }else{
                    Snackbar.make(v, "Please fill in all Fields", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }
        });
    }


    public class LongOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(Login_user.this);
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
                Log.i("my_err", "goto");
                // Defined URL  where to send data
                URL url = new URL(urls[0]);

                // Send POST data request

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( "" );
                wr.flush();

                Log.i("my_err", "ouput");
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
                    Log.i("my_err","error="+e.getMessage());
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

            if(otpt.contains("Login Successfull.")){

                SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.putString("MEM1", otpt.substring(18,otpt.length()));
                editor.commit();


                String user_idd = myPrefs.getString("MEM1","");
                Toast.makeText(getApplicationContext(), "Login Successful ", Toast.LENGTH_LONG).show();

                Intent intent=new Intent(getApplicationContext(),Welcome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);

            }else if(otpt.hashCode()==("Activate your Account by OTP").hashCode()) {
                Bundle bun=new Bundle();
                bun.putString("email_ids",user_inp.getText().toString());

                Intent intent=new Intent(getApplicationContext(),Activate_Account.class);
                intent.putExtras(bun);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Activate your Account by OTP", Toast.LENGTH_LONG).show();
            }else{
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
