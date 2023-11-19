package com.example.workflow.activities;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.example.workflow.R;

public class SplashScreen extends AppCompatActivity {

    private Dialog dialog = null;
    private String versonName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        try {
            showProgressDialog();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismissProgressDialog();
                    startActivity(new Intent(SplashScreen.this, RegistrationActivity.class));
                }
            }, 3000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showProgressDialog()
    {
        try {
            if (dialog == null) {
                dialog = new Dialog(SplashScreen.this);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.loading_layout);
            }
            if (!dialog.isShowing())
                dialog.show();
            }
        catch (Exception e) {
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
