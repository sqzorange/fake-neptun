package com.example.fakeneptun.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fakeneptun.adapter.NotificationHelper;
import com.example.fakeneptun.R;
import com.example.fakeneptun.adapter.GradesAdapter;
import com.example.fakeneptun.model.Grade;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class GradesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GradesAdapter adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        recyclerView = findViewById(R.id.recyclerViewGrades);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GradesAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadGrades();
        listenForGradeChanges();
    }

    private void loadGrades() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("grades")
                .whereEqualTo("studentId", currentUserId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Grade> grades = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Grade grade = doc.toObject(Grade.class);
                        if (grade != null) {
                            grade.setId(doc.getId());
                            grades.add(grade);
                        }
                    }
                    adapter.setGrades(grades);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(GradesActivity.this, "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGrades();
    }

    private void listenForGradeChanges() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("grades")
                .whereEqualTo("studentId", currentUserId)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Toast.makeText(GradesActivity.this, "Hiba a jegyek figyelésekor!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (snapshot != null && !snapshot.isEmpty()) {
                        for (DocumentChange dc : snapshot.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                Grade newGrade = dc.getDocument().toObject(Grade.class);
                                NotificationHelper.sendGradeNotification(this, "Új jegybeírás történt!");
                            }
                        }
                    }
                });
    }
}
