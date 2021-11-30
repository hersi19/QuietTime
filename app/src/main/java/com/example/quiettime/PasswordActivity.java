package com.example.quiettime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordActivity extends AppCompatActivity {


    EditText email, current_password, new_password;
    Button save;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        //initialize edittext and button
        email = findViewById(R.id.Password_email);
        current_password = findViewById(R.id.Password_password);
        save = findViewById(R.id.Email_save);

        //handle save button
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword(email.getText().toString(), current_password.getText().toString());
            }
        });

    }

    public void changePassword(String email,String password){
        new_password = findViewById(R.id.Email_new);

        user = FirebaseAuth.getInstance().getCurrentUser();

        //get credential for re-authentication
        AuthCredential credential = EmailAuthProvider.getCredential(email,password);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                //get the user identity again
                user = FirebaseAuth.getInstance().getCurrentUser();
                user.updatePassword(new_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(PasswordActivity.this,"Password changed.",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}