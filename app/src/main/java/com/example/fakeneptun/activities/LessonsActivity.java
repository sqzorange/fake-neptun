package com.example.fakeneptun.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fakeneptun.R;
import com.example.fakeneptun.adapter.LessonsAdapter;
import com.example.fakeneptun.model.Lesson;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

        // RecyclerView inicializálása
        recyclerView = findViewById(R.id.recyclerViewLessons);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LessonsAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Floating Action Button (FAB) inicializálása
        fabNewLesson = findViewById(R.id.fab_new_lesson);
        fabNewLesson.setOnClickListener(view -> {
            // Új activity indítása, ahol az új óra adatai rögzíthetők
            startActivity(new Intent(LessonsActivity.this, NewLessonActivity.class));
        });

        // Firestore példány inicializálása
        db = FirebaseFirestore.getInstance();
        loadLessons();
    }

    // Lekérdezzük az órákat (például a "lessons" kollekcióból)
    private void loadLessons() {
        db.collection("lessons")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Lesson> lessons = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Lesson lesson = doc.toObject(Lesson.class);
                        if (lesson != null) {
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
        // Ha új óra kerül felvételre a NewLessonActivity-ből, frissítsük a listát
        loadLessons();
    }
}
