package com.startup.smartnoteapp.adapter;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.startup.smartnoteapp.room_db.Note;
import com.startup.smartnoteapp.EditorActivity;
import com.startup.smartnoteapp.R;

import java.util.List;


public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder> {


    List<Note> list;
    Context context;
    int position;

    private void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }


    public AdapterClass(Context context, List<Note> list) {
        this.list = list;

        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.design, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final Note noteModel = list.get(position);

        holder.textView.setText(noteModel.getText());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });


        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditorActivity.class);
                intent.putExtra("UPDATE", noteModel.getId());
                context.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView btnUpdate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.desgn_tv);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
        }

    }
}