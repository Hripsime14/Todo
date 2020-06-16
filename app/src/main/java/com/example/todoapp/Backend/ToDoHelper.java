package com.example.todoapp.Backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

public class ToDoHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ToDo.db";
    public static final String TABLE_NAME = "todo_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "TYPE";
    public static final String COL_3 = "TITLE";
    public static final String COL_4 = "PLACE";
    public static final String COL_5 = "TIME";
    public static final String COL_6 = "NOTIFICATION";
    private SQLiteDatabase db;

    public ToDoHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE table " + TABLE_NAME + " (" + COL_1 +
                " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2 + " INTEGER," + COL_3 + " TEXT," +
                COL_4 + " TEXT," + COL_5 + " TEXT," + COL_6 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP table IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String type, String title, String place, String time, String notification) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, type);
        contentValues.put(COL_3, title);
        contentValues.put(COL_4, place);
        contentValues.put(COL_5, time);
        contentValues.put(COL_6, notification);
        long isInserted = db.insert(TABLE_NAME, null, contentValues);
        return isInserted > 0;
    }

    public Cursor getAllData() {
        db = getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
