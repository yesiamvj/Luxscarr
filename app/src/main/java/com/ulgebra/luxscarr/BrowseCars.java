package com.ulgebra.luxscarr;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class BrowseCars extends AppCompatActivity {


    TextView from_to_inp;
    ListView lin;

    public ArrayList<Car_lists> parents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_cars);

        Bundle buns=getIntent().getExtras();
       int frm_day= buns.getInt("frm_day");
       int frm_month= buns.getInt("frm_mnth");
       int frm_year= buns.getInt("frm_year");
       int to_day= buns.getInt("to_day");
        int tomonth=buns.getInt("to_mnth");
        int to_year=buns.getInt("to_year");

        from_to_inp=(TextView)findViewById(R.id.BrowseCarsDateText);

        String book_date=frm_day+"-"+frm_month+"-"+frm_year+" to "+to_day+"-"+tomonth+"-"+to_year;
        from_to_inp.setText(book_date);

        final String sel_url="http://luxscar.com/luxscar_app/view_avl_cars.php";

        new LongOperation().execute(sel_url);









    }

    private class LongOperation  extends AsyncTask<String, Void, Void> {


        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(BrowseCars.this);
        String data ="";
        int sizeData = 0;


        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)

            Dialog.setMessage("Please wait..");
            Dialog.show();

            try{
                Log.v("net_err","data set");
                // Set Request parameter
                data +="&" + URLEncoder.encode("jsk", "UTF-8") + "="+"njshjdshhs";

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.v("net_err","data set err ");
            }

        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            /************ Make Post Call To Web Server ***********/
            BufferedReader reader=null;

            // Send data
            try
            {
                Log.v("net_err","back try");
                // Defined URL  where to send data
                URL url = new URL(urls[0]);

                // Send POST data request

                URLConnection conn = url.openConnection();
                Log.v("net_err","url open");
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( data );
                wr.flush();
                Log.v("net_err","url fin");
                // Get the server response

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line = null;
                String otpt="";

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    otpt+=line;
                    // Append server response in string

                }

                // Append Server Response To Content String
                Content = otpt;
                Log.v("net_err","otpt="+otpt);
               // Toast.makeText(getApplicationContext(),Content,Toast.LENGTH_LONG).show();
            }
            catch(Exception ex)
            {
                Error = ex.getMessage();
                Log.v("net_err","err "+ex.getMessage());
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
                Log.i("net_err", "lh returned");
                return;
            }else{
                Log.i("net_err","lh ok");
            }



            parents = newParents;
            Log.i("net_err","lv");
            // Check for ExpandableListAdapter object
             lin=(ListView) findViewById(R.id.list_hold);

            Log.i("net_err","fea");
            final MyExpandableListAdapter mAdapter = new MyExpandableListAdapter();
            Log.i("net_err","seta");
            // Set Adapter to ExpandableList Adapter

            lin.setAdapter(mAdapter);

            Log.i("net_err", "seta ok");
        }
        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            if (Error != null) {

//                uiUpdate.setText("Output : "+Error);

            } else {

                // Show Response Json On Screen (activity)
  //              uiUpdate.setText( Content );

                /****************** Start Parse Response JSON Data *************/


              String OutputData = "";
                JSONObject jsonResponse;
                final ArrayList<Car_lists> lists=new ArrayList<Car_lists>();

                Log.i("net_err", "json en ok arr");
                try {
                    Log.i("net_err", "try json");
                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                    jsonResponse = new JSONObject(Content);

                    /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                    /*******  Returns null otherwise.  *******/
                    JSONArray jsonMainNode = jsonResponse.optJSONArray("Car_items");
                    Log.i("net_err", "json main");
                    /*********** Process each JSON Node ************/

                    int lengthJsonArr = jsonMainNode.length();

                    for(int i=0; i < lengthJsonArr; i++)
                    {
                        final Car_lists mp=new Car_lists();
                        Log.i("net_err", "foloop json");


                        /****** Get Object for each JSON node.***********/
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                        /******* Fetch node values **********/
                        String name       = jsonChildNode.optString("car_name").toString();
                        String cost_car     = jsonChildNode.optString("cost").toString();
                        String car_images = jsonChildNode.optString("car_image").toString();
                        mp.set_carname(name);
                        mp.setCar_cost(cost_car);
                        mp.setCar_image(car_images);

                        lists.add(mp);



                        //Log.i("JSON parse", song_name);
                    }
                    Log.i("net_err", "out for json");

                    loadHosts(lists);

                    /****************** End Parse Response JSON Data *************/

                    //Show Parsed Output on screen (activity)
                   // jsonParsed.setText( OutputData );


                } catch (JSONException e) {

                    e.printStackTrace();
                    Log.i("net_err", "error json");
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
            h.selc_car=(Button)conView.findViewById(R.id.selc_car);


            //h.car_name_inp.setText(my_parent.getCar_name());
           // h.cost_inp.setText(my_parent.getCar_cost());
            URL myUrl = null;
            try {
                myUrl = new URL("http://luxscar.com/luxscar_app/"+my_parent.getCar_image());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            InputStream inputStream = null;
            try {
                inputStream = (InputStream)myUrl.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Drawable drawable = Drawable.createFromStream(inputStream, null);
          //  h.car_image_inp.setImageDrawable(drawable);

            return conView;

        }

        public class Holder {
            TextView car_name_inp,cost_inp,image_inp;
            Button selc_car;
            ImageView car_image_inp;

        }
    }
}
