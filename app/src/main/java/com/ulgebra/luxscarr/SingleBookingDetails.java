package com.ulgebra.luxscarr;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SingleBookingDetails extends AppCompatActivity {


    String booking_iddd,car_image,cars_name,car_number,ride_form,ride_to,ride_advance,booked_on,ride_total_cost,car_reg_no;

    int car_id,cost,total_cost;
    double adv_amt;

    public ArrayList<Car_lists> parents;

    public ProgressDialog Dialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intentzc = getIntent();


        String booking_idd= intentzc.getStringExtra("booking_idd").substring(1);

        setContentView(R.layout.activity_single_booking_details);

        Dialog= new ProgressDialog(SingleBookingDetails.this);
        Dialog.setMessage("please wait");
        Dialog.show();




        Log.v("booking_id_sent",booking_idd+"");
        String otp_nums = "http://luxscar.com/luxscar_app/SingleBookingDetails.php?booking_id="+booking_idd+"";


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

            Dialog.dismiss();



            if (Error != null) {



            } else {

                // Toast.makeText(getApplicationContext(),Content,Toast.LENGTH_LONG).show();

                // Show Response Json On Screen (activity)
                //uiUpdate.setText( Content );

                /****************** Start Parse Response JSON Data *************/

                String OutputData = "";
                JSONObject jsonResponse;
                final ArrayList<Car_lists> lists=new ArrayList<Car_lists>();
                JSONArray jsonMainNode,jsonMainNode1;

                try {

                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                    jsonResponse = new JSONObject(Content);

                    /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                    /*******  Returns null otherwise.  *******/

                    jsonMainNode1=jsonResponse.optJSONArray("Car_images");

                  //  int car_img_len=jsonMainNode1.length();








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


                         ride_form=jsonChildNode.optString("ride_from").toString();
                         ride_to=jsonChildNode.optString("ride_to").toString();
                         ride_advance=jsonChildNode.optString("ride_advance").toString();
                         booked_on=jsonChildNode.optString("booked_on").toString();
                        booking_iddd=jsonChildNode.optString("booking_id").toString();
                        ride_total_cost=jsonChildNode.optString("ride_price").toString();
                        cars_name       = jsonChildNode.optString("car_name").toString();;
                        cost     = jsonChildNode.optInt("cost");
                        car_reg_no=jsonChildNode.optString("car_no").toString();
                        car_number = jsonChildNode.optString("car_no").toString();
                        car_id=jsonChildNode.optInt("car_id");

                        car_image=jsonChildNode.optString("car_image").toString();


                        Log.i("net_err","tot_cnt="+ride_form);
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
            TextView from_to_inp=(TextView)findViewById(R.id.ride_dure);
            TextView car_name_inp=(TextView)findViewById(R.id.car_brand);
            TextView car_cost=(TextView)findViewById(R.id.car_cost);
            TextView car_regNo=(TextView)findViewById(R.id.ride_carNo);
            TextView bookingHeaderDets=(TextView)findViewById(R.id.bookingHeaderDets);
            ImageView car_image_inp=(ImageView)findViewById(R.id.car_image_inps);
            Button cancel_bookBtn=(Button)findViewById(R.id.cancel_booking);
          //  Button edit_bookingBtn=(Button)findViewById(R.id.edit_booking);
            final TextView adv_amont=(TextView)findViewById(R.id.adv_amount);
            final TextView tot_cost=(TextView)findViewById(R.id.tot_cost);



            cancel_bookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                  Intent intentcn=new Intent(getApplicationContext(),CancelBooking.class);
                    intentcn.putExtra("booking_idd",booking_iddd);
                    startActivity(intentcn);


                }
            });
//            edit_bookingBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intentcn=new Intent(getApplicationContext(),ExtendBooking.class);
//                    intentcn.putExtra("booking_idd",booking_iddd);
//                    intentcn.putExtra("booking_from_date",ride_form);
//                    intentcn.putExtra("booking_to_date",ride_to);
//                    startActivity(intentcn);
//                }
//            });

            bookingHeaderDets.setText("Booking ID : #"+booking_iddd+" On "+booked_on+"");
            from_to_inp.setText("From "+ride_form+" to "+ride_to+"");
            car_name_inp.setText(cars_name);
            car_regNo.setText(car_reg_no);
             tot_cost.setText("Rs. "+ride_total_cost);

            adv_amont.setText("Rs "+ride_advance);
            car_cost.setText("Rs. "+cost+" / per day");


            new ImageLoadTask("http://luxscar.com/luxscar_app/"+car_image, car_image_inp).execute();



        }


    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }



    public class Booking  extends AsyncTask<String, Void, Void> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(SingleBookingDetails.this);
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


                finish();
                Intent intent=new Intent(getApplicationContext(),Welcome.class);
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


}
