package com.example.workflow.locationClasses;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

public class MyLocationManager {
    private static final long MIN_TIME_INTERVAL = 1000; // 1 seconds
    private static final float MIN_DISTANCE_CHANGE = 1.0f; // 1 meters

    private LocationManager locationManager;
    private MyLocationListener locationListener;
    private LocationUpdateListener updateListener;

    private Context context;

    public MyLocationManager(Context context, LocationUpdateListener listener) {
        this.updateListener = listener;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.locationListener = new MyLocationListener();
        this.locationListener.setUpdateListener(updateListener);
        this.context = context;
    }

    public void startLocationUpdates() {
        // Choose the location provider (NETWORK_PROVIDER or GPS_PROVIDER)
        String provider = LocationManager.NETWORK_PROVIDER;

        // Request location updates with a specified min time interval and min distance change
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(provider, MIN_TIME_INTERVAL, MIN_DISTANCE_CHANGE, locationListener);
        }
    }

    public void stopLocationUpdates() {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }


}
