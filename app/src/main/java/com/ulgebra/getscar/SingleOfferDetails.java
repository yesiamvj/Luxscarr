package com.ulgebra.getscar;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class SingleOfferDetails extends AppCompatActivity {


    String minPurAmt,maxDisAmt,disPercent,disPrice,expiryDate,addedOn,coupon_code;
    int offer_id;
    double adv_amt;
    public ProgressDialog dialog;

    public ArrayList<Offers> parents;

    public ProgressDialog Dialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intentzc = getIntent();


        final String booking_idd= intentzc.getStringExtra("user_idd");

        setContentView(R.layout.activity_single_offer_details);

        Log.v("offer_id",booking_idd);

        Dialog= new ProgressDialog(SingleOfferDetails.this);
        Dialog.setMessage("please wait");
        Dialog.show();




        Log.v("booking_id_sent",booking_idd+"");
        String otp_nums = "http://luxscar.com/luxscar_app/SingleOfferDetails.php?offer_id="+booking_idd+"";
        Log.v("booking_id_sent",otp_nums+"");

        final String otp_url = otp_nums;
        new LongOperation().execute(otp_url);

    }
    private class LongOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;

        String data ="";
        int sizeData = 0;

        // Log.v("net_err","dlg");
        protected void onPreExecute() {


            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)


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
                wr.write( data );
                wr.flush();

                // Get the server response

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line + "\n");
                }

                // Append Server Response To Content String
                Content = sb.toString();
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

            Log.v("net_res","enter");
            Dialog.dismiss();



            if (Error != null) {

                Log.v("net_res",Error);

            } else {
                Log.v("net_res","enter ok");
                // Toast.makeText(getApplicationContext(),Content,Toast.LENGTH_LONG).show();

                // Show Response Json On Screen (activity)
                //uiUpdate.setText( Content );

                /****************** Start Parse Response JSON Data *************/

                String OutputData = "";
                JSONObject jsonResponse;
                final ArrayList<Car_lists> lists=new ArrayList<Car_lists>();
                JSONArray jsonMainNode,jsonMainNode1;

                try {

                    Log.v("net_res","enter okl");

                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                    jsonResponse = new JSONObject(Content);

                    /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                    /*******  Returns null otherwise.  *******/


                    //  int car_img_len=jsonMainNode1.length();






                    Log.v("net_res","enter okll");

                    jsonMainNode = jsonResponse.optJSONArray("Car_items");

                    /*********** Process each JSON Node ************/

                    int lengthJsonArr = jsonMainNode.length();


                    Log.v("net_res","json len="+lengthJsonArr);
                    for(int i=0; i < lengthJsonArr; i++)
                    {
                        final Car_lists mp=new Car_lists();


                        /****** Get Object for each JSON node.***********/
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        /******* Fetch node values **********/


                        offer_id=jsonChildNode.optInt("offer_id");
                        coupon_code=jsonChildNode.optString("coupon_code").toString();
                        minPurAmt=jsonChildNode.optString("minPurAmt").toString();
                        maxDisAmt=jsonChildNode.optString("maxDisAmt").toString();
                        disPercent=jsonChildNode.optString("disPercent").toString();

                        expiryDate=jsonChildNode.optString("expiryDate").toString();
                        addedOn=jsonChildNode.optString("addedOn").toString();





                    }

                    loadHosts(lists);

                    /****************** End Parse Response JSON Data *************/

                    //Show Parsed Output on screen (activity)


                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }
        }

    }

    public void loadHosts(final ArrayList<Car_lists> newParents)
    {
        if (newParents == null){
            Log.i("net_err", "lh returned");
            return;
        }else{
            Log.i("net_err","lh ok");
        }



        //  Log.v("car_name",car_image);


        for(int i=0;i<1;i++){
            TextView userNameTxt=(TextView)findViewById(R.id.ride_username);
            TextView usermobTxt=(TextView)findViewById(R.id.usermob);
            TextView usermail=(TextView)findViewById(R.id.usermail);
            TextView userlic=(TextView)findViewById(R.id.userlic);
            TextView ride_totBook=(TextView)findViewById(R.id.ride_totBook);
            TextView ride_regDate=(TextView)findViewById(R.id.ride_regDate);
            TextView ride_userId=(TextView)findViewById(R.id.bookingHeaderDets);
            userNameTxt.setText(coupon_code);
            usermobTxt.setText("Rs."+minPurAmt);
            usermail.setText("Rs."+maxDisAmt);
            userlic.setText(disPercent);
            ride_totBook.setText(expiryDate);
            ride_regDate.setText(addedOn);
            ride_userId.setText("Offer ID #"+offer_id);
            userNameTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("LUXSCOO", coupon_code);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(),"Coupon Code Copied",Toast.LENGTH_LONG).show();
                }
            });



        }


    }




    public class Booking  extends AsyncTask<String, Void, Void> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(SingleOfferDetails.this);
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


            if(otpt.hashCode()==0){
                Toast.makeText(getApplicationContext(),"Check your Internet Connection",Toast.LENGTH_LONG).show();

            }else{


                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),otpt,Toast.LENGTH_LONG).show();

            }

            if (Error != null) {

                // uiUpdate.setText("Output : "+Error);
                //Log.i("my_err",Content);
            } else {

                // Show Response Json On Screen (activity)
                // uiUpdate.setText( Content );

                /****************** Start Parse Response JSON Data *************/

                String OutputData = "";
                JSONObject jsonResponse;





            }
        }

    }
    public class DeltOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        String data ="";
        String otpt="";



        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)
            dialog = new ProgressDialog(SingleOfferDetails.this);
            dialog.setMessage("Please wait...");
            dialog.show();



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
            dialog.dismiss();


            if(otpt.hashCode()==("Successfully Registered").hashCode()){


            }else {

                if(otpt.hashCode()==0){
                    Toast.makeText(getApplicationContext(),"Check your Internet Connection",Toast.LENGTH_LONG).show();

                }else{

                    Intent intent=new Intent(getApplicationContext(),AllOffers.class);
                    finish();
                    startActivity(intent);

                    Toast.makeText(getApplicationContext(),otpt,Toast.LENGTH_LONG).show();

                }
            }

        }

    }


}
