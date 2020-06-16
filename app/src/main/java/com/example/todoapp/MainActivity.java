package com.example.todoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.todoapp.Backend.ToDoHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private RecyclerView list;
    private ToDoListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final int TEMP = 1;
    private TextView personalTodoNumber;
    private TextView businessTodoNumber;
    private ToDoModel model;
    private TextView workDonePercent;
    private ToDoHelper toDoHelper;
    private Servis servis = Servis.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toDoHelper = new ToDoHelper(this);
        personalTodoNumber = findViewById(R.id.personal_value_text_id);
        businessTodoNumber = findViewById(R.id.business_value_text_id);
        workDonePercent = findViewById(R.id.work_done_id);

        list = findViewById(R.id.todo_list_id);
        layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        servis.setContext(getApplicationContext());
        adapter = new ToDoListAdapter(servis.getList());
        list.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(list.getContext(), DividerItemDecoration.VERTICAL);
        list.addItemDecoration(dividerItemDecoration);


        FloatingActionButton plusButton = findViewById(R.id.fab_id);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddToDo.class);
                startActivityForResult(intent, TEMP);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TEMP) {
            model = servis.getList().get(servis.getList().size() - 1);
            adapter.notifyItemChanged(0, model);
            if (model != null) {
                if (model.getType() == ToDoTypeAccess.BUSINESS_TYPE) {
                    String text = (Integer.parseInt(businessTodoNumber.getText().toString()) + 1)+"";
                    businessTodoNumber.setText(text);
                } else if (model.getType() == ToDoTypeAccess.PERSONAL_TYPE){
                    String text = (Integer.parseInt(personalTodoNumber.getText().toString()) + 1)+"";
                    personalTodoNumber.setText(text);
                }
            }
        }
    }
}
