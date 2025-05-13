package com.example.fakeneptun.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fakeneptun.R;
import com.example.fakeneptun.adapter.LessonsAdapter;
import com.example.fakeneptun.model.Lesson;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LessonsAdapter adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        recyclerView = findViewById(R.id.recyclerViewSchedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new LessonsAdapter(new ArrayList<>(), false, true);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadEnrolledLessons();
    }

    private void loadEnrolledLessons() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("lessons")
                .whereArrayContains("enrolledStudents", currentUserId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Lesson> enrolledLessons = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Lesson lesson = doc.toObject(Lesson.class);
                        if (lesson != null) {
                            lesson.setId(doc.getId());
                            enrolledLessons.add(lesson);
                        }
                    }
                    adapter.setLessons(enrolledLessons);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(ScheduleActivity.this, "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEnrolledLessons();
    }
}
