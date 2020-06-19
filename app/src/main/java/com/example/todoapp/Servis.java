package com.example.todoapp;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.todoapp.Backend.ToDoHelper;

import java.util.ArrayList;
import java.util.List;

public class Servis {
    private Context context;
    private static List<ToDoModel> modelList;
    private static Servis instance;
    //TODO: haskanal te vonc pass anel contexty, erb unenq singleton, u kara memory leak arajacni
    private ToDoHelper toDoHelper;

    private Servis() {
        instance = this;
    }

    public static Servis getInstance() {
        return new Servis();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void addToDo(ToDoModel model) {
        if (modelList == null) {
            modelList = new ArrayList<>();
        }
        modelList.add(model);
    }

    public long addToDoDB(ToDoModel model) {
        if (model != null && context != null) {
            toDoHelper  = new ToDoHelper(context);
            long addResult = toDoHelper.insertData(model.getType() + "", model.getTitle(), model.getPlace(),
                    model.getTime(), model.getNotification());
            return addResult;
        } else return -1;
    }

    public boolean removeToDoDB(long ID) {
        if (ID > 0) {
            toDoHelper  = new ToDoHelper(context);
            int removeResult = toDoHelper.removeData(ID);
            return removeResult > 0;
        }
        return false;
    }

    public boolean removeToDo(long ID) {
        if (ID > 0) {
            for (ToDoModel model : modelList) {
                if (model.getID() == ID) {
                    modelList.remove(model);
                    return true;
                }
            }
        }
        return false;
    }

    public List<ToDoModel> getList() {
        toDoHelper = new ToDoHelper(context);
        Cursor cursor = toDoHelper.getAllData();
        modelList = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            int i = 0;
            if (cursor.moveToFirst()) {
                ToDoModel model;
                while (cursor.moveToNext()) {
                    model = new ToDoModel();
                    model.setID(cursor.getLong(i++));
                    model.setType(cursor.getInt(i++));
                    model.setTitle(cursor.getString(i++));
                    model.setPlace(cursor.getString(i++));
                    model.setTime(cursor.getString(i++));
                    model.setNotification(cursor.getString(i));
                    modelList.add(model);
                    i = 0;
                }
            }
        }
        return modelList;
    }
}
