package com.example.electricitybillapp;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import java.util.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "BillDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE bill (id INTEGER PRIMARY KEY AUTOINCREMENT, month TEXT, unit INTEGER, total REAL, rebate REAL, final REAL)");
    }

    public void insertBill(Bill bill) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("month", bill.getMonth());
        cv.put("unit", bill.getUnit());
        cv.put("total", bill.getTotalCharges());
        cv.put("rebate", bill.getRebate());
        cv.put("final", bill.getFinalCost());
        db.insert("bill", null, cv);
    }

    public List<Bill> getAllBills() {
        List<Bill> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM bill", null);
        while (c.moveToNext()) {
            list.add(new Bill(
                    c.getString(1),
                    c.getInt(2),
                    c.getDouble(3),
                    c.getDouble(4),
                    c.getDouble(5)
            ));
        }
        return list;
    }

    // ✅ Add this method to support deleting all history
    public void deleteAllBills() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("bill", null, null);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS bill");
        onCreate(db);
    }
}

