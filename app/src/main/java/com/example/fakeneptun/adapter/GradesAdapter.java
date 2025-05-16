package com.example.fakeneptun.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fakeneptun.R;
import com.example.fakeneptun.model.Grade;

import java.util.List;

public class GradesAdapter extends RecyclerView.Adapter<GradesAdapter.GradeViewHolder> {

    private List<Grade> gradeList;

    public GradesAdapter(List<Grade> gradeList) {
        this.gradeList = gradeList;
    }

    public void setGrades(List<Grade> grades) {
        this.gradeList = grades;
        notifyDataSetChanged();
    }

    public static class GradeViewHolder extends RecyclerView.ViewHolder {
        public TextView tvLessonName;
        public TextView tvGradeValue;

        public GradeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLessonName = itemView.findViewById(R.id.tvLessonName);
            tvGradeValue = itemView.findViewById(R.id.tvGradeValue);
        }
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_item, parent, false);
        return new GradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        Grade grade = gradeList.get(position);
        holder.tvLessonName.setText("Tantárgy: " + grade.getLesson());
        holder.tvGradeValue.setText("Jegy: " + grade.getGradeValue());
    }

    @Override
    public int getItemCount() {
        return gradeList != null ? gradeList.size() : 0;
    }
}
