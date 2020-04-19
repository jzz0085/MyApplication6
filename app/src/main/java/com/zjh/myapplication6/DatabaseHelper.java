package com.zjh.myapplication6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "transactions.db";
    public static final String TABLE_NAME = "History";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "DATE";
    public static final String COL_3 = "AMOUNT";
    public static final String COL_4 = "CATEGORY";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, DATE TEXT, AMOUNT DOUBLE, CATEGORY TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean insertTransaction(TransactionModel transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, transaction.mDate);
        contentValues.put(COL_3, transaction.mAmount);
        contentValues.put(COL_4, transaction.mCategory);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Cursor filter(String fromDate, String toDate, String fromAmtString, String toAmtString) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        Double fromAmt = null;
        Double toAmt = null;

        if (!fromAmtString.isEmpty()) {
            fromAmt = Double.parseDouble(fromAmtString);
        }
        if (!toAmtString.isEmpty()) {
            toAmt = Double.parseDouble(toAmtString);
        }

        if (!fromDate.isEmpty() && toDate.isEmpty() && fromAmt == null && toAmt == null) {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE DATE >= '" + fromDate + "'", null);
        }

        else if (fromDate.isEmpty() && !toDate.isEmpty() && fromAmt == null && toAmt == null) {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE DATE <= '" + toDate + "'", null);
        }
        else if (fromDate.isEmpty() && toDate.isEmpty() && fromAmt != null && toAmt == null) {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE AMOUNT >= " + fromAmt, null);
        }
        else if (fromDate.isEmpty() && toDate.isEmpty() && fromAmt == null && toAmt != null) {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE AMOUNT <= " + toAmt, null);
        }
        else if (!fromDate.isEmpty() && !toDate.isEmpty() && fromAmt == null && toAmt == null) {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE DATE >= '" + fromDate + "' AND DATE <= '" + toDate + "'", null);
        }
        else if (fromDate.isEmpty() && toDate.isEmpty() && fromAmt != null && toAmt != null) {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE AMOUNT >= " + fromAmt + " AND AMOUNT <= " + toAmt, null);
        }
        else if (!fromDate.isEmpty() && toDate.isEmpty() && fromAmt != null && toAmt == null) {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE AMOUNT >= " + fromAmt + " AND DATE >= '" + fromDate + "'", null);
        }
        else if (!fromDate.isEmpty() && toDate.isEmpty() && fromAmt == null && toAmt != null) {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE AMOUNT <= " + toAmt + " AND DATE >= '" + fromDate + "'", null);
        }
        else if (fromDate.isEmpty() && !toDate.isEmpty() && fromAmt != null && toAmt == null) {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE AMOUNT >= " + fromAmt + " AND DATE <= '" + toDate + "'", null);
        }
        else if (fromDate.isEmpty() && !toDate.isEmpty() && fromAmt == null && toAmt != null) {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE AMOUNT <= " + toAmt + " AND DATE <= '" + toDate + "'", null);
        }
        else if (!fromDate.isEmpty() && !toDate.isEmpty() && fromAmt != null && toAmt == null) {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE AMOUNT >= " + fromAmt + " AND DATE >= '" + fromDate + "' AND DATE <= '" + toDate +"'", null);
        }
        else if (!fromDate.isEmpty() && !toDate.isEmpty() && fromAmt == null && toAmt != null) {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE AMOUNT <= " + toAmt + " AND DATE >= '" + fromDate + "' AND DATE <= '" + toDate +"'", null);
        }
        else if (!fromDate.isEmpty() && toDate.isEmpty() && fromAmt != null && toAmt != null) {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE AMOUNT >= " + fromAmt + " AND AMOUNT <= " + toAmt + " AND DATE >= '" + fromDate + "'", null);
        }
        else if (fromDate.isEmpty() && !toDate.isEmpty() && fromAmt != null && toAmt != null) {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE AMOUNT >= " + fromAmt + " AND AMOUNT <= " + toAmt + " AND DATE <= '" + toDate + "'", null);
        }
        else if (!fromDate.isEmpty() && !toDate.isEmpty() && fromAmt != null && toAmt != null) {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE AMOUNT >= " + fromAmt + " AND AMOUNT <= " + toAmt + " AND DATE >= '" + fromDate + "' AND DATE <= '" + toDate + "'", null);
        }

        return cursor;
    }

}
