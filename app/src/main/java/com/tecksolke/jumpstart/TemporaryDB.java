package com.tecksolke.jumpstart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by techguy on 2/12/18.
 */

public class TemporaryDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "JumpStart.db";
    private static final String TABLE_NAME = "Jump_Users";

    public static final String COL_1 = "ID";
    private static final String COL_2 = "USERNAME";


    public TemporaryDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }


    //insert data
    public boolean insertData(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, username);
        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();

        return result != -1;
    }

    //read data
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;
    }

    //update data
    public boolean updateData(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, username);
        long result = db.update(TABLE_NAME, contentValues, "USERNAME =?", new String[]{username});
        db.close();

        return result != -1;
    }

    //delete specific data in database
    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_NAME, "ID=?", new String[]{id});
        return i;
    }

    //delete all data
    boolean deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + DATABASE_NAME);
//        db.execSQL("delete from "+TABLE_NAME);
//        db.delete(TABLE_NAME, null, null);
        return false;
    }
}
