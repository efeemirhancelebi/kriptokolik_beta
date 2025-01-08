package com.findik.kriptokolik;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PortfolioDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PortfolioDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "portfolio_values";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_VALUE = "value";

    public PortfolioDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tablo: id (PK), timestamp, value
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TIMESTAMP + " INTEGER, "
                + COLUMN_VALUE + " REAL"
                + ");";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertPortfolioValue(double totalValue) {
        long now = System.currentTimeMillis();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TIMESTAMP, now);
        cv.put(COLUMN_VALUE, totalValue);
        db.insert(TABLE_NAME, null, cv);
        db.close();
    }

    public Cursor getAllPortfolioValues() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " ASC", null);
    }
}
