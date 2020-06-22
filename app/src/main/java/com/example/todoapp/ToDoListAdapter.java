package com.example.todoapp;

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

import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.MyViewHolder> {
    private List<ToDoModel> list;
    ToDoService toDoService;
    private RecyclerViewClickListener listener;

    public interface RecyclerViewClickListener {
        void recyclerViewListClicked(View v, int position, long id);
    }

    ToDoListAdapter(List<ToDoModel> list, ToDoService service, RecyclerViewClickListener listener) {
        this.list = list;
        this.toDoService = service;
        this.listener = listener;
    }


    public void addItem() {
        if (list.add(toDoService.getList().get(toDoService.getList().size() -1))) notifyItemInserted(list.size() - 1);
    }

    public void editItem(int pos) {
        if (list.set(pos, toDoService.getList().get(pos)) != null) notifyItemChanged(pos); //TODO: tuftel em?
    }


    @NonNull
    @Override
    public ToDoListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.todo_list_single_row, parent, false);
        return new MyViewHolder(listItem);
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
                if (toDoService.removeToDoDB(model.getID())) notifyItemRemoved(position);
                list = toDoService.getList();
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
        TextView type, time, place;
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
