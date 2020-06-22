package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.todoapp.Models.ToDoModel;
import com.example.todoapp.Services.ToDoService;

public class AddToDo extends AppCompatActivity {
    public static String ID = "ID";
    private long id = -1;
    private ToDoModel model = new ToDoModel();
    private ToDoService service = ToDoService.getInstance();
    private Spinner spinner;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);
        service.setContext(getApplicationContext());

        intent = getIntent();
        id = intent.getLongExtra(ID, -1);

        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.todo_types,
                R.layout.color_spinner_layout
        );
//        spinnerAdapter.setDropDownViewResource(R.layout.spinner_songle_row);
//        TODO: haskanal te es toxy inchi a petq u hamapatasxan layout@

        spinner = findViewById(R.id.type_spinner_id);
        spinner.setAdapter(spinnerAdapter);


        ImageButton backButton = findViewById(R.id.back_button_id);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        EditText titleEditField = findViewById(R.id.title_edit_id);
        titleEditField.addTextChangedListener(new ToDoTextWatcher(titleEditField));

        EditText placeEditField = findViewById(R.id.place_edit_id);
        placeEditField.addTextChangedListener(new ToDoTextWatcher(placeEditField));

        EditText timeEditField = findViewById(R.id.time_edit_id);
        timeEditField.addTextChangedListener(new ToDoTextWatcher(timeEditField));

        EditText notificEditField = findViewById(R.id.notification_edit_id);
        notificEditField.addTextChangedListener(new ToDoTextWatcher(notificEditField));

        if (id > -1) {
            for (ToDoModel model1 : service.getList())  {
                if (model1.getID() == id)  {
                    model = model1;
                    break;
                }
            }

            spinner.setSelection(model.getType());
            titleEditField.setText(model.getTitle());
            placeEditField.setText(model.getPlace());
            timeEditField.setText(model.getTime());
            notificEditField.setText(model.getNotification());
        }

        Button saveButton = findViewById(R.id.save_button_id);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setType(spinner.getSelectedItemPosition());
                if (id > -1) {
                    if (service.updateToDoDB(id, model)) {
                        intent.putExtra("result", "done");
                        setResult(Activity.RESULT_OK, intent);
                    }
                } else {
                    long addResult = service.addToDoDB(model);
                    if (addResult > 0) {
                        intent.putExtra("result", "done");
                        setResult(Activity.RESULT_OK, intent);
                    }
                    model.setID(addResult);
                }
                id = -1;
                finish();
            }
        });
    }

    private class ToDoTextWatcher implements TextWatcher {
        private View view;

        private ToDoTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            switch (view.getId()) {
                case R.id.title_edit_id:
                    model.setTitle(text); break;
                case R.id.place_edit_id:
                    model.setPlace(text); break;
                case R.id.time_edit_id:
                    model.setTime(text); break;
                case R.id.notification_edit_id:
                    model.setNotification(text); break;
            }
        }
    }
}