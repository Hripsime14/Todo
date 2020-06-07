package com.example.todoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private RecyclerView list;
    private ToDoListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ToDoModel model;
    private List<ToDoModel> modelList = new ArrayList<>();
    private static final int TEMP = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.todo_list_id);
        layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        model = new ToDoModel();
        model.setType("Personal");
        model.setTitle("Shopping");
        model.setPlace("NYC");
        model.setTime("9am");
        model.setNotification("There is no notification yet");
        modelList.add(model);
        modelList.add(model);
        modelList.add(model);
        adapter = new ToDoListAdapter(modelList);
        list.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(list.getContext(), DividerItemDecoration.VERTICAL);
        list.addItemDecoration(dividerItemDecoration);


        FloatingActionButton plusButton = findViewById(R.id.fab_id);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddToDo.class);
                startActivity(intent);

            }
        });


        final AddToDo addToDo = AddToDo.getInstance();
        if (addToDo != null)
            addToDo.setModelListener(new AddToDo.ModelListener() {

                @Override
                public void onModelCreated(ToDoModel model) {
                    Log.d("Log", "onModelCreated: " + model);
                    modelList.set(0, model);
                    adapter.notifyItemChanged(0, model);
                }
            });
    }
}
