package com.ulgebra.luxscar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class IncomingSms extends BroadcastReceiver
{
    private String otpp;
    @Override
    public void onReceive(Context context, Intent intent)
    {

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null)
            {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj .length; i++)
                {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[])                                                                                                    pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber ;
                    String message = currentMessage .getDisplayMessageBody();
                    try
                    {
                        if (senderNum .equals("IM-LXSCAR"))
                        {
                            setOtpp(message);
                        }
                    }
                    catch(Exception e){}

                }
            }

        } catch (Exception e)
        {

        }
    }


    public void setOtpp(String otpp) {
        this.otpp = otpp;
    }

    public String getOtpp() {
        return otpp;
    }

}




