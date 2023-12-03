package com.example.workflow.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AttendanceModel implements Serializable {
    @SerializedName("AttendanceDate")
    @Expose
    private String attendanceDate;
    @SerializedName("CheckInTime")
    @Expose
    private String checkintime;
    @SerializedName("CheckInLocation")
    @Expose
    private String checkinlocation;
    @SerializedName("CheckOutTime")
    @Expose
    private String checkouttime;
    @SerializedName("CheckOutLocation")
    @Expose
    private String checkoutlocation;

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getCheckintime() {
        return checkintime;
    }

    public void setCheckintime(String checkintime) {
        this.checkintime = checkintime;
    }

    public String getCheckinlocation() {
        return checkinlocation;
    }

    public void setCheckinlocation(String checkinLocation) {
        checkinlocation = checkinLocation;
    }

    public String getCheckouttime() {
        return checkouttime;
    }

    public void setCheckouttime(String checkoutTime) {
        checkouttime = checkoutTime;
    }

    public String getCheckoutlocation() {
        return checkoutlocation;
    }

    public void setCheckoutlocation(String checkoutLocation) { this.checkoutlocation = checkoutLocation; }


}