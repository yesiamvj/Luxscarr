package com.ulgebra.luxscarr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class BookingHistory extends AppCompatActivity {


    public ArrayList<Car_lists> parents;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        //linearLayout=(LinearLayout)findViewById(R.id.all_history_hold);
        SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
        String user_id = myPrefs.getString("MEM1","");


        Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_LONG).show();
        String hitr_url="http://luxscar.com/luxscar_app/show_hstry.php?user_id="+user_id;

        new LongOperation().execute(hitr_url);

    }

    public class LongOperation  extends AsyncTask<String, Void, Void> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(BookingHistory.this);
        String data ="";
        String otpt="";



        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)

            Dialog.setMessage("Please wait...");
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
            try {
                JSONObject jsonResponse;
                Log.i("net_err", "try json");
                /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                jsonResponse = new JSONObject(Content);

                /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                /*******  Returns null otherwise.  *******/
                JSONArray jsonMainNode = jsonResponse.optJSONArray("Hstry_items");
                Log.i("net_err", "json main");
                /*********** Process each JSON Node ************/

                int lengthJsonArr = jsonMainNode.length();
                final ArrayList<Car_lists> lists=new ArrayList<Car_lists>();
                for(int i=0; i < lengthJsonArr; i++)
                {
                    final Car_lists mp=new Car_lists();
                    Log.i("net_err", "foloop json");


                    /****** Get Object for each JSON node.***********/
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    /******* Fetch node values **********/
                    String name       = jsonChildNode.optString("car_name").toString();
                    String ride     = jsonChildNode.optString("ride").toString();
                    String id = jsonChildNode.optString("booking_id").toString();

                    mp.set_carname(name);
                    mp.setRide_dure(ride);
                    mp.setBooking_id(id);

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
            if(otpt.hashCode()==0){
                Toast.makeText(getApplicationContext(),"Check your Internet Connection",Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(getApplicationContext(),otpt,Toast.LENGTH_LONG).show();

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
    public void loadHosts(final ArrayList<Car_lists> newParents)
    {
        if (newParents == null){
            Log.i("net_err", "lh returned");
            return;
        }else{
            Log.i("net_err","lh ok");
        }



        parents = newParents;


        for(int i=0;i<parents.size();i++){
            final Car_lists my_parent=parents.get(i);
            View vi= LayoutInflater.from(this).inflate(R.layout.single_history,null);

            TextView bk_no_inp=(TextView)vi.findViewById(R.id.booking_no);
            TextView bk_ca_num_inp=(TextView)vi.findViewById(R.id.bk_car_name);
            TextView bk_ride_dur=(TextView) vi.findViewById(R.id.bk_duration);
            LinearLayout holder_inp=(LinearLayout)vi.findViewById(R.id.single_hstry_hold);

            bk_no_inp.setText(my_parent.getBooking_id());
            bk_ca_num_inp.setText(my_parent.getCar_name());



            holder_inp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),BookingDetails.class);



                    intent.putExtra("booking_idd",my_parent.getBooking_id());

                    startActivity(intent);
                }
            });





            linearLayout.addView(vi);

        }


    }

}
