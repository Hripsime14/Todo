package com.example.todoapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Models.ToDoModel;
import com.example.todoapp.R;
import com.example.todoapp.Services.ToDoService;
import com.example.todoapp.ToDoTypeAccess;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.MyViewHolder> {
    private List<ToDoModel> list;
    ToDoService toDoService;
    private RecyclerViewClickListener listener;
    private WorkDoneListener workDoneListener;
    private Context context;
    private final SparseBooleanArray array = new SparseBooleanArray();

    public interface RecyclerViewClickListener {
        void recyclerViewItemClicked(View v, int position, long id);
    }

    public interface WorkDoneListener {
        void onDoneWorkChanged();
        void onItemRemoved();
    }

    public void setWorkDoneListener(WorkDoneListener listener) {
        this.workDoneListener = listener;
    }


    public ToDoListAdapter(List<ToDoModel> list, ToDoService service, Context context, RecyclerView recyclerView, RecyclerViewClickListener listener) {
        super();
        this.list = list;
        this.toDoService = service;
        this.context = context;
        this.listener = listener;
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        simpleCallback.getSwipeEscapeVelocity(0.25f);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void addItem() {
//        Log.d("finalTag", "addItem: size = " + toDoService.getList().size());
//        ToDoModel model = toDoService.getList().get(toDoService.getList().size() - 1);
//        if (list.add(/*toDoService.getList().get(toDoService.getList().size() -1)*/model)) {
            list = toDoService.getSortedList();
            notifyDataSetChanged();
//        }
    }

    public void editItem(int pos) {
        if (list.set(pos, toDoService.getList().get(pos)) != null) {// notifyItemChanged(pos); //TODO: tuftel em?
            list = toDoService.getSortedList();
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ToDoListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("finalTAGTAG", "onCreateViewHolder: notify");
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.todo_list_single_row, parent, false);//converting xml to View object
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final ToDoListAdapter.MyViewHolder holder, final int position) {
        Log.d("finalTAGTAG", "onBindViewHolder: notify");
        final ToDoModel model = list.get(position);
        if (model.getType() == ToDoTypeAccess.BUSINESS_TYPE) holder.type.setText(R.string.business);
        else holder.type.setText(R.string.personal);
        holder.title.setText(model.getTitle());
        holder.date_and_time.setText(getDateTime(model.getTimeStamp()));
        holder.id = model.getID();
        holder.isDone = model.isDone();
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toDoService.removeToDoDB(model.getID())) {
                    list = toDoService.getSortedList();
                    if (workDoneListener != null) {
                        workDoneListener.onItemRemoved();
                    }
                    notifyItemRemoved(position);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.recyclerViewItemClicked(holder.itemView, position, model.getID());
                }
            }
        });
        setItemColor(holder, model.isDone());
    }

    public String getDateTime (long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(date);


//
//        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneOffset.UTC);
//        TemporalAccessor date = fmt.parse(dateString);
//        Instant time = Instant.from(date);
//
//        DateTimeFormatter fmtOut = DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneOffset.UTC);
//        return fmtOut.format(time);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    //TODO: haskanal te vor depqum MyViewHolder@ piti liner static
    //TODO: mekel haskanal te ViewHolder@ inchqan erevum a ekrani vra item, edqani hamar kanchum a che findViewById,
    //TODO: u menak het scroll-i jamanak enq cahce-avorman effecty tesnum (u hamel ed chi hakasum recycle-ingin)
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView type, date_and_time, title;
        ImageView icon;
        long id;//TODO: vor id field unem stex, ed ok a?
        boolean isDone;
        private Button removeButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.todo_type_id);
            title = itemView.findViewById(R.id.todo_title_id);
            date_and_time = itemView.findViewById(R.id.todo_time_id);
            icon = itemView.findViewById(R.id.todo_icon_id);
            removeButton = itemView.findViewById(R.id.remove_button_id);
        }
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public float getSwipeVelocityThreshold(float defaultValue) {
            return super.getSwipeVelocityThreshold(defaultValue*1000);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

//        @Override
//        public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
//            return 1000f;
//        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX / 6, dY, actionState, isCurrentlyActive);
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            simpleCallback.getSwipeThreshold(viewHolder);
            int position = viewHolder.getAdapterPosition();
            ToDoModel model = list.get(position);
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    model.setDone(true);
                    break;
                case ItemTouchHelper.RIGHT:
                    model.setDone(false);
                    break;
            }
            if (toDoService.updateToDoDB(model.getID(), model)) {
                list.set(position, model);
                setItemColor(viewHolder, model.isDone());
            }
            if (workDoneListener != null) {
                workDoneListener.onDoneWorkChanged();
            }
            notifyDataSetChanged();
        }
    };

    private void setItemColor(RecyclerView.ViewHolder viewHolder, boolean isDone) {
        if (isDone) viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightGrey));
        else viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
    }
}
