package com.example.fakeneptun;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view) {
        EditText userName = findViewById(R.id.editTextUserName);
        EditText password = findViewById(R.id.editTextPassword);

        String userNameStr = userName.getText().toString();
        String passwordStr = password.getText().toString();

        Log.i("MainActivity", "Bejelentkezett: " + userNameStr + ", Jelszó: " + passwordStr);
    }
}