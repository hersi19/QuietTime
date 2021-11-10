package com.example.quiettime;

public class Task {
    private String task, description, duration, id;
    private boolean isComplete;

    public Task(){

    }

    public Task(String task, String description, String duration, String id, boolean isComplete) {
        this.task = task;
        this.description = description;
        this.duration = duration;
        this.id = id;
        this.isComplete = isComplete;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String duration) {
        this.id = id;
    }

    public Boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Boolean isComplete) {
        this.isComplete = isComplete;
    }

}
