package com.ulgebra.getscar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class ForgotPassStep1 extends AppCompatActivity {


        Button subt_email_inp,goto_signin_inp;

    AutoCompleteTextView email_inp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_step1);

        subt_email_inp=(Button)findViewById(R.id.subt_email_btn);
        email_inp=(AutoCompleteTextView)findViewById(R.id.frgt_email);
        goto_signin_inp=(Button)findViewById(R.id.goto_signin);
        goto_signin_inp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ints=new Intent(getApplicationContext(),Login_user.class);
                startActivity(ints);
            }
        });
        subt_email_inp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean check_all=false;
                if(TextUtils.isEmpty(email_inp.getText())){
                    email_inp.setError("Required");
                    email_inp.requestFocus();
                    check_all=true;
                }

                if(!check_all){
                    try {


                        String otp_nums="http://luxscar.com/luxscar_app/forgot_pass.php?";

                        otp_nums+= URLEncoder.encode("email_id","UTF-8")+"="+email_inp.getText();

                        final String otp_url=otp_nums;
                        new LongOperation().execute(otp_url);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }else{
                    Snackbar.make(v, "Please Enter an Email", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),Login_user.class);
        finish();
        startActivity(intent);
    }


    public class LongOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(ForgotPassStep1.this);
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
                Log.i("my_err","goto");
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

            if(otpt.hashCode()==("Password sent your mail").hashCode()){
                Intent ints=new Intent(getApplicationContext(),forgotPasswordFinal.class);
                startActivity(ints);
                Toast.makeText(getApplicationContext(),"Link sent your Email",Toast.LENGTH_LONG).show();

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
