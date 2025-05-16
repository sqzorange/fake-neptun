package com.example.fakeneptun.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fakeneptun.R;
import com.example.fakeneptun.model.Grade;
import com.example.fakeneptun.model.Lesson;
import com.example.fakeneptun.model.Student;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GradesEntryActivity extends AppCompatActivity {

    private Spinner spinnerStudents, spinnerLessons;
    private RadioGroup radioGroupGrades;
    private Button btnSaveGrade;
    private FirebaseFirestore db;

    private List<Student> studentObjects = new ArrayList<>();
    private List<String> studentIds = new ArrayList<>();
    private List<Lesson> lessonObjects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades_entry);

        spinnerStudents = findViewById(R.id.spinnerStudents);
        spinnerLessons = findViewById(R.id.spinnerLessons);
        radioGroupGrades = findViewById(R.id.radioGroupGrades);
        btnSaveGrade = findViewById(R.id.btnSaveGrade);
        db = FirebaseFirestore.getInstance();
        loadStudentsIntoSpinner();
        spinnerStudents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < studentIds.size()) {
                    String selectedStudentUid = studentIds.get(position);
                    loadLessonsForStudent(selectedStudentUid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                lessonObjects.clear();
                ArrayAdapter<String> lessonAdapter = new ArrayAdapter<>(GradesEntryActivity.this,
                        android.R.layout.simple_spinner_item, new ArrayList<>());
                lessonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerLessons.setAdapter(lessonAdapter);
            }
        });

        btnSaveGrade.setOnClickListener(v -> {
            int selectedStudentIndex = spinnerStudents.getSelectedItemPosition();
            int selectedLessonIndex = spinnerLessons.getSelectedItemPosition();

            if (selectedStudentIndex < 0 || selectedStudentIndex >= studentIds.size() ||
                    selectedLessonIndex < 0 || selectedLessonIndex >= lessonObjects.size()) {
                Toast.makeText(GradesEntryActivity.this, "Válassz diákot és tantárgyat!", Toast.LENGTH_SHORT).show();
                return;
            }

            Lesson selectedLesson = lessonObjects.get(selectedLessonIndex);
            String studentUid = studentIds.get(selectedStudentIndex);
            int selectedRadioId = radioGroupGrades.getCheckedRadioButtonId();
            if (selectedRadioId == -1) {
                Toast.makeText(GradesEntryActivity.this, "Válassz jegyet!", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton selectedRadio = findViewById(selectedRadioId);
            String gradeStr = selectedRadio.getText().toString();
            double gradeValue;
            try {
                gradeValue = Double.parseDouble(gradeStr);
            } catch (NumberFormatException e) {
                Toast.makeText(GradesEntryActivity.this, "Érvénytelen jegy!", Toast.LENGTH_SHORT).show();
                return;
            }
            Grade grade = new Grade(selectedLesson.getName(), studentUid, gradeValue);
            db.collection("grades")
                    .add(grade)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(GradesEntryActivity.this, "Jegy mentve!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(GradesEntryActivity.this, "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }
    private void loadStudentsIntoSpinner() {
        List<String> studentNames = new ArrayList<>();
        db.collection("users")
                .whereEqualTo("isTeacher", false)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    studentObjects.clear();
                    studentIds.clear();
                    studentNames.clear();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Student student = doc.toObject(Student.class);
                        if (student != null) {
                            studentObjects.add(student);
                            studentIds.add(doc.getId());
                            studentNames.add(student.getFamilyName() + " " + student.getFirstName());
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(GradesEntryActivity.this,
                            android.R.layout.simple_spinner_item, studentNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerStudents.setAdapter(adapter);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(GradesEntryActivity.this, "Hiba a diákok betöltésekor: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void loadLessonsForStudent(String studentUid) {
        List<String> lessonNames = new ArrayList<>();
        db.collection("lessons")
                .whereArrayContains("enrolledStudents", studentUid)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    lessonObjects.clear();
                    lessonNames.clear();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Lesson lesson = doc.toObject(Lesson.class);
                        if (lesson != null) {
                            lessonNames.add(lesson.getName());
                            lessonObjects.add(lesson);
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(GradesEntryActivity.this,
                            android.R.layout.simple_spinner_item, lessonNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLessons.setAdapter(adapter);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(GradesEntryActivity.this, "Hiba a tantárgyak betöltésekor: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
