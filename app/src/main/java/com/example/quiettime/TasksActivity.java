package com.example.quiettime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TasksActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
//    private MyAdapter myAdapter;
    private FloatingActionButton floatingActionButton;

    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String curUserID;
    private String key = "";
    private String task;
    private String description;
    private String duration;

    private ArrayList<Task> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        toolbar = findViewById(R.id.tasks_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tasks");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

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
    }

    private void addTask(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View view = inflater.inflate(R.layout.task_input, null);
        myDialog.setView(view);

        AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

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
                    Task t = new Task(mTask, mDescription, mDuration, id);
                    reference.child(id).setValue(t).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(TasksActivity.this, "Task has been saved successfully!",Toast.LENGTH_SHORT).show();
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
                .setQuery(reference, Task.class)
                .build();

        FirebaseRecyclerAdapter<Task, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Task, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Task model) {
                holder.setTask(model.getTask());
                holder.setDescription(model.getDescription());
                holder.setDuration(model.getDuration());

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        key = getRef(position).getKey();
                        task = model.getTask();
                        description = model.getDescription();

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
    

    private void updateTask(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.update_task, null);
        myDialog.setView(view);

        AlertDialog dialog = myDialog.create();

        EditText mTask = view.findViewById(R.id.update_task_text);
        EditText mDescription = view.findViewById(R.id.update_description_text);
        EditText mDuration = view.findViewById(R.id.update_duration_text);

        mTask.setText(task);
//        mTask.setSelection(task.length());
        mDescription.setText(description);
//        mDescription.setSelection(description.length());
        mDuration.setText(duration);
//        mDuration.setSelection(duration.length());

        Button deleteBtn = view.findViewById(R.id.delete_task_btn);
        Button updateBtn = view.findViewById(R.id.update_task_btn);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task = mTask.getText().toString();
                description = mDescription.getText().toString();
                duration = mDuration.getText().toString();

                Task t = new Task(task, description, duration, key);

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
        })
        dialog.show();
    }
}