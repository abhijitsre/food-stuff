package com.example.abhi.stockup.DB;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhi on 12/3/2017.
 */

public class TestMe {

    public static void fakeData(SQLiteDatabase db) {
        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(FoodStuffContract.FoodStuffEntry.COLUMN_NAME, "Milk");
        cv.put(FoodStuffContract.FoodStuffEntry.COLUMN_QUANTITY, 2);
        cv.put(FoodStuffContract.FoodStuffEntry.COLUMN_UNITS, "Gallons");
        list.add(cv);

        cv = new ContentValues();
        cv.put(FoodStuffContract.FoodStuffEntry.COLUMN_NAME, "Eggs");
        cv.put(FoodStuffContract.FoodStuffEntry.COLUMN_QUANTITY, 5);
        cv.put(FoodStuffContract.FoodStuffEntry.COLUMN_UNITS, "eggs");
        list.add(cv);

        cv = new ContentValues();
        cv.put(FoodStuffContract.FoodStuffEntry.COLUMN_NAME, "Butter");
        cv.put(FoodStuffContract.FoodStuffEntry.COLUMN_QUANTITY, 1);
        cv.put(FoodStuffContract.FoodStuffEntry.COLUMN_UNITS, "Ounces");
        list.add(cv);

        cv = new ContentValues();
        cv.put(FoodStuffContract.FoodStuffEntry.COLUMN_NAME, "Hummus");
        cv.put(FoodStuffContract.FoodStuffEntry.COLUMN_QUANTITY, 1);
        cv.put(FoodStuffContract.FoodStuffEntry.COLUMN_UNITS, "Carton");
        list.add(cv);


        try {
            db.beginTransaction();
            db.delete(FoodStuffContract.FoodStuffEntry.TABLE_NAME, null, null);
            for (ContentValues c : list) {
                db.insert(FoodStuffContract.FoodStuffEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {

        } finally{
            db.endTransaction();
        }
    }
}
