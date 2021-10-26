package com.example.quiettime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity {
    final String TAG = "AccountActivity";
    private ListView mListView;
    EditText mUsername, mEmail, mPhone;
    Button mUpdateBtn, mLogoutBtn, mDeleteBtn;
    FirebaseAuth fAuth;
    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);

        mUsername= findViewById(R.id.profileUsernameField);
        mEmail= findViewById(R.id.profileEmailField);
        mPhone=findViewById(R.id.editTextPhone);
        mUpdateBtn=findViewById(R.id.updateProfileBtn);
        mLogoutBtn=findViewById(R.id.loginBtn);
        mDeleteBtn=findViewById(R.id.deleteProfileBtn);
        fAuth= FirebaseAuth.getInstance();
        String userId=fAuth.getUid();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
//
//                    Log.i("AccountActivity", snapshot.getValue().toString());
//
//                }
                String emailString=dataSnapshot.child("email").getValue().toString();
                String usernameString=dataSnapshot.child("username").getValue().toString();
                String phoneNumber=dataSnapshot.child("phone").getValue().toString();
                mEmail.setText(emailString);
                mUsername.setText(usernameString);
                mPhone.setText(phoneNumber);

                Log.i("AccountActivity", dataSnapshot.child("email").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //final FirebaseUser user = fAuth.getCurrentUser();
        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.d(TAG,"User Account deleted");
                        startActivity(new Intent(AccountActivity.this,MainActivity.class));
                    }else{
                        Log.w(TAG,"Something went wrong");
                    }
                }
            });
            }
        });

    }

    public void updateProfile(View view){


        mUsername= findViewById(R.id.profileUsernameField);
        mEmail= findViewById(R.id.profileEmailField);
        mPhone=findViewById(R.id.editTextPhone);

        String email=mEmail.getText().toString().trim();
        String username=mUsername.getText().toString().trim();
        String phone=mPhone.getText().toString().trim();

        ReadWriteUserDetails writeUserDetails= new ReadWriteUserDetails(username, email,phone );

        //Extracting User reference from Database for "users"
        DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("users");

        referenceProfile.child(fAuth.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AccountActivity.this, "Updated Account",Toast.LENGTH_SHORT).show();

            }
        });

    }
    public void deleteUser(String email,String Username){

    }
    public void logout(View view){

        FirebaseAuth.getInstance().signOut();

        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}