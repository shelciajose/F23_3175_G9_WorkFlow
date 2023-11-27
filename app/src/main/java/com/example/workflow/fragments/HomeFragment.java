package com.example.workflow.fragments;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.example.workflow.utils.ConstantUtils.CHECKING_IN_BUTTON_NAME;
import static com.example.workflow.utils.ConstantUtils.CHECKING_OUT_BUTTON_NAME;
import static com.example.workflow.utils.ConstantUtils.CHECK_IN_BUTTON_NAME;
import static com.example.workflow.utils.ConstantUtils.CHECK_OUT_BUTTON_NAME;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.workflow.R;
import com.example.workflow.database.OfflineDatabase;
import com.example.workflow.services.PostCheckinServices;
import com.example.workflow.services.PostCheckoutServices;
import com.example.workflow.utils.CommonFunc;
import com.example.workflow.utils.ConstantUtils;
import com.example.workflow.utils.PermissionUtils;
import com.example.workflow.utils.PreferenceUtils;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Timer;

public class HomeFragment extends Fragment implements View.OnClickListener {


    private View viewFragment = null;
    private StartServiceAsyncTask startServiceAsyncTask = null;
    private OfflineDatabase offlineDatabase = null;
    private Timer timer = null;
    public LinearLayoutCompat linearLayoutCompat = null;
    public FrameLayout frameLayout = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewFragment = inflater.inflate(R.layout.fragment_home, container, false);
        viewFragment.findViewById(R.id.fragment_home_check_in_out).setOnClickListener(this);

        initializeDb();

        frameLayout = viewFragment.findViewById(R.id.fragment_home_scroolview);

        viewFragment.findViewById(R.id.fragment_home_check_in_location_id).setOnClickListener(this);

        timercheck();

        return viewFragment;
    }

    private void initializeDb() {
        if (offlineDatabase == null) {
            offlineDatabase = new OfflineDatabase(getActivity());
        }
    }


    @SuppressLint("WrongConstant")
    @Override
    public void onResume() {
        super.onResume();
        initializeDb();
        settingView();
    }

    private void timercheck(){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToRepeat = 1000;    //in milissegunds
                try{Thread.sleep(timeToRepeat);}catch (Exception e) {}
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(getActivity()!=null) {
                            if (PreferenceUtils.getIsUserCheckedInManually(getActivity(), false,
                                    null, null, null, null)) {
                                setTimeAndLocation(PreferenceUtils.getCheckInLocationToSharedPreference(getActivity()),
                                        CommonFunc.convertTimestampToTime(PreferenceUtils.getTodayCheckInTimeStamp(getActivity()) + ""));
                                // runContiniouslyWorkTime();
                                ((AppCompatButton) viewFragment.findViewById(R.id.fragment_home_check_in_out))
                                        .setText(CHECK_OUT_BUTTON_NAME);
                            } else {
                                setTimeAndLocation("Check-In for location", "On Duty From NA");
                                if (timer != null)
                                    timer.cancel();
                                ((AppCompatTextView) viewFragment.findViewById(R.id.fragment_home_worked_time_id))
                                        .setText(" 00:00:00");
                                ((AppCompatButton) viewFragment.findViewById(R.id.fragment_home_check_in_out))
                                        .setText(CHECK_IN_BUTTON_NAME);
                            }
                        }

                        timercheck();
                    }
                });
            }
        }).start();
    }

    public void settingView(){
        try {
            setTheButtonTextDefaultText(false, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (PreferenceUtils.getIsUserCheckedInManually(getActivity(), false,
                null, null, null, null)) {
            runContiniouslyWorkTime();
        } else {
            setTimeAndLocation("Check-In for location", "On Duty From NA");
            if (timer != null)
                timer.cancel();
        }
    }

    void setTimeAndLocation(String address, String time) {
        ((AppCompatTextView) viewFragment.findViewById(R.id.fragment_home_check_in_time_id))
                .setText(time);

        ((AppCompatTextView) viewFragment.findViewById(R.id.fragment_home_check_in_location_id))
                .setText(address);
    }

    public void setTheButtonTextDefaultText(boolean showSnakebar, String message) throws Exception {

        if (PreferenceUtils.isCheckInOutServiceRunning(getActivity())) {
            if (CommonFunc.isServiceRunningForCheckInAndOut(getActivity(), true)) {
                ((AppCompatButton) viewFragment.findViewById(R.id.fragment_home_check_in_out))
                        .setText(CHECKING_IN_BUTTON_NAME);
                if (showSnakebar) {
                    CommonFunc.commonDialog(getActivity(),
                            getString(R.string.alert),
                            "Checking-in in process...", false,
                            (AppCompatActivity) getActivity(),
                            viewFragment.findViewById(R.id.fragment_home_scroolview));
                }
            } else if (CommonFunc.isServiceRunningForCheckInAndOut(getActivity(), false)) {
                ((AppCompatButton) viewFragment.findViewById(R.id.fragment_home_check_in_out))
                        .setText(CHECKING_OUT_BUTTON_NAME);
                if (showSnakebar) {
                    CommonFunc.commonDialog(getActivity(),
                            getString(R.string.alert),
                            "Checking-out in process...", false,
                            (AppCompatActivity) getActivity(),
                            viewFragment.findViewById(R.id.fragment_home_scroolview));
                }
            } else {
                if (!PreferenceUtils.getIsUserCheckedInManually(getActivity(), false, null,
                        null, null, null)) {
                    ((AppCompatButton) viewFragment.findViewById(R.id.fragment_home_check_in_out))
                            .setText(CHECK_IN_BUTTON_NAME);

                    setTimeAndLocation("Check-In for location", "On Duty From NA");
                    if (timer != null)
                        timer.cancel();
                    runContiniouslyWorkTime();

                    if (showSnakebar) {
                        CommonFunc.commonDialog(getActivity(),
                                getString(R.string.alert),
                                message, false,
                                (AppCompatActivity) getActivity(),
                                viewFragment.findViewById(R.id.fragment_home_scroolview));
                    }
                } else {
                    ((AppCompatButton) viewFragment.findViewById(R.id.fragment_home_check_in_out))
                            .setText(CHECK_OUT_BUTTON_NAME);

                    setTimeAndLocation(PreferenceUtils.getCheckInLocationToSharedPreference(getActivity()),
                            CommonFunc.convertTimestampToTime(PreferenceUtils.getTodayCheckInTimeStamp(getActivity()) + ""));
                    runContiniouslyWorkTime();

                    if (showSnakebar) {
                        CommonFunc.commonDialog(getActivity(),
                                getString(R.string.alert),
                                message, false,
                                (AppCompatActivity) getActivity(),
                                viewFragment.findViewById(R.id.fragment_home_scroolview));
                    }
                }
            }


        } else {

            if (!PreferenceUtils.getIsUserCheckedInManually(getActivity(), false,
                    null, null, null, null)) {
                ((AppCompatButton) viewFragment.findViewById(R.id.fragment_home_check_in_out))
                        .setText(CHECK_IN_BUTTON_NAME);
                setTimeAndLocation("Check-In for location", "On Duty From NA");
                if (timer != null)
                    timer.cancel();
                runContiniouslyWorkTime();

                if (showSnakebar) {
                    CommonFunc.commonDialog(getActivity(),
                            getString(R.string.alert),
                            message, false,
                            (AppCompatActivity) getActivity(),
                            viewFragment.findViewById(R.id.fragment_home_scroolview));
                }
            } else {
                ((AppCompatButton) viewFragment.findViewById(R.id.fragment_home_check_in_out))
                        .setText(CHECK_OUT_BUTTON_NAME);

                setTimeAndLocation(PreferenceUtils.getCheckInLocationToSharedPreference(getActivity()),
                        CommonFunc.convertTimestampToTime(PreferenceUtils.getTodayCheckInTimeStamp(getActivity()) + ""));
                runContiniouslyWorkTime();

                if (showSnakebar) {
                    CommonFunc.commonDialog(getActivity(),
                            getString(R.string.alert),
                            message, false,
                            (AppCompatActivity) getActivity(),
                            viewFragment.findViewById(R.id.fragment_home_scroolview));
                }
            }
        }
    }

    void runContiniouslyWorkTime() {
        long millis = CommonFunc.getCurrentSystemTimeStamp() - PreferenceUtils.getTodayCheckInTimeStamp(getActivity());
        CommonFunc.getDurationString(millis);
        if (isAdded()) {
            if (PreferenceUtils.getIsUserCheckedInManually(getActivity(), false,
                    null, null, null, null)) {
                ((AppCompatTextView) viewFragment.findViewById(R.id.fragment_home_worked_time_id))
                        .setText(CommonFunc.getDurationString(millis));
            } else {
                if (timer != null)
                    timer.cancel();
                ((AppCompatTextView) viewFragment.findViewById(R.id.fragment_home_worked_time_id))
                        .setText(" 00:00:00");
            }
        }




      /*  timer = new Timer();
        if (getActivity() != null)
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setWorkingTime();
                        }
                    });
                }
            }, 0, 1000);

        else {
            timer.cancel();
            return;
        }*/
    }

    public void setWorkingTime() {
        if (PreferenceUtils.getIsUserCheckedInManually(getActivity(), false,
                null, null, null, null)) {
            long millis = CommonFunc.getCurrentSystemTimeStamp() - PreferenceUtils.getTodayCheckInTimeStamp(getActivity());
            CommonFunc.getDurationString(millis);
            if (isAdded())
                ((AppCompatTextView) viewFragment.findViewById(R.id.fragment_home_worked_time_id))
                        .setText(CommonFunc.getDurationString(millis));
        } else {
            if (timer != null)
                timer.cancel();
            ((AppCompatTextView) viewFragment.findViewById(R.id.fragment_home_worked_time_id))
                    .setText(" 00:00:00");
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fragment_home_check_in_out)
            checkIsCheckingInRunning();

        else if (view.getId() == R.id.fragment_home_check_in_location_id){
            if (PreferenceUtils.getIsUserCheckedInManually(getActivity(), false,
                    null, null, null, null))
                Toast.makeText(getActivity(), "Check-in Address:: "
                        + ((AppCompatTextView) viewFragment.findViewById(R.id.fragment_home_check_in_location_id)).getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }


    private void checkIsCheckingInRunning() {
        if (((AppCompatButton) viewFragment.findViewById(R.id.fragment_home_check_in_out))
                .getText().toString().equals(ConstantUtils.CHECKING_IN_BUTTON_NAME) ||
                ((AppCompatButton) viewFragment.findViewById(R.id.fragment_home_check_in_out))
                        .getText().toString().equals(ConstantUtils.CHECKING_OUT_BUTTON_NAME)) {
            switch (((AppCompatButton) viewFragment.findViewById(R.id.fragment_home_check_in_out))
                    .getText().toString()) {
                case ConstantUtils.CHECKING_IN_BUTTON_NAME:
                    CommonFunc.commonDialog(getActivity(),
                            getString(R.string.alert),
                            "Checking-in in process... please wait..", false,
                            (AppCompatActivity) getActivity(),
                            viewFragment.findViewById(R.id.fragment_home_scroolview));
                    break;
                case ConstantUtils.CHECKING_OUT_BUTTON_NAME:
                    CommonFunc.commonDialog(getActivity(),
                            getString(R.string.alert),
                            "Checking-out in process... please wait..", false,
                            (AppCompatActivity) getActivity(),
                            viewFragment.findViewById(R.id.fragment_home_scroolview));

            }
        } else {
            alertCheckInOuTDialog();
        }
    }

    private void alertCheckInOuTDialog() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        switch (((AppCompatButton) viewFragment.findViewById(R.id.fragment_home_check_in_out))
                .getText().toString()) {
            case ConstantUtils.CHECK_IN_BUTTON_NAME:
                builder1.setMessage("Do you want to check-in ?");
                break;
            case ConstantUtils.CHECK_OUT_BUTTON_NAME:
                builder1.setMessage("Do you want to check-out ?");
        }
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        if (PermissionUtils.checkLoactionPermission(getActivity())) {
                            connectToGoogleApiClientAndGetTheAddress(true);
                           // showProgressDialog();
                        } else {
                            PermissionUtils.openPermissionDialog(getActivity(), "Please grant location permission");
                        }

                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertCheckInAlertDialog = builder1.create();
        alertCheckInAlertDialog.show();
    }


    private Dialog dialogProgress = null;

    ////
    public void showProgressDialog() {
        if (dialogProgress == null) {
            dialogProgress = new Dialog(getActivity());
            dialogProgress.setCancelable(false);
            dialogProgress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////    get loc stuff. . .  .
////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////// google api client establishment to access address
////
//    private GoogleApiClient googleApiClient = null;
    private LocationRequest locationRequest = null;
    private FusedLocationProviderClient mFusedLocationClient = null;
    //
    private LocationManager locationManager = null;

    ///
    private void connectToGoogleApiClientAndGetTheAddress(boolean isForCheckInOut) {
        if (CommonFunc.isGooglePlayServicesAvailable(getActivity())) {
            if (dialogProgress == null) {
                showProgressDialog();
                checkForLocationSettings(isForCheckInOut);
            }
        }
    }

    private void checkForLocationSettings(final boolean isForCheckInOut) {
        ////////////////////////////////////////////////////////

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************

        Task<LocationSettingsResponse> locationSettingsResponseTask =
                LocationServices.getSettingsClient(getActivity()).checkLocationSettings(builder.build());
        //
        locationSettingsResponseTask.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    if (isForCheckInOut) {
                        callServiceRunningClass();
                    }

                } catch (ApiException exception) {
                    dismissProgressDialog();

                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;

                                if (CommonFunc.isGpsOn(getActivity())) {
                                    if (CommonFunc.getLocationMode(getActivity()) != 3) {
                                        CommonFunc.gotoLocationSettings(getActivity());
                                    }
                                } else {
                                    if (isForCheckInOut)
                                        resolvable.startResolutionForResult(
                                                getActivity(),
                                                ConstantUtils.REQUEST_CHECK_SETTINGS_HOME_FRAGMENT_AT_THE_TIME_OF_CHECK_IN_OUT);
                                    else
                                        resolvable.startResolutionForResult(
                                                getActivity(),
                                                ConstantUtils.REQUEST_CHECK_SETTINGS_HOME_FRAGMENT_AT_THE_TIME_OF_MY_VISIT);
                                }

                            } catch (IntentSender.SendIntentException | ClassCastException e) {
                                // Ignore the error.
                                CommonFunc.commonDialog(getActivity(), "GPS not working.. Please refresh your device",
                                        getString(R.string.justErrorCode) + " 98", false,
                                        (AppCompatActivity) getActivity(), viewFragment.findViewById(R.id.fragment_home_scroolview));

                            }

                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            CommonFunc.commonDialog(getActivity(), "GPS not working.. Please refresh your device",
                                    getString(R.string.justErrorCode) + " 87", false,
                                    (AppCompatActivity) getActivity(), viewFragment.findViewById(R.id.fragment_home_scroolview));
                            break;
                    }
                }
            }
        });
        //

    }

    private void callServiceRunningClass() {
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

            if (CommonFunc.isServiceRunningForCheckInAndOut(getActivity(), true)) {
                res = CHECK_IN_BUTTON_NAME;
            } else if (CommonFunc.isServiceRunningForCheckInAndOut(getActivity(), false)) {
                res = CHECK_OUT_BUTTON_NAME;
            }
            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            dismissProgressDialog();
            if (res == null) {
                if (!PreferenceUtils.getIsUserCheckedInManually(getActivity(), false,
                        null, null, null, null)) {

                    Intent intent = new Intent(getActivity(), PostCheckinServices.class);
                    getActivity().startService(intent);
                    ((AppCompatButton) viewFragment.findViewById(R.id.fragment_home_check_in_out))
                            .setText(CHECKING_IN_BUTTON_NAME);
                    PreferenceUtils.setCheckInOutServiceRunning(getActivity(), true, "cin");

                } else {
                    Intent intent = new Intent(getActivity(), PostCheckoutServices.class);
                    getActivity().startService(intent);
                    ((AppCompatButton) viewFragment.findViewById(R.id.fragment_home_check_in_out))
                            .setText(CHECKING_OUT_BUTTON_NAME);
                    PreferenceUtils.setCheckInOutServiceRunning(getActivity(), true, "cout");
                }

            } else if (res.equals(CHECK_IN_BUTTON_NAME)) {
                CommonFunc.commonDialog(getActivity(), getString(R.string.alert), "Check-in under process please wait..",
                        false,
                        (AppCompatActivity) getActivity(), viewFragment.findViewById(R.id.fragment_home_scroolview));

            } else if (res.equals(CHECK_OUT_BUTTON_NAME)) {
                CommonFunc.commonDialog(getActivity(), getString(R.string.alert), "Check-out under process please wait..",
                        false,
                        (AppCompatActivity) getActivity(), viewFragment.findViewById(R.id.fragment_home_scroolview));
            } else {
                Toast.makeText(getActivity(), "Open app again", Toast.LENGTH_SHORT).show();
                System.exit(0);
            }
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        dismissProgressDialog();
        if (timer != null)
            timer.cancel();
    }

}
