package com.example.todoapp.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


import com.example.todoapp.MainActivity;
import com.example.todoapp.R;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    private static String CHANNEL_ID = "CHANNEL_ID";
    private static int NOTIFICATION_ID = 55;
    private String TAG = "Timers" ;
    private Timer timer ;
    private TimerTask timerTask ;
    private int Your_X_SECS = 5 ;
    private final Handler handler = new Handler() ;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand (Intent intent , int flags , int startId) {
        String input = intent.getStringExtra("inputExtra");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 55, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("TITILE")
                .setContentText(input)
                .setSmallIcon(R.drawable.notifications)
                .setContentIntent(pendingIntent)
                .build();


        startForeground(1, notification);

        return START_NOT_STICKY;

    }
    @Override
    public void onCreate () {
        Log. e ( TAG , "onCreate" ) ;
    }

    @Override
    public void onDestroy () {
        Log. e ( TAG , "onDestroy" ) ;
        super .onDestroy() ;
    }
}
