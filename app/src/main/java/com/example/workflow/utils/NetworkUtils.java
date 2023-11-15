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
     * @param context
     * @return
     */
    public static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     * @param context
     * @return
     */
    public static boolean isConnected(Context context){
        NetworkInfo info = NetworkUtils.getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    /**
     * Check if there is any connectivity to a Wifi network
     * @param context
     * @return
     */
    public static boolean isConnectedWifi(Context context){
        NetworkInfo info = NetworkUtils.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Check if there is any connectivity to a mobile network
     * @param context
     * @return
     */
    public static boolean isConnectedMobile(Context context){
        NetworkInfo info = NetworkUtils.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Check if there is fast connectivity
     * @param context
     * @return
     */
    public static boolean isConnectedFast(Context context){
        NetworkInfo info = NetworkUtils.getNetworkInfo(context);
        return (info != null && info.isConnected() && NetworkUtils.isConnectionFast(info.getType(),info.getSubtype()));
    }

    /**
     * Check if the connection is fast
     * @param type
     * @param subType
     * @return
     */
    public static boolean isConnectionFast(int type, int subType){
        if(type== ConnectivityManager.TYPE_WIFI){
            return true;
        }else if(type== ConnectivityManager.TYPE_MOBILE){
            switch(subType){
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return true; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return true; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return true; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                /*
                 * Above API level 7, make sure to set android:targetSdkVersion
                 * to appropriate level to use these
                 */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        }else{
            return false;
        }
    }





    public static String getConnectionType(Context context) {

        NetworkInfo info = NetworkUtils.getNetworkInfo(context);
        if (info!=null)
        {
            if(info.getType()== ConnectivityManager.TYPE_WIFI){
                return "W";
            }else if(info.getType()== ConnectivityManager.TYPE_MOBILE){
                return "M";
            }else{
                return "NA";
            }
        }else {
            return "NA";
        }

    }


    public static boolean checkInternetAndOpenDialog(Context context, AppCompatActivity appCompatActivity, View view)
    {
        if (isConnected(context))
        {
            return true;
        }else {
            CommonFunc.commonDialog(context,"No internet","Please check network settings",true,
                    appCompatActivity,view);
            return false;
        }
    }

    public static void openGpsSettings(final Context context)
    {
        new AlertDialog.Builder(context)
                .setTitle("Alert!!")
                .setMessage("Please grant gps access")
                .setCancelable(false)
                .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }

                })
                .create().show();
    }
    public static boolean isGpsActivatedOpenDialogIfNot(Context context)
    {
        LocationManager manager = (LocationManager)context. getSystemService(Context.LOCATION_SERVICE );
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            return true;
        }else {
            openGpsSettings(context);
        }
        return false;
    }

    public static boolean isGpsOn(Context context)
    {
        LocationManager manager = (LocationManager)context. getSystemService(Context.LOCATION_SERVICE );
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    ///////////////////

    public static String getNetworkConnectionType(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo= null;
        if (cm != null) {
            networkInfo = cm.getActiveNetworkInfo();
        }else {
            return "NA";
        }
        if (networkInfo == null) {
            return "NA";
        }

        if(networkInfo.getType()== ConnectivityManager.TYPE_WIFI){
            return "Wi-Fi";
        }else if(networkInfo.getType()== ConnectivityManager.TYPE_MOBILE){
            switch(networkInfo.getSubtype()){
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return "4G";
                default:
                    return "NA";
            }
        }else{
            return "NA";
        }
    }
}
