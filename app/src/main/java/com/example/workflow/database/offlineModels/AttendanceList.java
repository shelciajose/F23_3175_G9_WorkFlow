package com.example.workflow.database.offlineModels;

import com.google.gson.annotations.SerializedName;

public class AttendanceList {

    @SerializedName("CheckInLatitude")
    private String checkInLatitude;
    @SerializedName("CheckInLocation")
    private String checkInLocation;
    @SerializedName("CheckInLongitude")
    private String checkInLongitude;
    @SerializedName("CheckInTimeStamp")
    private String checkInTime;
    @SerializedName("CheckOutLatitude")
    private String checkOutLatitude;
    @SerializedName("CheckOutLocation")
    private String checkOutLocation;
    @SerializedName("CheckOutLongitude")
    private String checkOutLongitude;
    @SerializedName("CheckOutTimeStamp")
    private String checkOutTime;
    @SerializedName("UserId")
    private String empID;
    @SerializedName("RefNo")
    private String refNo;

    private String checkInRef;
    private String isMock;
    private String batteryPresentage;
    private String networkType;
    private String signalStrength;

    public String getIsMock() {
        return isMock;
    }

    public void setIsMock(String isMock) {
        this.isMock = isMock;
    }

    public String getBatteryPresentage() {
        return batteryPresentage;
    }

    public void setBatteryPresentage(String batteryPresentage) {
        this.batteryPresentage = batteryPresentage;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(String signalStrength) {
        this.signalStrength = signalStrength;
    }

    public String getCheckInLatitude() {
        return checkInLatitude;
    }

    public void setCheckInLatitude(String checkInLatitude) {
        this.checkInLatitude = checkInLatitude;
    }

    public String getCheckInLocation() {
        return checkInLocation;
    }

    public void setCheckInLocation(String checkInLocation) {
        this.checkInLocation = checkInLocation;
    }

    public String getCheckInLongitude() {
        return checkInLongitude;
    }

    public void setCheckInLongitude(String checkInLongitude) {
        this.checkInLongitude = checkInLongitude;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutLatitude() {
        return checkOutLatitude;
    }

    public void setCheckOutLatitude(String checkOutLatitude) {
        this.checkOutLatitude = checkOutLatitude;
    }


    public String getCheckOutLocation() {
        return checkOutLocation;
    }

    public void setCheckOutLocation(String checkOutLocation) {
        this.checkOutLocation = checkOutLocation;
    }

    public String getCheckOutLongitude() {
        return checkOutLongitude;
    }

    public void setCheckOutLongitude(String checkOutLongitude) {
        this.checkOutLongitude = checkOutLongitude;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getCheckInRef() {
        return checkInRef;
    }

    public void setCheckInRef(String refNo) {
        this.checkInRef = refNo;
    }
}
