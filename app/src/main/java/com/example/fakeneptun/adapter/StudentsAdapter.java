package com.example.fakeneptun.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fakeneptun.R;
import com.example.fakeneptun.model.Student;
import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.StudentViewHolder> {

    private List<Student> studentList;

    public StudentsAdapter(List<Student> studentList) {
        this.studentList = studentList;
    }
    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        public TextView studentName;
        public TextView studentEmail;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.studentName);
            studentEmail = itemView.findViewById(R.id.studentEmail);
        }
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.studentName.setText(String.format("%s %s", student.getFamilyName(), student.getFirstName()));
        holder.studentEmail.setText(student.getEmail());
    }

    @Override
    public int getItemCount() {
        return studentList != null ? studentList.size() : 0;
    }

    public void setStudents(List<Student> students) {
        this.studentList = students;
    }
}
