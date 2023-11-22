package com.example.workflow.services;

import static android.app.Notification.PRIORITY_HIGH;
import static com.example.workflow.utils.ConstantUtils.CHECK_IN_OUT_LISNER_NAME;
import static com.example.workflow.utils.ConstantUtils.IDENTIFIERS_FOR_CHECK_OUT_NOTIFICATION;
import static com.example.workflow.utils.ConstantUtils.NOTIFICATION_CHANNEL_CHECK_OUT_OF_TRACKING_NOTIFICATION;
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
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostCheckoutServices extends Service implements BaseView {
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
        // no use
    }

    @Override
    public void dismissProgressDialog() {
        // no use
    }

    @Override
    public void afterCheckInSuccess(String location, String attenceId, String message) {
        // no use
    }

    @Override
    public void afterCheckInFail(String message, String dialogTitle, boolean isInternetError) {
        // no use
    }

    @Override
    public void afterCheckOutSuccess(String location, String message) {
        PreferenceUtils.addCheckInTimeToSharedPreference(this, 0);
        PreferenceUtils.setCheckInLocationToSharedPreference(this, "NA");
        sendBroadcast(false, "You have successfully checked-Out", null, false);
    }

    @Override
    public void afterCheckOutFail(String message, String dialogTitle, boolean isInternetError) {
        CommonFunc.showTheServiceClassErrorNotification(message, message,
                NOTIFICATION_TYPE_NOTHING, context, true, false);
        sendBroadcast(false, message, dialogTitle, isInternetError);
    }

    private void sendBroadcast(boolean isCheckIn, String message, String dialogTitle, boolean isInternetError) {

        Intent intent1 = new Intent(CHECK_IN_OUT_LISNER_NAME);
        intent1.putExtra("IS_CHECK_IN", isCheckIn);
        intent1.putExtra("message", message);
        intent1.putExtra("dialogTitle", dialogTitle);
        intent1.putExtra("isInternetError", isInternetError);
        context.sendBroadcast(intent1);

    }


    /////////////////////


    /////////////////////

    //////////////////////////////// start the foreground thread
    private void showTheForegroundNotification() {

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_CHECK_OUT_OF_TRACKING_NOTIFICATION,
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

            pendingIntent =  PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        else   pendingIntent =  PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        //
        final NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_CHECK_OUT_OF_TRACKING_NOTIFICATION);
        builder.setSmallIcon(R.drawable.app_ticker_icon);
        builder.setContentTitle("Workflow");
        builder.setLargeIcon(logoBitmapIcon);
        builder.setTicker("Performing check-out");
        builder.setContentText("Checking-out...");
        builder.setContentIntent(pendingIntent);
        builder.setPriority(PRIORITY_HIGH);
        final Notification notification = builder.build();
        startForeground(IDENTIFIERS_FOR_CHECK_OUT_NOTIFICATION, notification);
    }


    //////////////////////////////////////////////////////////////////////////////////////// location stuff
    //////////////////////////////////////////////////////////////////////////////////////// location stuff
    private GoogleApiClient googleApiClient = null;
    private LocationRequest locationRequest = null;
    private FusedLocationProviderClient mFusedLocationClient = null;
    private ReverseGeocodingTask reverseGeocodingTask = null;
    private boolean isMockLocEnabled = false;
//    private FusedLocationProviderApi mFusedLocationClient = null;
//    Warning: Please continue using the FusedLocationProviderApi class and don't migrate to the FusedLocationProviderClient class until
//    Google Play services version 12.0.0 is available, which is expected to ship in early 2018. Using the FusedLocationProviderClient before
//    version 12.0.0 causes the client app
//    to crash when Google Play services is updated on the device. We apologize for any inconvenience this may have caused

    @SuppressWarnings("MissingPermission")
    private void connectToGoogleApiClientAndGetTheAddress() {
        connectGoogleApiClient();
    }

    ////////////////////////////////////////////////////////////////////////////////// google api stuff. . . . . .

    private void connectGoogleApiClient() {

      /*  googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        setRequestTime();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        CommonFunc.showTheServiceClassErrorNotification("Google api client not updated!", "Please update Google Api Client.",
                                NOTIFICATION_TYPE_NOTHING, context, true, false);
                        googleApiClient = null;
                    }
                }).build();
        googleApiClient.connect();*/
        setRequestTime();
    }

    private void setRequestTime() {
        ////////////////////////////////////////////////////////
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(ConstantUtils.LOCATION_REQ_INTERVAL);
        locationRequest.setFastestInterval(ConstantUtils.LOCATION_REQ_FAST_INTERVAL);
        locationRequest.setSmallestDisplacement(ConstantUtils.LOCATION_REQ_SMALLEST_DISPLACEMENT);
        System.out.println("----------ppppppppppppppp------location-----");
        startLocationReq();
    }

    @SuppressWarnings("MissingPermission")
    private void startLocationReq() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        System.out.println("----------ppppppppppppppp------startLocationReq-----");
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {

            for (Location location : locationResult.getLocations()) {
                System.out.println("----------ppppppppppppppp------locationCallback-----" + location.getLatitude());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    isMockLocEnabled = location.isFromMockProvider();
                } else {
                    isMockLocEnabled = false;
                }

                saveLocToServer(location);

                /*officeLocation = new Location("officeLocation");
                officeLocation.setLatitude(Double.parseDouble("12.981316"));
                officeLocation.setLongitude(Double.parseDouble("77.576571"));

                locationCurrentOfUser = new Location("LocationCurrentUser");
                locationCurrentOfUser.setLatitude(location.getLatitude());
                locationCurrentOfUser.setLongitude(location.getLongitude());

                if (PreferenceUtils.getRoleFromThePreference(context).equals("4")) {
                    saveLocToServer(location);
                } else {
                    calculateTheDistance = new CalculateTheDistance();
                    calculateTheDistance.execute();
                }*/
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
                CommonFunc.showTheServiceClassErrorNotification("Check-Out Fail",
                        "Your more than 500 mts away from office",
                        NOTIFICATION_TYPE_NOTHING, context, true, false);

                sendBroadcast(false, "Your more than 500 mts away from office", "Check-Out Fail",
                        false);
            }
        }
    }


    protected void stopLocationUpdatesAndRemoveGoogleApiClient() {
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
            mFusedLocationClient = null;
        }
      /*  if (googleApiClient != null) {
            if (googleApiClient.isConnected() || googleApiClient.isConnecting())
                googleApiClient.disconnect();
            googleApiClient = null;
        }*/


    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////// postLocToUser

    private void saveLocToServer(Location location) {
        System.out.println("----------ppppppppppppppp------saveLocToServer-----" + new Gson().toJson(location));
        if (NetworkUtils.isConnected(context)) {
            if (reverseGeocodingTask == null) {
                reverseGeocodingTask = new ReverseGeocodingTask();
                reverseGeocodingTask.execute(new LatLng(location.getLatitude(), location.getLongitude()));
            } else {
                if (reverseGeocodingTask.getStatus() != AsyncTask.Status.RUNNING) {
                    reverseGeocodingTask = new ReverseGeocodingTask();
                    reverseGeocodingTask.execute(new LatLng(location.getLatitude(), location.getLongitude()));
                }
            }
        } else {

        }

    }


    private class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String> {
        double _latitude, _longitude;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(LatLng... params) {

            Geocoder geocoder = new Geocoder(context);
            _latitude = params[0].latitude;
            _longitude = params[0].longitude;

            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(
                        _latitude,
                        _longitude,
                        // In this sample, get just a single address.
                        1);
            } catch (IOException ioException) {
                // Catch network or other I/O problems.

            }

            // Handle case where no address was found.
            if (addresses == null || addresses.size() == 0) {
                return "NA";

            } else {
                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<String>();

                // Fetch the address lines using getAddressLine,
                // join them, and send them to the thread.
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                }
                return TextUtils.join(System.getProperty("line.separator"),
                        addressFragments);

            }
            ////////////////////////////////
        }

        @Override
        protected void onPostExecute(String addressText) {
            if (addressText == null)
                addressText = "NA";
            else if (addressText.isEmpty())
                addressText = "NA";

        }
    }




}
