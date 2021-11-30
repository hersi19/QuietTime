package com.example.quiettime;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

import android.view.View;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule= new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mActivity=null;


    @Before
    public void setUp() throws Exception{
        mActivity= mActivityTestRule.getActivity();
        Intents.init();

    }

    @Test
    public void mainActivityTitle(){
        Espresso.onView(withId(R.id.textView)).check(matches(withText("Welcome To QuietTime")));

    }

    @Test
    public void goToLogin(){
        Espresso.onView(withId(R.id.MainPage_Login)).perform(click());

        intended(hasComponent(LogInActivity.class.getName()));
    }

    @Test
    public void goToRegistration(){
        Espresso.onView(withId(R.id.MainPage_Register)).perform(click());

        intended(hasComponent(RegisterAccountActivity.class.getName()));
    }


    @Test
    public void onResume() {
        View view=mActivity.findViewById(R.id.MainPage_Login);

        assertNotNull(view);
    }


    @After
    public void tearDown() throws Exception{

        mActivity=null;
        Intents.release();
    }
}