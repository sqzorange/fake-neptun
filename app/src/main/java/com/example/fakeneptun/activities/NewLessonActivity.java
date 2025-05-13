package com.example.fakeneptun.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fakeneptun.R;
import com.example.fakeneptun.model.Lesson;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewLessonActivity extends AppCompatActivity {

    private EditText etLessonName, etDuration, etCapacity;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_lesson);

        etLessonName = findViewById(R.id.etLessonName);
        etDuration = findViewById(R.id.etDuration);
        etCapacity = findViewById(R.id.etCapacity);
        Button btnSave = findViewById(R.id.btnSave);

        db = FirebaseFirestore.getInstance();

        btnSave.setOnClickListener(view -> {
            String name = etLessonName.getText().toString().trim();
            String durationStr = etDuration.getText().toString().trim();
            String capacityStr = etCapacity.getText().toString().trim();

            if (name.isEmpty() || durationStr.isEmpty() || capacityStr.isEmpty()) {
                Toast.makeText(NewLessonActivity.this, "Minden mező kitöltése kötelező!", Toast.LENGTH_SHORT).show();
                return;
            }

            int duration, capacity;

            try {
                duration = Integer.parseInt(durationStr);
                capacity = Integer.parseInt(capacityStr);
            } catch (NumberFormatException e) {
                Toast.makeText(NewLessonActivity.this, "Érvénytelen számformátum!", Toast.LENGTH_SHORT).show();
                return;
            }

            Lesson newLesson = new Lesson();
            newLesson.setName(name);
            newLesson.setDuration(duration);
            newLesson.setCapacity(capacity);

            db.collection("lessons")
                    .add(newLesson)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(NewLessonActivity.this, "Óra felvéve!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(NewLessonActivity.this, "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }
}
