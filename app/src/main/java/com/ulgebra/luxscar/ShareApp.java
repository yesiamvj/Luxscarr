package com.ulgebra.luxscar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class ShareApp extends AppCompatActivity {

    AsyncTask<Void, Void, Void> mTask;
    String jsonString,oldCusCha,newCusCha,user_idd;
    String url = "http://luxscar.com/luxscar_app";
    Button b;
    ProgressDialog dialog;
    String url_select;
    TextView txt1,txt2,txt3;
    AutoCompleteTextView newCusTxtA,oldCusTxtA;
    int i,j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_app);

        txt1 = (TextView) findViewById(R.id.oldCusto);

        txt2 = (TextView) findViewById(R.id.newCusto);
        SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
         user_idd = myPrefs.getString("MEM1","");

        Button btn=(Button) findViewById(R.id.share_bbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Sharing LuxsCar app");
                i.putExtra(Intent.EXTRA_TEXT, "Hi , Sign Up & and get discount on Booking rides on LuxsCar app .Please go to http://luxscar.com/app/signUp.php?inviter="+user_idd+"&src=app");
                startActivity(Intent.createChooser(i, "Share URL"));
            }
        });


        url_select = "http://luxscar.com/luxscar_app/sharingOfferDets.php";

        new MyAsyncTask().execute(url_select);


    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),Welcome.class);
        finish();
        startActivity(intent);
    }

    class MyAsyncTask extends AsyncTask<String, String, Void> {

        private ProgressDialog progressDialog = new ProgressDialog(ShareApp.this);
        InputStream inputStream = null;
        String result = "";

        @Override
        protected Void doInBackground(String... params) {




            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try {
                // Set up HTTP post

                // HttpClient is more then less deprecated. Need to change to URLConnection
                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(url_select);
                httpPost.setEntity(new UrlEncodedFormEntity(param));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                // Read content & Log
                inputStream = httpEntity.getContent();
            } catch (UnsupportedEncodingException e1) {
                Log.e("UnsuppongException", e1.toString());
                e1.printStackTrace();
            } catch (ClientProtocolException e2) {
                Log.e("ClientProtocolException", e2.toString());
                e2.printStackTrace();
            } catch (IllegalStateException e3) {
                Log.e("IllegalStateException", e3.toString());
                e3.printStackTrace();
            } catch (IOException e4) {
                Log.e("IOException", e4.toString());
                e4.printStackTrace();
            }
            // Convert response to string using String Builder
            try {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }

                inputStream.close();
                result = sBuilder.toString();

                Log.v("fetched result",result);


            } catch (Exception e) {
                Log.e("StrgBudg & BufredRdr", "Error converting result " + e.toString());
            }
            return null;
        }


        protected void onPreExecute() {

            progressDialog.setMessage("Please wait ...");
            progressDialog.show();

        }
        protected void onPostExecute(Void v) {


            //parse JSON data
            try {
                JSONArray jArray = new JSONArray(result);
                for(i=0; i < jArray.length(); i++) {

                    JSONObject jObject = jArray.getJSONObject(i);

                    String oldCutAmt = jObject.getString("old_customer");
                    String newCusAmt = jObject.getString("new_customer");

                    txt1.setText("Rs."+oldCutAmt);
                    txt2.setText("Rs."+newCusAmt);



                }
                this.progressDialog.dismiss();
            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            }
        }
    }


}
