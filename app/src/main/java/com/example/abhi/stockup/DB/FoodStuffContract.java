package com.example.abhi.stockup.DB;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Abhi on 11/21/2017.
 */

public class FoodStuffContract {

    public static final class FoodStuffEntry implements BaseColumns {
        public static final String TABLE_NAME = "stuff";

        //ID is automatically produced as the first column
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_UNITS = "units";





        /*

        It'll look kind of like this:




        stuff
        ----------------------------------------------------
        | _id |      name       |    quantity    |  units  |
        ----------------------------------------------------
        |  1  |      Milk       |        1       |  gallon |
        ----------------------------------------------------
        .
        .
        .
        ----------------------------------------------------
        |  4  |      Eggs       |        4       |  eggs   |
        ----------------------------------------------------
         */





    }

}
