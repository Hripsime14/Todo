package com.example.todoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.MyViewHolder> {
    List<ToDoModel> list;

    ToDoListAdapter(List<ToDoModel> list) {
        this.list = list;
    }

    public void reloadList(List<ToDoModel> list) {
        this.list = list;
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
    public void onBindViewHolder(@NonNull ToDoListAdapter.MyViewHolder holder, int position) {
        final ToDoModel model = list.get(position);
        holder.type.setText(model.getType());
        holder.place.setText(model.getPlace());
        holder.time.setText(model.getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView type, title, time, place;
        ImageView icon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.todo_type_id);
            place = itemView.findViewById(R.id.todo_place_id);
            time = itemView.findViewById(R.id.todo_time_id);
            icon = itemView.findViewById(R.id.todo_icon_id);

        }

    }
}
