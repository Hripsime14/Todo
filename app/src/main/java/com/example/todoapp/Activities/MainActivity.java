package com.example.todoapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.todoapp.Backend.ToDoHelper;
import com.example.todoapp.Models.ToDoModel;
import com.example.todoapp.MyJobService;
import com.example.todoapp.R;
import com.example.todoapp.Services.ToDoService;
import com.example.todoapp.Adapters.ToDoListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ToDoListAdapter adapter;
    private List<ToDoModel> sortedList;
    private static final int ADD_TODO_REQUEST_CODE = 1;
    private static final int EDIT_TODO_REQUEST_CODE = 2;
    private TextView personalTodoNumber;
    private TextView businessTodoNumber;
    private TextView workDonePercent;
    private ImageView image;
    private ConstraintLayout topBar;
    private ToDoService toDoService = ToDoService.getInstance();
    private int editedPos;
    public static final String TAG = "jobschedulertag";

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        sortedList = toDoService.getSortedList();
        scheduleJob();

        final RecyclerView list = findViewById(R.id.todo_list_id);
        list.setLayoutManager(new LinearLayoutManager(this));
        //TODO:jshtel te stex arajin argument-@ pass by reference a? u inchi adapterum aftomat update chi linum listy
        adapter = new ToDoListAdapter(/*toDoService.getSortedList()*/sortedList, toDoService, this, list,  new ToDoListAdapter.RecyclerViewClickListener() {
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

        topBar = findViewById(R.id.top_bar_id);
        image = findViewById(R.id.background_image_id);
        final int initialMaxSize = image.getLayoutParams().height;
        final DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            android.view.ViewGroup.LayoutParams imageParams = null;
            android.view.ViewGroup.LayoutParams listViewParams = null;
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                double height =  recyclerView.getHeight();
                imageParams = image.getLayoutParams();
                listViewParams = list.getLayoutParams();

                if (dy < 0) dy *= 3;
                double dyy = (imageParams.height * dy * 2) / height;

                if (imageParams.height - dyy >= initialMaxSize * 0.5 && imageParams.height -dyy <= initialMaxSize) {

                    imageParams.height -= dyy;
                    listViewParams.height = metrics.heightPixels - topBar.getHeight();
                    list.setLayoutParams(listViewParams);
                    image.setLayoutParams(imageParams);
                }
            }
        });

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

    private long checkTheClosestDate(long currentTime, List<ToDoModel> list) {
        long timeToReturn = 0;
        for (ToDoModel model: list) {
            long tempTime = model.getTimeStamp();
            if (tempTime > currentTime) {
                timeToReturn = tempTime;
                break;
            }
        }
        return Math.abs(timeToReturn - currentTime);
    }

    private void scheduleJob() {
        List<ToDoModel> sortedList = toDoService.getSortedList(); //TODO:es toxy durs chi galis, soretdList@ dinamik popoxakan chi?
        Log.d(TAG, "scheduleJob: I'm inthe scheduleJob");
        long time = sortedList.get(sortedList.size() - 1).getTimeStamp();
        long currentTime = System.currentTimeMillis();
        checkTheClosestDate(currentTime, sortedList);
        Log.d(TAG, "scheduleJob: curTime = " + time );
        Log.d(TAG, "scheduleJob: time = " + currentTime);
        Log.d(TAG, "scheduleJob: tarb = " + (currentTime - time)/1000);
        ComponentName componentName = new ComponentName(this, MyJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(12, componentName)
//                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setMinimumLatency(checkTheClosestDate(currentTime, sortedList))
                .build();

        JobScheduler jobScheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = jobScheduler.schedule(jobInfo);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled! RC = " + resultCode);
        } else {
            Log.d(TAG, "Job not scheduled RC = " + resultCode);
        }
    }

    public void cancelJob(View view) {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(12);
        Log.d(TAG, "cancelJob: job is cancelled");
    }

    /*@Override
    protected void onDestroy() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, ReminderBroadcast.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }*/

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
            scheduleJob();
        }
        if (requestCode == ADD_TODO_REQUEST_CODE) adapter.addItem();
        else if (requestCode == EDIT_TODO_REQUEST_CODE) adapter.editItem(editedPos);
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.d ("delLog", "Running");
                return true;
            }
        }
        Log.d ("delLog", "Not running");
        return false;
    }
}
