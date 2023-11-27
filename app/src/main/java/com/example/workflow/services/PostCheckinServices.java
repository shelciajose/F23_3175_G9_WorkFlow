package com.example.workflow.services;

import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;
import static com.example.workflow.utils.ConstantUtils.CHECK_IN_OUT_LISNER_NAME;
import static com.example.workflow.utils.ConstantUtils.IDENTIFIERS_FOR_CHECK_IN_NOTIFICATION;
import static com.example.workflow.utils.ConstantUtils.NOTIFICATION_CHANNEL_CHECK_IN_OF_TRACKING_NOTIFICATION;
import static com.example.workflow.utils.ConstantUtils.NOTIFICATION_CHANNEL_NAME_OF_TRACKING_NOTIFICATION;
import static com.example.workflow.utils.ConstantUtils.NOTIFICATION_TYPE_NOTHING;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.workflow.R;
import com.example.workflow.activities.NavigationActivity;
import com.example.workflow.mvp.BasePresentor;
import com.example.workflow.mvp.BasePresentorImp;
import com.example.workflow.mvp.BaseView;
import com.example.workflow.utils.CommonFunc;
import com.example.workflow.utils.ConstantUtils;
import com.example.workflow.utils.NetworkUtils;
import com.example.workflow.utils.PreferenceUtils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostCheckinServices extends Service implements BaseView {

    private Context context = null;
    private BasePresentor basePresentor = null;
    private CalculateTheDistance calculateTheDistance = null;

    private Location locationCurrentOfUser = null;
    private Location officeLocation = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    ///////////////////
    @Override
    public void onCreate() {
        // The service is being created
        // And From your main() method or any other method

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        this.context = this;
        basePresentor = new BasePresentorImp(this);
        showTheForegroundNotification();
        connectToGoogleApiClientAndGetTheAddress();
        // The service is starting, due to a call to startService()
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
        stopLocationUpdatesAndRemoveGoogleApiClient();
        PreferenceUtils.setCheckInOutServiceRunning(context, false, null);

    }


    @Override
    public void showProgressDialog() {

    }

    @Override
    public void dismissProgressDialog() {

    }

    @Override
    public void afterCheckInSuccess(String location, String attendenceKey, String message) {
        System.out.println("uuuuuuuuuuu------attenceId--------" + attendenceKey);
        PreferenceUtils.setUserHasCheckedIn(context, attendenceKey);
        PreferenceUtils.addCheckInTimeToSharedPreference(context, CommonFunc.getCurrentSystemTimeStamp());
        PreferenceUtils.setCheckInLocationToSharedPreference(context, location);
        sendBroadcast(true, PreferenceUtils.getCheckedInUserAttenceId(context),
                "You have successfully checked-In", null, false);
    }

    @Override
    public void afterCheckInFail(String message, String dialogTitle, boolean isInternetError) {
        CommonFunc.showTheServiceClassErrorNotification(message, message,
                NOTIFICATION_TYPE_NOTHING, context, true, false);
        System.out.println("uuuuuuuuuuu------afterCheckInFail--------");
        sendBroadcast(true, null, message, dialogTitle, isInternetError);
    }

    @Override
    public void afterCheckOutSuccess(String location, String message) {
        // no use
    }

    @Override
    public void afterCheckOutFail(String message, String dialogTitle, boolean isInternetError) {
        // no use
    }


    ///////////////////////////////////////
    private void sendBroadcast(boolean isCheckIn, String attendenceID,
                               String message, String dialogTitle, boolean isInternetError) {

        Intent intent1 = new Intent(CHECK_IN_OUT_LISNER_NAME);
        intent1.putExtra("IS_CHECK_IN", isCheckIn);
        intent1.putExtra("ATTENDENCE_ID", attendenceID);
        intent1.putExtra("message", message);
        intent1.putExtra("dialogTitle", dialogTitle);
        intent1.putExtra("isInternetError", isInternetError);
        context.sendBroadcast(intent1);
        stopSelf();
    }

    //////////////////////////////// start the foreground thread
    private void showTheForegroundNotification() {

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_CHECK_IN_OF_TRACKING_NOTIFICATION,
                    NOTIFICATION_CHANNEL_NAME_OF_TRACKING_NOTIFICATION,
                    importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        Bitmap logoBitmapIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.app_logo);
        Intent intent = new Intent(this, NavigationActivity.class);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)

            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        else    pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        //
        final NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_CHECK_IN_OF_TRACKING_NOTIFICATION);
        builder.setSmallIcon(R.drawable.app_ticker_icon);
        builder.setContentTitle("Workflow");
        builder.setLargeIcon(logoBitmapIcon);
        builder.setTicker("Performing check-in");
        builder.setContentText("Checking-in...");
        builder.setContentIntent(pendingIntent);
        builder.setPriority(PRIORITY_HIGH);
        final Notification notification = builder.build();
        startForeground(IDENTIFIERS_FOR_CHECK_IN_NOTIFICATION, notification);
    }


    //////////////////////////////////////////////////////////////////////////////////////// location stuff
    //////////////////////////////////////////////////////////////////////////////////////// location stuff
    private GoogleApiClient googleApiClient = null;
    private LocationRequest locationRequest = null;
    private FusedLocationProviderClient mFusedLocationClient = null;
    private boolean isMockLocEnabled = false;
    //

    @SuppressWarnings("MissingPermission")
    private void connectToGoogleApiClientAndGetTheAddress() {
        connectGoogleApiClient();
    }

    ////////////////////////////////////////////////////////////////////////////////// google api stuff. . . . . .

    private void connectGoogleApiClient() {

        setRequestTime();
    }

    private void setRequestTime() {
        ////////////////////////////////////////////////////////
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(ConstantUtils.LOCATION_REQ_INTERVAL);
        locationRequest.setFastestInterval(ConstantUtils.LOCATION_REQ_FAST_INTERVAL);
        locationRequest.setSmallestDisplacement(ConstantUtils.LOCATION_REQ_SMALLEST_DISPLACEMENT);
        startLocationReq();
    }

    @SuppressWarnings("MissingPermission")
    private void startLocationReq() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {

            for (Location location : locationResult.getLocations()) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    isMockLocEnabled = location.isFromMockProvider();
                } else {
                    isMockLocEnabled = false;
                }
                saveLocToServer(location);


            }

        }
    };

    private class CalculateTheDistance extends AsyncTask<Void, Void, Float> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Float doInBackground(Void... v) {
            return officeLocation.distanceTo(locationCurrentOfUser);
        }

        @Override
        protected void onPostExecute(Float aFloat) {
            super.onPostExecute(aFloat);

            if (aFloat <= 300.0) {
                saveLocToServer(locationCurrentOfUser);
            } else {
                CommonFunc.showTheServiceClassErrorNotification("Check-In Fail",
                        "Your more than 500 mts away from office",
                        NOTIFICATION_TYPE_NOTHING, context, true, false);
                System.out.println("uuuuuuuuuuu------afterCheckInFail----due to 500 mts----");
                sendBroadcast(true, null,
                        "Your more than 500 mts away from office",
                        "Check-In Fail", false);
            }

        }
    }

    protected void stopLocationUpdatesAndRemoveGoogleApiClient() {
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
            mFusedLocationClient = null;
        }
       /* if (googleApiClient != null) {
            if (googleApiClient.isConnected() || googleApiClient.isConnecting())
                googleApiClient.disconnect();
            googleApiClient = null;
        }*/


    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////// postLocToUser

    private void saveLocToServer(Location location) {
        String address = "";
        if(NetworkUtils.isConnected(context)) {
            address = CommonFunc.getAddressFromCoordinates(context, location.getLatitude(), location.getLongitude());
        }
        else {
            address = "NA";
        }

        basePresentor.performCheckIn(context, address,
                String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()),
                null, null,
                ConstantUtils.OFFLINE_MODE);

    }


}
