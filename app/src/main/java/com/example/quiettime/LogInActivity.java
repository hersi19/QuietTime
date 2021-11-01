package com.example.quiettime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {


    EditText mEmail, mPassword;
    Button mLoginBtn;

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        mEmail= findViewById(R.id.loginEmail);
        mPassword=findViewById(R.id.loginPassword);
        mLoginBtn= findViewById(R.id.loginBtn);

        fAuth=FirebaseAuth.getInstance();

        FirebaseUser user = fAuth.getCurrentUser();

        //Redirect logged in users to main page
        if(fAuth.getCurrentUser() != null){

            String uid = user.getUid();
            Log.i("LoginActivity:", "Current UserId: "+uid);

        }

        mLoginBtn.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();



                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Enter your email");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Required");
                    return;
                }


                //Authenicate the user
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            Toast.makeText(LogInActivity.this, "Login is successful",Toast.LENGTH_SHORT).show();

                            FirebaseUser user = fAuth.getCurrentUser();
                            String uid = user.getUid();
                            Log.i("LoginActivity:", "Current UserId: "+uid);

                            Intent intent=new Intent(LogInActivity.this, TasksActivity.class);
//                            Intent intent=new Intent(LogInActivity.this, AccountActivity.class);
                            startActivity(intent);
                            finish();

                        }else{

                            Toast.makeText(LogInActivity.this, "Error! "+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            Log.i("Login error", task.getException().getMessage());
                        }
                    }
                });



            }

        });






    }
}