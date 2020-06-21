package com.example.todoapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.MyViewHolder> {
    private List<ToDoModel> list;
    Context context;
    Servis servis;
    private RecyclerViewClickListener listener;

    public interface RecyclerViewClickListener {
        void recyclerViewListClicked(View v, int position, long id);
    }

    ToDoListAdapter(List<ToDoModel> list, Context context, RecyclerViewClickListener listener) {
        this.list = list;
        this.context = context;
        servis = Servis.getInstance();
        servis.setContext(context);
        this.listener = listener;
    }


    public void addItem(ToDoModel model) {
        if (list == null) {
            list = new ArrayList<>();
        }
        if (model != null) {
            Log.d("logaddd", "addItem: adding from here, when size is  " + list.size());
            this.list.add(model);
            Log.d("logaddd", "addItem: adding from here, final size is  " + list.size());
        }
        notifyItemInserted(list.size() - 1);
    }

    public void editItem(int pos, ToDoModel model) {
        if (model != null) {
            this.list.set(pos, model);
        }
        notifyDataSetChanged();
//        notifyItemChanged(pos);
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
                servis.removeToDoDB(model.getID());
                servis.removeToDo(model.getID());
                if (list.size() > 0) list.remove(position);
//                notifyItemRemoved(position);
// TODO: jokel te es erku notify-eri tarberutyuny vorn a? nuyn effectn unen
                notifyDataSetChanged();
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
