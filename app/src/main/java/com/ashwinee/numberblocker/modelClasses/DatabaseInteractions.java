package com.ashwinee.numberblocker.modelClasses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ashwinee on 14-Nov-18.
 */

public class DatabaseInteractions extends SQLiteOpenHelper {

    private static final int TABLE_VERSION=7;
    private static final String TABLE_NAME="number_blocker";
    private static final String DB_NAME="Number_blocked";
    private static final String TABLE_BUILDER_QUERY=
            "CREATE TABLE "+TABLE_NAME+"(" +
                    "NUMBERS_BLOCKED VARCHAR(10) PRIMARY KEY NOT NULL," +
                    "FLAG INTEGER DEFAULT 1," +
                    "COUNT INTEGER AUTO INCREAMENT DEFAULT 0);";
    private static final String DELETE_TABLE_QUERY="" +
            "DROP TABLE IF EXISTS "+TABLE_NAME;

    public DatabaseInteractions(Context context){
        super(context,DB_NAME,null,TABLE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_BUILDER_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion){
            db.execSQL(DELETE_TABLE_QUERY);
            this.onCreate(db);
        }
    }
}
