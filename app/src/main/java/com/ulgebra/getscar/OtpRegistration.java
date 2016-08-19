package com.ulgebra.getscar;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
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

public class OtpRegistration extends AppCompatActivity {

    AutoCompleteTextView reg_otp_inp;
    Button resen_otp_inp,reg_otp_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_registration);
        reg_otp_inp=(AutoCompleteTextView)findViewById(R.id.reg_otp);
        resen_otp_inp=(Button)findViewById(R.id.resend_otp_btn);
        reg_otp_btn=(Button)findViewById(R.id.otp_reg_con);
        Bundle buns=getIntent().getExtras();
        final String email_ids=buns.getString("email_ids");

resen_otp_inp.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        try {


            String otp_nums="http://luxscar.com/luxscar_app/resend_otp.php?";

            otp_nums+=URLEncoder.encode("email_id","UTF-8")+"="+email_ids;

            final String otp_url=otp_nums;
            new LongOperation().execute(otp_url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
});
        reg_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  boolean check_all=false;

                if(TextUtils.isEmpty(reg_otp_inp.getText())){
                    reg_otp_inp.setError("Enter OTP");
                    reg_otp_inp.requestFocus();
                    check_all=true;
                }

                if(!check_all){
                    try {


                        String otp_nums="http://luxscar.com/luxscar_app/enter_otp.php?";

                        otp_nums+= URLEncoder.encode("otp_num","UTF-8")+"="+reg_otp_inp.getText().toString();
                        otp_nums+="&"+ URLEncoder.encode("email_id","UTF-8")+"="+email_ids;

                        final String otp_url=otp_nums;
                        new LongOperation().execute(otp_url);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }



            }
        });

    }


    public class LongOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(OtpRegistration.this);
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

            if(otpt.contains("Login Successfull.")){

                SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.putString("MEM1", otpt.substring(18,otpt.length()));
                editor.commit();


                String user_idd = myPrefs.getString("MEM1","");
                Toast.makeText(getApplicationContext(), "Welcome to GetsCar...", Toast.LENGTH_LONG).show();

                Intent intent=new Intent(getApplicationContext(),Welcome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);

            }else if(otpt.hashCode()==("Successfully Sent").hashCode()) {

                Toast.makeText(getApplicationContext(),"Successfully Sent OTP", Toast.LENGTH_LONG).show();
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
    public void setOOT(String string){
        reg_otp_inp.setText(string);
    }
    public static class SmsListener extends BroadcastReceiver{

        private SharedPreferences preferences;

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
                Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
                SmsMessage[] msgs = null;
                String msg_from;
                if (bundle != null){
                    //---retrieve the SMS message received---
                    try{
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        for(int i=0; i<msgs.length; i++){
                            msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                            msg_from = msgs[i].getOriginatingAddress();
                            String msgBody = msgs[i].getMessageBody();
                            Log.v("sendd",msg_from);

                            if(msg_from.toString().equals("IM-LXSCAR")){
                               String otp_numfetch=msgBody.substring(msgBody.length()-4,msgBody.length());

                                Toast.makeText(context, "Your OTP is "+otp_numfetch, Toast.LENGTH_LONG).show();
                            }

                                                    }
                    }catch(Exception e){
//                            Log.d("Exception caught",e.getMessage());
                    }
                }
            }
        }
    }
}
