package com.example.locateme;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ShutdownReceiver extends BroadcastReceiver {

    private static final String TAG = "ShutdownReceiver";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.i(TAG, "Shutting Down..........................");
        if("android.intent.action.ACTION_SHUTDOWN".equals(intent.getAction())) {

             }
    }
}