package com.example.todoapp.Services;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.example.todoapp.ReminderBroadcast;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {
    private static String TAG = "delLog";
    private long delay = 15000;
    public int counter = 0;
    String NOTIFICATION_CHANNEL_ID = "5000";
    private Timer timer;
    private TimerTask timerTask;
    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                Log.d("delLog", "run: running");
                showNotification();
                Log.d("delLog", "=========  "+ (counter++));
            }
        };
        Log.d(TAG, "startTimer: scheduling , delay = " + delay);

        //TODO: erb vor delay-@ ruchnoy em anum, ed jamanak normal delay a linum, isk erb popoxakann em tedxadrum miagamic a linum eshy
        timer.schedule(timerTask, Math.abs(20000));
    }

    public NotificationService(/*long timeStamp*/) {
//        this.delay = (int) timeStamp;
//        Log.d("delLog", "NotificationService: in the constructor, delay = "  + delay);
//        int del = (int) (timeStamp - new Date().getTime());
//        Log.d("delLog", "NotificationService: del = " + del);
//        this.delay = del;
    }

    private void showNotification() {
        Log.d(TAG, "showNotification: ");
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("Don't miss the todo")
                .setContentText("blablabla")
//                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    public void stoptimertask() {
        Log.d(TAG, "stoptimertask: ");
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        if (intent != null) delay = (int) intent.getExtras().get("temp");
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand (Intent intent , int flags , int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d("delLog", "onStartCommand: started command, delay = " + delay);
        long temptemp = 0;
        if (intent != null && intent.getExtras() != null) delay = (long) intent.getExtras().get("temp");/*temptemp = (long) intent.getExtras().get("temp");
        delay = (int) temptemp - new Date().getTime();*/

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            startMyOwnForeground();
            startTimer();
        }

        return START_STICKY;

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate () {
        Log. e ( TAG , "onCreate" ) ;
        super.onCreate();



//        startMyOwnForeground();
//        startTimer();

        /*if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            startMyOwnForeground();
            startTimer();
        }*/
//        else
//            startForeground(1, new Notification());
    }

    @SuppressLint("ShortAlarm")
    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        Log.d(TAG, "startMyOwnForeground: ");
//        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        Intent notifyIntent = new Intent(this,ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (getApplicationContext(), 100, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  System.currentTimeMillis(),
                10000, pendingIntent);




//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
//        Notification notification = notificationBuilder.setOngoing(true)
//                .setContentTitle("App is running in background")
//                .setContentText("blablabla")
//                .setPriority(NotificationManager.IMPORTANCE_MIN)
//                .setCategory(Notification.CATEGORY_SERVICE)
//                .build();
//        startForeground(2, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stoptimertask();

        Log.d("delLog", "onDestroy of service: called");

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, ReminderBroadcast.class);
        this.sendBroadcast(broadcastIntent);
    }
}
class xx extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}