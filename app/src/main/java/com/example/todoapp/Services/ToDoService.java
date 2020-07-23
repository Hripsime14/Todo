package com.example.todoapp.Services;

import android.content.Context;
import android.database.Cursor;
import com.example.todoapp.Backend.ToDoHelper;
import com.example.todoapp.Models.ToDoModel;
import com.example.todoapp.ToDoTypeAccess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ToDoService {
    private Context context;
    private static ToDoService instance;
    //TODO: haskanal te vonc pass anel contexty, erb unenq singleton, u kara memory leak arajacni
    private ToDoHelper toDoHelper;
    private int persTODO = 0, busTODO = 0, doneWork = 0;

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
        List<ToDoModel> modelList = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            int i = 0;
            if (cursor.moveToFirst()) {
                ToDoModel model;
                do {
                    model = new ToDoModel();
                    model.setID(cursor.getLong(i++));
                    model.setType(cursor.getInt(i++));
                    model.setTitle(cursor.getString(i++));
                    model.setPlace(cursor.getString(i++));
                    model.setTimeStamp(cursor.getLong(i++));
                    model.setNotification(cursor.getString(i++));
                    if (cursor.getInt(i) == 1)
                    model.setDone(true); else model.setDone(false);
                    modelList.add(model);
                    i = 0;
                } while (cursor.moveToNext());
            }
        }
        return modelList;
    }

    public List<ToDoModel> getSortedList() {
        List<ToDoModel> sortedList = getList();
        Collections.sort(sortedList, new SortByTime());
        return sortedList;
    }

    private void countPB() {
        busTODO = 0; persTODO = 0;
        for (ToDoModel model : getList()) {
            if (model.getType() == ToDoTypeAccess. BUSINESS_TYPE) busTODO++;
            else persTODO ++;
        }
    }

    private int countDoneWork() {
        doneWork = 0;
        for (ToDoModel model : getList()) {
            if (model.isDone()) doneWork ++;
        }
        if (getList().size() > 0) return 100 * doneWork / getList().size();
        else return 0;
    }

    public String getDoneWork() {
        return countDoneWork() + "% done";
    }

    public String getPersTODO() {
        countPB();
        return persTODO + "";
    }

    public String getBusTODO() {
        countPB();
        return busTODO + "";
    }

    private static class SortByTime implements Comparator<ToDoModel> {

        @Override
        public int compare(ToDoModel model1, ToDoModel model2) {
            return (int)(model1.getTimeStamp()/1000 - model2.getTimeStamp()/1000);
        }
    }
}

