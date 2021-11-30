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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterAccountActivity extends AppCompatActivity {
    public static final String REG = "RegisterAccountActivity";

    EditText mUsername, mEmail, mPhone,mPass, mConfirmPass;
    TextView mUserMsg;
    Button mRegisterBtn;

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        mUserMsg = findViewById(R.id.register_error);
        mUsername= findViewById(R.id.usernameInput);
        mEmail=findViewById(R.id.emailInput);
        mPhone = findViewById(R.id.phoneinput);
        mPass=findViewById(R.id.passInput);
        mConfirmPass=findViewById(R.id.confirmPassInput);

        mRegisterBtn= findViewById(R.id.registerBtn);

        fAuth=FirebaseAuth.getInstance();

        //Redirect logged in users to main page
        if(fAuth.getCurrentUser() != null){

            //startActivity(new Intent(getApplicationContext(), Activity.class));
            //finish();
        }

        mRegisterBtn.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v){
                mUserMsg.setText("");
                String email=mEmail.getText().toString().trim();
                String username=mUsername.getText().toString().trim();
                String phone = mPhone.getText().toString().trim();
                String pass=mPass.getText().toString().trim();
                String confirmPass=mConfirmPass.getText().toString().trim();

                if(TextUtils.isEmpty(username)){
                    mUserMsg.setText("Please enter a username");
                    mUsername.setError("Enter a username");
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    mUserMsg.setText("Please enter your email");
                    mEmail.setError("Email is required");
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    mUserMsg.setText("Please enter your phone");
                    mPhone.setError("Phone is required");
                    return;
                }

                if(TextUtils.isEmpty(pass)){
                    mUserMsg.setText("Please enter a password");
                    mPass.setError("Password is Required");
                    return;
                }

                if(TextUtils.isEmpty(confirmPass)){
                    mUserMsg.setText("Please enter password again");
                    mConfirmPass.setError("Enter password again");
                    return;
                }


                if(pass.length()<6){
                    mUserMsg.setText("Password must have 6 or more characters");
                    mPass.setError("Password must have 6 or more characters");
                    return;
                }

                if(!pass.equals(confirmPass)){
                    mUserMsg.setText("Please enter the same password");
                    mPass.setError("Enter same password");
                    mConfirmPass.setError("Enter same password");
                }


                //Start registeration process
                fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            //Save User Data into the firebase Realtime Database
                            //Temp phone number
                            ReadWriteUserDetails writeUserDetails= new ReadWriteUserDetails(username, email,phone);

                            //Extracting User reference from Database for "users"
                            DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("users");

                            referenceProfile.child(fAuth.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(RegisterAccountActivity.this, "User Created",Toast.LENGTH_SHORT).show();

                                    Intent intent=new Intent(RegisterAccountActivity.this, TasksActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });


                        }else{

                            Toast.makeText(RegisterAccountActivity.this, "Error! "+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            Log.i("Register error", task.getException().getMessage());
                        }
                    }
                });


            }

        });


    }


}