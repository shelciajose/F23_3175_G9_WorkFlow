package com.example.workflow.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workflow.R;
import com.example.workflow.services.PostCheckinServices;
import com.example.workflow.services.PostCheckoutServices;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class CheckinPrompt {

    private Context context = null;
    private AppCompatActivity appCompatActivity = null;
    private View viewTopView = null;

    public CheckinPrompt(Context context, AppCompatActivity appCompatActivity, View viewTopView) {
        this.context = context;
        this.appCompatActivity = appCompatActivity;
        this.viewTopView = viewTopView;
    }

    private Dialog dialogProgress = null;
    ////

    public void showProgressDialog() {
        if (dialogProgress == null) {
            dialogProgress = new Dialog(context);
            dialogProgress.setCancelable(false);
            dialogProgress.getWindow().setBackgroundDrawable(new
                    ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialogProgress.setContentView(R.layout.loading_layout);
            dialogProgress.show();
        } else {
            if (!dialogProgress.isShowing())
                dialogProgress.show();
        }
    }

    public void dismissProgressDialog() {
        if (dialogProgress != null) {
            if (dialogProgress.isShowing()) {
                dialogProgress.dismiss();
            }
            dialogProgress = null;
        }
    }

    public void startToChkInOutOut() {
        if (PermissionUtils.checkLoactionPermission(context)) {
            connectToGoogleApiClientAndGetTheAddress();
        } else {
            PermissionUtils.openPermissionDialog(context, "Please grant location permission");
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////    get loc stuff. . .  .
    ////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////// google api client establishment to access address
    ////
    private GoogleApiClient googleApiClient = null;
    private LocationRequest locationRequest = null;
    private StartServiceAsyncTask startServiceAsyncTask = null;

    //
    ///
    private void connectToGoogleApiClientAndGetTheAddress() {
        if (NetworkUtils.checkInternetAndOpenDialog(context, appCompatActivity, viewTopView)) {
            if (CommonFunc.isGooglePlayServicesAvailable(appCompatActivity)) {
                if (dialogProgress == null) {
                    showProgressDialog();
                    connectGoogleApiClient();
                }
            }
        }
    }


    private void connectGoogleApiClient() {

        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        checkForLocationSettings();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(context, "Google play service not responding... please refresh your mobile",
                                Toast.LENGTH_LONG).show();
                        googleApiClient = null;
                        dismissProgressDialog();

                    }
                }).build();
        googleApiClient.connect();


    }


    private void checkForLocationSettings() {
        ////////////////////////////////////////////////////////
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************

        Task<LocationSettingsResponse> locationSettingsResponseTask =
                LocationServices.getSettingsClient(context).checkLocationSettings(builder.build());
        //
        locationSettingsResponseTask.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    callServiceRunningClass();

                } catch (ApiException exception) {
                    dismissProgressDialog();

                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                        appCompatActivity,
                                        ConstantUtils.REQUEST_CHECK_SETTINGS_HOME_FRAGMENT);
                                CommonFunc.commonDialog(context, context.getString(R.string.alert),
                                        "Please grant GPS access", false, appCompatActivity, viewTopView);
                            } catch (IntentSender.SendIntentException | ClassCastException e) {
                                // Ignore the error.
                                CommonFunc.commonDialog(context, "GPS not working.. Please refresh your device",
                                        context.getString(R.string.justErrorCode) + " 98", false, appCompatActivity, viewTopView);

                            }

                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            CommonFunc.commonDialog(context, "GPS not working.. Please refresh your device",
                                    context.getString(R.string.justErrorCode) + " 87", false, appCompatActivity, viewTopView);
                            break;
                    }
                }
            }
        });
        //

    }


    private void callServiceRunningClass() {
        if (googleApiClient != null) {
            if (googleApiClient.isConnected() || googleApiClient.isConnecting())
                googleApiClient.disconnect();

        }
        if (startServiceAsyncTask == null) {
            startServiceAsyncTask = new StartServiceAsyncTask();
            startServiceAsyncTask.execute();
        } else if (startServiceAsyncTask.getStatus() == AsyncTask.Status.RUNNING ||
                startServiceAsyncTask.getStatus() == AsyncTask.Status.PENDING) {
            startServiceAsyncTask.cancel(true);
            startServiceAsyncTask = new StartServiceAsyncTask();
            startServiceAsyncTask.execute();
        } else {
            startServiceAsyncTask = new StartServiceAsyncTask();
            startServiceAsyncTask.execute();
        }

    }


    private class StartServiceAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String res = null;

            if (CommonFunc.isServiceRunningForCheckInAndOut(context, true)) {
                res = "checkIn";
            } else if (CommonFunc.isServiceRunningForCheckInAndOut(context, false)) {
                res = "checkOut";
            }
            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            dismissProgressDialog();
            if (res == null) {
                if (!PreferenceUtils.getIsUserCheckedInManually(context, false,
                        null, null, null, null)) {
                    Intent intent = new Intent(context, PostCheckinServices.class);
                    context.startService(intent);
                    PreferenceUtils.setCheckInOutServiceRunning(context, true, "cin");
                } else {
                    Intent intent = new Intent(context, PostCheckoutServices.class);
                    context.startService(intent);
                    PreferenceUtils.setCheckInOutServiceRunning(context, true, "cout");
                }
            } else if (res.equals("checkIn")) {
                CommonFunc.commonDialog(context, context.getString(R.string.alert), "Check-in under process please wait..",
                        false, appCompatActivity, viewTopView);

            } else if (res.equals("checkOut")) {
                CommonFunc.commonDialog(context, context.getString(R.string.alert), "Check-out under process please wait..",
                        false, appCompatActivity, viewTopView);
            } else {
                Toast.makeText(context, "Open app again", Toast.LENGTH_SHORT).show();
                System.exit(0);
            }
        }
    }

}
