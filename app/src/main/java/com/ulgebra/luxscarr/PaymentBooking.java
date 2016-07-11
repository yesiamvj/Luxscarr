package com.ulgebra.luxscarr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URL;

import static java.security.AccessController.getContext;

public class PaymentBooking extends AppCompatActivity {

    String url;
     WebView paymentWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_payment_booking);
        SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
        String user_idd = myPrefs.getString("MEM1","");
        paymentWebView = (WebView) findViewById(R.id.payment_webView);
        paymentWebView.setWebViewClient(new MyBrowser());
        url="http://luxscar.com/luxscar_app/preConfirmPage.php?user_id="+user_idd+"&selel=vijb";
        Log.v("payment_url",url);

        paymentWebView.getSettings().setLoadsImagesAutomatically(true);
        paymentWebView.getSettings().setJavaScriptEnabled(true);
        paymentWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        paymentWebView.loadUrl(url);

    }
    private class MyBrowser extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.equals("http://luxcar.com/luxscar_app/finishall.php")) {
                Intent intent = new Intent(getApplicationContext(), Welcome.class);
                startActivity(intent);
                return true; // Handle By application itself
            }else{
                view.loadUrl(url);
                return true;
            }
        }
    }
}
