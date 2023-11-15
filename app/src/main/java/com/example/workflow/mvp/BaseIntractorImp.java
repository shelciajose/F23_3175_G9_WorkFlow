package com.example.workflow.mvp;

import android.content.Context;

import com.example.workflow.R;
import com.example.workflow.database.OfflineDatabase;
import com.example.workflow.notification.CheckInOutNotifi;
import com.example.workflow.utils.CommonFunc;
import com.example.workflow.utils.PreferenceUtils;

public class BaseIntractorImp implements BaseIntractor {

    //////////////////

    ////// check in. . . . . . .
    @Override
    public void reqToPerformCheckInFromServer(CheckInLisner checkInLisner, Context context, String loc,
                                              String lat, String lng,
                                              int SENDING_TYPE) {

        OfflineDatabase offlineDatabase = new OfflineDatabase(context);
        String refNo = CommonFunc.getCurrentSystemTimeStamp() + "";
        int checkValue = offlineDatabase.addCheckInDetails(refNo, lat, lng, loc, refNo);
        if (checkValue == -1) {
            checkInLisner.OnCheckInFailLisner("Unable to check-in..Please Try again",
                    context.getString(R.string.alert), false);
        } else {
            if (PreferenceUtils.setUserHasCheckedIn(context, refNo)) {
                if (PreferenceUtils.addCheckInTimeToSharedPreference(context, CommonFunc.getCurrentSystemTimeStamp())) {
                    if (PreferenceUtils.addLastSubmitedAddress(lat, lng, loc, context)) {
                        PreferenceUtils.setCheckInLocationToSharedPreference(context, loc);
                        checkInLisner.OnCheckInSuccesLisner(loc, refNo, "You have successfully checked-In");
                        CheckInOutNotifi.closeCheckInAndOutWarnningNotification(context, true);
                    }
                }
            }
        }
    }

    ////// chech out. . . . . .
    @Override
    public void reqToPerformCheckOutFromServer(CheckOutLisner checkOutLisner,
                                               Context context, String loc, String lat, String lng,
                                               int SENDING_TYPE) {

        OfflineDatabase offlineDatabase = new OfflineDatabase(context);
        String refNo = CommonFunc.getCurrentSystemTimeStamp() + "";
        int checkValue = offlineDatabase.addCheckOutDetails(PreferenceUtils.getCheckedInUserAttenceId(context)
                , refNo, lat, lng, loc);

        if (checkValue == -1) {
            checkOutLisner.OnCheckOutFailLisner("Unable to check-out..Please Try again",
                    context.getString(R.string.internalError), false);
        } else {
            if (PreferenceUtils.setUserHasCheckedOut(context)) {
                if (PreferenceUtils.clearLastSubmitedAddress(context)) {
                    PreferenceUtils.addCheckInTimeToSharedPreference(context, 0);
                    PreferenceUtils.setCheckInLocationToSharedPreference(context, "NA");
                    checkOutLisner.OnCheckOutSuccessLisner(loc, "You have successfully checked-Out");
                    CheckInOutNotifi.closeCheckInAndOutWarnningNotification(context, false);
                }
            }
        }

    }

    @Override
    public void OnMvpStop() {

    }
}