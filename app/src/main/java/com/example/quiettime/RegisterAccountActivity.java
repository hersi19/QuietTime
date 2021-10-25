package com.example.quiettime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class RegisterAccountActivity extends AppCompatActivity {
    public static final String REG = "RegisterAccountActivity";
    FirebaseDatabase database;
    DatabaseReference myRef;

    //input variables
    EditText regUserName,regEmail,reg_phoneNumber,regPassword;
    Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        //assign each input
        regUserName = findViewById(R.id.reg_Username);
        regEmail = findViewById(R.id.reg_Email);
        reg_phoneNumber = findViewById(R.id.reg_Phone);
        regPassword = findViewById(R.id.reg_Password);
        create = findViewById(R.id.RegisterAccountActivity_Create);

        //handle Create button
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("users");
                String userName = regUserName.getText().toString();
                String email = regEmail.getText().toString();
                String phone = reg_phoneNumber.getText().toString();
                String password = regPassword.getText().toString();
                AccountHelper one = new AccountHelper(userName,email,phone,password);
                myRef.child(phone).setValue(one);
            }
        });
    }


}