package com.example.workflow.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workflow.R;

public class PreferenceUtils {

    public static final String PASSCODE_PREFF = "PASSCODE_PREFF";
    public static final String PASSCODE_PASSED_KEY = "PASSCODE_PASSED_KEY";
    ///

    public static final String FIREABSE_BIOMETRIC_NOTIFICATION = "FIREABSE_BIOMETRIC_NOTIFICATION";
    public static final String FIREABSE_BIOMETRIC_NOTIFICATION_VALUE = "FIREABSE_BIOMETRIC_NOTIFICATION_VALUE";

    public static void addPasscodeIsPassed(Context context, String code) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PASSCODE_PREFF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PASSCODE_PASSED_KEY, code);
        editor.apply();
    }

    public static boolean clearPassCode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PASSCODE_PREFF, Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            return editor.commit();
        } else
            return true;
    }

    public static String getPasscodeValue(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PASSCODE_PREFF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PASSCODE_PASSED_KEY, "");
    }


    /////////////////////////////////// OTP preferences
    public static final String RANDOM_NO_PREFERENCE_NAME = "RANDOM_NO_PREFERENCE_NAME";
    public static final String RANDOM_NO_PREFERENCE_VALUE = "RANDOM_NO_PREFERENCE_VALUE";

    /////////////////////////////
    public static void addOTPvalueToSharedPreference(Context context, String otpValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(RANDOM_NO_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(RANDOM_NO_PREFERENCE_VALUE, otpValue);
        editor.apply();
    }

    public static String getOTPvalueFromPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(RANDOM_NO_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(RANDOM_NO_PREFERENCE_VALUE, null);
    }



    ////////////////////////////// session preference
    public static final String SESSION_PREFERENCE_NAME = "SESSION_PREFERENCE_NAME";
    //
    public static final String SESSION_PREFERENCE_MOBILE_NO = "SESSION_PREFERENCE_MOBILE_NO";
    public static final String SESSION_PREFERENCE_EMAIL = "SESSION_PREFERENCE_EMAIL";
    public static final String SESSION_PREFERENCE_FULLNAME = "SESSION_PREFERENCE_FULLNAME";
    public static final String SESSION_PREFERENCE_PROFILE_PIC = "SESSION_PREFERENCE_PROFILE_PIC";
    public static final String SESSION_PREFERENCE_USER_ID = "SESSION_PREFERENCE_USER_ID";
    public static final String SESSION_PREFERENCE_DAYS_LEFT = "SESSION_PREFERENCE_DAYS_LEFT";
    public static final String SESSION_PREFERENCE_ROLE_ID = "SESSION_PREFERENCE_ROLE_ID";
    public static final String SESSION_PREFERENCE_ROLE_NAME = "SESSION_PREFERENCE_ROLE_NAME";
    public static final String SESSION_PREFERENCE_REG_ID = "SESSION_PREFERENCE_REG_ID";
    public static final String SESSION_PREFERENCE_SUBSCRIPTION_ID = "SESSION_PREFERENCE_SUBSCRIPTION_ID";
    public static final String SESSION_PREFERENCE_SIM_DATA = "SESSION_PREFERENCE_SIM_DATA";
    public static final String SESSION_PREFERENCE_APP_SESSION = "SESSION_PREFERENCE_APP_SESSION";
    //
    public static final String SESSION_PREFERENCE_DEPARTMENT_ID = "SESSION_PREFERENCE_DEPARTMENT_ID";
    public static final String SESSION_PREFERENCE_DEPARTMENT_NAME = "SESSION_PREFERENCE_DEPARTMENT_NAME";
    //
    public static final String SESSION_PREFERENCE_DESIGNATION_ID = "SESSION_PREFERENCE_DESIGNATION_ID";
    public static final String SESSION_PREFERENCE_DESIGNATION_NAME = "SESSION_PREFERENCE_DESIGNATION_NAME";

    public static final String SESSION_PREFERENCE_UPDATED_NEW_VERSION = "SESSION_PREFERENCE_UPDATED_NEW_VERSION";

    //
    public static void addUserPreferences(Context context,
                                          String mobileNo, String email, String fullName,
                                          String profilePic, String userId, String DaysLeft,
                                          String roleId, String roleName, String departmentId, String departmentName,
                                          String designationId, String designationName, String regId,
                                          String subscriptionId, String simInfoJson, String appSession) {


        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SESSION_PREFERENCE_MOBILE_NO, mobileNo);
        editor.putString(SESSION_PREFERENCE_EMAIL, email);
        editor.putString(SESSION_PREFERENCE_FULLNAME, fullName);
        editor.putString(SESSION_PREFERENCE_PROFILE_PIC, profilePic);
        editor.putString(SESSION_PREFERENCE_USER_ID, userId);
        editor.putString(SESSION_PREFERENCE_DAYS_LEFT, DaysLeft);
        editor.putString(SESSION_PREFERENCE_ROLE_ID, roleId);
        editor.putString(SESSION_PREFERENCE_ROLE_NAME, roleName);
        editor.putString(SESSION_PREFERENCE_DEPARTMENT_ID, departmentId);
        editor.putString(SESSION_PREFERENCE_DEPARTMENT_NAME, departmentName);
        editor.putString(SESSION_PREFERENCE_DESIGNATION_ID, designationId);
        editor.putString(SESSION_PREFERENCE_DESIGNATION_NAME, designationName);
        editor.putString(SESSION_PREFERENCE_REG_ID, regId);
        editor.putString(SESSION_PREFERENCE_SUBSCRIPTION_ID, subscriptionId);
        editor.putString(SESSION_PREFERENCE_SIM_DATA, simInfoJson);
        editor.putString(SESSION_PREFERENCE_APP_SESSION, appSession);
        editor.apply();
    }

    // get the profile image
    public static String getUserImagePreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(SESSION_PREFERENCE_PROFILE_PIC, "no_img.png");
    }

    /// ADD decline version
    public static boolean addDeclineVersionToSharedPreference(Context context, String newDeclineVersion) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SESSION_PREFERENCE_UPDATED_NEW_VERSION, newDeclineVersion);
        return editor.commit();
    }

    // get the feeded version name
    public static String getDeclineVersionName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(SESSION_PREFERENCE_UPDATED_NEW_VERSION, "");
    }

    // get the designation
    public static String getUserDesignationPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(SESSION_PREFERENCE_DESIGNATION_NAME, "NA");
    }

    // get the mobileNumber
    public static String getUserEmailPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(SESSION_PREFERENCE_EMAIL, "NA");
    }

    // get the mobileNumber
    public static String getUserMobilePreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(SESSION_PREFERENCE_MOBILE_NO, "NA");
    }

    // get the app session
    public static String getTheAppSessionPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(SESSION_PREFERENCE_APP_SESSION, "");
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

    // get the user name in preference
    public static String getUserNameFromThePreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(SESSION_PREFERENCE_FULLNAME, null);
    }

    // get the role id in preference
    public static String getRoleFromThePreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(SESSION_PREFERENCE_ROLE_ID, null);
    }

    // get subscription id
    public static String getsubscriptionIdFromThePreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(SESSION_PREFERENCE_SUBSCRIPTION_ID, null);
    }

    // get sim data
    public static String getSimDataFromPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(SESSION_PREFERENCE_SIM_DATA, null);
    }

    // get days left
    public static String getDaysLeftFromThePreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(SESSION_PREFERENCE_DAYS_LEFT, null);
    }

    public static boolean addLeftDaysToSharedPreference(Context context, String leftDays) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SESSION_PREFERENCE_DAYS_LEFT, leftDays);
        return editor.commit();
    }

    public static boolean setFromFirebaseBiometricNotification(Context context, Boolean bool) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FIREABSE_BIOMETRIC_NOTIFICATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FIREABSE_BIOMETRIC_NOTIFICATION_VALUE, bool);
        return editor.commit();
    }

    public static Boolean getFromFirebaseBiometricNotification(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FIREABSE_BIOMETRIC_NOTIFICATION,
                Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(FIREABSE_BIOMETRIC_NOTIFICATION_VALUE, false);
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

    //// get firebase token form the sharedprefernenc
    public static String getFirebaseTokenFromTheSharedPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FIREBASE_TOKEN_PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences == null) {
            return "";
        } else {
            return sharedPreferences.getString(FIREBASE_TOKEN_PREFERENCE_KEY, "");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////  signal status preference
    public static final String SIGNAL_STATUS_PREFERENCE_NAME = "SIGNAL_STATUS_PREFERENCE_NAME";
    public static final String SIGNAL_STATUS_VALUE_KEY = "SIGNAL_STATUS_VALUE_KEY";

    public static void addlatestSignalStatusToPref(Context context, String signalStrength) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SIGNAL_STATUS_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SIGNAL_STATUS_VALUE_KEY, signalStrength);
        editor.apply();
    }

    public static String getlatestSignalStatus(Context context) {
        if (context.getSharedPreferences(SIGNAL_STATUS_PREFERENCE_NAME, Context.MODE_PRIVATE) != null) {
            return context.getSharedPreferences(SIGNAL_STATUS_PREFERENCE_NAME, Context.MODE_PRIVATE)
                    .getString(SIGNAL_STATUS_VALUE_KEY, "NA");
        } else {
            addlatestSignalStatusToPref(context, "NA");
        }
        return "NA";
    }


    ///////////////////////////// checked in - or checked out. . . .

    public static final String CHECK_IN_OUT_PREFERENCE_NAME = "CHECK_IN_OUT_PREFERENCE_NAME";
    public static final String CHECK_IN_OUT_PREFERENCE_ATTENCE_ID_KEY = "CHECK_IN_OUT_PREFERENCE_ATTENCE_ID_KEY";
    public static final String CHECK_IN_OUT_PREFERENCE_CHECK_IN_DATE_ID_KEY = "CHECK_IN_OUT_PREFERENCE_CHECK_IN_DATE_ID_KEY";

    public static boolean clearCheckInPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CHECK_IN_OUT_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        return editor.commit();
    }

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


    public static boolean isCheckInDataMatchTodayDate(Context context, SharedPreferences sharedPreferences,
                                                      boolean showDialog, AppCompatActivity activity,
                                                      View displayView, String message, String buttonName) {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences(CHECK_IN_OUT_PREFERENCE_NAME, MODE_PRIVATE);

        if (sharedPreferences != null)
            if (sharedPreferences.getString(CHECK_IN_OUT_PREFERENCE_CHECK_IN_DATE_ID_KEY, null) != null) {
                if (sharedPreferences.getString(CHECK_IN_OUT_PREFERENCE_CHECK_IN_DATE_ID_KEY, null).equals(CommonFunc.getTodayDate())) {
                    return true;
                } else {
                    if (showDialog)
                        CommonFunc.showCheckINSnackBar(activity, context, displayView, message, buttonName);
                    return false;
                }
            } else {
                return true;
            }
        else
            return true;
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

    public static String isWhatRunningInCheckInAndOut(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CHECK_IN_OUT_SERVICE_RUNNING_PREFERENCE_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(CHECK_IN_OUT_SERVICE_RUNNING_PREFERENCE_WHAT_RUNNING_KEY, null);
    }

    public static boolean clearTheCheckInOutServiceRunningSharedPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CHECK_IN_OUT_SERVICE_RUNNING_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        return editor.commit();

    }

    ////// location off opstarction preference
    public static final String LOC_PERMISSION_OFF_STATUS_PREFERENCE = "LOC_PERMISSION_OFF_STATUS_PREFERENCE";
    public static final String LOC_PERMISSION_OFF_STATUS_KEY = "LOC_PERMISSION_OFF_STATUS_KEY";

    //
    public static void storeInitialLocPermissionNotChangedOnStartingService(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOC_PERMISSION_OFF_STATUS_PREFERENCE
                , MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOC_PERMISSION_OFF_STATUS_KEY, true);
        editor.apply();
    }

    //
    public static void storeLocPermissionChangedToSharedPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOC_PERMISSION_OFF_STATUS_PREFERENCE
                , MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOC_PERMISSION_OFF_STATUS_KEY, false);
        editor.apply();
    }


    public static boolean getLocPermissonGotChangedOrNot(Context context) {
        return context.getSharedPreferences(LOC_PERMISSION_OFF_STATUS_PREFERENCE, MODE_PRIVATE)
                .getBoolean(LOC_PERMISSION_OFF_STATUS_KEY, false);
    }

    public static boolean deleteAllSharedPreferences(Context context) {
        return
                setUserHasCheckedOut(context)
                        &&
                        clearUserPreference(context)
                        &&
                        clearCheckInPreference(context)
                        &&
                        clearToDayCheckInTimePreference(context)
                        &&
                        clearPassCode(context)
                        &&
                        clearTheCheckInOutServiceRunningSharedPreference(context);

    }


}
