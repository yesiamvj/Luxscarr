package com.ulgebra.luxscar;

/**
 * Created by Vijayakumar on 08/07/2016.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class Check_listview extends Activity
{



    public ArrayList<Car_lists> parents;
    ListView listView;

    TextView from_to_inp;
    private int ChildClickStatus=-1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_cars);



        Intent intentz=getIntent();
        int frm_day= intentz.getIntExtra("frm_day",0);
        int frm_month= intentz.getIntExtra("frm_mnth",0);
        int frm_year= intentz.getIntExtra("frm_year",0);
        int to_day= intentz.getIntExtra("to_day",0);
        int tomonth=intentz.getIntExtra("to_mnth",0);
        int to_year=intentz.getIntExtra("to_year",0);

        from_to_inp=(TextView)findViewById(R.id.BrowseCarsDateText);

        String dt="From "+frm_day+"."+frm_month+"."+frm_year+" to "+to_day+"."+tomonth+"."+to_year;
        from_to_inp.setText(dt);
        final Button GetServerData = (Button) findViewById(R.id.GetServerData);

        String serverURL = "http://luxscar.com/luxscar_app/view_avl_cars.php";

        // Use AsyncTask execute Method To Prevent ANR Problem
        new LongOperation().execute(serverURL);

    }


    // Class with extends AsyncTask class
    private class LongOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(Check_listview.this);
        String data ="";
        int sizeData = 0;


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

        private void loadHosts(final ArrayList<Car_lists> newParents)
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
            listView=(ListView)findViewById(R.id.list_hold);

            Log.i("err","fea");
            final MyExpandableListAdapter mAdapter = new MyExpandableListAdapter();
            Log.i("err","seta"+parents);
            // Set Adapter to ExpandableList Adapter
            listView.setAdapter(mAdapter);
            Log.i("err", "seta ok");
        }
        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            if (Error != null) {



            } else {

                // Show Response Json On Screen (activity)
                //uiUpdate.setText( Content );

                /****************** Start Parse Response JSON Data *************/

                String OutputData = "";
                JSONObject jsonResponse;
                final ArrayList<Car_lists> lists=new ArrayList<Car_lists>();


                try {

                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                    jsonResponse = new JSONObject(Content);

                    /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                    /*******  Returns null otherwise.  *******/
                    JSONArray jsonMainNode = jsonResponse.optJSONArray("Car_items");

                    /*********** Process each JSON Node ************/

                    int lengthJsonArr = jsonMainNode.length();

                    for(int i=0; i < lengthJsonArr; i++)
                    {
                        final Car_lists mp=new Car_lists();


                        /****** Get Object for each JSON node.***********/
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        /******* Fetch node values **********/
                        String name       = jsonChildNode.optString("car_name").toString();
                        String cost     = jsonChildNode.optString("cost").toString();
                        String c_image = jsonChildNode.optString("car_image").toString();
                        int car_id=jsonChildNode.optInt("car_id");
                        mp.set_carname(name);
                        mp.setCar_cost(cost);
                        mp.setCar_id(car_id);
                        mp.setCar_image(c_image);

                        lists.add(mp);



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

    private class MyExpandableListAdapter extends BaseAdapter {


        @Override
        public int getCount() {


            return parents.size();


        }

        @Override
        public Object getItem(int position) {
            return parents.get(position);
        }

        @Override
        public long getItemId(int position) {
            //***** When Child row clicked then this function call ******

            //Log.i("Noise", "parent == "+groupPosition+"=  child : =="+childPosition);
            if( ChildClickStatus!=position)
            {
                ChildClickStatus = position;


            }

            return position;
        }

        @Override

        public View getView(int groupPosition, View conView, ViewGroup parent) {

            final Car_lists my_parent = parents.get(groupPosition);

            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            conView = inflater.inflate(R.layout.sigle_car_container, parent,false);
            Holder h = new Holder();
            conView.setTag(h);
            h.car_name_inp = (TextView) conView.findViewById(R.id.car_name);
            h.cost_inp = (TextView) conView.findViewById(R.id.car_cost);
            h.selc_car_inp=(Button)conView.findViewById(R.id.selc_car);
            h.car_image_inp=(ImageView)conView.findViewById(R.id.car_image);



            h.car_name_inp.setText(my_parent.getCar_name());
             h.cost_inp.setText(my_parent.getCar_cost());


            new ImageLoadTask("http://luxscar.com/luxscar_app/"+my_parent.getCar_image(), h.car_image_inp).execute();

                Log.v("net_err","set ");

            return conView;

        }


        public class Holder {
            TextView car_name_inp,cost_inp,image_inp;
            Button selc_car_inp;
            ImageView car_image_inp;

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


}

