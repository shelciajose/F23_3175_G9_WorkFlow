package com.example.workflow.utils;

import static android.content.Context.ACTIVITY_SERVICE;
import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;
import static com.example.workflow.utils.ConstantUtils.FORMAT_CALENDAR_EVENT;
import static com.example.workflow.utils.ConstantUtils.NOTIFICATION_CHANNEL_ID_OF_COMM_NOTIFICATION;
import static com.example.workflow.utils.ConstantUtils.NOTIFICATION_CHANNEL_NAME_OF_COMM_NOTIFICATION;
import static com.example.workflow.utils.ConstantUtils.NOTIFICATION_TYPE_GRANT_PERMISSION;
import static com.example.workflow.utils.ConstantUtils.NOTIFICATION_TYPE_GRANT_PERMISSION_ID;
import static com.example.workflow.utils.ConstantUtils.NOTIFICATION_TYPE_NOTHING;
import static com.example.workflow.utils.ConstantUtils.NOTIFICATION_TYPE_NOTHING_ID;
import static com.example.workflow.utils.ConstantUtils.NOTIFICATION_TYPE_ON_GPS;
import static com.example.workflow.utils.ConstantUtils.NOTIFICATION_TYPE_ON_GPS_ID;
import static com.example.workflow.utils.ConstantUtils.NOTIFICATION_TYPE_RELOG_USER;
import static com.example.workflow.utils.ConstantUtils.NOTIFICATION_TYPE_RELOG_USER_ID;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.view.View;
import android.window.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.workflow.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class CommonFunc {


    public static void commonDialog(final Context context, String title, String subjuct, final boolean isInternetError,
                                    AppCompatActivity appCompatActivity, final View viewDisplay) {
        if (isInternetError) {
            if (NetworkUtils.getConnectionType(context).equals("NA")) {
                showNoInternetSnackBar(appCompatActivity, context, viewDisplay, "Enable your mobile data or wifi", "Settings");
            } else if (NetworkUtils.getConnectionType(context).equals("W")) {
                showNoInternetSnackBar(appCompatActivity, context, viewDisplay, "Your wifi connection is limited. Try alternatives", "Settings");

            } else if (NetworkUtils.getConnectionType(context).equals("M")) {
                showNoInternetSnackBar(appCompatActivity, context, viewDisplay, "Your mobile data connection is limited. Try alternatives", "Settings");

            }

        } else {
            try {
                Snackbar snackbar1 = Snackbar.make(viewDisplay, subjuct, Snackbar.LENGTH_LONG);
                snackbar1.setDuration(7000);
                snackbar1.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    public static String getTodayDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_CALENDAR_EVENT);
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }

    public static String convertTimestampToTime(String dateInMilliseconds) {
        return DateFormat.format("hh:mm a", Long.parseLong(dateInMilliseconds)).toString();
    }

    public static String convertTimestampToDate(String dateInMilliseconds) {
        return DateFormat.format(FORMAT_CALENDAR_EVENT, Long.parseLong(dateInMilliseconds)).toString();
    }

    ///// convert sec to hhmmss
    ///////////
    public static String getDurationString(long millis) {

        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return hms;
    }

    //// convert bytes to mb


    public static long getCurrentSystemTimeStamp() {
        Calendar calendar = new GregorianCalendar();
        return calendar.getTimeInMillis();
    }


    private static boolean isTimeAutomaticTime(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return Settings.System.getInt(c.getContentResolver(), Settings.System.AUTO_TIME, 0) == 1;
        }
    }

    private static boolean isTimeAutomaticTimeZone(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME_ZONE, 0) == 1;
        } else {
            return Settings.System.getInt(c.getContentResolver(), Settings.System.AUTO_TIME_ZONE, 0) == 1;
        }
    }


    public static void showNotification(String tickerMessage, String contentMessage, String notificationType,
                                                            Context context, boolean autoCancel, boolean Ongoing) {


        CreateCommNotificationChannel(context);


        Bitmap logoBitmapIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_logo);
        Intent intentForPendingIntent = null;
        if (notificationType.equals(NOTIFICATION_TYPE_RELOG_USER)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                intentForPendingIntent = new Intent(context, SplashScreen.class);
            }
            intentForPendingIntent.setAction("CLEAR_ALL_USER_PREFERENCES");
        }
        if (notificationType.equals(NOTIFICATION_TYPE_ON_GPS)) {
            intentForPendingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        }
        if (notificationType.equals(NOTIFICATION_TYPE_GRANT_PERMISSION)) {
            intentForPendingIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        }
        if (notificationType.equals(NOTIFICATION_TYPE_NOTHING)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                intentForPendingIntent = new Intent(context, SplashScreen.class);
            }
        }
        PendingIntent pendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)

            pendingIntent =  PendingIntent.getActivity(context, 0, intentForPendingIntent, PendingIntent.FLAG_IMMUTABLE);
        else   pendingIntent =  PendingIntent.getActivity(context, 0, intentForPendingIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        //
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID_OF_COMM_NOTIFICATION);
        builder.setSmallIcon(R.mipmap.ic_logo);
        builder.setLargeIcon(logoBitmapIcon);
        builder.setContentTitle(context.getString(R.string.app_name));
        builder.setTicker(tickerMessage);
        builder.setContentText(contentMessage);
        builder.setAutoCancel(autoCancel);
        builder.setOngoing(Ongoing);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(contentMessage));
        builder.setContentIntent(pendingIntent);
        builder.setPriority(PRIORITY_HIGH);
        final Notification notification = builder.build();
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (notificationType.equals(NOTIFICATION_TYPE_RELOG_USER)) {
            mNotificationManager.notify(NOTIFICATION_TYPE_RELOG_USER_ID, notification);
        }
        if (notificationType.equals(NOTIFICATION_TYPE_ON_GPS)) {
            mNotificationManager.notify(NOTIFICATION_TYPE_ON_GPS_ID, notification);
        }
        if (notificationType.equals(NOTIFICATION_TYPE_GRANT_PERMISSION)) {
            mNotificationManager.notify(NOTIFICATION_TYPE_GRANT_PERMISSION_ID, notification);
        }
        if (notificationType.equals(NOTIFICATION_TYPE_NOTHING)) {
            mNotificationManager.notify(NOTIFICATION_TYPE_NOTHING_ID, notification);
        }


        //
    }


    public static void CreateCommNotificationChannel(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID_OF_COMM_NOTIFICATION,
                    NOTIFICATION_CHANNEL_NAME_OF_COMM_NOTIFICATION,
                    importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }



    private static void showNoInternetSnackBar(final AppCompatActivity activity, final Context context,
                                               View displayView, String message, String buttonName) {

        try {
            Snackbar snackbar = Snackbar
                    .make(displayView, message, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Settings", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent myIntent = new Intent(Settings.ACTION_SETTINGS);
                            context.startActivity(myIntent);
                        }
                    });

            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static int getLocationMode(Context context) {
        try {
            return Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            return 2;
        }
    }

    public static boolean isGpsOn(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean status = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return status;
    }

    public static void gotoLocationSettings(final Context context) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.app_name) + " asking to maintain high accuracy GPS state.")
                .setTitle(context.getString(R.string.alert));

        builder.setMessage(context.getString(R.string.app_name) + " asking to maintain high accuracy GPS state.")
                .setCancelable(false)
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        androidx.appcompat.app.AlertDialog alert = builder.create();
        alert.setTitle(context.getString(R.string.alert));
        alert.show();
    }

    private static Map<String, ?> getAllSharedPreference(Context context, String sharedPreferenceName) {

        SharedPreferences preferences = context.getSharedPreferences(sharedPreferenceName, 0);

        return preferences.getAll();
    }


    public static String getAddressFromCoordinates(Context context, Double latitude, Double longitude){
        Geocoder geocoder;
        List<Address> addresses;
        String address = "NA",city = "",state = "",country = "",postalCode = "", knownName = "";
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            knownName = addresses.get(0).getFeatureName();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

}
