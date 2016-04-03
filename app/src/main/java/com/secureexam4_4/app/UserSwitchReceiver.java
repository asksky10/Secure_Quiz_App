package com.secureexam4_4.app;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/*REDUNDANT IN THE FINAL VERSION*/
public class UserSwitchReceiver extends BroadcastReceiver {

    private static final String TAG = "UserSwitchReceiver";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        boolean userSentBackground = intent.getAction().equals( Intent.ACTION_USER_BACKGROUND );
        boolean userSentForeground = intent.getAction().equals( Intent.ACTION_USER_FOREGROUND );
        Log.d( TAG, "Switch received. User sent background = " + userSentBackground + "; User sent foreground = " + userSentForeground + ";" );

        int user = intent.getExtras().getInt( "android.intent.extra.user_handle" );
        Log.d( TAG, "user = " + user );
    }

}