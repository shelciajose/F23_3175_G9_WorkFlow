package com.example.workflow.notification;

import static android.app.Notification.PRIORITY_HIGH;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.window.SplashScreen;

import androidx.core.app.NotificationCompat;

import com.example.workflow.R;
import com.example.workflow.activities.NavigationActivity;
import com.example.workflow.utils.ConstantUtils;
import com.example.workflow.utils.PreferenceUtils;

public class CheckInOutNotifi {
    public static void show(String message, boolean isCheckInAlert, Context context) {

        String notificationChannel;
        int notificationId;
        if (isCheckInAlert) {
            notificationChannel = ConstantUtils.NOTIFICATION_ALERT_CHANNEL_FOR_CHECK_IN;
            notificationId = ConstantUtils.NOTIFICATION_ALERT_ID_FOR_CHECK_IN;
        } else {
            notificationChannel = ConstantUtils.NOTIFICATION_ALERT_CHANNEL_FOR_CHECK_OUT;
            notificationId = ConstantUtils.NOTIFICATION_ALERT_ID_FOR_CHECK_OUT;
        }
        CreateNotificationChannel(context, notificationChannel, "Auto");


        Bitmap logoBitmapIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_logo);
        Intent intentForPendingIntent = null;
            if (PreferenceUtils.checkUserisLogedin(context))
                intentForPendingIntent = new Intent(context, NavigationActivity.class);
            else
                intentForPendingIntent = new Intent(context, SplashScreen.class);


        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S)

            pendingIntent =  PendingIntent.getActivity(context, 0, intentForPendingIntent, PendingIntent.FLAG_IMMUTABLE);
        else pendingIntent =  PendingIntent.getActivity(context, 0, intentForPendingIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        //
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, notificationChannel);
        builder.setSmallIcon(R.drawable.app_ticker_icon);
        builder.setLargeIcon(logoBitmapIcon);
        builder.setContentTitle(context.getString(R.string.app_name));
        builder.setTicker(message);
        builder.setContentText(message);
        builder.setAutoCancel(false);
        builder.setOngoing(true);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        builder.setContentIntent(pendingIntent);
        builder.setPriority(PRIORITY_HIGH);
        final Notification notification = builder.build();
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(notificationId, notification);


        //
    }


    private static void CreateNotificationChannel(Context context, String notificationId, String notificationName) {
        ///////////////////////////////
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(notificationId,
                    notificationName,
                    importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
        }
        ///////////////////////////////
    }


    public static void closeCheckInAndOutWarnningNotification(Context context, boolean isCheckInAlert) {
        try {
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (isCheckInAlert)
                mNotificationManager.cancel(ConstantUtils.NOTIFICATION_ALERT_ID_FOR_CHECK_IN);
            else
                mNotificationManager.cancel(ConstantUtils.NOTIFICATION_ALERT_ID_FOR_CHECK_OUT);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

