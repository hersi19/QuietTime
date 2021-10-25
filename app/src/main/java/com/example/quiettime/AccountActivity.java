package com.example.quiettime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;

public class AccountActivity extends AppCompatActivity {

    ActivityReadDataBinding binding;
    DatabaseReference reference;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);
    }

    public void updateProfile(View view){


    }
}