package com.ulgebra.getscar;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
   
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
  
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class Welcome extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    String coopCode;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
        String oldOfferId = myPrefs.getString("lastOfferCode","");
       String oldMsgId = myPrefs.getString("lastMsgCode","");
        int needTab=intent.getIntExtra("needTab",0);
        if(!isOnline())
        {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("You must be connected to the internet via wifi or 3g/4g.  Please connect and load the application again.")
//                    .setCancelable(false)
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            Welcome.this.finish();
//                        }
//                    });
//            AlertDialog alert = builder.create();
//
//            alert.show();

            Intent inty=new Intent(getApplicationContext(),NoConnection.class);
            startActivity(inty);

        }
        else {
//
//            ClipboardManager myClipboard;
//            myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
//
//            ClipData abc = myClipboard.getPrimaryClip();
//            ClipData.Item item = abc.getItemAt(0);
//
//            if(abc.getDescription().toString().contains("LUXSCOO")){
//                coopCode = item.getText().toString();
//            }
//            else{
//                coopCode="nocoupoo";
//            }
//            Log.v("cooppp",coopCode+" desc "+abc.getDescription().toString());

            try {

                String serverURL2 = "http://luxscar.com/luxscar_app/todayOffer.php?oldID="+oldOfferId+"&msgId="+oldMsgId+"&c=p";

                new LongOperation2().execute(serverURL2);
            }
            catch (Exception e){
                Log.v("net_err","err="+e.getMessage());

            }

            setContentView(R.layout.activity_welcome);

        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(needTab);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


      
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement



        if (id == R.id.action_myaccount) {
            Intent intentq=new Intent(getApplicationContext(),SingleUserDetails.class);
            startActivity(intentq);
        }
        if (id == R.id.action_about) {
            Intent intentq=new Intent(getApplicationContext(),About.class);
            startActivity(intentq);
        }
        if (id == R.id.action_offers) {
            Intent intentq=new Intent(getApplicationContext(),AllOffers.class);
            startActivity(intentq);
        }

        if (id == R.id.action_adMsgs) {
            Intent intentq=new Intent(getApplicationContext(),allMessages.class);
            startActivity(intentq);
        }

        if (id == R.id.action_share) {
            Intent intentq=new Intent(getApplicationContext(),ShareApp.class);
            startActivity(intentq);
        }


        if (id == R.id.action_terms) {


                Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse("http://luxscar.com/luxscar_app/terms.htm"));
                startActivity(browse);


        }


        return super.onOptionsItemSelected(item);
    }

    
  

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position==1){
                return new FragmentBookingHistory();
            }
           else {
                return new FragmentBookingDate();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Book Now";
                case 1:
                    return "History";

            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment newInstance(int sectionNumber) {
            Fragment fragment = new FragmentBookingDate();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
    public class LongOperation2  extends AsyncTask<String, Void, Void> {

        // Required initialization

        // private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        String data ="";
        String otpt="";



        protected void onPreExecute() {
            // NOTE: You can call UI Element here.




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


            if(otpt.hashCode()==0){
                Toast.makeText(getApplicationContext(),"Check your Internet Connection",Toast.LENGTH_LONG).show();

            }else{

                if(otpt.contains("x_x_x")){
                   // Toast.makeText(getApplicationContext(),"No new Offers",Toast.LENGTH_LONG).show();

                }
                else{
                    //Toast.makeText(getApplicationContext(),otpt,Toast.LENGTH_LONG).show();
                    Log.v("otpt of offer id ",otpt);
                    String[] otptArr=otpt.split("_",4);

                    String offerId=otptArr[0];
                    String msgId=otptArr[1];
                    String msgTtl=otptArr[2];
                    String msgTxt=otptArr[3];

                    if(offerId.contains("x")){

                    }
                    else{
                        SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
                        SharedPreferences.Editor editor = myPrefs.edit();
                        editor.putString("lastOfferCode",offerId+"");
                        editor.commit();

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
                        mBuilder.setSmallIcon(R.drawable.ic_stat_name);
                        mBuilder.setLargeIcon(BitmapFactory.decodeResource( getResources(), R.drawable.luxscarico));
                        mBuilder.setContentTitle("GetsCar - New Offer");
                        mBuilder.setContentText("Click to view your offers");
                        mBuilder.setTicker("You have a new Offer on GetsCar");
                        mBuilder.setAutoCancel(true);

                        Intent resultIntent = new Intent(getApplicationContext(), AllOffers.class);

                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                        stackBuilder.addParentStack(AllOffers.class);
                        stackBuilder.addNextIntent(resultIntent);
                        PendingIntent resultPendingIntent =
                                stackBuilder.getPendingIntent(
                                        0,
                                        PendingIntent.FLAG_UPDATE_CURRENT
                                );
                        mBuilder.setContentIntent(resultPendingIntent);
                        NotificationManager mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(1, mBuilder.build());
                    }
                    if(msgId.contains("x")){

                    }
                    else{
                        SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
                        SharedPreferences.Editor editor = myPrefs.edit();
                        editor.putString("lastMsgCode",msgId+"");
                        editor.commit();


                        AlertDialog.Builder builder = new AlertDialog.Builder(Welcome.this);
                        builder.setMessage(msgTxt)
                                .setCancelable(false)
                                .setTitle(msgTtl)
                                .setIcon(R.mipmap.ic_launcher)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                       // Welcome.this.finish();
                                    }
                                });
                        AlertDialog alert = builder.create();

                        alert.show();
                    }



                }

            }


        }

    }


}
