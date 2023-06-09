package com.example.studenttrack;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    static ArrayList<Studentitem> studentitems;
    static Context context;

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public StudentAdapter(Context context, ArrayList<Studentitem> studentitems){
        this.studentitems = studentitems;
        this.context = context;
    }
    public static class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        TextView roll;
        TextView name;
        TextView status;
        CardView cardView;
        public StudentViewHolder(@Nullable View itemView,OnItemClickListener onItemClickListener){
            super(itemView);
            roll= itemView.findViewById(R.id.roll);
            name= itemView.findViewById(R.id.name);
            status= itemView.findViewById(R.id.status);
            cardView = itemView.findViewById(R.id.cardview);
            itemView.setOnClickListener(v->onItemClickListener.onClick(getAdapterPosition()));
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(),0,0,"Edit");
            menu.add(getAdapterPosition(),1,0,"Delete");
            menu.add(getAdapterPosition(), 2, 0, "Details");

        }
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView =LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item,parent,false);
        return new StudentViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.roll.setText(studentitems.get(position).getRoll()+"");
        holder.name.setText(studentitems.get(position).getName());
        holder.status.setText(studentitems.get(position).getStatus());
        holder.cardView.setCardBackgroundColor(getColor(position));
    }

    private int getColor(int position) {
        String status = studentitems.get(position).getStatus();
        if (status.equals("P"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context, R.color.present)));
        else if (status.equals("A"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context, R.color.absent)));

        return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context, R.color.normal)));
    }


    @Override
    public int getItemCount() {
        return studentitems.size();
    }
}
