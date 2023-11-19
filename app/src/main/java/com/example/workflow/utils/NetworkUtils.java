package com.example.workflow.utils;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Check device's network connectivity and speed
 * @author emil http://stackoverflow.com/users/220710/emil
 *
 */
public class NetworkUtils {

    /**
     * Get the network info
     *
     * @param context
     * @return
     */
    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        NetworkInfo info = NetworkUtils.getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    /**
     * Check if there is any connectivity to a Wifi network
     *
     * @param context
     * @return
     */


    /**
     * Check if there is any connectivity to a mobile network
     *
     * @param context
     * @return
     */

    /**
     * Check if there is fast connectivity
     *
     * @param context
     * @return
     */


    /**
     * Check if the connection is fast
     *
     * @param type
     * @param subType
     * @return
     */



    public static String getConnectionType(Context context) {

        NetworkInfo info = NetworkUtils.getNetworkInfo(context);
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return "W";
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                return "M";
            } else {
                return "NA";
            }
        } else {
            return "NA";
        }

    }


    public static boolean checkInternetAndOpenDialog(Context context, AppCompatActivity appCompatActivity, View view) {
        if (isConnected(context)) {
            return true;
        } else {
            CommonFunc.commonDialog(context, "No internet", "Please check network settings", true,
                    appCompatActivity, view);
            return false;
        }
    }

}