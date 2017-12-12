package com.example.abhi.stockup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhi.stockup.DB.FoodStuffContract;
import com.example.abhi.stockup.DB.FoodStuffDBHelper;

/**
 * Created by Abhi on 11/21/2017.
 */

public class CustomCursorAdapter extends RecyclerView.Adapter<CustomCursorAdapter.FoodStuffViewHolder>{

    private Context mContext;
    private Cursor mCursor;

    public CustomCursorAdapter(Context context) {
        mContext = context;
    }

    @Override
    public FoodStuffViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate task layout to view
        View view = LayoutInflater.from(mContext).inflate(R.layout.foodstuffs_layout, parent, false);
        return new FoodStuffViewHolder(view);

    }


    //displays data onto the recyclerview
    @Override
    public void onBindViewHolder(FoodStuffViewHolder holder, int position) {

        //get indices for all the column names in order to interact with the columns
        int idIndex = mCursor.getColumnIndex(FoodStuffContract.FoodStuffEntry._ID);
        int nameIndex = mCursor.getColumnIndex(FoodStuffContract.FoodStuffEntry.COLUMN_NAME);
        int quantityIndex = mCursor.getColumnIndex(FoodStuffContract.FoodStuffEntry.COLUMN_QUANTITY);
        int unitsIndex = mCursor.getColumnIndex(FoodStuffContract.FoodStuffEntry.COLUMN_UNITS);
        //move cursor to the right position because you are activating this everytime a new viewholder is being bound to be displayed
        mCursor.moveToPosition(position);

        //get the current stuff from the DB
        int id = mCursor.getInt(idIndex);
        String name = mCursor.getString(nameIndex);
        int quantity = mCursor.getInt(quantityIndex);
        String units = mCursor.getString(unitsIndex);

        //set the values
        holder.itemView.setTag(id);
        holder.tv_foodstuffName.setText(name);
        holder.tv_quantity.setText(String.valueOf(quantity));
        holder.tv_units.setText(units);

        //highlight the item if you are out of it
        if (quantity == 0) {

        }
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    //when the data changes, function swaps the cursor for the new one
    public Cursor swapCursor(Cursor cursor) {
        if (mCursor == cursor) {
            return null;
        }
        Cursor temp = mCursor;
        mCursor = cursor;

        //is this null?
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public class FoodStuffViewHolder extends RecyclerView.ViewHolder{

        TextView tv_foodstuffName;
        TextView tv_quantity;
        TextView tv_units;

        public FoodStuffViewHolder(View itemView) {
            super(itemView);
//            itemView.setOnClickListener(this);
            tv_foodstuffName = itemView.findViewById(R.id.textview_foodName);
            tv_quantity = itemView.findViewById(R.id.textview_quantity);
            tv_units = itemView.findViewById(R.id.textview_units);
        }

    }
}

