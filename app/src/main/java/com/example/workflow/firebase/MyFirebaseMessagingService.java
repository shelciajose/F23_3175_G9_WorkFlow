package com.example.workflow.firebase;

import android.annotation.SuppressLint;

import com.example.workflow.notification.CheckInOutNotifi;
import com.example.workflow.utils.PreferenceUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public MyFirebaseMessagingService() {
    }

    @Override
    public void onNewToken(String s) {
        PreferenceUtils.addFirebaseTokenToSharedPreference(this, s);
    }


    @SuppressLint("WrongConstant")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        System.out.println("ffffffffffffff-------onMessageReceived------" + new Gson().toJson(remoteMessage.getData()));

    }
    ///////////////////////////////////////////////////////////////////////   CHECK_IN_ALERT

    private void dropAnAlertNotificationForCheckIn(RemoteMessage remoteMessage) {
        CheckInOutNotifi.show(remoteMessage.getData().get("message"), true, this);
    }
    //////////////////////////////////////////////////////////////////////    CHECK_OUT_ALERT

    private void dropAnAlertNotificationForCheckOut(RemoteMessage remoteMessage) {
        CheckInOutNotifi.show(remoteMessage.getData().get("message"), false, this);
    }

}



