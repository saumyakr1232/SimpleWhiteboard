package com.goodapps.simplewhiteboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);


    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            new Handler().postDelayed(this::signInAnonymously, 2000);

        } else {
            new Handler().postDelayed(() -> {
                startActivity(intent);
                // fetch images here
            }, 2000);
        }


    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(authResult -> startActivity(intent)).addOnFailureListener(e -> {
            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();


            startActivity(intent);
        });
    }
}