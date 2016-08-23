package com.ulgebra.getscar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class SingleCarDetail extends AppCompatActivity {


    public ProgressDialog dialog;
    ListView listView;
    String cars_name,cost,car_id,car_number,xtra_km,xtra_hr;

    String[] all_imgs;

    public ArrayList<Car_lists> parents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_car_detail);

        Intent intentzc = getIntent();
        int car_id_input=intentzc.getIntExtra("car_id",0);


        dialog = new ProgressDialog(SingleCarDetail.this);
        dialog.setMessage("Please wait...");
        dialog.show();

        Log.v("net_res","car id="+car_id_input);
        String otp_nums = "http://luxscar.com/luxscar_app/single_car_dtl.php?car_id="+car_id_input;


        final String otp_url = otp_nums;
        new LongOperation().execute(otp_url);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




    }
    @Override
    public void onBackPressed() {

      //  Toast.makeText(getApplicationContext(),"extt",Toast.LENGTH_LONG).show();


        Intent intent = new Intent(getApplicationContext(),chekAct.class);
        Bundle buns=getIntent().getExtras();
        int frm_day= buns.getInt("frm_day");
        int frm_month= buns.getInt("frm_mnth");
        int frm_year= buns.getInt("frm_year");
        int to_day= buns.getInt("to_day");
        int tomonth=buns.getInt("to_mnth");
        int to_year=buns.getInt("to_year");
        String from_time= buns.getString("from_time");
        String to_time= buns.getString("to_time");
        int from_to_diff=buns.getInt("from_to_diff");
        intent.putExtra("frm_day",frm_day);
        intent.putExtra("frm_mnth",frm_month);
        intent.putExtra("frm_year",frm_year);
        intent.putExtra("to_day",to_day);
        intent.putExtra("to_mnth",tomonth);
        intent.putExtra("to_year",to_year);
        intent.putExtra("from_time",from_time);
        intent.putExtra("to_time",to_time);
        intent.putExtra("from_to_diff",from_to_diff);
        finish();
        startActivity(intent);
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
            // dialog.show();

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

            dialog.dismiss();



            if (Error != null) {



            } else {



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

                    int car_img_len=jsonMainNode1.length();

                    for(int i=0;i<car_img_len;i++){
                        final Car_lists mp=new Car_lists();


                        JSONObject jsonChildNode1 = jsonMainNode1.getJSONObject(i);

                        String car_image=jsonChildNode1.optString("car_image").toString();
                        // all_imgs[i]=jsonChildNode1.optString("car_image").toString();;

                        if(car_image=="no-image"){
                            mp.setCar_image("no-image");
                        }
                        else {
                            mp.setCar_image(car_image);
                        }






                        Log.i("img_cnt",car_image+" @ "+i);
                        lists.add(mp);

                    }


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
                        cars_name       = jsonChildNode.optString("car_name").toString();;
                        cost     = jsonChildNode.optString("cost").toString();
                        car_number = jsonChildNode.optString("car_no").toString();
                        car_id=jsonChildNode.optString("car_id");
                        xtra_hr=jsonChildNode.optString("xtra_hr");
                        xtra_km=jsonChildNode.optString("xtra_km");







                        Log.i("net_err","tot_cnt="+i);
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
            Log.i("err", "returned");
            return;
        }else{
            Log.i("err","ok");
        }



        parents = newParents;
        Log.i("err","lv");
        // Check for ExpandableListAdapter object

        Log.i("err","fea");

        TextView car_name_inp=(TextView)findViewById(R.id.car_brand_name);
        car_name_inp.setText(cars_name);

        Log.v("net_res","car name="+cars_name);
        Log.v("net_res","car no="+car_number);
        Log.v("net_res","car cost="+cost);

        TextView cost_inp=(TextView)findViewById(R.id.car_cost);
        TextView xtra_km_txt=(TextView)findViewById(R.id.xtra_km_cost);
        TextView xtra_hr_txt=(TextView)findViewById(R.id.xtra_hr_cost);
        cost_inp.setText("RS "+cost+" / per day");
        xtra_km_txt.setText("Rs "+xtra_km+" / per Km");
        xtra_hr_txt.setText("Rs "+xtra_hr+" / per Hour");
        TextView caar_num_inp=(TextView)findViewById(R.id.car_number);
        caar_num_inp.setText(car_number);
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.img_lin_cont);


        for(int i=0;i<parents.size();i++){
            final Car_lists my_parent = parents.get(i);
            View view = LayoutInflater.from(this).inflate(R.layout.single_car_img_dtl,null);
            ImageView imgs=(ImageView)view.findViewById(R.id.single_car_image);

            if(my_parent.getCar_image().equals("no-image")){

            }
            else {
                Log.v("net_img_c",my_parent.getCar_image().length()+"");
                new ImageLoadTask("http://luxscar.com/luxscar_app/"+my_parent.getCar_image(), imgs).execute();

            }

            linearLayout.addView(view);
        }


    }
    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public Bitmap bitmap;

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

                Log.v("url_open",url);
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                bitmap=myBitmap;
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            Log.v("img_bitmap",bitmap.toString());
            imageView.setImageBitmap(result);

        }

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("bit_map_err", "Error getting bitmap", e);
        }
        return bm;
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
            dialog = new ProgressDialog(SingleCarDetail.this);
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

//                    Intent intent=new Intent(getApplicationContext(),Cars.class);
//                    startActivity(intent);

                    Toast.makeText(getApplicationContext(),otpt,Toast.LENGTH_LONG).show();

                }
            }

        }

    }


}
