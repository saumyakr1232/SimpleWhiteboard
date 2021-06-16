package com.goodapps.simplewhiteboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();


        intent = new Intent(LoginActivity.this, MainActivity.class);


    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            new Handler().postDelayed(() -> signInAnonymously(), 1000);

        } else {
            new Handler().postDelayed(() -> {
                startActivity(intent);
                // fetch images here
            }, 3000);
        }


    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(authResult -> startActivity(intent)).addOnFailureListener(e -> {
            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();


            startActivity(intent);
        });
    }
}