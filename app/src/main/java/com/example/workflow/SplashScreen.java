package com.example.workflow;

import static com.example.workflow.utils.ConstantUtils.ACCOUNT;
import static com.example.workflow.utils.ConstantUtils.ACCOUNT_TYPE;
import static com.example.workflow.utils.ConstantUtils.NO_SIM_CARD;
import static com.example.workflow.utils.PermissionUtils.openPermissionDialog;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.workflow.utils.CommonFunc;
import com.example.workflow.utils.ConstantUtils;
import com.example.workflow.utils.NetworkUtils;
import com.example.workflow.utils.PermissionUtils;
import com.example.workflow.utils.PreferenceUtils;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SplashScreen extends AppCompatActivity {

    private String versonName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        try {
            ContentResolver.setSyncAutomatically(CommonFunc.getAccountToSync(SplashScreen.this), ConstantUtils.AUTHORITY, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CommonFunc.isAutomaticTimeAndZoneEnabelled(this)) {
            checkTheUserActivity();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Alert!!")
                    .setMessage("" + getString(R.string.app_name) + " " + getString(R.string.time_zone_alert))
                    .setCancelable(false)
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            CommonFunc.OpenDateTimeSettingsScreen(SplashScreen.this);

                        }

                    }).create().show();
        }
    }


    ///////////////////////////////////////////////////////////// check the user activity
    private void checkTheUserActivity() {
        if (getIntent().getAction() != null) {
            if (PermissionUtils.checkLocAndPhonePermission(SplashScreen.this)) {
                startTheApp();
            } else {
                reqForTheLocationAndPhonePermission(SplashScreen.this);
            }

        } else { // came after clearing the preference
            if (PermissionUtils.checkLocAndPhonePermission(SplashScreen.this)) {
                startTheApp();
            } else {
                reqForTheLocationAndPhonePermission(SplashScreen.this);
            }
        }
    }

    ///////////////////////////////////////////////////////////// open the app
    private void startTheApp() {
        showProgressDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissProgressDialog();
                if (PreferenceUtils.checkUserisLogedin(SplashScreen.this)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        checkForLocationSettings();
                    } else {
                        Toast.makeText(SplashScreen.this, "Low android version unable to detect fake location", Toast.LENGTH_SHORT).show();
                        if (NetworkUtils.checkInternetAndOpenDialog(SplashScreen.this, SplashScreen.this,
                                findViewById(R.id.activity_splashscreen_top_view_id))) {
                            String simJson = CommonFunc.formTheSimInfoString(SplashScreen.this);
                            if (!simJson.equals(NO_SIM_CARD)) // check sim card in lolopop
                            {
                                if (simJson.equals(PreferenceUtils.getSimDataFromPreference(SplashScreen.this))) {

                                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(SplashScreen.this, "Sim change detected.. verify again..", Toast.LENGTH_LONG).show();
                                    PreferenceUtils.clearUserPreference(SplashScreen.this);
                                    startActivity(new Intent(SplashScreen.this, SplashScreen.class));
                                    finish();
                                }
                            }
                        }
                    }
                } else {
                    startActivity(new Intent(SplashScreen.this, RegistrationActivity.class));
                    finish();
                }
            }
        }, 3000);

    }

    private LocationRequest locationRequest = null;
    private FusedLocationProviderClient mFusedLocationClient = null;

    private void checkForLocationSettings() {
        ////////////////////////////////////////////////////////
        showProgressDialog();
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************

        Task<LocationSettingsResponse> locationSettingsResponseTask =
                LocationServices.getSettingsClient(SplashScreen.this).checkLocationSettings(builder.build());
        //
        locationSettingsResponseTask.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    getLastLocationFromGoogleApiClient();
                } catch (ApiException exception) {
                    dismissProgressDialog();

                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) exception;

                                if (CommonFunc.isGpsOn(SplashScreen.this)) {
                                    if (CommonFunc.getLocationMode(SplashScreen.this) != 3) {
                                        CommonFunc.gotoLocationSettings(SplashScreen.this);
                                    }
                                } else {
                                    resolvable.startResolutionForResult(
                                            SplashScreen.this,
                                            ConstantUtils.REQUEST_CHECK_SETTINGS_HOME_FRAGMENT_AT_THE_TIME_OF_MY_VISIT);
                                }

                            } catch (IntentSender.SendIntentException | ClassCastException e) {
                                // Ignore the error.
                                CommonFunc.commonDialog(SplashScreen.this, getString(R.string.justErrorCode) + " 98",
                                        "GPS not working.. Please refresh your device", false,
                                        SplashScreen.this, findViewById(R.id.activity_splashscreen_top_view_id));
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            CommonFunc.commonDialog(SplashScreen.this, getString(R.string.justErrorCode) + " 87",
                                    "GPS not working.. Please refresh your device", false,
                                    SplashScreen.this, findViewById(R.id.activity_splashscreen_top_view_id));
                            break;
                    }
                }
            }
        });
        //
    }

    @SuppressWarnings("MissingPermission")
    private void getLastLocationFromGoogleApiClient() {
        try {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(SplashScreen.this);
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } catch (Exception e) {
            String simJson = CommonFunc.formTheSimInfoString(SplashScreen.this);
            if (!simJson.equals(NO_SIM_CARD)) // check sim card in lolopop
            {
                if (simJson.equals(PreferenceUtils.getSimDataFromPreference(SplashScreen.this))) {

                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(SplashScreen.this, "Sim change detected.. verify again..", Toast.LENGTH_LONG).show();
                    PreferenceUtils.clearUserPreference(SplashScreen.this);
                    startActivity(new Intent(SplashScreen.this, SplashScreen.class));
                    finish();
                }
                CommonFunc.commonDialog(SplashScreen.this, "",
                        e.getMessage(), false,
                        SplashScreen.this, findViewById(R.id.activity_splashscreen_top_view_id));
                e.printStackTrace();
            }
        }
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            dismissProgressDialog();
            for (Location location : locationResult.getLocations())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    String simJson = CommonFunc.formTheSimInfoString(SplashScreen.this);
                    if (!simJson.equals(NO_SIM_CARD)) // check sim card in lolopop
                    {
                        if (simJson.equals(PreferenceUtils.getSimDataFromPreference(SplashScreen.this))) {
                            startActivity(new Intent(SplashScreen.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(SplashScreen.this, "Sim change detected.. verify again..", Toast.LENGTH_LONG).show();
                            PreferenceUtils.clearUserPreference(SplashScreen.this);
                            startActivity(new Intent(SplashScreen.this, SplashScreen.class));
                            finish();
                        }
                    } else {
                        CommonFunc.commonDialog(SplashScreen.this, "",
                                "Low android version", false,
                                SplashScreen.this, findViewById(R.id.activity_splashscreen_top_view_id));

                    }
                    StopGoogleLocationLisner();
                }
        }

    };

        private void StopGoogleLocationLisner() {
            if (mFusedLocationClient != null)
                mFusedLocationClient.removeLocationUpdates(locationCallback);
        }

        ///////////////////////////////////////////////////////////////// req for loc permission
        private void reqForTheLocationAndPhonePermission(Activity activity) {

            if (PreferenceUtils.checkUserisLogedin(SplashScreen.this)) {
                PreferenceUtils.storeLocPermissionChangedToSharedPreference(SplashScreen.this);
            }
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_PHONE_STATE)
            ) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                openPermissionDialog(activity, "Please grant theWorkflow app all permissions from the app settings to run seamlessly (Location and Make,manage phone calls are mandatory)");
            } else {
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_PHONE_STATE},
                        10);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


        public static Account CreateSyncAccount(Context context) {
            // Create the account type and default account
            Account newAccount = new Account(
                    ACCOUNT, ACCOUNT_TYPE);
            // Get an instance of the Android account manager
            AccountManager accountManager =
                    (AccountManager) context.getSystemService(
                            ACCOUNT_SERVICE);
            /*
             * Add the account and account type, no password or user data
             * If successful, return the Account object, otherwise report an error.
             */
            if (accountManager.addAccountExplicitly(newAccount, null, null)) {
                /*
                 * If you don't set android:syncable="true" in
                 * in your <provider> element in the manifest,
                 * then call context.setIsSyncable(account, AUTHORITY, 1)
                 * here.
                 */
                // Inform the system that this account supports sync
//            ContentResolver.setIsSyncable(newAccount, AUTHORITY, 1);
//            // Inform the system that this account is eligible for auto sync when the network is up
//            ContentResolver.setSyncAutomatically(newAccount, AUTHORITY, true);
            } else {
                /*
                 * The account exists or some other error occurred. Log this, report it,
                 * or handle it internally.
                 */
            }
            return newAccount;
        }

        /////////////////////////////////////////////////////////////////////
        private Dialog dialog = null;

        public void showProgressDialog() {
            try {
                if (dialog == null) {
                    dialog = new Dialog(SplashScreen.this);
                    dialog.setCancelable(false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.loading_layout);
                }
                if (!dialog.isShowing())
                    dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void dismissProgressDialog() {
            try {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    dialog = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }



}
