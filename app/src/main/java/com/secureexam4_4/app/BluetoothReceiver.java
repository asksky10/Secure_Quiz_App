package com.secureexam4_4.app;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


// manages bluetooth connectivity
public class BluetoothReceiver extends BroadcastReceiver {

    MainActivity main = null;

    public BluetoothReceiver() {
    }

    void setMainActivity(MainActivity m){
//        Log.d("Focus Changed","MAIN SET FOR BT");
        main = m;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        Log.d("Focus Changed", "on receive called");
        final String action = intent.getAction();

        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
        if(ba.isEnabled() && main!=null){
//            Log.d("Focus Changed", "inside1 called");
            ba.disable();
//                main.unregisterReceiver(this);
        }



    }
}