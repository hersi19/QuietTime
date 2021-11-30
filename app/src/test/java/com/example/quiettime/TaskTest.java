package com.example.quiettime;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TaskTest {

    Task task;
    @Before
    public void setUp() throws Exception {
        task= new Task("Project 1", "This is a project for cse class", "40", "1234567890", false);
    }

    @After
    public void tearDown() throws Exception {
        task =null;
    }

    @Test
    public void getTask() {
        String expected="Project 1";
        assertEquals(expected, task.getTask());
    }

    @Test
    public void setTask() {
        String expected="Pickup from store";
        task.setTask("Pickup from store");
        assertEquals(expected, task.getTask());
    }

    @Test
    public void getDescription() {
        String expected="This is a project for cse class";
        assertEquals(expected, task.getDescription());

    }

    @Test
    public void setDescription() {
        String expected="Go to park to pickup leaves";
        task.setDescription("Go to park to pickup leaves");
        assertEquals(expected, task.getDescription());
    }

    @Test
    public void getDuration() {
        String expected="40";
        assertEquals(expected, task.getDuration());
    }

    @Test
    public void setDuration() {
        String expected="100";
        task.setDuration("100");
        assertEquals(expected, task.getDuration());
    }

    @Test
    public void getId() {
        String expected="1234567890";
        assertEquals(expected, task.getId());
    }

    @Test
    public void setId() {
        String expected="1234567";
        task.setId("1234567");
        assertEquals(expected, task.getId());
    }

    @Test
    public void getIsComplete() {
        boolean expected=false;
        assertEquals(expected,task.getIsComplete());
    }

    @Test
    public void setIsComplete() {
        boolean expected=true;
        task.setIsComplete(true);
        assertEquals(expected,task.getIsComplete());
    }
}