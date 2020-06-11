package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddToDo extends AppCompatActivity {
    private ToDoModel model = new ToDoModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this,
                R.array.todo_types,
                R.layout.color_spinner_layout
        );
//        adapter.setDropDownViewResource(R.layout.spinner_songle_row);
//        TODO: haskanal te es toxy inchi a petq u hamapatasxan layout@


        final Spinner spinner = findViewById(R.id.type_spinner_id);
        spinner.setAdapter(adapter);

        EditText editField = findViewById(R.id.title_edit_id);
        editField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                model.setTitle(s.toString());
            }
        });
        editField = findViewById(R.id.place_edit_id);
        editField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                model.setPlace(s.toString());
                if (model != null)
                    Log.d("Log0", "space is " + model.getPlace() + ", and title is " + model.getTitle());
            }
        });
        editField = findViewById(R.id.time_edit_id);
        editField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                model.setTime(s.toString());
            }
        });
        editField = findViewById(R.id.notification_edit_id);
        editField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                model.setNotification(s.toString());
            }
        });

        Button saveButton = findViewById(R.id.save_button_id);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setType(spinner.getSelectedItemPosition());//.getSelectedItem().toString());
                Log.d("Log11", "onClick: p = " + spinner.getSelectedItemPosition());
                Servis.getInstance().addToDo(model );
                finish();
            }
        });

    }
}
