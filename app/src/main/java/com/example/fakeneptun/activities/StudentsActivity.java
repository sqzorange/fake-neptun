package com.example.fakeneptun.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fakeneptun.R;
import com.example.fakeneptun.adapter.StudentsAdapter;
import com.example.fakeneptun.model.Student;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class StudentsActivity extends AppCompatActivity {

    private StudentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        RecyclerView recyclerView = findViewById(R.id.recyclerViewStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new StudentsAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);


        db.collection("users")
                .whereEqualTo("isTeacher", false)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Student> students = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Student student = doc.toObject(Student.class);
                        students.add(student);
                    }
                    adapter.setStudents(students);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(StudentsActivity.this, "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}