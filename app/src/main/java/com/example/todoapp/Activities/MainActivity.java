package com.example.todoapp.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.todoapp.Backend.ToDoHelper;
import com.example.todoapp.R;
import com.example.todoapp.Services.ToDoService;
import com.example.todoapp.Adapters.ToDoListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {
    private ToDoListAdapter adapter;
    private static final int ADD_TODO_REQUEST_CODE = 1;
    private static final int EDIT_TODO_REQUEST_CODE = 2;
    private TextView personalTodoNumber;
    private TextView businessTodoNumber;
    private TextView workDonePercent;
    private ToDoService toDoService = ToDoService.getInstance();
    private int editedPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ToDoHelper(this);
        toDoService.setContext(getApplicationContext());
        personalTodoNumber = findViewById(R.id.personal_value_text_id);
        businessTodoNumber = findViewById(R.id.business_value_text_id);
        workDonePercent = findViewById(R.id.work_done_id);
        updatePBValues(); //TODO: durs chi galis vor es method-nery esqan shat texic em kanchum, karox a mi ban en chi?
        updateDoneWork();

        RecyclerView list = findViewById(R.id.todo_list_id);
        list.setLayoutManager(new LinearLayoutManager(this));
        //TODO:jshtel te stex arajin argument-@ pass by reference a? u inchi adapterum aftomat update chi linum listy
        adapter = new ToDoListAdapter(toDoService.getSortedList(), toDoService, this, list,  new ToDoListAdapter.RecyclerViewClickListener() {
            @Override
            public void recyclerViewItemClicked(View v, int position, long id) {
                editedPos = position;
                Intent intent = new Intent(getApplicationContext(), AddToDoActivity.class);
                intent.putExtra(AddToDoActivity.ID, id);
                startActivityForResult(intent, EDIT_TODO_REQUEST_CODE);
            }
        });
        list.setItemAnimator(new DefaultItemAnimator());
        list.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(list.getContext(), DividerItemDecoration.VERTICAL);
        list.addItemDecoration(dividerItemDecoration);

        adapter.setWorkDoneListener(new ToDoListAdapter.WorkDoneListener() {
            @Override
            public void onDoneWorkChanged() {
                updateDoneWork();
            }

            @Override
            public void onItemRemoved() {
                updatePBValues();
                updateDoneWork();
            }
        });

        FloatingActionButton plusButton = findViewById(R.id.fab_id);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddToDoActivity.class);
                startActivityForResult(intent, ADD_TODO_REQUEST_CODE);
            }
        });
    }

    private void updatePBValues() {
        personalTodoNumber.setText(toDoService.getPersTODO());
        businessTodoNumber.setText(toDoService.getBusTODO());
    }

    private void updateDoneWork() {
        workDonePercent.setText(toDoService.getDoneWork());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode != RESULT_OK) return;
        else {
            updatePBValues();
            updateDoneWork();
        }
        if (requestCode == ADD_TODO_REQUEST_CODE) adapter.addItem();
        else if (requestCode == EDIT_TODO_REQUEST_CODE) adapter.editItem(editedPos);
    }
}
