package com.example.todoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.todoapp.Backend.ToDoHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {
    private RecyclerView list;
    private ToDoListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final int ADD_INTENT = 1;
    private static final int EDIT_INTENT = 2;
    private TextView personalTodoNumber;
    private TextView businessTodoNumber;
    private ToDoModel model;
    private TextView workDonePercent;
    private ToDoHelper toDoHelper;
    private Servis servis = Servis.getInstance();
    private int pos;
    private long ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toDoHelper = new ToDoHelper(this);
        servis.setContext(getApplicationContext());
        personalTodoNumber = findViewById(R.id.personal_value_text_id);
        businessTodoNumber = findViewById(R.id.business_value_text_id);
        workDonePercent = findViewById(R.id.work_done_id);

        list = findViewById(R.id.todo_list_id);
        layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        //TODO:jshtel te stex arajin argument-@ pass by reference a?, vor krknaki a arajin TODO-n nkarum?
        Log.d("logStart", "onCreate: size = " + servis.getList());
        adapter = new ToDoListAdapter(servis.getList(), getApplicationContext(), new ToDoListAdapter.RecyclerViewClickListener() {
            @Override
            public void recyclerViewListClicked(View v, int position, long id) {
                pos = position;
                ID = id;
                Intent intent = new Intent(getApplicationContext(), AddToDo.class);
                Log.d("logtouch", "onTouchEvent: position is " + position);
                Log.d("logtouch", "onTouchEvent: id is " + id);
                intent.putExtra(AddToDo.ID, id);
                intent.putExtra(AddToDo.POSITION, position);
                startActivityForResult(intent, EDIT_INTENT);
            }
        });
        list.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(list.getContext(), DividerItemDecoration.VERTICAL);
        list.addItemDecoration(dividerItemDecoration);



        FloatingActionButton plusButton = findViewById(R.id.fab_id);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddToDo.class);
                startActivityForResult(intent, ADD_INTENT);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_INTENT) {
            Log.d("logintent", "onActivityResult: it;s in the add_intent");
            if (servis.getList().size() > 0) {
                model = servis.getList().get(servis.getList().size() - 1);
            }
            adapter.notifyDataSetChanged();
            adapter.addItem(model); //TODO: vonc a jisht notify anel, adapter-ic? te estexic?
//            adapter.notifyItemInserted(servis.getList().size() - 1);//.notifyItemChanged(servis.getList().size() - 1, model);
            if (model != null) {
                if (model.getType() == ToDoTypeAccess.BUSINESS_TYPE) {
                    String text = (Integer.parseInt(businessTodoNumber.getText().toString()) + 1)+"";
                    businessTodoNumber.setText(text);
                } else if (model.getType() == ToDoTypeAccess.PERSONAL_TYPE){
                    String text = (Integer.parseInt(personalTodoNumber.getText().toString()) + 1)+"";
                    personalTodoNumber.setText(text);
                }
            }
        } else if (requestCode == EDIT_INTENT) {
            for(ToDoModel toDoModel : servis.getList()) {
                if (toDoModel.getID() == ID) {
                    adapter.editItem(pos, toDoModel);
                }
            }
        }
    }
}
