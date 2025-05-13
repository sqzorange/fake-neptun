package com.example.fakeneptun.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fakeneptun.R;
import com.example.fakeneptun.model.Lesson;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.LessonViewHolder> {

    private List<Lesson> lessonList;
    private boolean isTeacher;

    public LessonsAdapter(List<Lesson> lessonList, boolean isTeacher) {
        this.lessonList = lessonList;
        this.isTeacher = isTeacher;
    }

    public static class LessonViewHolder extends RecyclerView.ViewHolder {
        public TextView lessonName;
        public TextView lessonDuration;
        public TextView lessonCapacity;
        public Button btnEnroll;

        public LessonViewHolder(View itemView) {
            super(itemView);
            lessonName = itemView.findViewById(R.id.lessonName);
            lessonDuration = itemView.findViewById(R.id.lessonDuration);
            lessonCapacity = itemView.findViewById(R.id.lessonCapacity);
            btnEnroll = itemView.findViewById(R.id.btnEnroll);
        }
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lesson_item, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson lesson = lessonList.get(position);
        holder.lessonName.setText(lesson.getName());
        holder.lessonDuration.setText(String.format("Időtartam: %d perc", lesson.getDuration()));
        holder.lessonCapacity.setText(String.format("Férőhelyek száma: %d", lesson.getCapacity()));

        if (isTeacher) {
            holder.btnEnroll.setVisibility(View.GONE);
            return;
        } else {
            holder.btnEnroll.setVisibility(View.VISIBLE);
        }

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final boolean[] isEnrolledHolder = new boolean[]{
                lesson.getEnrolledStudents() != null && lesson.getEnrolledStudents().contains(currentUserId)
        };

        holder.btnEnroll.setText(isEnrolledHolder[0] ? "Leadás" : "Felvétel");

        holder.btnEnroll.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            if (isEnrolledHolder[0]) {
                db.collection("lessons").document(lesson.getId())
                        .update("enrolledStudents", FieldValue.arrayRemove(currentUserId),
                                "capacity", lesson.getCapacity() + 1)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(v.getContext(), "Kurzus leadva", Toast.LENGTH_SHORT).show();
                            if (lesson.getEnrolledStudents() != null) {
                                lesson.getEnrolledStudents().remove(currentUserId);
                            }
                            lesson.setCapacity(lesson.getCapacity() + 1);
                            holder.lessonCapacity.setText(String.format("Férőhelyek száma: %d", lesson.getCapacity()));
                            holder.btnEnroll.setText("Felvétel");
                            isEnrolledHolder[0] = false;
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(v.getContext(), "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                if (lesson.getCapacity() > 0) {
                    db.collection("lessons").document(lesson.getId())
                            .update("enrolledStudents", FieldValue.arrayUnion(currentUserId),
                                    "capacity", lesson.getCapacity() - 1)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(v.getContext(), "Kurzus felvéve", Toast.LENGTH_SHORT).show();
                                if (lesson.getEnrolledStudents() != null) {
                                    lesson.getEnrolledStudents().add(currentUserId);
                                }
                                lesson.setCapacity(lesson.getCapacity() - 1);
                                holder.lessonCapacity.setText(String.format("Férőhelyek száma: %d", lesson.getCapacity()));
                                holder.btnEnroll.setText("Leadás");
                                isEnrolledHolder[0] = true;
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(v.getContext(), "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(v.getContext(), "Nincs elérhető férőhely!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lessonList != null ? lessonList.size() : 0;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessonList = lessons;
    }
}
