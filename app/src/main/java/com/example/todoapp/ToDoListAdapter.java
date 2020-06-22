package com.example.todoapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Models.ToDoModel;
import com.example.todoapp.Services.ToDoService;

import java.util.ArrayList;
import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.MyViewHolder> {
    private List<ToDoModel> list;
    Context context;
    ToDoService toDoService;
    private RecyclerViewClickListener listener;

    public interface RecyclerViewClickListener {
        void recyclerViewListClicked(View v, int position, long id);
    }

    ToDoListAdapter(List<ToDoModel> list, Context context, ToDoService service, RecyclerViewClickListener listener) {
        this.list = list;
        this.context = context;
//        toDoService = ToDoService.getInstance();
        this.toDoService = service;
//        toDoService.setContext(context);
        this.listener = listener;
    }


    public void addItem(ToDoModel model) {
        if (list == null) {
            list = new ArrayList<>();
        }
        if (model != null) {
            this.list.add(model);
        }
        notifyItemInserted(list.size() - 1);
    }

    public void editItem(int pos, ToDoModel model) {
        if (model != null) {
            this.list.set(pos, model);
        }
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ToDoListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.todo_list_single_row, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ToDoListAdapter.MyViewHolder holder, final int position) {
        final ToDoModel model = list.get(position);
        if (model.getType() == ToDoTypeAccess.BUSINESS_TYPE) holder.type.setText(R.string.business);
        else holder.type.setText(R.string.personal);
        holder.place.setText(model.getPlace());
        holder.time.setText(model.getTime());
        holder.id = model.getID();
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("logdeletesize", "onClick:1 list size is  = "  + list.size());
                boolean isDeleted = toDoService.removeToDoDB(model.getID());
                Log.d("logdeletesize", "onClick:2 list size is  = "  + list.size());
//                if (list.size() > 0 && isDeleted) list.remove(position);
                list = toDoService.getList();
                Log.d("logdeletesize", "onClick:3 list size is  = "  + list.size());
//                notifyItemRemoved(position);

// TODO: jokel te es erku notify-eri tarberutyuny vorn a? nuyn effectn unen
                notifyDataSetChanged();
                Log.d("logdeletesize", "onClick:4 list size is  = "  + list.size());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.recyclerViewListClicked(holder.itemView, position, model.getID());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    //TODO: haskanal te vor depqum MyViewHolder@ piti liner static
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView type, title, time, place;
        ImageView icon;
        long id;  //TODO: vor id field unem stex, ed ok a?
        private Button removeButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.todo_type_id);
            place = itemView.findViewById(R.id.todo_place_id);
            time = itemView.findViewById(R.id.todo_time_id);
            icon = itemView.findViewById(R.id.todo_icon_id);
            removeButton = itemView.findViewById(R.id.remove_button_id);

        }

    }
}
