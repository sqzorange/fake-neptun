package com.example.fakeneptun;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fakeneptun.adapter.MenuAdapter;
import com.example.fakeneptun.model.MenuItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    //private static final String LOG_TAG = HomeActivity.class.getName();
    RecyclerView recyclerView;
    private FirebaseUser user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerView);

        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (user != null){
            db.collection("users").document(user.getUid()).get().addOnSuccessListener(document -> {
                if (document.exists()) {
                    boolean isTeacher = Boolean.TRUE.equals(document.getBoolean("isTeacher"));
                    setupMenu(isTeacher);
                }
            }).addOnFailureListener(e -> Toast.makeText(this, "Nem sikerült lekérni a felhasználót.", Toast.LENGTH_SHORT).show());
        } else {
            //Log.d(LOG_TAG, "Felhasználó nincs bejelentkezve!");
            finish();
        }
    }

    private void setupMenu(boolean isTeacher) {
        List<MenuItem> menuItems;
        if (isTeacher) {
            menuItems = Arrays.asList(
                    new MenuItem("Tantárgyak", R.drawable.ic_exam),
                    new MenuItem("Hallgatók", R.drawable.ic_students),
                    new MenuItem("Jegyek rögzítése", R.drawable.ic_grades),
                    new MenuItem("Vizsgák kezelése", R.drawable.ic_exam),
                    new MenuItem("Üzenetek ", R.drawable.ic_message)
            );
        } else {
            menuItems = Arrays.asList(
                    new MenuItem("Óráim", R.drawable.ic_schedule),
                    new MenuItem("Jegyek", R.drawable.ic_grades),
                    new MenuItem("Tárgyfelvét", R.drawable.ic_subjects),
                    new MenuItem("Vizsgák", R.drawable.ic_exam),
                    new MenuItem("Üzenetek", R.drawable.ic_message)
            );
        }

        MenuAdapter adapter = new MenuAdapter(this, menuItems);
        recyclerView.setAdapter(adapter);
    }

    public void onLogout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
