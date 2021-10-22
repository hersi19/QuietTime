package com.example.quiettime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class CreateNewTask extends AppCompatActivity {
    public static final String CAT = "CreateNewTask";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_task);

        Intent intent = getIntent();
        //TextView textView = findViewById(R.id.textView);
        Log.i(CAT,"onCreate");

    }

    @Override
    protected void onStart(){
        super.onStart();

        Log.i(CAT, "onStart");
    }

    @Override
    protected void onResume(){
        super.onResume();

        Log.i(CAT, "onResume");
    }

    @Override
    protected void onPause(){
        super.onPause();

        Log.i(CAT, "onPause");
    }


    @Override
    protected void onStop(){
        super.onStop();

        Log.i(CAT, "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        Log.i(CAT, "onDestroy");
    }

}