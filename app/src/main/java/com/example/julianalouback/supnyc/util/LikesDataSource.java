package com.example.julianalouback.supnyc.util;

/**
 * Created by moldy530 on 12/20/14.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.julianalouback.supnyc.Models.Event;
import com.example.julianalouback.supnyc.Models.StoredEvent;

public class LikesDataSource {

    private SQLiteDatabase database;
    private SupSQLiteHelper dbHelper;
    private String[] likesAllColumns = { SupSQLiteHelper.LIKES_COLUMN_KEY, SupSQLiteHelper.LIKES_COLUMN_TYPE };
    private String[] dislikesAllColumns ={SupSQLiteHelper.DISLIKES_COLUMN_KEY, SupSQLiteHelper.DISLIKES_COLUMN_TYPE};

    public LikesDataSource(Context ctx){
        dbHelper = new SupSQLiteHelper(ctx);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    public StoredEvent createLike(Event event){
        removeLike(new StoredEvent(event.getRangeKey(), event.getType()));
        removeDislike(new StoredEvent(event.getRangeKey(), event.getType()));

        ContentValues values = new ContentValues();
        values.put(SupSQLiteHelper.LIKES_COLUMN_KEY, event.getRangeKey());
        values.put(SupSQLiteHelper.LIKES_COLUMN_TYPE, event.getType());
        long insertId = database.insert(SupSQLiteHelper.LIKES_TABLE_NAME, null,
                values);
        Cursor cursor = database.query(SupSQLiteHelper.LIKES_TABLE_NAME,
                likesAllColumns, SupSQLiteHelper.LIKES_COLUMN_KEY + " = \'" + event.getRangeKey() + "\' AND " + SupSQLiteHelper.LIKES_COLUMN_TYPE + " = \'" + event.getType() + "\'", null,
                null, null, null);
        cursor.moveToFirst();
        StoredEvent result = cursorToEvent(cursor);
        cursor.close();
        return result;
    }
    public StoredEvent createDislike(Event event){
        removeLike(new StoredEvent(event.getRangeKey(), event.getType()));
        removeDislike(new StoredEvent(event.getRangeKey(), event.getType()));

        ContentValues values = new ContentValues();
        values.put(SupSQLiteHelper.DISLIKES_COLUMN_KEY, event.getRangeKey());
        values.put(SupSQLiteHelper.DISLIKES_COLUMN_TYPE, event.getType());
        long insertId = database.insert(SupSQLiteHelper.DISLIKES_TABLE_NAME, null,
                values);
        Cursor cursor = database.query(SupSQLiteHelper.DISLIKES_TABLE_NAME,
                dislikesAllColumns, SupSQLiteHelper.DISLIKES_COLUMN_KEY + " = \'" + event.getRangeKey() + "\' AND " + SupSQLiteHelper.DISLIKES_COLUMN_TYPE + " = \'" + event.getType() + "\'", null,
                null, null, null);
        cursor.moveToFirst();
        StoredEvent result = cursorToEvent(cursor);
        cursor.close();
        return result;
    }

    public StoredEvent createDislike(String range, String type){
        removeLike(new StoredEvent(range, type));
        removeDislike(new StoredEvent(range, type));

        ContentValues values = new ContentValues();
        values.put(SupSQLiteHelper.DISLIKES_COLUMN_KEY, range);
        values.put(SupSQLiteHelper.DISLIKES_COLUMN_TYPE, type);
        long insertId = database.insert(SupSQLiteHelper.DISLIKES_TABLE_NAME, null,
                values);
        Cursor cursor = database.query(SupSQLiteHelper.DISLIKES_TABLE_NAME,
                dislikesAllColumns, SupSQLiteHelper.DISLIKES_COLUMN_KEY + " = \'" + range + "\' AND " + SupSQLiteHelper.DISLIKES_COLUMN_TYPE + " = \'" + type + "\'", null,
                null, null, null);
        cursor.moveToFirst();
        StoredEvent result = cursorToEvent(cursor);
        cursor.close();
        return result;
    }

    public StoredEvent createLike(String range, String type){
        removeLike(new StoredEvent(range, type));
        removeDislike(new StoredEvent(range, type));

        ContentValues values = new ContentValues();
        values.put(SupSQLiteHelper.LIKES_COLUMN_KEY, range);
        values.put(SupSQLiteHelper.LIKES_COLUMN_TYPE, type);
        long insertId = database.insert(SupSQLiteHelper.LIKES_TABLE_NAME, null,
                values);
        Cursor cursor = database.query(SupSQLiteHelper.LIKES_TABLE_NAME,
                likesAllColumns, SupSQLiteHelper.LIKES_COLUMN_KEY + " = \'" + range + "\' AND " + SupSQLiteHelper.LIKES_COLUMN_TYPE + " = \'" + type + "\'", null,
                null, null, null);
        cursor.moveToFirst();
        StoredEvent result = cursorToEvent(cursor);
        cursor.close();
        return result;
    }

    public void removeLike(StoredEvent event){
        database.delete(SupSQLiteHelper.LIKES_TABLE_NAME,  SupSQLiteHelper.LIKES_COLUMN_KEY + " = \'" + event.getKey() + "\' AND " + SupSQLiteHelper.DISLIKES_COLUMN_TYPE + " = \'" + event.getType() + "\'", null);
    }

    public void removeDislike(StoredEvent event){
        database.delete(SupSQLiteHelper.DISLIKES_TABLE_NAME,  SupSQLiteHelper.DISLIKES_COLUMN_KEY + " = \'" + event.getKey() + "\' AND " + SupSQLiteHelper.DISLIKES_COLUMN_TYPE + " = \'" + event.getType() + "\'", null);
    }

    public List<StoredEvent> getAllLikes(){
        List<StoredEvent> result = new ArrayList<StoredEvent>();
        Cursor cursor = database.query(SupSQLiteHelper.LIKES_TABLE_NAME,
                likesAllColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            StoredEvent event = cursorToEvent(cursor);
            result.add(event);
            cursor.moveToNext();
        }

        cursor.close();
        return result;
    }

    public List<StoredEvent> getAllDisLikes(){
        List<StoredEvent> result = new ArrayList<StoredEvent>();
        Cursor cursor = database.query(SupSQLiteHelper.DISLIKES_TABLE_NAME,
                dislikesAllColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            StoredEvent event = cursorToEvent(cursor);
            result.add(event);
            cursor.moveToNext();
        }

        cursor.close();
        return result;
    }

    private StoredEvent cursorToEvent(Cursor cursor){
        return new StoredEvent(cursor.getString(0), cursor.getString(1));
    }
}
