package com.example.workflow.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.workflow.database.offlineModels.AttendanceList;
import com.example.workflow.utils.PreferenceUtils;

import java.util.ArrayList;


public class OfflineDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "travelizelite.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;
    // tabel name
    private static final String OFFLINE_LOC_TABEL = "OFFLINE_LOC_TABEL";
    // COL
    private static final String OFFLINE_LOC_ID = "OFFLINE_LOC_ID";
    private static final String OFFLINE_LOC_USER_ID = "OFFLINE_LOC_USER_ID";
    private static final String OFFLINE_LOC_LONGITUDE = "OFFLINE_LOC_LONGITUDE";
    private static final String OFFLINE_LOC_LATITUDE = "OFFLINE_LOC_LATITUDE";
    private static final String OFFLINE_LOC_USER_ACTIVITY = "OFFLINE_LOC_USER_ACTIVITY";
    private static final String OFFLINE_LOC_DECODED_ADDRESS = "OFFLINE_LOC_DECODED_ADDRESS";
    private static final String OFFLINE_LOC_BATTERY_STRENGTH = "OFFLINE_LOC_BATTERY_STRENGTH";
    private static final String OFFLINE_LOC_NETWORK_STRENGTH = "OFFLINE_LOC_NETWORK_STRENGTH";
    private static final String OFFLINE_LOC_TIME_STAMP = "OFFLINE_LOC_TIME_STAMP";
    private static final String OFFLINE_LOC_BATTERY_TEMP = "OFFLINE_LOC_BATTERY_TEMP";
    private static final String OFFLINE_LOC_NETWORK_TYPE = "OFFLINE_LOC_NETWORK_TYPE";
    private static final String OFFLINE_LOC_IS_OFFLINE = "OFFLINE_LOC_IS_OFFLINE";

    private static final String LAST_CHECK_OUT_TABLE = "LAST_CHECK_OUT";

    public OfflineDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_OFFLINE_LOC_DB = "CREATE TABLE " + OFFLINE_LOC_TABEL + "(" +
                OFFLINE_LOC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                OFFLINE_LOC_USER_ID + "  TEXT," +
                OFFLINE_LOC_LONGITUDE + " TEXT," +
                OFFLINE_LOC_LATITUDE + " TEXT," +
                OFFLINE_LOC_USER_ACTIVITY + " TEXT," +
                OFFLINE_LOC_BATTERY_STRENGTH + " TEXT," +
                OFFLINE_LOC_NETWORK_STRENGTH + " TEXT," +
                OFFLINE_LOC_DECODED_ADDRESS + " TEXT," +
                OFFLINE_LOC_TIME_STAMP + " DEFAULT CURRENT_TIMESTAMP," +
                OFFLINE_LOC_BATTERY_TEMP + " TEXT," +
                OFFLINE_LOC_IS_OFFLINE + " TEXT," +
                OFFLINE_LOC_NETWORK_TYPE + " TEXT" + ");";
        db.execSQL(CREATE_OFFLINE_LOC_DB);

        // last check out tabel
        String CREATE_LAST_CHECK_OUT = "CREATE TABLE " + LAST_CHECK_OUT_TABLE + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "type TEXT" + ");";
        db.execSQL(CREATE_LAST_CHECK_OUT);

        db.execSQL(DBTableColumnsNames.CREATE_OFFLINE_ATTENDENCE_CHECKIN_TABLE);
        db.execSQL(DBTableColumnsNames.CREATE_OFFLINE_ATTENDENCE_CHECKOUT_TABLE);
        db.execSQL(DBTableColumnsNames.CREATE_VISIT_DETAILS_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + OFFLINE_LOC_TABEL);
        db.execSQL("DROP TABLE IF EXISTS " + DBTableColumnsNames.OFFLINE_ATTENDENCE_CHECKIN_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBTableColumnsNames.OFFLINE_ATTENDENCE_CHECKOUT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBTableColumnsNames.VISIT_DETAILS_TABLE_NAME);
        onCreate(db);
    }

    public void deleteAllAtTheTimeOfLogout() {
        deleteAllCheckOut();
        deleteAllCheckIn();
        deleteAllLastCheckOuts();
    }

    public void addLastCheckOutDetails(String notification) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("type", notification);
        database.insert(LAST_CHECK_OUT_TABLE, null, values);
    }

    public void deleteAllLastCheckOuts() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(LAST_CHECK_OUT_TABLE, null, null);
        database.close();
    }

    /////////////////// attendence add checkin //////////////////////////
    public int addCheckInDetails(String checkInTime, String latitude, String longitude, String location,
                                 String referenceId) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBTableColumnsNames.OFFLINE_CHECK_IN_TIME, checkInTime);
        values.put(DBTableColumnsNames.OFFLINE_CHECK_IN_LATITUDE, latitude);
        values.put(DBTableColumnsNames.OFFLINE_CHECK_IN_LONGITUDE, longitude);
        values.put(DBTableColumnsNames.OFFLINE_CHECK_IN_LOCATION, location);
        values.put(DBTableColumnsNames.OFFLINE_CHECK_IN_REFERENCE_ID, referenceId);
        return (int) database.insert(DBTableColumnsNames.OFFLINE_ATTENDENCE_CHECKIN_TABLE_NAME, null, values);
    }

    /////////////////// attendence add checkout //////////////////////////
    public int addCheckOutDetails(String referenceId, String checkOutTime, String checkOutLatitude,
                                  String checkOutLongitude, String location) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBTableColumnsNames.OFFLINE_CHECK_OUT_TIME, checkOutTime);
        values.put(DBTableColumnsNames.OFFLINE_CHECK_OUT_LATITUDE, checkOutLatitude);
        values.put(DBTableColumnsNames.OFFLINE_CHECK_OUT_LONGITUDE, checkOutLongitude);
        values.put(DBTableColumnsNames.OFFLINE_CHECK_OUT_LOCATION, location);
        values.put(DBTableColumnsNames.OFFLINE_CHECK_OUT_REFERENCE_ID, referenceId);
        return (int) database.insert(DBTableColumnsNames.OFFLINE_ATTENDENCE_CHECKOUT_TABLE_NAME, null, values);
    }

    ////////////////////////// all user checkin details /////////////////
    public ArrayList<AttendanceList> getUserCheckIn() {
        ArrayList<AttendanceList> attendenceListArrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + DBTableColumnsNames.OFFLINE_ATTENDENCE_CHECKIN_TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                AttendanceList attendenceList = new AttendanceList();

                attendenceList.setCheckInLatitude(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_IN_LATITUDE)));
                attendenceList.setCheckInLongitude(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_IN_LONGITUDE)));
                attendenceList.setCheckInTime(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_IN_TIME)));
                attendenceList.setCheckInLocation(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_IN_LOCATION)));

                attendenceList.setBatteryPresentage(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_IN_BATTERY_PERCENTAGE)));
                attendenceList.setIsMock(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_IN_IS_MOCK)));
                attendenceList.setNetworkType(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_IN_NETWORK_TYPE)));
                attendenceList.setSignalStrength(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_IN_SIGNAL_STRENGTH)));

                attendenceList.setRefNo(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_IN_REFERENCE_ID)));
                attendenceList.setEmpID(PreferenceUtils.getUserIdFromThePreference(context));

                attendenceListArrayList.add(attendenceList);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return attendenceListArrayList;
    }

    ////////////////////////// all user checkout details /////////////////
    public ArrayList<AttendanceList> getUserCheckOut() {
        ArrayList<AttendanceList> attendenceListArrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + DBTableColumnsNames.OFFLINE_ATTENDENCE_CHECKOUT_TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                AttendanceList attendenceList = new AttendanceList();

                attendenceList.setCheckOutLatitude(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_OUT_LATITUDE)));
                attendenceList.setCheckOutLongitude(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_OUT_LONGITUDE)));
                attendenceList.setCheckOutTime(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_OUT_TIME)));
                attendenceList.setCheckOutLocation(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_OUT_LOCATION)));

                attendenceList.setBatteryPresentage(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_OUT_BATTERY_PERCENTAGE)));
                attendenceList.setIsMock(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_OUT_IS_MOCK)));
                attendenceList.setNetworkType(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_OUT_NETWORK_TYPE)));
                attendenceList.setSignalStrength(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_OUT_SIGNAL_STRENGTH)));

                attendenceList.setRefNo(cursor.getString(cursor.getColumnIndexOrThrow(DBTableColumnsNames.OFFLINE_CHECK_OUT_REFERENCE_ID)));
                attendenceList.setEmpID(PreferenceUtils.getUserIdFromThePreference(context));

                attendenceListArrayList.add(attendenceList);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return attendenceListArrayList;
    }

    //////////////////////////// get count from check in
    public int getCheckInCountFromDb() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DBTableColumnsNames.OFFLINE_ATTENDENCE_CHECKIN_TABLE_NAME;
        Cursor res = db.rawQuery(query, null);
        return res.getCount();
    }

    //////////////////////////// get count from check out
    public int getCheckOutCountFromDb() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DBTableColumnsNames.OFFLINE_ATTENDENCE_CHECKOUT_TABLE_NAME;
        Cursor res = db.rawQuery(query, null);
        return res.getCount();
    }


    //////////////////// delete all from checkin table
    public void deleteAllCheckIn() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(DBTableColumnsNames.OFFLINE_ATTENDENCE_CHECKIN_TABLE_NAME, null, null);
        database.close();
    }

    //////////////////// delete all from checkout table
    public void deleteAllCheckOut() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(DBTableColumnsNames.OFFLINE_ATTENDENCE_CHECKOUT_TABLE_NAME, null, null);
        database.close();
    }




}
