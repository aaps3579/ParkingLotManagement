package com.parkinglotmanagement;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String slot;
        if(intent!=null) {
           slot= (String) intent.getExtras().get("Slot");
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
            DatabaseReference booked = databaseReference.child("Booked");
            booked.child(slot).removeValue();
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(Integer.parseInt(slot));
            NotificationCompat.Builder mBuilder;
            mBuilder= new NotificationCompat.Builder(this);
            mBuilder.setContentTitle("Parking Slot Time Finished !");
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(alarmSound);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.mipmap.ic_launcher));
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(Integer.parseInt(slot), mBuilder.build());
        }return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }
}
