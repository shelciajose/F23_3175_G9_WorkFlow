package com.example.workflow.mvp;

public interface BaseView {
    void showProgressDialog();

    void dismissProgressDialog();

    ////////////////////////////////////////////    check in and check out
    void afterCheckInSuccess(String location,String attenceId, String messsage);

    void afterCheckInFail(String message, String dialogTitle, boolean isInternetError);

    void afterCheckOutSuccess(String location,String message);

    void afterCheckOutFail(String message, String dialogTitle, boolean isInternetError);
}
