package com.example.todoapp;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.todoapp.Backend.ToDoHelper;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class Servis {
    private Context context;
    private static List<ToDoModel> modelList;// = new ArrayList<>();
    private static Servis instance;
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

    public boolean addToDoDB(ToDoModel model) {
        if (model != null && context != null) {
            toDoHelper  = new ToDoHelper(context);
            return toDoHelper.insertData(model.getType() + "", model.getTitle(), model.getPlace(),
                     model.getTime(), model.getNotification());
        } else return false;
    }

    public String getAllData() {
        toDoHelper  = new ToDoHelper(context);
        Cursor cursor = toDoHelper.getAllData();
        StringBuffer buffReader = new StringBuffer();
        if (cursor.getCount() > 0)
        while (cursor.moveToNext()) {
            buffReader.append("Type: " + cursor.getString(1) + "\nTitle: " +
                    cursor.getString(2) + "\nPlace : " + cursor.getString(3) +
                    "\nTime : " + cursor.getString(4) + "\nNotification : " + cursor.getString(5));
        }
        return buffReader.toString();
    }

    public List<ToDoModel> getList() {
        toDoHelper = new ToDoHelper(context);
        Cursor cursor = toDoHelper.getAllData();
//        if (modelList == null) {
            modelList = new ArrayList<>();
//        }
        if (cursor != null && cursor.getCount() > 0) {
            int i = 1;
            if (cursor.moveToFirst()) {
                ToDoModel model;
                while (cursor.moveToNext()) {
                    model = new ToDoModel();
                    model.setType(cursor.getInt(i++));
                    model.setTitle(cursor.getString(i++));
                    model.setPlace(cursor.getString(i++));
                    model.setTime(cursor.getString(i++));
                    model.setNotification(cursor.getString(i++));
                    Log.d("loglog", "getList: model = " + model.toString());
                    modelList.add(model);
                    i = 1;
                }
            }

        }

//        model.setType("Personal");
//        model.setTitle("Shopping");
//        model.setPlace("NYC");
//        model.setTime("9am");
//        model.setNotification("There is no notification yet");
//        modelList = new ArrayList<>();
//        modelList.add(model);
//        modelList.add(model);
//        modelList.add(model);

        for (ToDoModel model : modelList) {
            Log.d("LoglOg", "onCreate: model = " + model.toString());
        }
        return modelList;
    }



}
