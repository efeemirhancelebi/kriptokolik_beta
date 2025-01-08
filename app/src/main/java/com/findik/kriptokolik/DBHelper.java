package com.findik.kriptokolik;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CoinDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Tablo ve kolon isimleri
    public static final String TABLE_NAME = "coins";
    public static final String COLUMN_COIN_NAME = "coin_name";  // örn: "btc", "ocean"
    public static final String COLUMN_AMOUNT = "miktar";        // double miktar

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Tablo oluşturma
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_COIN_NAME + " TEXT,"
                + COLUMN_AMOUNT + " REAL"
                + ");";
        db.execSQL(CREATE_TABLE);
    }

    // Versiyon artınca tabloyu silip yeniden oluşturma
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertOrUpdateCoin(String coinShortName, double girilenMiktar, boolean isSell) {
        coinShortName = coinShortName.trim().toLowerCase();

        SQLiteDatabase db = this.getWritableDatabase();

        // 1) Bu coin var mı?
        Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_AMOUNT + " FROM " + TABLE_NAME
                        + " WHERE " + COLUMN_COIN_NAME + "=?",
                new String[]{coinShortName}
        );

        if (cursor != null && cursor.moveToFirst()) {
            // Kayıt varsa
            double mevcutMiktar = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT));
            cursor.close();

            double yeniMiktar = isSell
                    ? (mevcutMiktar - girilenMiktar)
                    : (mevcutMiktar + girilenMiktar);

            // 0 veya altına düştüyse sil
            if (yeniMiktar <= 0) {
                int deleted = db.delete(TABLE_NAME, COLUMN_COIN_NAME + "=?",
                        new String[]{coinShortName});
                db.close();
                return (deleted > 0);
            } else {
                // Miktarı güncelle
                ContentValues values = new ContentValues();
                values.put(COLUMN_COIN_NAME, coinShortName);
                values.put(COLUMN_AMOUNT, yeniMiktar);

                int updated = db.update(TABLE_NAME, values,
                        COLUMN_COIN_NAME + "=?",
                        new String[]{coinShortName});
                db.close();
                return (updated > 0);
            }

        } else {
            // Kayıt yoksa ekle
            if (cursor != null) cursor.close();

            double ilkMiktar = isSell ? -girilenMiktar : girilenMiktar;

            ContentValues values = new ContentValues();
            values.put(COLUMN_COIN_NAME, coinShortName);
            values.put(COLUMN_AMOUNT, ilkMiktar);

            long result = db.insert(TABLE_NAME, null, values);

            // Eğer ilkMiktar <= 0 olduysa ekleyip siliyoruz
            if (ilkMiktar <= 0) {
                db.delete(TABLE_NAME, COLUMN_COIN_NAME + "=?", new String[]{coinShortName});
            }

            db.close();
            return (result != -1);
        }
    }

    public Cursor getAllCoins() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
