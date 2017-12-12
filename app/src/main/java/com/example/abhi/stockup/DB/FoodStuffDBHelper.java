package com.example.abhi.stockup.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

/**
 * Created by Abhi on 11/21/2017.
 */

public class FoodStuffDBHelper extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "stockupDB.db";


    public FoodStuffDBHelper(Context context) {
        super(context,DATABASE_NAME,null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create the stuff table
        final String CREATE_TABLE = "CREATE TABLE "                     + FoodStuffContract.FoodStuffEntry.TABLE_NAME + " (" +
                FoodStuffContract.FoodStuffEntry._ID                    + " INTEGER PRIMARY KEY, " +
                FoodStuffContract.FoodStuffEntry.COLUMN_NAME            + " TEXT NOT NULL, " +
                FoodStuffContract.FoodStuffEntry.COLUMN_QUANTITY        + " INTEGER NOT NULL, " +
                FoodStuffContract.FoodStuffEntry.COLUMN_UNITS           + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FoodStuffContract.FoodStuffEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
