package com.secureexam4_4.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;


// USED TO CHECK HEADPHONE CONNECTIVITY AND DISABLE IT
public class HeadphoneReceiver extends BroadcastReceiver {

    MainActivity main = null;

    public HeadphoneReceiver() {
    }

    void setMainActivity(MainActivity m){
//        Log.d("Focus Changed","MAIN SET FOR HR4");
        main = m;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        Log.d("Focus Changed", "on receive called");
        final String action = intent.getAction();

//        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
//            BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
//            if(ba.isEnabled()){
//                Log.d("Focus Changed", "inside1 called");
//                main.onBackPressed();
//            }
//        }
//        else{
        AudioManager amanager=(AudioManager)main.getSystemService(Context.AUDIO_SERVICE);
        if(amanager.isWiredHeadsetOn() && main!=null){
//            Log.d("Focus Changed", "inside2 called");
            main.onBackPressed();
//                main.unregisterReceiver(this);
        }
//        }



    }
}