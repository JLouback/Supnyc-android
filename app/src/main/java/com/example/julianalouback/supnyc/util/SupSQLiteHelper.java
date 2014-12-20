package com.example.julianalouback.supnyc.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by moldy530 on 12/19/14.
 */
public class SupSQLiteHelper extends SQLiteOpenHelper {

    public static final String LIKES_TABLE_NAME = "Likes";
    public static final String LIKES_COLUMN_TYPE = "type";
    public static final String LIKES_COLUMN_KEY = "range_key";

    public static final String DISLIKES_TABLE_NAME = "Dislikes";
    public static final String DISLIKES_COLUMN_TYPE = "type";
    public static final String DISLIKES_COLUMN_KEY = "range_key";

    public static final String DELETE_FROM_LIKES = LIKES_COLUMN_KEY + "=?, " + LIKES_COLUMN_TYPE + "=?";
    public static final String DELETE_FROM_DISLIKES = DISLIKES_COLUMN_KEY + "=?, " + DISLIKES_COLUMN_TYPE + "=?";

    private static final String DATABASE_NAME = "userstats.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_LIKES_TABLE = "create table " +
            LIKES_TABLE_NAME + " ( "
            + LIKES_COLUMN_KEY + " text not null, "
            + LIKES_COLUMN_TYPE + " text not null, "
            + " primary key(" + LIKES_COLUMN_KEY + " , " + LIKES_COLUMN_TYPE + " ));";

    private static final String CREATE_DISLIKES_TABLE = "create table " +
            DISLIKES_TABLE_NAME + " ( "
            + DISLIKES_COLUMN_KEY + " text not null, "
            + DISLIKES_COLUMN_TYPE + " text not null, "
            + " primary key( " + DISLIKES_COLUMN_KEY + " , " + DISLIKES_COLUMN_TYPE + " ));";

    public SupSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DISLIKES_TABLE);
        db.execSQL(CREATE_LIKES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SupSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + DISLIKES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LIKES_TABLE_NAME);
        onCreate(db);
    }
}
