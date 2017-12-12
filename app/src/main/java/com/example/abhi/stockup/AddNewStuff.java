package com.example.abhi.stockup;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abhi.stockup.DB.FoodStuffContract;
import com.example.abhi.stockup.DB.FoodStuffDBHelper;

public class AddNewStuff extends AppCompatActivity {

    private SQLiteDatabase mDbAccess;

    //EditText fields
    EditText et_stuff_name;
    EditText et_stuff_quantity;
    EditText et_stuff_units;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_stuff);

        //possible error in the context call
        mDbAccess = MainActivity.mDb;


        //instantiate EditText
        et_stuff_name = findViewById(R.id.et_stuff_name);
        et_stuff_quantity = findViewById(R.id.et_stuff_quantity);
        et_stuff_units = findViewById(R.id.et_stuff_units);
    }

    public void addToPantry (View view) {
        String name = et_stuff_name.getText().toString();
        String units = et_stuff_units.getText().toString();
        int quantity = 0;
        if (name.length() == 0 || units.length() == 0) {
            Toast.makeText(this,"Something's empty!",Toast.LENGTH_SHORT).show();
            return;
        }
        try
        {
            quantity = Integer.parseInt(et_stuff_quantity.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "I can't read that number for the quantity",Toast.LENGTH_LONG).show();
        }
        addStuff(name, quantity, units);


    }

    private void addStuff(String name, int quantity, String units) {
        ContentValues cv = new ContentValues();
        cv.put(FoodStuffContract.FoodStuffEntry.COLUMN_NAME, name);
        cv.put(FoodStuffContract.FoodStuffEntry.COLUMN_QUANTITY, quantity);
        cv.put(FoodStuffContract.FoodStuffEntry.COLUMN_UNITS, units);
        mDbAccess.insert(FoodStuffContract.FoodStuffEntry.TABLE_NAME, null, cv);
        NavUtils.navigateUpFromSameTask(this);
    }
}
