package com.example.fakeneptun;

import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    //private static final String LOG_TAG = MainActivity.class.getName();
    private FirebaseAuth mAuth;
    EditText usernameET, passwordET;
    TextView successText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        usernameET = findViewById(R.id.editTextUserName);
        passwordET = findViewById(R.id.editTextPassword);
        successText = findViewById(R.id.successText);

    }

    public void onLogin(View view) {
        String userName = usernameET.getText().toString();
        String password = passwordET.getText().toString();

        //Log.i(LOG_TAG, "Bejelentkezett: " + userName + ", Jelszó: " + password);

        mAuth.signInWithEmailAndPassword(userName, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                //Log.d(LOG_TAG, "Bejelentkezve!");
                successText.setVisibility(View.VISIBLE);
                successText.animate()
                        .alpha(1f)
                        .setDuration(500)
                        .withEndAction(() -> successText.animate()
                                .alpha(0f)
                                .setDuration(1500)
                                .setStartDelay(500)
                                .withEndAction(this::moveToHome)
                                .start())
                        .start();
            } else {
                //Log.d(LOG_TAG, "A bejelentkezés nem sikerült: " + Objects.requireNonNull(task.getException()).getMessage());
                Toast.makeText(MainActivity.this, "A bejelentkezés nem sikerült: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //Log.d(LOG_TAG, "Felhasználó már be van jelentkezve -> átlépés HomeActivity-re");
            moveToHome();
        }
    }

    public void onRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void moveToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}