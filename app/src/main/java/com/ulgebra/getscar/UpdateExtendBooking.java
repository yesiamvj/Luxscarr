package com.ulgebra.getscar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class UpdateExtendBooking extends AppCompatActivity {


    String book_date,bookFroom,bookTo,booking_iddd,car_image,cars_name,car_number,ride_form,ride_to,ride_advance,booked_on,ride_total_cost,car_reg_no;

    int car_id,cost,total_cost,already_paid_adv,total_amnt;
    double adv_amt,new_adv_amt,extra_adv;

    public ArrayList<Car_lists> parents;

    public ProgressDialog Dialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intentzc = getIntent();
        Bundle buns=getIntent().getExtras();
        String booking_idd= intentzc.getStringExtra("booking_idd");
       ////  bookFroom=intentzc.getIntExtra("frm_day",0)+"-"+intentzc.getIntExtra("frm_month",0)+"-"+intentzc.getIntExtra("frm_year",0);
         bookTo=intentzc.getIntExtra("to_day",0)+"-"+intentzc.getIntExtra("to_month",0)+"-"+intentzc.getIntExtra("to_year",0);
        int frm_day= buns.getInt("frm_day");
        int frm_month= buns.getInt("frm_mnth");
        int frm_year= buns.getInt("frm_year");
        int to_day= buns.getInt("to_day");
        int tomonth=buns.getInt("to_mnth");
        int to_year=buns.getInt("to_year");
        String froom=buns.getString("froom");
        book_date=froom+" to "+to_day+"-"+tomonth+"-"+to_year;

        setContentView(R.layout.activity_update_extend_booking);
        //TextView from_to_inp=(TextView)findViewById(R.id.ride_dure);


        Dialog= new ProgressDialog(UpdateExtendBooking.this);
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
           // Button cancel_bookBtn=(Button)findViewById(R.id.cancel_booking);
            Button extnd_bookingBtn=(Button)findViewById(R.id.extnd_booking);
            final TextView adv_amont=(TextView)findViewById(R.id.adv_amount);
            final TextView tot_cost=(TextView)findViewById(R.id.tot_cost);
            final TextView ok_adv_amont=(TextView)findViewById(R.id.ok_adv_amount);
            final TextView extra_adv_cost=(TextView)findViewById(R.id.extra_adv_amount);

            Bundle buns=getIntent().getExtras();
            int frm_day= buns.getInt("frm_day");
            int frm_month= buns.getInt("frm_mnth");
            String frm_year= buns.getString("frm_year");

            Log.v("book from detils","day"+frm_day+"mon"+frm_month+"yr"+frm_year);
            int to_day= buns.getInt("to_day");
            int tomonth=buns.getInt("to_mnth");
            int to_year=buns.getInt("to_year");
            final int diff_day=calc_diff(frm_day,frm_month,Integer.parseInt(frm_year),to_day,tomonth,to_year);

            total_cost=(diff_day+1)*cost;


            //already paid advance

            adv_amt=Integer.parseInt(ride_advance);

            //new advance amount

            new_adv_amt=(0.25)*total_cost;

            //amount to pay extra

            extra_adv=new_adv_amt-adv_amt;
            tot_cost.setText("Rs. "+total_cost);
            adv_amont.setText("Rs "+new_adv_amt);
            ok_adv_amont.setText(adv_amt+"");
            extra_adv_cost.setText(extra_adv+"");
            car_cost.setText("Rs. "+cost+" / per day");



            extnd_bookingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle buns=getIntent().getExtras();
                    int frm_day= buns.getInt("frm_day");
                    int frm_month= buns.getInt("frm_mnth");
                    String frm_year= buns.getString("frm_year");

                    Log.v("book from detils","day"+frm_day+"mon"+frm_month+"yr"+frm_year);
                    int to_day= buns.getInt("to_day");
                    int tomonth=buns.getInt("to_mnth");
                    int to_year=buns.getInt("to_year");

                    String booking_url = "http://luxscar.com/luxscar_app/extendBooking.php?";

                    String bookt_date=to_day+"-"+tomonth+"-"+to_year;

                    Log.v("car_cost",cost+"");
                    //total cost for the car
                    total_cost=(diff_day+1)*cost;


                    //already paid advance

                    adv_amt=Integer.parseInt(ride_advance);

                    //new advance amount

                    new_adv_amt=(0.25)*total_cost;

                    //amount to pay extra

                    extra_adv=new_adv_amt-adv_amt;

                    Log.v("diffodate",diff_day+"");



                    String my_data="";
                    SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
                    String user_id = myPrefs.getString("MEM1","");

                    try {
                        my_data+="&"+URLEncoder.encode("booking_id","UTF-8")+"="+booking_iddd;
                        my_data+="&"+URLEncoder.encode("user_id","UTF-8")+"="+user_id;
                        my_data+="&"+URLEncoder.encode("bookt_date","UTF-8")+"="+bookt_date;
                        my_data+="&"+URLEncoder.encode("ride_price","UTF-8")+"="+total_cost;
                        my_data+="&"+URLEncoder.encode("ride_advance","UTF-8")+"="+new_adv_amt;
                        my_data+="&"+URLEncoder.encode("extra_to_pay","UTF-8")+"="+extra_adv;

                        my_data+="&"+URLEncoder.encode("editings","UTF-8");
                        my_data+="="+URLEncoder.encode("Extended for "+diff_day+" days ."+" Total = "+total_cost+" , new Advance ="+new_adv_amt+"" +
                                " Already Paid ="+adv_amt+", Amount to Pay Extra ="+extra_adv+"","UTF-8");
                        my_data+="&"+URLEncoder.encode("JSK","UTF-8")+"="+"skjdkskdskjhkjn";
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    booking_url+=my_data;
                    Log.v("booking_url",booking_url);
                    new Booking().execute(booking_url);
                }
            });

            bookingHeaderDets.setText("Booking ID : #"+booking_iddd+" On "+booked_on+"");
            from_to_inp.setText(book_date);
           // from_to_inp.setText("From "+ride_form+" to "+ride_to+"");
            car_name_inp.setText(cars_name);
            car_regNo.setText(car_reg_no);


            //tot_cost.setText("Rs. "+ride_total_cost);





            new ImageLoadTask("http://luxscar.com/luxscar_app/"+car_image, car_image_inp).execute();



        }


    }
    public int calc_diff(int f_d,int f_m,int f_y,int to_d,int to_m,int to_y){




        int cur_yr=f_y;
        int  cur_mnth=f_m;
        int  cur_date=f_d;



        int alm_yr=to_y;
        int alm_mnth=to_m;
        int alm_date=to_d;


        int y,m,d,tot_day=0,diff_mnt,frm_mnth,to_mnth,from_day,to_day,from_yr,en=0;

        int fin_yr=alm_yr;
        int fin_mnth=alm_mnth;
        int fin_day=alm_date;

        from_yr=cur_yr;

        for(y=from_yr;y<=fin_yr;y++){
//alert(y);
            en=en+1;
            //  alert(en);
            if(en==1){
                frm_mnth=cur_mnth;
                from_day=cur_date;
                if(y==fin_yr){
                    to_mnth=fin_mnth;
                }else{
                    to_mnth=12;
                }

            }else{

                if(y==fin_yr){

                    to_mnth=fin_mnth;
                    to_day=fin_day;

                    if(en==1){
                        frm_mnth=f_m;
                    }else{
                        frm_mnth=1;
                    }
                    //alert(to_mnth+ " ti "+to_day +" "+frm_mnth);
                }else{
                    to_mnth=12;
                    frm_mnth=1;
                }

            }


            for(m=frm_mnth;m<=to_mnth;m++){
                //  alert(frm_mnth+" "+to_mnth);
                switch(m){
                    case 1:
                        diff_mnt=31;
                        break;
                    case 2:
                        if(y%4==0){

                            diff_mnt=29;
                        }else
                        {
                            diff_mnt=28;
                        }

                        break;
                    case 3:
                        diff_mnt=31;
                        break;
                    case 4:
                        diff_mnt=30;
                        break;
                    case 5:
                        diff_mnt=31;
                        break;
                    case 6:
                        diff_mnt=30;
                        break;
                    case 7:
                        diff_mnt=31;
                        break;
                    case 8:
                        diff_mnt=31;
                        break;
                    case 9:
                        diff_mnt=30;
                        break;
                    case 10:
                        diff_mnt=31;
                        break;
                    case 11:
                        diff_mnt=30;
                        break;
                    case 12:
                        diff_mnt=31;
                        break;

                    default:
                        diff_mnt=30;
                        break;

                }


                if(y==fin_yr && m==fin_mnth){
                    to_day=fin_day;

                }else{
                    to_day=diff_mnt;

                }

                if(en==1 && m==frm_mnth){

                    if(frm_mnth!=to_mnth){
                        to_day=diff_mnt - f_d;
                    }else{


                        to_day=(fin_day-f_d);
                    }
                }else{
                    from_day=1;
                }


                if(y==fin_yr && m==fin_mnth){
                    tot_day+=to_day;
                }else{
                    tot_day+=to_day;

                }



            }
            // console.log("year day="+tot_day);
        }
        // tot_day=tot_day-1;
        return tot_day;


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
        private ProgressDialog Dialog = new ProgressDialog(UpdateExtendBooking.this);
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


                Intent intent=new Intent(getApplicationContext(),SingleBookingDetails.class);
                intent.putExtra("booking_idd","#"+booking_iddd);


                Toast.makeText(getApplicationContext(),otpt,Toast.LENGTH_LONG).show();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();


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
