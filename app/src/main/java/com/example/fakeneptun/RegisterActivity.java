package com.example.fakeneptun;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private FirebaseAuth mAuth;

    EditText userNameET;
    EditText userEmailET;
    EditText passwordET;
    EditText passowrdConfirmET;
    EditText familyNameET;
    EditText firstNameET;
    CheckBox isTeacher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userNameET = findViewById(R.id.editTextUserName);
        userEmailET = findViewById(R.id.editEmail);
        passwordET = findViewById(R.id.editTextPassword);
        passowrdConfirmET = findViewById(R.id.passwordAgain);
        familyNameET = findViewById(R.id.familyName);
        firstNameET = findViewById(R.id.firstName);
        isTeacher = findViewById(R.id.isTeacher);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 69){
            finish();
        }

        mAuth = FirebaseAuth.getInstance();

    }

    public void onRegister(View view) {
        String userName = userNameET.getText().toString();
        String userEmail = userEmailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordConfirm = passowrdConfirmET.getText().toString();
        String familyName = familyNameET.getText().toString();
        String firstName = firstNameET.getText().toString();
        boolean teacher = isTeacher.isChecked();

        if (!password.equals(passwordConfirm)) {
            Log.e(LOG_TAG, "Nem egyezik meg a két jelszó!");
            return;
        }
        Log.i(LOG_TAG, "Regisztrált: " + userName + ", Email: " + userEmail + ", Jelszó: " + password + ", családi: " + familyName + ", keresztnév: " + firstName + ", tanár: " + teacher);
        mAuth.createUserWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d(LOG_TAG, "Felhasználó létrehozva!");
                            moveToHome();
                        } else {
                            Log.d(LOG_TAG, "Felhasználó létrehozása sikertelen: " + task.getException().getMessage());
                            Toast.makeText(RegisterActivity.this, "Felhasználó létrehozása sikertelen: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void moveToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void moveToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}