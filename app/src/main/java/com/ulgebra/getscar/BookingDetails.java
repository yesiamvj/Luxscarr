package com.ulgebra.getscar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Calendar;

public class BookingDetails extends AppCompatActivity {


    String car_image,cars_name,car_number,pickUpLocationOther,pickUpLoate,coopCode,from_timeF,to_timeF;

    int car_id,cost,total_cost,fdiff_date;
    double adv_amt;
    Spinner spinner;
    Button editText;
    TextView txt1,txt2;
    boolean check_all=false;
    private TimePicker timePicker1;

    public ArrayList<Car_lists> parents;

    public ProgressDialog Dialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txt1=(TextView) findViewById(R.id.otherPickUpLoc);
        txt2=(TextView)findViewById(R.id.extraPickCost);
        spinner = (Spinner) findViewById(R.id.pickUpAt);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.pickUps, R.layout.single_spin_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position==5){

                    txt1.setVisibility(View.VISIBLE);
                    txt2.setVisibility(View.VISIBLE);
                    txt2.setText("Extra Rs.300 as Delivery Charge");
                    txt2.setTextColor(Color.parseColor("#d35400"));

                }
                else {

                    if(position==1){
                        txt2.setVisibility(View.VISIBLE);
                        txt2.setText("No Delivery charge");
                        txt2.setTextColor(Color.parseColor("#27ae60"));
                    }
                    else{
                        txt2.setVisibility(View.VISIBLE);
                        txt2.setText("Extra Rs.300 as Delivery Charge");
                        txt2.setTextColor(Color.parseColor("#d35400"));
                    }

                    txt1.setVisibility(View.GONE);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

       // editText=(Button)findViewById(R.id.editTxt);
//        editText.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(BookingDetails.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        String extns;
//                        if(selectedHour>12){
//                            selectedHour=selectedHour-12;
//                             extns="PM";
//                        }
//                        else{
//                            selectedHour=selectedHour;
//                             extns="AM";
//                        }
//                        editText.setText( selectedHour + ":" + selectedMinute +" "+ extns);
//                    }
//                }, hour, minute, true);//Yes 24 hour time
//                mTimePicker.setTitle("Select Time");
//                mTimePicker.show();
//
//            }
//        });

        Dialog= new ProgressDialog(BookingDetails.this);
Dialog.setMessage("please wait");
        Dialog.show();

        Intent intentzc = getIntent();
        TextView from_to_inp=(TextView)findViewById(R.id.ride_dure);
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

        if(to_time==null){
            to_timeF="11:59 PM";
        }
        else {
            to_timeF=to_time;
        }
        String book_date="From "+frm_day+"-"+frm_month+"-"+frm_year+" @ "+from_time+"\n\n"+" To "+to_day+"-"+tomonth+"-"+to_year+" , "+to_timeF;
        from_to_inp.setText(book_date);
        int car_id_input=buns.getInt("car_id");
        Log.v("car_ids=",car_id_input+"kk");

        String otp_nums = "http://luxscar.com/luxscar_app/booking_dtls.php?car_id="+car_id_input;


        final String otp_url = otp_nums;
        new LongOperation().execute(otp_url);

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

                    int car_img_len=jsonMainNode1.length();

                    for(int i=0;i<car_img_len;i++){


                        JSONObject jsonChildNode1 = jsonMainNode1.getJSONObject(i);

                        car_image=jsonChildNode1.optString("car_image").toString();

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
                        cost     = jsonChildNode.optInt("cost");
                        car_number = jsonChildNode.optString("car_no").toString();
                        car_id=jsonChildNode.optInt("car_id");







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
            Log.i("net_err", "lh returned");
            return;
        }else{
            Log.i("net_err","lh ok");
        }



        Log.v("car_name",car_image);


        for(int i=0;i<1;i++){

            TextView car_name_inp=(TextView)findViewById(R.id.car_brand);
            TextView car_cost=(TextView)findViewById(R.id.car_cost);
          //  ImageView car_image_inp=(ImageView)findViewById(R.id.car_image_inps);
            Button selt_car=(Button)findViewById(R.id.cnfrm_booking);
            final TextView adv_amont=(TextView)findViewById(R.id.adv_amount);
            final TextView tot_cost=(TextView)findViewById(R.id.tot_cost);



            selt_car.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
                    String pickTimee=from_time;
                   // int hours=timePicker1.getCurrentHour();
                    //int min=timePicker1.getCurrentMinute();

                    //Log.v("timm",hours+"dd"+min);
                    SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
                    String user_id = myPrefs.getString("MEM1","");

                    String booking_url = "http://luxscar.com/luxscar_app/bookin_carss.php?";


                    ClipboardManager myClipboard;
                    myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

                    ClipData abc = myClipboard.getPrimaryClip();

                    if(abc==null){
                        coopCode="nocoupoo";
                    }else{
                        ClipData.Item item = abc.getItemAt(0);

                        if(abc.getDescription().toString().equals("LUXSCOO")){
                            coopCode = item.getText().toString();
                        }
                        else{
                            coopCode="nocoupoo";
                        }
                    }








                    String bookf_date=frm_day+"-"+frm_month+"-"+frm_year;
                    String bookt_date=to_day+"-"+tomonth+"-"+to_year;

                    pickUpLoate="";

                    if(spinner.getSelectedItemId()==5){
                        if(TextUtils.isEmpty(txt1.getText()) ){
                            check_all=false;
                            txt1.setError("Required");
                            txt1.requestFocus();

                        }
                        else {
                            check_all=true;
                            pickUpLoate=txt1.getText().toString();
                        }

                    }
                    else {
                        if(spinner.getSelectedItemId()==0){
                            check_all=false;
                            new AlertDialog.Builder(BookingDetails.this)
                                    .setTitle(" Alert !")
                                    .setMessage("Please Select Pick up location ")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                        else{
                            check_all=true;
                            pickUpLoate=spinner.getSelectedItem().toString();
                        }

                    }

                    if(check_all==true){
                        CheckBox checkBox=(CheckBox)findViewById(R.id.checkBoxFT);
                        if(checkBox.isChecked()){
                            check_all=true;
                        }
                        else {
                            checkBox.setError("Required");
                            checkBox.requestFocus();
                            check_all=false;
                        }
                    }





                    String pickUpTime=pickTimee;


                    if(from_time==null){
                        from_timeF=pickUpTime;
                    }
                    else{
                        from_timeF=from_time;
                    }
                    if(to_time==null){
                        to_timeF="11:59 PM";
                    }
                    else {
                        to_timeF=to_time;
                    }
                    int from_to_diffF=buns.getInt("from_to_diff");
                    if(from_time==null || to_time==null){
                        if(from_to_diffF==1){
                            if(frm_day==to_day){
                                from_to_diffF=1;
                            }
                            if(frm_day!=to_day){
                                from_to_diffF=2;
                            }
                        }
                        else{
                            from_to_diffF++;
                        }

                    }


                    String my_data="";
                    try {
                        pickUpLoate=URLEncoder.encode(pickUpLoate,"UTF-8");
                        pickUpTime=URLEncoder.encode(pickUpTime,"UTF-8");
                        Log.v("timm",pickUpTime+"loc "+pickUpLoate);
                        my_data+= URLEncoder.encode("user_idnnn", "UTF-8") + "="+user_id;
                        my_data+="&"+URLEncoder.encode("car_idss","UTF-8")+"="+car_id;
                        my_data+="&"+URLEncoder.encode("bookf_date","UTF-8")+"="+bookf_date;
                        my_data+="&"+URLEncoder.encode("bookt_date","UTF-8")+"="+bookt_date;
                        my_data+="&"+URLEncoder.encode("from_time","UTF-8")+"="+URLEncoder.encode(from_timeF,"UTF-8");
                        my_data+="&"+URLEncoder.encode("to_time","UTF-8")+"="+URLEncoder.encode(to_timeF,"UTF-8");
                        my_data+="&"+URLEncoder.encode("ride_price","UTF-8")+"="+total_cost;
                        my_data+="&"+URLEncoder.encode("ride_advance","UTF-8")+"="+adv_amt;
                        my_data+="&"+URLEncoder.encode("pickUpLocation","UTF-8")+"="+pickUpLoate;
                        my_data+="&"+URLEncoder.encode("pickUpTime","UTF-8")+"="+pickUpTime;
                        my_data+="&"+URLEncoder.encode("ride_days","UTF-8")+"="+from_to_diffF;
                        my_data+="&"+URLEncoder.encode("coopcode","UTF-8")+"="+coopCode;
                        my_data+="&"+URLEncoder.encode("JSK","UTF-8")+"="+"skjdkskdskjhkjn";
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                     booking_url+=my_data;

                    if(check_all){
                        Log.v("sent_urll",booking_url);
                        new Booking().execute(booking_url);
                    }
                    else {

                    }

                }
            });


            car_name_inp.setText(cars_name);
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
            if(from_time==null || to_time==null){
                if(from_to_diff==1){
                    if(frm_day==to_day){
                        from_to_diff=1;
                    }
                    if(frm_day!=to_day){
                        from_to_diff=2;
                    }
                }
                else{
                    from_to_diff++;
                }

            }


            int diff_day=from_to_diff;
             fdiff_date=diff_day;
             total_cost=(diff_day)*cost;
            tot_cost.setText("Rs. "+total_cost);

            adv_amt=(0.25)*total_cost;
            adv_amont.setText("Minimum Rs.2000");
            car_cost.setText("Rs. "+cost+" / per day");


            if(car_image.equals("null")){

            }
            else {
              //  new ImageLoadTask("http://luxscar.com/luxscar_app/"+car_image, car_image_inp).execute();
            }



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

    public class Booking  extends AsyncTask<String, Void, Void> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(BookingDetails.this);
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

                Log.v("nett_error_booking",otpt.toString());

                Intent intentCC=new Intent(getApplicationContext(),PaymentBooking.class);
                finish();
                startActivity(intentCC);
                Toast.makeText(getApplicationContext(),otpt.toString(),Toast.LENGTH_LONG).show();

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
    public void viewTerms(View view){
        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse("http://luxscar.com/luxscar_app/terms.htm"));
        startActivity(browse);
    }


}
