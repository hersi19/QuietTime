package com.example.quiettime;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

//import static org.junit.Assert.*;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class TasksActivityTest {

    @Rule
    public ActivityTestRule<TasksActivity> mActivityTestRule= new ActivityTestRule<TasksActivity>(TasksActivity.class);

    private TasksActivity mActivity=null;

    @Before
    public void setUp() throws Exception {
        mActivity= mActivityTestRule.getActivity();
    }



    @Test
    public void testLaunchAddActivity(){
        Espresso.onView(withId(R.id.fab)).perform(click());

        Espresso.onView(withId(R.id.add_task_title)).check(matches(withText("Add a new task")));

    }


    @Test
    public void testAddActivity(){
        Espresso.onView(withId(R.id.fab)).perform(click());

        // input some text in the edit text
        Espresso.onView(withId(R.id.task_input)).perform(typeText("Project 1"));

        Espresso.onView(withId(R.id.description_input)).perform(typeText("This is project for cse3423"));

        Espresso.onView(withId(R.id.duration_input)).perform(typeText("100"));
        // close soft keyboard
        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.task_save_button)).perform(click());
    }

    @Test
    public void testMissingName(){
        Espresso.onView(withId(R.id.fab)).perform(click());

        Espresso.onView(withId(R.id.description_input)).perform(typeText("This is project for cse3423"));

        Espresso.onView(withId(R.id.duration_input)).perform(typeText("100"));
        // close soft keyboard
        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.task_save_button)).perform(click());

        Espresso.onView(withId(R.id.task_error)).check(matches(withText("Please enter the task's name")));
    }

    @Test
    public void testMissingDescription(){
        Espresso.onView(withId(R.id.fab)).perform(click());

        Espresso.onView(withId(R.id.task_input)).perform(typeText("Project 1"));

        Espresso.onView(withId(R.id.duration_input)).perform(typeText("100"));
        // close soft keyboard
        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.task_save_button)).perform(click());

        Espresso.onView(withId(R.id.task_error)).check(matches(withText("Please enter the task's description")));
    }

    @Test
    public void testMissingDuration(){
        Espresso.onView(withId(R.id.fab)).perform(click());

        Espresso.onView(withId(R.id.task_input)).perform(typeText("Project 1"));

        Espresso.onView(withId(R.id.description_input)).perform(typeText("This is project for cse3423"));
        // close soft keyboard
        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.task_save_button)).perform(click());

        Espresso.onView(withId(R.id.task_error)).check(matches(withText("Please enter the task's duration")));
    }


    @After
    public void tearDown() throws Exception {
        mActivity=null;
    }
}