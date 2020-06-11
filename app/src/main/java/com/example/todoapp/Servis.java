package com.example.todoapp;

import java.util.ArrayList;
import java.util.List;

public class Servis {
    private static List<ToDoModel> modelList;// = new ArrayList<>();
    private ToDoModel model;
    private static Servis instance;

    private Servis() {
        instance = this;
    }

    public static Servis getInstance() {
        return new Servis();
    }

    public void addToDo(ToDoModel model) {
        if (modelList == null) {
            modelList = new ArrayList<>();
        }
        modelList.add(model);
    }

    public List<ToDoModel> getList() {
        if (modelList == null) {
            modelList = new ArrayList<>();
//            model = new ToDoModel();
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
        return modelList;
    }



}
