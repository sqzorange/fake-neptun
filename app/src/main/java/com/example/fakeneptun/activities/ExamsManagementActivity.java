package com.example.fakeneptun.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fakeneptun.R;

public class ExamsManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams_management);

        TextView easterEggText = findViewById(R.id.easter_egg_text);
        easterEggText.setText("Biztos szivatni akarod a hallgatókat ezzel?");

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
    }
}