package com.example.quiettime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class TasksActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    BottomNavigationView bottom_nav;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String curUserID;
    private String key = "";
    private String task;
    private String description;
    private String duration;
    private boolean isComplete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        toolbar = findViewById(R.id.tasks_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tasks");

        recyclerView = findViewById(R.id.tasksRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.tasksRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        curUserID = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("tasks").child(curUserID);

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }
        });


        //handle bottom navigation bar
        bottom_nav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottom_nav.setSelectedItemId(R.id.navigation_task);

        bottom_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.navigation_task:
                        startActivity(new Intent(getApplicationContext(),TasksActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_maps:
                        startActivity(new Intent(getApplicationContext(),PlaceFinder.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_profile:
                        startActivity(new Intent(getApplicationContext(),AccountActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
    }

    private void addTask(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View view = inflater.inflate(R.layout.task_input, null);
        myDialog.setView(view);

        AlertDialog dialog = myDialog.create();
        dialog.setCancelable(true);

        EditText task = view.findViewById(R.id.task_input);
        EditText description = view.findViewById(R.id.description_input);
        EditText duration = view.findViewById(R.id.duration_input);
        Button cancel = view.findViewById(R.id.task_cancel_button);
        Button save = view.findViewById(R.id.task_save_button);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mTask = task.getText().toString();
                String mDescription = description.getText().toString();
                String mDuration = duration.getText().toString();
                String id = reference.push().getKey();

                if(TextUtils.isEmpty(mTask)){
                    task.setError("Task Required");
                    Toast.makeText(TasksActivity.this, "Failed: Must enter Task ",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(mDescription)){
                    description.setError("Description Required");
                    Toast.makeText(TasksActivity.this, "Failed: Must enter Description ",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(mDuration)){
                    duration.setError("Duration Required");
                    Toast.makeText(TasksActivity.this, "Failed: Must enter Duration ",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Task t = new Task(mTask, mDescription, mDuration, id, false);
                    reference.child(id).setValue(t).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(TasksActivity.this, "Task has been saved successfully!",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }else{
                                Toast.makeText(TasksActivity.this, "Failed: Tasks unable to save "+ task.getException().toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Task> options = new FirebaseRecyclerOptions.Builder<Task>()
                .setQuery(reference.orderByChild("isComplete"), Task.class)
                .build();
        FirebaseRecyclerAdapter<Task, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Task, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Task model) {
                holder.setStatus(model.getIsComplete());
                holder.setTask(model.getTask());
                holder.setDescription(model.getDescription());
                holder.setDuration(model.getDuration());

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        key = getRef(position).getKey();
                        task = model.getTask();
                        description = model.getDescription();
                        isComplete = model.getIsComplete();
                        updateTask();
                    }
                });
            }
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
                return new MyViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();



    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        View view;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            view = itemView;
        }

        public void setTask(String task){
            TextView taskTextView = view.findViewById(R.id.task_view);
            taskTextView.setText(task);

        }

        public void setDescription(String description){
            TextView descTextView = view.findViewById(R.id.description_view);
            descTextView.setText(description);
        }

        public void setDuration(String duration){
            TextView durationTextView = view.findViewById(R.id.duration_view);
            durationTextView.setText(duration);
        }

        public void setStatus(boolean isChecked){
            TextView statusView = view.findViewById(R.id.taskStatus);
            LinearLayout statusParent = view.findViewById(R.id.statusParent);
            if(isChecked){
                statusView.setText("Completed");
                statusView.setBackgroundResource(R.color.green);
                statusParent.setBackgroundResource(R.color.green);
            }else{
                statusView.setText("Task");
                statusView.setBackgroundResource(R.color.purple_500);
                statusParent.setBackgroundResource(R.color.purple_500);
            }

        }

    }

    private void updateTask(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.update_task, null);
        myDialog.setView(view);

        AlertDialog dialog = myDialog.create();
        dialog.setCancelable(true);

        EditText mTask = view.findViewById(R.id.update_task_text);
        EditText mDescription = view.findViewById(R.id.update_description_text);
        EditText mDuration = view.findViewById(R.id.update_duration_text);
        CheckBox mCheckbox = view.findViewById(R.id.completed);
        mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        });

        mTask.setText(task);
        mDescription.setText(description);
        mDuration.setText(duration);
        mCheckbox.setChecked(isComplete);
        Button deleteBtn = view.findViewById(R.id.delete_task_btn);
        Button updateBtn = view.findViewById(R.id.update_task_btn);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task = mTask.getText().toString();
                description = mDescription.getText().toString();
                duration = mDuration.getText().toString();
                isComplete = mCheckbox.isChecked();
                Task t = new Task(task, description, duration, key, isComplete);

                reference.child(key).setValue(t).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(TasksActivity.this, "Task has been updated successfully",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(TasksActivity.this, "Update Failed "+ task.getException().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(TasksActivity.this, "Task has been deleted successfully",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(TasksActivity.this, "Delete Failed "+ task.getException().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}