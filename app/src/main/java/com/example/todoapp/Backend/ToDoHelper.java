package com.example.todoapp.Backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todoapp.Models.ToDoModel;

public class ToDoHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ToDo.db";
    public static final String TABLE_NAME = "todo_table";
    private SQLiteDatabase db;

    public ToDoHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE table " + TABLE_NAME + " (" + Columns.ID_COL_1 +
                " INTEGER PRIMARY KEY AUTOINCREMENT," + Columns.TYPE_COL_2 + " INTEGER," + Columns.TITLE_COL_3 + " TEXT," +
                Columns.PLACE_COL_4 + " TEXT," + Columns.TIME_COL_5 + " TEXT," + Columns.NOTIFICATION_COL_6 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP table IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertData(ToDoModel model) {
        db = this.getWritableDatabase();
        return db.insert(TABLE_NAME, null, tempMethod(model, null));
    }

    public Cursor getAllData() {
        db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public int removeData(long id) {
        db = this.getWritableDatabase();
        String stringId = id + "";
        return db.delete(TABLE_NAME, "ID=?", new String[]{stringId});
    }

    public int updateData(long id, ToDoModel model) {
        db = this.getWritableDatabase();
        String stringId = id + "";
        return db.update(TABLE_NAME, tempMethod(model, stringId), "ID=?", new String[]{stringId});
    }

    private ContentValues tempMethod(ToDoModel model, String id) {
        ContentValues contentValues = new ContentValues();
        if (id != null) contentValues.put(Columns.ID_COL_1, id);
        //TODO: bazayum ID hamarakalum@ 1-ic a sksvum te 0-ic?
        contentValues.put(Columns.TYPE_COL_2, model.getType());
        contentValues.put(Columns.TITLE_COL_3, model.getTitle());
        contentValues.put(Columns.PLACE_COL_4, model.getPlace());
        contentValues.put(Columns.TIME_COL_5, model.getTime());
        contentValues.put(Columns.NOTIFICATION_COL_6, model.getNotification());
        return contentValues;
    }

    private static class Columns {
        public static final String ID_COL_1 = "ID";
        public static final String TYPE_COL_2 = "TYPE";
        public static final String TITLE_COL_3 = "TITLE";
        public static final String PLACE_COL_4 = "PLACE";
        public static final String TIME_COL_5 = "TIME";
        public static final String NOTIFICATION_COL_6 = "NOTIFICATION";
    }
}
