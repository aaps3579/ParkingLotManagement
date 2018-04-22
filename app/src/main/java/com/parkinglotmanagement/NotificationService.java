package com.parkinglotmanagement;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationService extends Service {
    String slot;
    long time;
    public NotificationService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            slot= (String) intent.getExtras().get("Slot");
            time=(long)intent.getExtras().get("Time");
            Log.d("SERVIC",time+"");
            time=time/60000;
            Log.d("SERVIC",time+"");
            final NotificationCompat.Builder mBuilder;
            mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setContentTitle("Parking Slot Time Started !");
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(alarmSound);
            mBuilder.setOngoing(true);
            mBuilder.setOnlyAlertOnce(true);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.mipmap.ic_launcher));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(time!=0) {

                        mBuilder.setContentText("Slot No. "+slot+" Will Expire In " + time + " Minutes");
                        time--;
                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(Integer.parseInt(slot), mBuilder.build());
                        try {
                            Thread.sleep(60000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();


        }
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
