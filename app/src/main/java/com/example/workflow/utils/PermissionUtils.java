package com.example.workflow.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.Window;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.workflow.R;

public class PermissionUtils {
    ////////////////////////////////////////////////////////// loc permission check
    public static boolean checkLoactionPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    //////////////////////////////////////////////////////// splash screen. . . .
    public static boolean checkLocAndPhonePermission(Context context) {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) + ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED;
    }

    ////////////////////////////////////////////////////////////// sms permission check
    public static boolean Smspermissioncheck(Context context) {
        //Call whatever you want
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat
                    .checkSelfPermission(context,
                            Manifest.permission.READ_SMS) +
                    ContextCompat
                            .checkSelfPermission(context,
                                    Manifest.permission.RECEIVE_SMS)
                    ==
                    PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    public static boolean checkCallPermission(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    public static boolean checkStoragePermission(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }

    }


    public static boolean checkCameraPermissionAndStoragePermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CAMERA) + ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) +
                    ContextCompat.checkSelfPermission(context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }

    }


    ///////////////////////////////////////////////////////////////// check call
    /////////////////////////////////////////////////////////////////// open loc dislog
    public static void openPermissionDialog(final Context context, String subjuct) {
        final Dialog dialog = new Dialog(context, R.style.comm_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.common_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        AppCompatTextView textViewTitle = dialog.findViewById(R.id.commom_dialog_title_id);
        AppCompatTextView textViewSubjuct = dialog.findViewById(R.id.commom_dialog_subjuct_id);
        AppCompatButton buttonSubmit = dialog.findViewById(R.id.commom_dialog_button_id);
        buttonSubmit.setText("Grant");

        textViewTitle.setText("Permission Needed!");
        textViewSubjuct.setText(subjuct);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                context.startActivity(intent);
            }
        });
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /////// app data. . . .
    public static boolean hasAppDetailsPermission(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AppOpsManager appOps = (AppOpsManager)
                    context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        android.os.Process.myUid(), context.getPackageName());
            }
            return mode == AppOpsManager.MODE_ALLOWED;
        } else {
            return true;
        }


    }

    ////////////////////////////////////////////////////////////// sms permission check
    public static boolean storagePermissioncheck(Context context) {
        //Call whatever you want
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat
                    .checkSelfPermission(context,
                            Manifest.permission.READ_EXTERNAL_STORAGE) +
                    ContextCompat
                            .checkSelfPermission(context,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    ==
                    PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    public static void requestStoragePermissions(Activity activity, int REQUEST_CODE_STORAGE_PERMISSION) {
        ActivityCompat.requestPermissions(activity,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                REQUEST_CODE_STORAGE_PERMISSION);
    }
}