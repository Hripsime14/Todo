package com.example.todoapp.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.todoapp.Models.ToDoModel;
import com.example.todoapp.R;
import com.example.todoapp.Services.ToDoService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class

AddToDoActivity extends AppCompatActivity {
    //TODO: erb vor texty erkara linum, textView chi erkarum
    public static String ID = "ID";
    private long id = -1;
    private ToDoModel model = new ToDoModel();
    private ToDoService service = ToDoService.getInstance();
    private Spinner spinner;
    private Intent intent;
    private static final int MAP_REQUEST_CODE = 1;
    private EditText titleEditField;
    private EditText placeEditField;
    private EditText dateEditField;
    private EditText timeEditField;
    private ImageButton dateDeleteButton;
    private ImageButton timeDeleteButton;
    private Button saveButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);
        service.setContext(getApplicationContext());

        intent = getIntent();
        id = intent.getLongExtra(ID, -1);
        saveButton = findViewById(R.id.save_button_id);

        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.todo_types,
                R.layout.color_spinner_layout //this shows the spinner layout when it's closed
        );
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_single_row);//and this, for the suggested items

        spinner = findViewById(R.id.type_spinner_id);
        spinner.setAdapter(spinnerAdapter);



        ImageButton backButton = findViewById(R.id.back_button_id);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleEditField = findViewById(R.id.title_edit_id);
        titleEditField.addTextChangedListener(new ToDoTextWatcher(titleEditField));

        placeEditField = findViewById(R.id.place_edit_id);
        placeEditField.setShowSoftInputOnFocus(false);
        placeEditField.setCursorVisible(false);
        placeEditField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeEditField.setEnabled(false);
                startActivityForResult(new Intent(getApplicationContext(), MapsActivity.class), MAP_REQUEST_CODE);
            }
        });
        placeEditField.addTextChangedListener(new ToDoTextWatcher(placeEditField));

        ImageButton placeDeleteButton = findViewById(R.id.delete_place_button_id);
        placeDeleteButton.setOnClickListener(new ToDoFieldDeleteOnClick(placeEditField));

        dateEditField = findViewById(R.id.date_edit_id);
        dateEditField.setShowSoftInputOnFocus(false);
        dateEditField.setCursorVisible(false);
        dateEditField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    dateEditField.setEnabled(false);
                    final Calendar calendar = Calendar.getInstance();
                    if (model.getTimeStamp() != 0) {
                        Date date = new Date(model.getTimeStamp());
                        calendar.setTime(date);
                    }
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int month = calendar.get(Calendar.MONTH);
                    int year = calendar.get(Calendar.YEAR);
                    DatePickerDialog datePicker = new DatePickerDialog(AddToDoActivity.this, R.style.DialogTheme,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    dateEditField.setError(null);
                                    dateDeleteButton.setVisibility(View.VISIBLE);
                                    String day;
                                    String month;
                                    if (dayOfMonth < 10) day = "0" + dayOfMonth;
                                    else day = dayOfMonth + "";
                                    if (++monthOfYear < 10) month = "0" + monthOfYear;
                                    else month = monthOfYear + "";
                                    String dateText = day + "/" + month + "/" + year;
                                    dateEditField.setText(dateText);
                                    dateEditField.setEnabled(true);
                                }
                            }, year, month, day);
                    datePicker.show();

                    datePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            dateEditField.setEnabled(true);
                        }
                    });
                }
                return false;
            }
        });
        dateEditField.addTextChangedListener(new ToDoTextWatcher(dateEditField));

        dateDeleteButton = findViewById(R.id.delete_date_button_id);
        dateDeleteButton.setOnClickListener(new ToDoFieldDeleteOnClick(dateEditField));

        timeEditField = findViewById(R.id.time_edit_id);
        timeEditField.setShowSoftInputOnFocus(false);
        timeEditField.setCursorVisible(false);
        timeEditField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    timeEditField.setEnabled(false);
                    Calendar calendar = Calendar.getInstance();
                    if (model.getTimeStamp() != 0) {
                        Date date = new Date(model.getTimeStamp());
                        calendar.setTime(date);
                    }
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int min = calendar.get(Calendar.MINUTE);
                    TimePickerDialog timePicker = new TimePickerDialog(AddToDoActivity.this, R.style.DialogTheme,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    timeEditField.setError(null);
                                    timeDeleteButton.setVisibility(View.VISIBLE);
                                    String hour;
                                    String min;
                                    if (hourOfDay < 10) hour = "0" + hourOfDay;
                                    else hour = hourOfDay + "";
                                    if (minute < 10) min = "0" + minute;
                                    else min = minute + "";
                                    String timeText = hour + ":" + min;
                                    timeEditField.setText(timeText);
                                    timeEditField.setEnabled(true);
                                }
                            }, hour, min, true);
                    timePicker.show();

                    timePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            timeEditField.setEnabled(true);
                        }
                    });

                }
                return false;
            }
        });
        timeEditField.addTextChangedListener(new ToDoTextWatcher(timeEditField));

        timeDeleteButton = findViewById(R.id.delete_time_button_id);
        timeDeleteButton.setOnClickListener(new ToDoFieldDeleteOnClick(timeEditField));

        EditText notificEditField = findViewById(R.id.notification_edit_id);
        notificEditField.addTextChangedListener(new ToDoTextWatcher(notificEditField));

        if (id > -1) {
            for (ToDoModel model1 : service.getList()) {
                if (model1.getID() == id) {
                    model = model1;
                    break;
                }
            }

            spinner.setSelection(model.getType());
            titleEditField.setText(model.getTitle());
            placeEditField.setText(model.getPlace());
            dateEditField.setText(getDate(model.getTimeStamp()));
            timeEditField.setText(getTime(model.getTimeStamp()));
            notificEditField.setText(model.getNotification());
        }

        if (!saveButton.isClickable()) coverEmptyCases();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coverEmptyCases()) {
                    saveType();
                    saveDateTime();
                    if (id > -1) {
                        if (service.updateToDoDB(id, model)) {
                            setResult(RESULT_OK, intent);
                        }
                    } else {
                        long addResult = service.addToDoDB(model);
                        if (addResult > 0) {
                            setResult(RESULT_OK, intent);
                        }
                        model.setID(addResult);
                    }
                    id = -1;
                    finish();
                }
            }
        });
    }

    private boolean coverEmptyCases() {
        int titleTextLength = titleEditField.getText().length();
        int dateTextLength = dateEditField.getText().length();
        int timeTextLength = timeEditField.getText().length();
        if (titleTextLength == 0) {
            saveButton.setEnabled(false);
            titleEditField.setError("Title is required");
            titleEditField.requestFocus();
        }
        if (dateTextLength == 0) {
            saveButton.setEnabled(false);
            dateEditField.setError("Date is required");
            dateEditField.requestFocus();
            dateDeleteButton.setVisibility(View.GONE);
        }
        if (timeTextLength == 0) {
            saveButton.setEnabled(false);
            timeEditField.setError("Time is required");
            timeEditField.requestFocus();
            timeDeleteButton.setVisibility(View.GONE);
        }
        if (titleTextLength > 0 && dateTextLength > 0 && timeTextLength > 0) {
            saveButton.setEnabled(true);
            return true;
        }
        return false;
    }

    private String getDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    private String getTime(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(date);
    }

    private void saveDateTime() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            String date0 = dateEditField.getText() + "";
            String [] dates = date0.split("/");
            String selectedDay = dates[0];
            String selectedMonth = dates[1];
            String selectedYear = dates[2];
            String [] times = (timeEditField.getText() + "").split(":");
            String selectedHour = times[0];
            String selectedMin = times[1];

            Date date = sdf.parse(selectedDay + "/" + selectedMonth + "/" + selectedYear
                    + " " + selectedHour + ":" + selectedMin + ":00");
            if (date != null) model.setTimeStamp(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void saveType() {
        model.setType(spinner.getSelectedItemPosition());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String extra = MapsActivity.NAME_EXTRA;
        if (requestCode == MAP_REQUEST_CODE && data != null && data.hasExtra(extra)) {
            placeEditField.setText(data.getStringExtra(extra));
        }
    }

    private class ToDoFieldDeleteOnClick implements View.OnClickListener {
        private View view;

        private ToDoFieldDeleteOnClick(View view) {
            this.view = view;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.delete_place_button_id:
                    placeEditField.setText(""); break;
                case R.id.delete_date_button_id:
                    dateEditField.setText(""); break;
                case R.id.delete_time_button_id:
                    timeEditField.setText(""); break;
            }
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
                    model.setTitle(text);
                    saveButton.setEnabled(true); break;
                case R.id.place_edit_id:
                    placeEditField.setEnabled(true);
                    model.setPlace(text); break;
                case R.id.date_edit_id:
                case R.id.time_edit_id:
                    saveButton.setEnabled(true); break;
                case R.id.notification_edit_id:
                    model.setNotification(text); break;
            }
        }
    }
}