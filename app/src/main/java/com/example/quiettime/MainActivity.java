package com.example.quiettime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private Button logIn,register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //handle login button
        logIn = findViewById(R.id.MainPage_Login);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogInActivity();
            }
        });

        //handle register button
        register = findViewById(R.id.MainPage_Register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegisterActivity();
            }
        });
        Log.i(TAG, "onCreate");


    }

    @Override
    protected void onStart(){
        super.onStart();

        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume(){
        super.onResume();

        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause(){
        super.onPause();

        Log.i(TAG, "onPause");
    }


    @Override
    protected void onStop(){
        super.onStop();

        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        Log.i(TAG, "onDestroy");
    }

    @Override
    protected void onRestart(){
        super.onRestart();

        Log.i(TAG, "onRestart");
    }

    public void openLogInActivity(){
        Intent intent1 = new Intent(this,LogInActivity.class);
        startActivity(intent1);
    }

    public void openRegisterActivity(){
        Intent intent2 = new Intent(this,RegisterAccountActivity.class);
        startActivity(intent2);
    }


}