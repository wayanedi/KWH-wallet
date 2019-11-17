package com.example.kwh_wallet.NotificationService;

;
import android.app.PendingIntent;
import android.app.Service;

import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


import com.example.kwh_wallet.MainActivity;
import com.example.kwh_wallet.R;

import static com.example.kwh_wallet.NotificationService.Notification.CHANNEL_ID;


public class TransferNotificationService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        android.app.Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("KWH WALLET")
                .setContentText(input)
                .setSmallIcon(R.drawable.kwh_wallet_logo)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
