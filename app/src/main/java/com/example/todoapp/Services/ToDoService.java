package com.example.todoapp.Services;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.todoapp.Backend.ToDoHelper;
import com.example.todoapp.Models.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class ToDoService {
    private Context context;
    private static List<ToDoModel> modelList;
    private static ToDoService instance;
    //TODO: haskanal te vonc pass anel contexty, erb unenq singleton, u kara memory leak arajacni
    private ToDoHelper toDoHelper;

    public static ToDoService getInstance() {
        if (instance == null) {
            instance = new ToDoService();
        }
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public long addToDoDB(ToDoModel model) {
        if (model != null && context != null) {
            toDoHelper  = new ToDoHelper(context);
            return toDoHelper.insertData(model);
        } else return -1;
    }

    public boolean removeToDoDB(long ID) {
        if (ID > 0) {
            toDoHelper  = new ToDoHelper(context);
            return toDoHelper.removeData(ID) > 0;
        }
        return false;
    }


    public boolean updateToDoDB(long ID, ToDoModel model) {
        boolean isUpdated = false;
        if (ID > 0 && model != null) {
            toDoHelper  = new ToDoHelper(context);
            isUpdated = toDoHelper.updateData(ID, model) > 0;
        }
        return isUpdated;
    }

    public List<ToDoModel> getList() {
        toDoHelper = new ToDoHelper(context);
        Cursor cursor = toDoHelper.getAllData();
        modelList = new ArrayList<>();
        String temp = null;
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
                    temp = cursor.getString(i++);
                    if (temp.length() > 0) {
                        if (temp.contains("/") && temp.contains(":")) {
                            model.setDate(temp.substring(0, 10));
                            model.setTime(temp.substring(10));
                        } else if (temp.contains(":")) {
                            model.setTime(temp);
                        } else if (temp.contains("/")) {
                            model.setDate(temp);
                        }
                    }
                    model.setNotification(cursor.getString(i));
                    modelList.add(model);
                    i = 0;
                }
            }
        }
        return modelList;
    }
}
