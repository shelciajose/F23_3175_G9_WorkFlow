package com.example.workflow.fragments;

import static com.example.workflow.utils.ConstantUtils.CHECKING_IN_BUTTON_NAME;
import static com.example.workflow.utils.ConstantUtils.CHECKING_OUT_BUTTON_NAME;
import static com.example.workflow.utils.ConstantUtils.CHECK_IN_BUTTON_NAME;
import static com.example.workflow.utils.ConstantUtils.CHECK_OUT_BUTTON_NAME;
import static com.example.workflow.utils.ConstantUtils.NOTIFICATION_TYPE_NOTHING;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.workflow.R;
import com.example.workflow.database.OfflineDatabase;
import com.example.workflow.locationClasses.LocationUpdateListener;
import com.example.workflow.locationClasses.MyLocationManager;
import com.example.workflow.notification.CheckInOutNotifi;
import com.example.workflow.services.MyLocationListener;
import com.example.workflow.utils.CommonFunc;
import com.example.workflow.utils.ConstantUtils;
import com.example.workflow.utils.PreferenceUtils;

import java.util.Timer;

public class HomeFragment extends Fragment implements View.OnClickListener, LocationUpdateListener {


    private View viewFragment = null;
    private OfflineDatabase offlineDatabase = null;
    private Timer timer = null;
    public LinearLayoutCompat linearLayoutCompat = null;
    public FrameLayout frameLayout = null;

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final long MIN_TIME_INTERVAL = 10000; // 10 seconds
    private static final float MIN_DISTANCE_CHANGE = 10.0f; // 10 meters

    private MyLocationManager locationManager;
    private MyLocationListener locationListener;

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

        settingView();

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

    private void timercheck() {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToRepeat = 1000;    //in milissegunds
                try {
                    Thread.sleep(timeToRepeat);
                } catch (Exception e) {
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (getActivity() != null) {
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

    public void settingView() {
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

        else if (view.getId() == R.id.fragment_home_check_in_location_id) {
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
                        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            startLocationUpdates();
                        } else {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                        }
                        /*if (PermissionUtils.checkLoactionPermission(getActivity())) {
                            connectToGoogleApiClientAndGetTheAddress(true);
                            // showProgressDialog();
                        } else {
                            PermissionUtils.openPermissionDialog(getActivity(), "Please grant location permission");
                        }*/

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


    private void startLocationUpdates() {
        showProgressDialog();
        // Initialize MyLocationManager and pass this fragment as the listener
        if (isAdded()) { // Check if the fragment is added to an activity
            Context context = requireContext();
            if (context != null) {
                locationManager = new MyLocationManager(context, this);
                locationManager.startLocationUpdates();
            } else {
                Log.e("YourFragment", "Fragment's context is null");
                dismissProgressDialog();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, start receiving location updates
            startLocationUpdates();
        } else {
            // Permission denied, handle accordingly (e.g., show a message)
        }
    }

    // Implement LocationUpdateListener interface method
    @Override
    public void onLocationUpdate(Location location) {
        // Handle location updates

        locationManager.stopLocationUpdates();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        String address = CommonFunc.getAddressFromCoordinates(getActivity(), latitude, longitude);
        String timeRef = String.valueOf(CommonFunc.getCurrentSystemTimeStamp());


        if(((AppCompatButton) viewFragment.findViewById(R.id.fragment_home_check_in_out)).getText().toString().equals(CHECK_IN_BUTTON_NAME)) {
            if ((new OfflineDatabase(getActivity()).addCheckInDetails(timeRef, String.valueOf(latitude), String.valueOf(longitude),
                    address, timeRef, CommonFunc.getTodayDate())) == -1) {
                dismissProgressDialog();
                Toast.makeText(getActivity(), "Something wrong happened", Toast.LENGTH_SHORT).show();
            } else {
                if (PreferenceUtils.setUserHasCheckedIn(getActivity(), timeRef)) {
                    if (PreferenceUtils.addCheckInTimeToSharedPreference(getActivity(), Long.parseLong(timeRef))) {
                        PreferenceUtils.setCheckInLocationToSharedPreference(getActivity(), address);
                        CheckInOutNotifi.closeCheckInAndOutWarnningNotification(getActivity(), true);
                        setTimeAndLocation(address, CommonFunc.convertTimestampToTime(timeRef));
                        runContiniouslyWorkTime();
                        ((AppCompatButton) viewFragment.findViewById(R.id.fragment_home_check_in_out))
                                .setText(CHECK_OUT_BUTTON_NAME);
                        CommonFunc.showNotification("Check-in", "You have successfully checked-in", NOTIFICATION_TYPE_NOTHING,
                                getActivity(), false, false);
                        dismissProgressDialog();

                    }
                }
            }
        }
        else{
            if ((new OfflineDatabase(getActivity()).addCheckOutDetails(timeRef, timeRef, String.valueOf(latitude), String.valueOf(longitude),
                    address, CommonFunc.getTodayDate(), PreferenceUtils.getCheckedInUserAttenceId(getActivity()))) == -1) {
                dismissProgressDialog();
                Toast.makeText(getActivity(), "Something wrong happened", Toast.LENGTH_SHORT).show();
            } else {
                if (PreferenceUtils.setUserHasCheckedOut(getActivity())) {
                        PreferenceUtils.setCheckInLocationToSharedPreference(getActivity(), address);
                        CheckInOutNotifi.closeCheckInAndOutWarnningNotification(getActivity(), false);
                        setTimeAndLocation("Check-In for location", "On Duty From NA");
                        ((AppCompatButton) viewFragment.findViewById(R.id.fragment_home_check_in_out))
                                .setText(CHECK_IN_BUTTON_NAME);
                        if(timer!=null){
                            timer.cancel();
                            ((AppCompatTextView) viewFragment.findViewById(R.id.fragment_home_worked_time_id))
                                    .setText(" 00:00:00");
                        }

                    CommonFunc.showNotification("Check-out", "You have successfully checked-out", NOTIFICATION_TYPE_NOTHING,
                            getActivity(), false, false);

                        dismissProgressDialog();
                }
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
