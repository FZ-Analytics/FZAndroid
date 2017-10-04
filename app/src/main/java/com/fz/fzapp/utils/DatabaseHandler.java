package com.fz.fzapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fz.fzapp.data.TrackingTrx;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Heru Permana on 10/3/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "trackingUploadDataBase";


    private static final String TABLE_TRACKING_DATA = "trackingTable";

    // trackings Table Columns names

    private static final String KEY_LONGTITUDE = "Longitude";
    private static final String KEY_LATITUDE = "Latitude";
    private static final String KEY_VEHICLE_ID = "VehicleID";
    private static final String KEY_USER_ID = "UserID";
    private static final String KEY_END_TIME = "EndDate";
    private static final String KEY_STATUS = "status";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String CREATE_TRACKING_DATA_TABLE = "CREATE TABLE " + TABLE_TRACKING_DATA + "("
//                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LATITUDE + " TEXT," + KEY_LONGTITUDE + " TEXT,"
//                + KEY_END_TIME + " TEXT," + KEY_USER_ID + " INTEGER," + KEY_VEHICLE_ID + " INTEGER,"
//                + KEY_STATUS + " INTEGER" + ")";
//        db.execSQL(CREATE_TRACKING_DATA_TABLE);

        String CREATE_CONTACTS_TABLE = "CREATE TABLE "
                + TABLE_TRACKING_DATA + "("
                + KEY_LATITUDE + " TEXT,"
                + KEY_LONGTITUDE + " TEXT,"
                + KEY_END_TIME + " TEXT"
                + KEY_USER_ID + " INTEGER PRIMARY KEY,"
                + KEY_VEHICLE_ID + " INTEGER,"
                + KEY_STATUS + " INTEGER" + ");";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKING_DATA);

        // Create tables again
        onCreate(db);
    }

    // Adding new
    public void addTracking(TrackingTrx trackingTrx) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE, trackingTrx.getLatitude());
        values.put(KEY_LONGTITUDE, trackingTrx.getLongitude());
        values.put(KEY_END_TIME, trackingTrx.getEndDate());
        values.put(KEY_USER_ID, trackingTrx.getUserID());
        values.put(KEY_VEHICLE_ID, trackingTrx.getVehicleID());
        values.put(KEY_STATUS, trackingTrx.getStatus());

        // Inserting Row
        db.insert(TABLE_TRACKING_DATA, null, values);
        db.close(); // Closing database connection
    }

    // Getting single
    TrackingTrx getTracking(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TRACKING_DATA, new String[]{ KEY_LATITUDE,
                        KEY_LONGTITUDE, KEY_END_TIME, KEY_USER_ID, KEY_VEHICLE_ID, KEY_STATUS},"",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        TrackingTrx trackingTrx = new TrackingTrx(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                Integer.parseInt(cursor.getString(3)),
                Integer.parseInt(cursor.getString(4)),
                Integer.parseInt(cursor.getString(5)));

        // return tracking
        return trackingTrx;
    }

    // Getting All trackings
    public List<TrackingTrx> getAllTracking() {
        List<TrackingTrx> trackingList = new ArrayList<TrackingTrx>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TRACKING_DATA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TrackingTrx trackingTrx = new TrackingTrx();
                trackingTrx.setLatitude(cursor.getString(0));
                trackingTrx.setLongitude(cursor.getString(1));
                trackingTrx.setEndDate(cursor.getString(2));
                trackingTrx.setUserID(Integer.parseInt(cursor.getString(3)));
                trackingTrx.setVehicleID(Integer.parseInt(cursor.getString(4)));
                trackingTrx.setStatus(Integer.parseInt(cursor.getString(5)));
                // Adding tracking to list
                trackingList.add(trackingTrx);
            } while (cursor.moveToNext());
        }

        // return tracking list
        return trackingList;
    }

    // Updating single tracking
    public int updateTracking(TrackingTrx trackingTrx) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE, trackingTrx.getLatitude());
        values.put(KEY_LONGTITUDE, trackingTrx.getLongitude());
        values.put(KEY_END_TIME, trackingTrx.getEndDate());
        values.put(KEY_USER_ID, trackingTrx.getUserID());
        values.put(KEY_VEHICLE_ID, trackingTrx.getVehicleID());
        values.put(KEY_STATUS, trackingTrx.getStatus());

        // updating row
        return db.update(TABLE_TRACKING_DATA, values, "'",
                new String[]{String.valueOf(trackingTrx.getUserID())});
    }

    // Deleting single tracking
    public void deleteTracking(TrackingTrx tracking) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRACKING_DATA, KEY_USER_ID + "=?",
                new String[]{String.valueOf(tracking.getUserID())});
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TRACKING_DATA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
