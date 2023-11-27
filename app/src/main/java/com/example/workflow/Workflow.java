package com.example.workflow;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;

public class Workflow extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("FirebaseInitialization", "FirebaseApp.initializeApp called");
        FirebaseApp.initializeApp(this);
    }
}
