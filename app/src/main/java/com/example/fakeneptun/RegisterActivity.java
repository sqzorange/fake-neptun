package com.example.fakeneptun;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText userNameET;
    EditText userEmailET;
    EditText passwordET;
    EditText passowrdConfirmET;
    private static final String LOG_TAG = RegisterActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Bundle bundle = getIntent().getExtras();
        //bundle.getInt("SECRET_KEY");

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 69){
            finish();
        }

    }

    public void onRegister(View view) {
        String userName = userNameET.getText().toString();
        String userEmail = userEmailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordConfirm = passowrdConfirmET.getText().toString();

        if (!password.equals(passwordConfirm)) {
            Log.e(LOG_TAG, "Nem egyezik meg a két jelszó!");
        }

        Log.i(LOG_TAG, "Regisztrált: " + userName + ", Email: " + userEmail + ", Jelszó: " + password);
    }

    public void onCancel(View view) {
    }
}