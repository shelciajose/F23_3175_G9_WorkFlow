package com.example.workflow.mvp;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public interface BasePresentor {
    void performCheckIn(Context context, String location, String lat, String lng,
                        AppCompatActivity appCompatActivity, View viewTop, int SENDING_TYPE);

    void performCheckOut(Context context, String location, String lat, String lng,
                         AppCompatActivity appCompatActivity, View viewTop, int SENDING_TYPE);

    void onStopMvpPresentor();
}
