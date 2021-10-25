package com.example.quiettime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }
    @Override
    protected void onStart(){

    }

}