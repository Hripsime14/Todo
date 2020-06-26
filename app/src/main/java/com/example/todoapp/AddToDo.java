package com.example.todoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.todoapp.Models.ToDoModel;
import com.example.todoapp.Services.ToDoService;

import java.util.Calendar;

public class AddToDo extends AppCompatActivity {
    //TODO: erb vor texty erkara linum, textView chi erkarum
    public static String ID = "ID";
    private long id = -1;
    private ToDoModel model = new ToDoModel();
    private ToDoService service = ToDoService.getInstance();
    private Spinner spinner;
    private Intent intent;
    private static final int MAP_REQUEST_CODE = 1;
    private EditText placeEditField;

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
//        spinnerAdapter.setDropDownViewResource(R.layout.spinner_sIngle_row);
//        TODO: haskanal te es toxy inchi a petq u hamapatasxan layout@

        spinner = findViewById(R.id.type_spinner_id);
        spinner.setAdapter(spinnerAdapter);

        ImageButton placeDeleteButton = findViewById(R.id.delete_place_button_id);
        placeDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeEditField.setText("");
                model.setPlace("");
                //TODO: es iqsikic sax editTexteri mot dnel, u dranc onClick-i mej mi methodov pordzel anel
            }
        });

        ImageButton backButton = findViewById(R.id.back_button_id);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        EditText titleEditField = findViewById(R.id.title_edit_id);
        titleEditField.addTextChangedListener(new ToDoTextWatcher(titleEditField));

        placeEditField = findViewById(R.id.place_edit_id);
        placeEditField.setShowSoftInputOnFocus(false);
        placeEditField.setCursorVisible(false);
        placeEditField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), MapsActivity.class), MAP_REQUEST_CODE);
            }
        });
        placeEditField.addTextChangedListener(new ToDoTextWatcher(placeEditField));

        final EditText dateEditField = findViewById(R.id.date_edit_id);
        dateEditField.setShowSoftInputOnFocus(false);
        dateEditField.setCursorVisible(false);
        dateEditField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog picker = new DatePickerDialog(AddToDo.this, R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String day;
                                String month;
                                if (dayOfMonth < 10) day = "0" + dayOfMonth;
                                else day = dayOfMonth + "";
                                if (++monthOfYear < 10) month = "0" + monthOfYear;
                                else month = monthOfYear + "";
                                dateEditField.setText(day + "/" + month + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        dateEditField.addTextChangedListener(new ToDoTextWatcher(dateEditField));

        ImageButton dateDeleteButton = findViewById(R.id.delete_date_button_id);
        dateDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateEditField.setText("");
                model.setDate(null);
            }
        });


        final EditText timeEditField = findViewById(R.id.time_edit_id);
        timeEditField.setShowSoftInputOnFocus(false);
        timeEditField.setCursorVisible(false);
        timeEditField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddToDo.this, R.style.DialogTheme,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String hour;
                                String min;
                                if (hourOfDay < 10) hour = "0" + hourOfDay;
                                else hour = hourOfDay + "";
                                if (minute < 10) min = "0" + minute;
                                else min = minute + "";
                                timeEditField.setText(hour + ":" + min);
                            }
                        }, 0, 0, true);
                timePickerDialog.show();
            };
        });
        timeEditField.addTextChangedListener(new ToDoTextWatcher(timeEditField));

        ImageButton timeDeleteButton = findViewById(R.id.delete_time_button_id);
        timeDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeEditField.setText("");
                model.setTime(null);
            }
        });


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
            dateEditField.setText(model.getDate());
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String extra = "name";
        if (requestCode == MAP_REQUEST_CODE && data != null && data.hasExtra(extra)) {
            placeEditField.setText(data.getStringExtra(extra));
//            model.setPlace(placeEditField.getText().toString());
        }
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
                case R.id.date_edit_id:
                    model.setDate(text); break;
                case R.id.time_edit_id:
                    model.setTime(text); break;
                case R.id.notification_edit_id:
                    model.setNotification(text); break;
            }
        }
    }
}