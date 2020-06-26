package com.example.todoapp.Models;

public class ToDoModel {
    private long ID;
    private int type;
    private String title;
    private String place;
    private String date;
    private String time;
    private String notification;

    public long getID() {
        return ID;
    }

    public void setID(long id) {
        ID = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    @Override
    public String toString() {
        return "ToDoModel{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", place='" + place + '\'' +
                ", time='" + time + '\'' +
                ", notification='" + notification + '\'' +
                '}';
    }
}
