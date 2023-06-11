package com.example.studenttrack;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class AbsenceDetailsAdapter extends RecyclerView.Adapter<AbsenceDetailsAdapter.AbsenceViewHolder> {
    ArrayList<AbsenceDetails> absenceDetailsList;
    Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener(AbsenceDetailsAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AbsenceDetailsAdapter(Context context, ArrayList<AbsenceDetails> absenceDetailsList){
        this.absenceDetailsList = absenceDetailsList;
        this.context = context;
    }


    public static class AbsenceViewHolder extends RecyclerView.ViewHolder {

        TextView status_ab;
        TextView date_ab;
        TextView subjectname_ab;
        public AbsenceViewHolder(@Nullable View itemView, AbsenceDetailsAdapter.OnItemClickListener onItemClickListener){
            super(itemView);
            status_ab= itemView.findViewById(R.id.status_ab);
            date_ab= itemView.findViewById(R.id.date_ab);
            subjectname_ab= itemView.findViewById(R.id.subjectname_ab);
            itemView.setOnClickListener(v->onItemClickListener.onClick(getAdapterPosition()));
        }

    }
    @NonNull
    @Override
    public AbsenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_absence_details, parent, false);
        return new AbsenceViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AbsenceViewHolder holder, int position) {

        holder.status_ab.setText(absenceDetailsList.get(position).getStatus());
        holder.date_ab.setText(absenceDetailsList.get(position).getDate());
        holder.subjectname_ab.setText(absenceDetailsList.get(position).getSubjectName());
    }

    @Override
    public int getItemCount() {
        return absenceDetailsList.size();
    }


}


