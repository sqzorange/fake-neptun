package com.example.fakeneptun;

import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    //private static final String LOG_TAG = RegisterActivity.class.getName();
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    EditText userNameET , userEmailET,  passwordET, passowrdConfirmET, familyNameET, firstNameET;
    CheckBox isTeacher;
    TextView successText;

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

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        successText = findViewById(R.id.successText);
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
            Toast.makeText(this, "Nem egyezik a két jelszó!", Toast.LENGTH_SHORT).show();
            //Log.e(LOG_TAG, "Nem egyezik meg a két jelszó!");
            return;
        }

        if (userName.isEmpty() || userEmail.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty() || familyName.isEmpty() || firstName.isEmpty()) {
            Toast.makeText(this, "Minden mező kitöltése kötelező!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            Toast.makeText(this, "Érvénytelen e-mail cím!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8 || !password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*") || !password.matches(".*\\d.*") || !password.matches(".*[!@#$%^&*+=?-].*")) {
            Toast.makeText(this, "A jelszónak legalább 8 karakterből kell állnia, tartalmaznia kell nagy- és kisbetűt, számot és speciális karaktert!", Toast.LENGTH_LONG).show();
            return;
        }

        //Log.i(LOG_TAG, "Regisztrált: " + userName + ", Email: " + userEmail + ", Jelszó: " + password + ", családi: " + familyName + ", keresztnév: " + firstName + ", tanár: " + teacher);
        mAuth.createUserWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()) {
                        //Log.d(LOG_TAG, "Felhasználó létrehozva!");
                        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                        Map<String, Object> user = new HashMap<>();
                        user.put("userName", userName);
                        user.put("email", userEmail);
                        user.put("familyName", familyName);
                        user.put("firstName", firstName);
                        user.put("isTeacher", teacher);
                        user.put("UUID", uid);

                        db.collection("users").document(uid)
                                .set(user)
                                .addOnSuccessListener(aVoid -> moveToHome())
                                .addOnFailureListener(e -> {
                                    //Log.w(LOG_TAG, "Hiba történt a Firestore mentés során", e);
                                    Toast.makeText(RegisterActivity.this, "Nem sikerült elmenteni az adatokat: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                });
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
                        //Log.d(LOG_TAG, "Felhasználó létrehozása sikertelen: " + Objects.requireNonNull(task.getException()).getMessage());
                        Toast.makeText(RegisterActivity.this, "Felhasználó létrehozása sikertelen: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void moveToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void moveToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}