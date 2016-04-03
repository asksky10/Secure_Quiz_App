package com.secureexam4_4.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


// TRACKS NOTIFICATIONS RECEIVED AND ALERTS
public class NotificationReceiver extends BroadcastReceiver {
    MainActivity main = null;
    public NotificationReceiver() {
    }

    void setMainActivity(MainActivity m){
        Log.d("Focus Changed", "MAIN SET FOR NOTIF");
        main = m;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d("Focus Changed", "receive for notification called");


    }
}