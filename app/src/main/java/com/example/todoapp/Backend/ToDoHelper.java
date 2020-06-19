package com.example.todoapp.Backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ToDoHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ToDo.db";
    public static final String TABLE_NAME = "todo_table";
    public static final String ID_COL_1 = "ID";
    public static final String TYPE_COL_2 = "TYPE";
    public static final String TITLE_COL_3 = "TITLE";
    public static final String PLACE_COL_4 = "PLACE";
    public static final String TIME_COL_5 = "TIME";
    public static final String NOTIFICATION_COL_6 = "NOTIFICATION";
    private SQLiteDatabase db;

    public ToDoHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE table " + TABLE_NAME + " (" + ID_COL_1 +
                " INTEGER PRIMARY KEY AUTOINCREMENT," + TYPE_COL_2 + " INTEGER," + TITLE_COL_3 + " TEXT," +
                PLACE_COL_4 + " TEXT," + TIME_COL_5 + " TEXT," + NOTIFICATION_COL_6 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP table IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertData(String type, String title, String place, String time, String notification) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TYPE_COL_2, type);
        contentValues.put(TITLE_COL_3, title);
        contentValues.put(PLACE_COL_4, place);
        contentValues.put(TIME_COL_5, time);
        contentValues.put(NOTIFICATION_COL_6, notification);
        long insertResult = db.insert(TABLE_NAME, null, contentValues);
        return insertResult;
    }

    public Cursor getAllData() {
        db = getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public int removeData(long id) {
        db = this.getWritableDatabase();
        String temp = id + "";
        int deleteResult = db.delete(TABLE_NAME, "ID=?", new String[]{temp});
        return deleteResult;
    }
}
