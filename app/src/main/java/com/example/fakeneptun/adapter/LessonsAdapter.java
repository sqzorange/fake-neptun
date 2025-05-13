package com.example.fakeneptun.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fakeneptun.R;
import com.example.fakeneptun.model.Lesson;
import java.util.List;

public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.LessonViewHolder> {

    private List<Lesson> lessonList;

    public LessonsAdapter(List<Lesson> lessonList) {
        this.lessonList = lessonList;
    }

    // Belső ViewHolder osztály az egyes listaelemek kezeléséhez
    public static class LessonViewHolder extends RecyclerView.ViewHolder {
        public TextView lessonName;
        public TextView lessonDuration;
        public TextView lessonCapacity;

        public LessonViewHolder(View itemView) {
            super(itemView);
            lessonName = itemView.findViewById(R.id.lessonName);
            lessonDuration = itemView.findViewById(R.id.lessonDuration);
            lessonCapacity = itemView.findViewById(R.id.lessonCapacity);
        }
    }

    @Override
    public LessonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lesson_item, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LessonViewHolder holder, int position) {
        Lesson lesson = lessonList.get(position);
        holder.lessonName.setText(lesson.getName());
        // Formázott szöveg a megjelenítéshez:
        holder.lessonDuration.setText(String.format("Időtartam: %d perc", lesson.getDuration()));
        holder.lessonCapacity.setText(String.format("Férőhelyek száma: %d", lesson.getCapacity()));
    }

    @Override
    public int getItemCount() {
        return lessonList != null ? lessonList.size() : 0;
    }
    public void setLessons(List<Lesson> lessons) {
        this.lessonList = lessons;
    }
}
