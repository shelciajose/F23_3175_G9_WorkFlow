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
}