package com.example.workflow.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workflow.R;

public class PreferenceUtils {

    ////////////////////////////// session preference
    public static final String SESSION_PREFERENCE_NAME = "SESSION_PREFERENCE_NAME";
    //
    public static final String SESSION_PREFERENCE_MOBILE_NO = "SESSION_PREFERENCE_MOBILE_NO";
    public static final String SESSION_PREFERENCE_EMAIL = "SESSION_PREFERENCE_EMAIL";
    public static final String SESSION_PREFERENCE_FULLNAME = "SESSION_PREFERENCE_FULLNAME";
    public static final String SESSION_PREFERENCE_ADDRESS = "SESSION_PREFERENCE_ADDRESSS";
    public static final String SESSION_PREFERENCE_USERNAME = "SESSION_PREFERENCE_USERNAME";
    public static final String SESSION_PREFERENCE_USER_ID = "SESSION_PREFERENCE_USER_ID";
    public static final String SESSION_PREFERENCE_DEPARTMENT_NAME = "SESSION_PREFERENCE_DEPARTMENT_NAME";

    public static void addUserDetailsToPreferences(Context context,
                                                   String mobileNo, String email, String fullName,
                                                   String userId, String department,
                                                   String address, String userName) {


        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SESSION_PREFERENCE_MOBILE_NO, mobileNo);
        editor.putString(SESSION_PREFERENCE_EMAIL, email);
        editor.putString(SESSION_PREFERENCE_FULLNAME, fullName);
        editor.putString(SESSION_PREFERENCE_ADDRESS, address);
        editor.putString(SESSION_PREFERENCE_USER_ID, userId);
        editor.putString(SESSION_PREFERENCE_DEPARTMENT_NAME, department);
        editor.putString(SESSION_PREFERENCE_USERNAME, userName);
        editor.apply();
    }


    // check shared preference is filledOr not
    public static boolean checkUserisLogedin(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(SESSION_PREFERENCE_USER_ID, null) != null;
    }

    // get the user id in preference
    public static String getUserIdFromThePreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(SESSION_PREFERENCE_USER_ID, null);
    }


    // delete the user session
    public static boolean clearUserPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        return editor.commit();
    }


    ///////////////////////////////////////////////////////////////////// firebase token preference
    public static final String FIREBASE_TOKEN_PREFERENCE_NAME = "FIREBASE_TOKEN_PREFERENCE_NAME";
    public static final String FIREBASE_TOKEN_PREFERENCE_KEY = "FIREBASE_TOKEN_PREFERENCE_KEY";

    /// ADD FIREBASE TOKEN KEY
    public static boolean addFirebaseTokenToSharedPreference(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FIREBASE_TOKEN_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FIREBASE_TOKEN_PREFERENCE_KEY, token);
        return editor.commit();
    }


    public static final String CHECK_IN_OUT_PREFERENCE_NAME = "CHECK_IN_OUT_PREFERENCE_NAME";
    public static final String CHECK_IN_OUT_PREFERENCE_ATTENCE_ID_KEY = "CHECK_IN_OUT_PREFERENCE_ATTENCE_ID_KEY";
    public static final String CHECK_IN_OUT_PREFERENCE_CHECK_IN_DATE_ID_KEY = "CHECK_IN_OUT_PREFERENCE_CHECK_IN_DATE_ID_KEY";


    /////// SET HAS checked in
    public static boolean setUserHasCheckedIn(Context context, String attenceKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CHECK_IN_OUT_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CHECK_IN_OUT_PREFERENCE_ATTENCE_ID_KEY, attenceKey);
        editor.putString(CHECK_IN_OUT_PREFERENCE_CHECK_IN_DATE_ID_KEY, CommonFunc.getTodayDate());
        return editor.commit();
    }

    ////////////// set has checked out
    public static boolean setUserHasCheckedOut(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CHECK_IN_OUT_PREFERENCE_NAME, MODE_PRIVATE);
        if (sharedPreferences == null) {
            return true;
        } else if (sharedPreferences.getAll().isEmpty()) {
            return true;
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            return editor.commit();
        }
    }

    // check in home fragment. . .
    public static boolean getIsUserCheckedInManually(Context context, boolean showDialog, AppCompatActivity activity,
                                                     View displayView, String message, String buttonName) {

        if (!isCheckInOutServiceRunning(context)) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(CHECK_IN_OUT_PREFERENCE_NAME, MODE_PRIVATE);
            if (sharedPreferences != null) {
                if (sharedPreferences.getString(CHECK_IN_OUT_PREFERENCE_ATTENCE_ID_KEY, null) != null) {
                    return true;
                } else {
                    if (showDialog)
                        CommonFunc.showCheckINSnackBar(activity, context, displayView, message, buttonName);
                    return false;
                }
            } else {
                if (showDialog)
                    CommonFunc.showCheckINSnackBar(activity, context, displayView, message, buttonName);
                return false;
            }
        } else {
            if (showDialog)
                CommonFunc.commonDialog(context, context.getString(R.string.alert),
                        "Checking in/out in process...", false, activity, displayView);
            return false;
        }
    }


    ///// get attence id

    public static String getCheckedInUserAttenceId(Context context) {
        return context.getSharedPreferences(CHECK_IN_OUT_PREFERENCE_NAME, MODE_PRIVATE)
                .getString(CHECK_IN_OUT_PREFERENCE_ATTENCE_ID_KEY, null);
    }

    ///////////////////////////////////////////////////////// today check in time perference. . .
    public static final String TODAY_CHECK_IN_TIME_STAMP_PREFERENCE_NAME = "TODAY_TIME_STAMP_PREFERENCE_NAME";
    public static final String TODAY_TODAY_CHECK_IN_TIME_STAMP_LONG_VALUE_KEY = "TODAY_TODAY_CHECK_IN_TIME_STAMP_LONG_VALUE_KEY";
    public static final String TODAY_TODAY_CHECK_IN_LOCATION_VALUE_KEY = "TODAY_TODAY_CHECK_IN_LOCATION_VALUE_KEY";

    public static boolean clearToDayCheckInTimePreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TODAY_CHECK_IN_TIME_STAMP_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        return editor.commit();
    }

    ///
    public static boolean addCheckInTimeToSharedPreference(Context context, long checkInTimeStamp) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TODAY_CHECK_IN_TIME_STAMP_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(TODAY_TODAY_CHECK_IN_TIME_STAMP_LONG_VALUE_KEY, checkInTimeStamp);
        return editor.commit();
    }

    //
    public static long getTodayCheckInTimeStamp(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TODAY_CHECK_IN_TIME_STAMP_PREFERENCE_NAME, MODE_PRIVATE);
        if (sharedPreferences == null) {
            return 0;
        } else {
            return sharedPreferences.getLong(TODAY_TODAY_CHECK_IN_TIME_STAMP_LONG_VALUE_KEY, 0);
        }
    }

    public static void setCheckInLocationToSharedPreference(Context context, String checkInLocation) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TODAY_CHECK_IN_TIME_STAMP_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TODAY_TODAY_CHECK_IN_LOCATION_VALUE_KEY, checkInLocation);
        editor.apply();
    }

    public static String getCheckInLocationToSharedPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TODAY_CHECK_IN_TIME_STAMP_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TODAY_TODAY_CHECK_IN_LOCATION_VALUE_KEY, "NA");
    }


    //////////////////////////////////////////////////////  get latest submited location
    public static final String PREFERENCE_TO_STORE_LAST_SUBMITED_LOC_PREFERENCE_NAME = "PREFERENCE_TO_STORE_LAST_SUBMITED_LOC_PREFERENCE_NAME";
    // keys
    public static final String PREFERENCE_TO_STORE_LAST_SUBMITED_LOC_PREFERENCE_LAT_KEY = "PREFERENCE_TO_STORE_LAST_SUBMITED_LOC_PREFERENCE_LAT_KEY";
    public static final String PREFERENCE_TO_STORE_LAST_SUBMITED_LOC_PREFERENCE_LNG_KEY = "PREFERENCE_TO_STORE_LAST_SUBMITED_LOC_PREFERENCE_LNG_KEY";
    public static final String PREFERENCE_TO_STORE_LAST_SUBMITED_LOC_PREFERENCE_LOCATION_KEY = "PREFERENCE_TO_STORE_LAST_SUBMITED_LOC_PREFERENCE_LOCATION_KEY";


    public static boolean addLastSubmitedAddress(String lat, String lng
            , String address, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_TO_STORE_LAST_SUBMITED_LOC_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREFERENCE_TO_STORE_LAST_SUBMITED_LOC_PREFERENCE_LAT_KEY, lat);
        editor.putString(PREFERENCE_TO_STORE_LAST_SUBMITED_LOC_PREFERENCE_LNG_KEY, lng);
        editor.putString(PREFERENCE_TO_STORE_LAST_SUBMITED_LOC_PREFERENCE_LOCATION_KEY, address);
        return editor.commit();
    }

    public static boolean clearLastSubmitedAddress(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_TO_STORE_LAST_SUBMITED_LOC_PREFERENCE_NAME, MODE_PRIVATE);

        if (sharedPreferences == null) {
            return true;
        } else if (sharedPreferences.getAll().isEmpty()) {
            return true;
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            return editor.commit();
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////// is checkInOutServiceRunning
    private static final String CHECK_IN_OUT_SERVICE_RUNNING_PREFERENCE_NAME = "CHECK_IN_OUT_SERVICE_RUNNING_PREFERENCE_NAME";
    private static final String CHECK_IN_OUT_SERVICE_RUNNING_PREFERENCE_IS_RUNNING_KEY = "CHECK_IN_OUT_SERVICE_RUNNING_PREFERENCE_IS_RUNNING_KEY";
    private static final String CHECK_IN_OUT_SERVICE_RUNNING_PREFERENCE_WHAT_RUNNING_KEY =
            "CHECK_IN_OUT_SERVICE_RUNNING_PREFERENCE_WHAT_RUNNING_KEY"; // cin out cout


    public static boolean setCheckInOutServiceRunning(Context context, boolean isServiceRunning, String whatRunning) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CHECK_IN_OUT_SERVICE_RUNNING_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CHECK_IN_OUT_SERVICE_RUNNING_PREFERENCE_IS_RUNNING_KEY, isServiceRunning);
        editor.putString(CHECK_IN_OUT_SERVICE_RUNNING_PREFERENCE_WHAT_RUNNING_KEY, whatRunning);
        return editor.commit();
    }

    public static boolean isCheckInOutServiceRunning(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CHECK_IN_OUT_SERVICE_RUNNING_PREFERENCE_NAME, MODE_PRIVATE);
        if (sharedPreferences == null) {
            return false;
        } else {
            return sharedPreferences.getBoolean(CHECK_IN_OUT_SERVICE_RUNNING_PREFERENCE_IS_RUNNING_KEY, false);
        }
    }




}
