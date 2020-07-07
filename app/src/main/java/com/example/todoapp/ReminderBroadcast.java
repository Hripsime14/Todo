package com.example.todoapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 55;
    private static final String CHANNEL_ID = "CHANNEL_ID";

    @Override
    public void onReceive(Context context, Intent intent) {
        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notifications, 10)
                    .setContentTitle("ToDo notification")
                    .setContentText("Be careful not to miss TODO")
                    .setCategory(NotificationCompat.CATEGORY_REMINDER)
                    .setAutoCancel(true);
        }
        Notification notification = builder.build();
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification);
    }
}
