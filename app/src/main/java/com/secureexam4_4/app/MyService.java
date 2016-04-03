package com.secureexam4_4.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;


/*cancels notifications of API >19*/
public class MyService extends NotificationListenerService {


    @Override
    public void onNotificationRemoved( StatusBarNotification sb){

    }
    @Override
    public void onNotificationPosted( StatusBarNotification sb){
        Log.d("Focus changed","notfi aaya");
        this.cancelAllNotifications();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return super.onBind(intent);
    }
}