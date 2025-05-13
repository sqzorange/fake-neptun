package com.example.fakeneptun.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fakeneptun.R;
import com.example.fakeneptun.adapter.LessonsAdapter;
import com.example.fakeneptun.model.Lesson;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class LessonsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LessonsAdapter adapter;
    private FirebaseFirestore db;
    private FloatingActionButton fabNewLesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);

        recyclerView = findViewById(R.id.recyclerViewLessons);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fabNewLesson = findViewById(R.id.fab_new_lesson);
        fabNewLesson.setOnClickListener(view -> {
            startActivity(new android.content.Intent(LessonsActivity.this, NewLessonActivity.class));
        });

        db = FirebaseFirestore.getInstance();

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users").document(currentUserId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Boolean teacherFlag = documentSnapshot.getBoolean("isTeacher");
                    boolean isTeacher = teacherFlag != null && teacherFlag;

                    adapter = new LessonsAdapter(new ArrayList<>(), isTeacher);
                    recyclerView.setAdapter(adapter);

                    loadLessons();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LessonsActivity.this, "Hiba a felhasználói adatok lekérésekor: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    adapter = new LessonsAdapter(new ArrayList<>(), false);
                    recyclerView.setAdapter(adapter);
                    loadLessons();
                });
    }

    private void loadLessons() {
        db.collection("lessons")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Lesson> lessons = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Lesson lesson = doc.toObject(Lesson.class);
                        if (lesson != null) {
                            lesson.setId(doc.getId());
                            lessons.add(lesson);
                        }
                    }
                    adapter.setLessons(lessons);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(LessonsActivity.this, "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLessons();
    }
}
