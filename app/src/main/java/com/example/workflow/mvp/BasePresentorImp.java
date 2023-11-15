package com.example.workflow.mvp;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class BasePresentorImp implements BasePresentor, BaseIntractor.CheckInLisner, BaseIntractor.CheckOutLisner {


    private BaseView baseView = null;
    private BaseIntractor baseIntractor = null;


    public BasePresentorImp(BaseView baseView) {
        this.baseView = baseView;
        this.baseIntractor = new BaseIntractorImp();
    }

    /////////////////////////////////////////////////////////////////////  check in
    @Override
    public void performCheckIn(Context context, String loc, String lat, String lng,
                               AppCompatActivity appCompatActivity, View viewTop, int SENDING_TYPE) {
        /*if (NetworkUtils.checkInternetAndOpenDialog(context, appCompatActivity, viewTop)) {
            baseView.showProgressDialog();
            baseIntractor.reqToPerformCheckInFromServer(this, context, loc, lat, lng,
                    batteryPerc, isMock, networkType, signalStrength, SENDING_TYPE);
        }*/

        baseView.showProgressDialog();
        baseIntractor.reqToPerformCheckInFromServer(this, context, loc, lat, lng, SENDING_TYPE);
    }

    @Override
    public void OnCheckInSuccesLisner(String location, String atendenceKey, String message) {
        baseView.dismissProgressDialog();
        baseView.afterCheckInSuccess(location, atendenceKey, message);
    }

    @Override
    public void OnCheckInFailLisner(String message, String dialogTitle, boolean isInternetError) {
        baseView.dismissProgressDialog();
        baseView.afterCheckInFail(message, dialogTitle, isInternetError);

    }

//////////////////////////////////////////////////////////////////////////// check out lisner. . . . .

    @Override
    public void performCheckOut(Context context, String loc, String lat, String lng,
                                AppCompatActivity appCompatActivity, View viewTop, int SENDING_TYPE) {
        /*if (NetworkUtils.checkInternetAndOpenDialog(context, appCompatActivity, viewTop)) {
            baseView.showProgressDialog();
            baseIntractor.reqToPerformCheckOutFromServer(this, context, loc, lat, lng,
                    batteryPerc, isMock, networkType, signalStrength, SENDING_TYPE);
        }*/
        baseView.showProgressDialog();
        baseIntractor.reqToPerformCheckOutFromServer(this, context, loc, lat, lng, SENDING_TYPE);
    }

    @Override
    public void OnCheckOutSuccessLisner(String location, String message) {
        baseView.dismissProgressDialog();
        baseView.afterCheckOutSuccess(location, message);
    }

    @Override
    public void OnCheckOutFailLisner(String message, String dialogTitle, boolean isInternetError) {
        baseView.dismissProgressDialog();
        baseView.afterCheckOutFail(message, dialogTitle, isInternetError);
    }

    @Override
    public void onStopMvpPresentor() {
        baseIntractor.OnMvpStop();
    }

}
