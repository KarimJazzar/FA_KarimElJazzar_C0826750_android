package com.karim.fa_karimeljazzar_c0826750_android.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDate;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "place_database";

    private static final String TABLE_NAME = "place";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_Latitude = "latitude";
    private static final String COLUMN_longitude = "longitude";
    private static final String COLUMN_Visited = "Visited";
    private static final String COLUMN_DateAdded = "DateAdded";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " varchar(20) NOT NULL, " +
                COLUMN_Latitude + " FLOAT NOT NULL, " +
                COLUMN_longitude + " FLOAT NOT NULL, " +
                COLUMN_Visited + " INT NOT NULL, " +
                COLUMN_DateAdded + " varchar(20) NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addPlace(String name, double latitude, double longitude, boolean visited, String localDate) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_Latitude, String.valueOf(latitude));
        contentValues.put(COLUMN_longitude, String.valueOf(longitude));
        if (visited == true) {
            contentValues.put(COLUMN_Visited, String.valueOf(1));
        } else {
            contentValues.put(COLUMN_Visited, String.valueOf(0));
        }
        contentValues.put(COLUMN_DateAdded, localDate);
        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues) != 1;
    }

    public Cursor getPlaces() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM place", null);
    }

    public void deleteAllPlaces() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_NAME);
    }

    public boolean deletePlace(int id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}) > 0;
    }

    public boolean updatePlace(int id, String name, double latitude, double longitude, boolean visited, String localDate) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_Latitude, String.valueOf(latitude));
        contentValues.put(COLUMN_longitude, String.valueOf(longitude));
        if (visited == true) {
            contentValues.put(COLUMN_Visited, String.valueOf(1));
        } else {
            contentValues.put(COLUMN_Visited, String.valueOf(0));
        }
        contentValues.put(COLUMN_DateAdded, localDate);

        return sqLiteDatabase.update(TABLE_NAME,
                contentValues,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}) > 0;
    }
}
