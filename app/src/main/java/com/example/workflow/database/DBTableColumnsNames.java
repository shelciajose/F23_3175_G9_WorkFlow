package com.example.workflow.database;

public class DBTableColumnsNames {

    static final String SYNC_STATUS = "SyncStatus";
    static final String REFERENCE_NO = "ReferenceNo";
    ///////////////////////////////////// Table names  ////////////////////////////////////////////
////////////////////// Table Create ---- Columns names


    // tabel name
    static final String OFFLINE_ATTENDENCE_CHECKIN_TABLE_NAME = "Offline_CheckIn";
    static final String OFFLINE_ATTENDENCE_CHECKOUT_TABLE_NAME = "Offline_CheckOut";
    // COL
    static final String OFFLINE_CHECK_IN_TIME = "CheckInTime";
    static final String OFFLINE_CHECK_IN_LATITUDE = "CheckIn_Latitude";
    static final String OFFLINE_CHECK_IN_LONGITUDE = "CheckIn_Longitude";
    static final String OFFLINE_CHECK_IN_LOCATION = "CheckIn_Location";
    static final String OFFLINE_CHECK_IN_IS_MOCK = "CheckIn_IsMock";
    static final String OFFLINE_CHECK_IN_BATTERY_PERCENTAGE = "CheckIn_BatteryPerc";
    static final String OFFLINE_CHECK_IN_SIGNAL_STRENGTH = "CheckIn_SignalStrength";
    static final String OFFLINE_CHECK_IN_NETWORK_TYPE = "CheckIn_NetworkType";
    static final String OFFLINE_CHECK_IN_REFERENCE_ID = "CheckIn_RefeId";

    static final String OFFLINE_CHECK_OUT_TIME = "CheckOutTime";
    static final String OFFLINE_CHECK_OUT_LATITUDE = "CheckOut_Latitude";
    static final String OFFLINE_CHECK_OUT_LONGITUDE = "CheckOut_Longitude";
    static final String OFFLINE_CHECK_OUT_LOCATION = "CheckOut_Location";
    static final String OFFLINE_CHECK_OUT_IS_MOCK = "CheckOut_IsMock";
    static final String OFFLINE_CHECK_OUT_BATTERY_PERCENTAGE = "CheckOut_BatteryPerc";
    static final String OFFLINE_CHECK_OUT_SIGNAL_STRENGTH = "CheckOut_SignalStrength";
    static final String OFFLINE_CHECK_OUT_NETWORK_TYPE = "CheckOut_NetworkType";
    static final String OFFLINE_CHECK_OUT_REFERENCE_ID = "CheckOut_RefeId";


    static final String CREATE_OFFLINE_ATTENDENCE_CHECKIN_TABLE = "CREATE TABLE " + OFFLINE_ATTENDENCE_CHECKIN_TABLE_NAME + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            OFFLINE_CHECK_IN_TIME + " TEXT," +
            OFFLINE_CHECK_IN_LATITUDE + " TEXT," +
            OFFLINE_CHECK_IN_LONGITUDE + " TEXT," +
            OFFLINE_CHECK_IN_LOCATION + " TEXT," +
            OFFLINE_CHECK_IN_IS_MOCK + " TEXT," +
            OFFLINE_CHECK_IN_BATTERY_PERCENTAGE + " TEXT," +
            OFFLINE_CHECK_IN_SIGNAL_STRENGTH + " TEXT," +
            OFFLINE_CHECK_IN_NETWORK_TYPE + " TEXT," +
            OFFLINE_CHECK_IN_REFERENCE_ID + " TEXT);";

    static final String CREATE_OFFLINE_ATTENDENCE_CHECKOUT_TABLE = "CREATE TABLE " + OFFLINE_ATTENDENCE_CHECKOUT_TABLE_NAME + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            OFFLINE_CHECK_OUT_TIME + " TEXT," +
            OFFLINE_CHECK_OUT_LATITUDE + " TEXT," +
            OFFLINE_CHECK_OUT_LONGITUDE + " TEXT," +
            OFFLINE_CHECK_OUT_LOCATION + " TEXT," +
            OFFLINE_CHECK_OUT_IS_MOCK + " TEXT," +
            OFFLINE_CHECK_OUT_BATTERY_PERCENTAGE + " TEXT," +
            OFFLINE_CHECK_OUT_SIGNAL_STRENGTH + " TEXT," +
            OFFLINE_CHECK_OUT_NETWORK_TYPE + " TEXT," +
            OFFLINE_CHECK_OUT_REFERENCE_ID + " TEXT);";


    ///////////////// table name
    static final String VISIT_DETAILS_TABLE_NAME = "VisitList";
    // COL
    static final String VISIT_DETAILS_COMPLAINT_NO = "ComplaintNo";
    static final String VISIT_DETAILS_CUSTOMER_NAME = "CustomerName";
    static final String VISIT_DETAILS_VISIT_CUR_LOCATION = "CurLocation";
    static final String VISIT_DETAILS_VISIT_CUR_LATITUDE = "CurLatitude";
    static final String VISIT_DETAILS_VISIT_CUR_LONGITUDE = "CurLongitude";
    static final String VISIT_DETAILS_TRAVEL_KM = "TravelKm";
    static final String VISIT_DETAILS_VISIT_RESULT = "VisitResult";
    static final String VISIT_DETAILS_VISIT_RESULTID = "VisitResultId";
    static final String VISIT_DETAILS_VISIT_STATUS = "VisitStatus";
    static final String VISIT_DETAILS_VISIT_STATUSID = "VisitStatusId";
    static final String VISIT_DETAILS_VISIT_ATTACHMENTS = "Attachments";
    static final String VISIT_DETAILS_VISIT_LST_LATITUDE = "LstLatitude";
    static final String VISIT_DETAILS_VISIT_LST_LONGITUDE = "LstLongitude";
    static final String VISIT_DETAILS_VISIT_LST_LOCATION = "LstLocation";
    static final String VISIT_DETAILS_VISIT_DATE = "Date";
    static final String VISIT_DETAILS_VISIT_TIME = "Time";
    static final String VISIT_DETAILS_VISIT_REMARKS = "Remarks";
    static final String VISIT_DETAILS_VISIT_REFERENCE_ID = "RefId";
    static final String VISIT_DETAILS_VISIT_SYNC_STATUS = "Sync";

    static final String VISIT_DETAILS_MOCK = "IsMock";
    static final String VISIT_DETAILS_BATTERY_PERCENTAGE = "BatteryPerc";
    static final String VISIT_DETAILS_SIGNAL_STRENGTH = "SignalStrength";
    static final String VISIT_DETAILS_NETWORK_TYPE = "NetworkType";

    static final String CREATE_VISIT_DETAILS_TABLE = "CREATE TABLE " + VISIT_DETAILS_TABLE_NAME + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            VISIT_DETAILS_COMPLAINT_NO + " TEXT," +
            VISIT_DETAILS_CUSTOMER_NAME + " TEXT," +
            VISIT_DETAILS_VISIT_CUR_LOCATION + " TEXT," +
            VISIT_DETAILS_VISIT_CUR_LATITUDE + " TEXT," +
            VISIT_DETAILS_VISIT_CUR_LONGITUDE + " TEXT," +
            VISIT_DETAILS_TRAVEL_KM + " TEXT," +
            VISIT_DETAILS_VISIT_LST_LOCATION + " TEXT," +
            VISIT_DETAILS_VISIT_LST_LATITUDE + " TEXT," +
            VISIT_DETAILS_VISIT_LST_LONGITUDE + " TEXT," +
            VISIT_DETAILS_VISIT_RESULT + " TEXT," +
            VISIT_DETAILS_VISIT_RESULTID + " TEXT," +
            VISIT_DETAILS_VISIT_STATUS + " TEXT," +
            VISIT_DETAILS_VISIT_STATUSID + " TEXT," +
            VISIT_DETAILS_VISIT_ATTACHMENTS + " TEXT," +
            VISIT_DETAILS_VISIT_DATE + " TEXT," +
            VISIT_DETAILS_VISIT_TIME + " TEXT," +
            VISIT_DETAILS_VISIT_REMARKS + " TEXT," +
            VISIT_DETAILS_VISIT_SYNC_STATUS + " TEXT," +
            VISIT_DETAILS_MOCK + " TEXT," +
            VISIT_DETAILS_BATTERY_PERCENTAGE + " TEXT," +
            VISIT_DETAILS_SIGNAL_STRENGTH + " TEXT," +
            VISIT_DETAILS_NETWORK_TYPE + " TEXT," +
            VISIT_DETAILS_VISIT_REFERENCE_ID + " TEXT);";

}

