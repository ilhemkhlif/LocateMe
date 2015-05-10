package com.example.locateme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.util.Log;

import java.util.ArrayList;

public class SmsReceiver extends BroadcastReceiver
{

    GPSTracker gps;
    String latitudeStr ;
    String longitudeStr;

	
    @Override
    public void onReceive(Context context, Intent intent)
    {

        //--- get current position ---

        gps = new GPSTracker(context);

        if(gps.canGetLocation()) {

            latitudeStr = String.valueOf(gps.getLatitude());

            longitudeStr = String.valueOf(gps.getLongitude());


        } else {
            gps.showSettingsAlert();
        }

        //---get the SMS message passed in---
    	Log.v("SmsReceiver", "Received");
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String messageRecu;
        String messageAEnvoyer;
        String numTel;
        ArrayList<String> longMessage;
        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            //---display the new SMS message---
            SmsManager smsManager = SmsManager.getDefault();
            msgs = new SmsMessage[pdus.length];
            for (int i=0; i<msgs.length; i++){
            	messageAEnvoyer="";
            	longMessage=new ArrayList<String>();
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                numTel=msgs[i].getOriginatingAddress();
                
                messageRecu = msgs[i].getMessageBody().replaceAll(" ", "");
                if(messageRecu!=null) {

                    if(messageRecu.equals(FirstMainActivity.getCode())){

                         messageAEnvoyer="The phone location is \n"
                                 +"https://www.google.tn/maps/@"+latitudeStr+","+longitudeStr+",18z" ;


                    } else {
	                	messageAEnvoyer="";


                    }

	                if(!messageAEnvoyer.equals("")) {
                		longMessage=smsManager.divideMessage(messageAEnvoyer);
                		smsManager.sendMultipartTextMessage(numTel, null, longMessage, null, null);
	                }

                }    
            }
        }                         
    }
    




}
