package com.example.workflow.mvp;

import android.content.Context;

public interface BaseIntractor {

    ////////////// check in and check out. . .
    interface CheckInLisner {
        void OnCheckInSuccesLisner(String location, String attendenceKey, String messsage);

        void OnCheckInFailLisner(String message, String dialogTitle, boolean isInternetError);
    }

    void reqToPerformCheckInFromServer(CheckInLisner checkInLisner, Context context, String loc, String lat, String lng,
                                       int SENDING_TYPE);

    // check out
    interface CheckOutLisner {
        void OnCheckOutSuccessLisner(String location, String message);

        void OnCheckOutFailLisner(String message, String dialogTitle, boolean isInternetError);
    }

    void reqToPerformCheckOutFromServer(CheckOutLisner checkOutLisner, Context context, String loc, String lat, String lng,
                                        int SENDING_TYPE);

    //on stop
    void OnMvpStop();
}
