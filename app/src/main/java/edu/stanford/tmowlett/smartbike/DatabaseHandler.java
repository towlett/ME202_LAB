package edu.stanford.tmowlett.smartbike;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    // Class Variables
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "rideManager";
    private static final String TABLE_RIDES = "rides";
    private static final String KEY_ID = "id";
    private static final String KEY_LOC = "ride_date";
    private static final String KEY_DATE = "ride_loc";
    private static final String KEY_ICON_ID = "icon_id";

    //Constructor
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creates the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RIDES_TABLE = "CREATE TABLE " + TABLE_RIDES + "(" + KEY_ID + " TEXT," + KEY_LOC + " TEXT," + KEY_DATE + " TEXT," + KEY_ICON_ID + " INTEGER" + ")";
        db.execSQL(CREATE_RIDES_TABLE);
    }

    // Upgrades the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RIDES);
        onCreate(db);
    }

    public void clearRideTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RIDES);
        onCreate(db);
    }

    // ALL CRUD OPERATIONS

    // Add a new ride to the database
    public long addRide(RideInfo rideInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, rideInfo.getRideId());
        values.put(KEY_LOC, rideInfo.getRideLocation());
        values.put(KEY_DATE, rideInfo.getRideDate());
        values.put(KEY_ICON_ID, rideInfo.getRideIcon());

        long rowId = db.insert(TABLE_RIDES, null, values);

        db.close();
        return rowId;
    }

    // Get ride info function -- Currently unused
    public RideInfo getRide(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RIDES, new String[] { KEY_ID, KEY_LOC, KEY_DATE, KEY_ICON_ID}, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            cursor.close();
        }

        db.close();
        return new RideInfo(cursor.getString(0), cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)));
    }

    // Function to return all rides
    public ArrayList<RideInfo> getAllRides() {
        ArrayList<RideInfo> rideList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_RIDES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RideInfo rideInfo = new RideInfo(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
                rideList.add(rideInfo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return rideList;
    }

    // Get total number of rides in DB - Currently unused
    public int getRidesCount() {
        String countQuery = "SELECT * FROM " + TABLE_RIDES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();
        return cursor.getCount();
    }

    // Delete a ride from the DB
    public void deleteRide(RideInfo rideInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RIDES, KEY_ID + " = ?", new String[] { rideInfo.getRideId()});
        db.close();
    }
}
