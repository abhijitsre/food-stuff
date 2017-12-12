package com.example.abhi.stockup;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.LoaderManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.abhi.stockup.DB.FoodStuffContract;
import com.example.abhi.stockup.DB.FoodStuffDBHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private CustomCursorAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private static final int FOODSTUFF_LOADER_ID = 1;
    public static SQLiteDatabase mDb;

    private boolean ALREADY_SWITCHED = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //Initialize the dbhelper to pull a writeable database to mess around with.
        FoodStuffDBHelper dbHelper = new FoodStuffDBHelper(this);
        mDb = dbHelper.getWritableDatabase();

        //fake data to test!
//        TestMe.fakeData(mDb);

        //Set RecyclerView variable up
        mRecyclerView = findViewById(R.id.recyclerViewFoodStuffs);

        //Set up layout for recyclerview
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Initialize adapter and attach it to the RecyclerView
        mAdapter = new CustomCursorAdapter(this);
        mRecyclerView.setAdapter(mAdapter);



        //FAB actions and intents go here!
        //TODO create content provider for access
        FloatingActionButton fab = findViewById(R.id.fab_addnew);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, AddNewStuff.class);
//                Bundle dbBundle = new Bundle()
                startActivity(intent);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                try {
                    long id = Long.valueOf(viewHolder.itemView.getTag().toString());
                    removeStuff(id);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "this is the id " + viewHolder.itemView.getTag().toString(), Toast.LENGTH_LONG).show();
                }

                mAdapter.swapCursor(getAllResults());
            }
        }).attachToRecyclerView(mRecyclerView);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerView.SimpleOnItemTouchListener(){
                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        if (mRecyclerView.findChildViewUnder(e.getX(), e.getY()) != null) {
                            onTouchEvent(rv,e);
                        }
//                        onTouchEvent(rv,e);
                        return super.onInterceptTouchEvent(rv, e);
                    }
//TODO CURRENT  need to figure out 0. also the background of the recyclerview to make it consistent.
                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                        if (e.getAction() == MotionEvent.ACTION_DOWN) {
                            View rvView = rv.findChildViewUnder(e.getX(), e.getY());
//                        int position = rv.getChildAdapterPosition(rvView);
                            long id = Long.valueOf(rvView.getTag().toString());
                            if (decrementStuff(id)) {
                                mAdapter.swapCursor(mDb.query(FoodStuffContract.FoodStuffEntry.TABLE_NAME,
                                        null,
                                        null,
                                        null,
                                        null,
                                        null,
                                        FoodStuffContract.FoodStuffEntry._ID));

                                //test if the replace actually worked
                                Cursor cursor = mDb.query(FoodStuffContract.FoodStuffEntry.TABLE_NAME,
                                        new String[]{FoodStuffContract.FoodStuffEntry.COLUMN_QUANTITY},
                                        FoodStuffContract.FoodStuffEntry._ID + "=" + id,
                                        null,
                                        null,
                                        null,
                                        null);
                                cursor.moveToFirst();
                                int temp = cursor.getInt(0);
                                Toast.makeText(MainActivity.this, "the new quantity is " + temp, Toast.LENGTH_SHORT).show();
                                cursor.close();
                            } else
                                Toast.makeText(MainActivity.this, "The switch didn't work!", Toast.LENGTH_SHORT).show();
                            ALREADY_SWITCHED = true;
                        }


//                        TextView tv_name = findViewById(R.id.textview_foodName);
//                        TextView tv_quantity = findViewById(R.id.textview_quantity);
//                        TextView tv_units = findViewById(R.id.textview_units);
//                        ContentValues cv = new ContentValues();
//                        Snackbar.make(rvView, "this is the name that I pulled " + tv_name.getText().toString(),Snackbar.LENGTH_SHORT).show();
//                        cv.put(FoodStuffContract.FoodStuffEntry._ID, position);
//                        cv.put(FoodStuffContract.FoodStuffEntry.COLUMN_NAME, tv_name.getText().toString());
//                        cv.put(FoodStuffContract.FoodStuffEntry.COLUMN_QUANTITY, Integer.valueOf(tv_quantity.getText().toString())-1);
//                        cv.put(FoodStuffContract.FoodStuffEntry.COLUMN_UNITS, tv_units.getText().toString());
//
//                        mDb.insert(FoodStuffContract.FoodStuffEntry.TABLE_NAME,null, cv);
//                        mDb.delete(FoodStuffContract.FoodStuffEntry.TABLE_NAME,
//                                FoodStuffContract.FoodStuffEntry._ID + "=" + position, null);
//                        mAdapter.swapCursor(getAllResults());
////                        mAdapter.notifyDataSetChanged()
                    }
                }
        );

        getSupportLoaderManager().initLoader(FOODSTUFF_LOADER_ID, null, this);
    }

    public boolean decrementStuff(long id) {
//        if (!ALREADY_SWITCHED) {
            final int NAME = 1;
            final int QUANTITY = 2;
            final int UNITS = 3;
            Cursor cursor = mDb.query(FoodStuffContract.FoodStuffEntry.TABLE_NAME,
                    null,
                    FoodStuffContract.FoodStuffEntry._ID + "=" + id,
                    null,
                    null,
                    null,
                    null);
            cursor.moveToFirst();
            ContentValues cv = new ContentValues();
            cv.put(FoodStuffContract.FoodStuffEntry._ID, (int) id);
            cv.put(FoodStuffContract.FoodStuffEntry.COLUMN_NAME, cursor.getString(NAME));
            cv.put(FoodStuffContract.FoodStuffEntry.COLUMN_QUANTITY, cursor.getInt(QUANTITY) - 1);
            cv.put(FoodStuffContract.FoodStuffEntry.COLUMN_UNITS, cursor.getString(UNITS));
            try {
                mDb.delete(FoodStuffContract.FoodStuffEntry.TABLE_NAME,
                        FoodStuffContract.FoodStuffEntry._ID + "=" + id,
                        null);
            } catch (Exception e) {
                Toast.makeText(this, "delete didn't work!", Toast.LENGTH_SHORT).show();
                return false;
            }
            cursor.close();
//            ALREADY_SWITCHED = true;
            return (mDb.insert(FoodStuffContract.FoodStuffEntry.TABLE_NAME, null, cv)) != -1;
//        }
//        ALREADY_SWITCHED = false;
//        return false;
    }

    public boolean removeStuff (long id) {
        return mDb.delete(FoodStuffContract.FoodStuffEntry.TABLE_NAME, FoodStuffContract.FoodStuffEntry._ID + "=" + id,null) > 0;
    }

    private Cursor getAllResults() {
        return mDb.query(
                FoodStuffContract.FoodStuffEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FoodStuffContract.FoodStuffEntry._ID
        );
    }


    public void editActivity (View view) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(FOODSTUFF_LOADER_ID, null, this);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mData = null;
            //called when the loader first starts
            @Override
            public void onStartLoading() {
                if (mData != null) {
                    deliverResult(mData);
                } else {
                    forceLoad();
                }
            }
            //called to asynchronously load data
            //TODO this is where the mainactivity will access the Content Provider in order to populate the screen
            //instead of using a Content Provider, for now I will directly access the db
            public Cursor loadInBackground() {
                return getAllResults();
            }
            //deliverResult send the Cursor to the registered listener
            public void deliverResult(Cursor data) {
                mData = data;
                super.deliverResult(mData);
            }
        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}
