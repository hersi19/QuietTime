package com.example.quiettime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    TextView userName;
    Button email,password,phone,delete_account,logout_button;
    FirebaseAuth fAuth;
    BottomNavigationView bottom_nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userName= findViewById(R.id.profile_username);
        email= findViewById(R.id.profile_email);
        password= findViewById(R.id.profile_password);
        phone= findViewById(R.id.profile_phone);
        logout_button= findViewById(R.id.profile_logout);
        fAuth= FirebaseAuth.getInstance();
        String userId = fAuth.getUid();


        //access to realtime database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(userId);

        //greet user with their user name
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String user_name=snapshot.child("username").getValue().toString().trim();
                userName.setText(user_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //for email button
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),EmailActivity.class));
                Log.i("Profile Activity:","transferred to email update page");
            }
        });

        //for password button
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),PasswordActivity.class));
                Log.i("Profile Activity:","transferred to password update page");
            }
        });

        //for phone button
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),PhoneActivity.class));
                Log.i("Profile Activity:","transferred to phone number page");
            }
        });

        //handle logout button
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(view);
            }
        });

        //handle bottom navigation bar
        bottom_nav = findViewById(R.id.bottom_navigation);
        bottom_nav.setSelectedItemId(R.id.navigation_task);
        bottom_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.navigation_task:
                        startActivity(new Intent(getApplicationContext(),TasksActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    //case R.id.navigation_maps:
                    //startActivity(new Intent(getApplicationContext(),Maps.class));
                    //overridePendingTransition(0,0);
                    // return true;
                    case R.id.navigation_profile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
    }


    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }




}