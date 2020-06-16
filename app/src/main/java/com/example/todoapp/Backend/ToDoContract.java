package com.example.todoapp.Backend;

import android.provider.BaseColumns;

public final class ToDoContract {
    private ToDoContract() {}

    public static class ToDoEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
}
