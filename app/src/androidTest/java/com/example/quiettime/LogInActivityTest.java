package com.example.quiettime;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

//import static org.junit.Assert.*;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LogInActivityTest {

    @Rule
    public ActivityTestRule<LogInActivity> mActivityTestRule= new ActivityTestRule<LogInActivity>(LogInActivity.class);

    private LogInActivity mActivity=null;

    @Before
    public void setUp() throws Exception {
        mActivity= mActivityTestRule.getActivity();
    }


    @Test
    public void testNoEmail(){

        Espresso.onView(withId(R.id.loginPassword)).perform(typeText("1234567890"));

        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.loginBtn)).perform(click());

        //Espresso.onView(withId(R.id.loginEmail)).check(matches(withText("")));
        Espresso.onView(withId(R.id.errorMsg)).check(matches(withText("Enter your email")));
    }

    @Test
    public void testNoPassword(){
        Espresso.onView(withId(R.id.loginEmail)).perform(typeText("nija@gmail.com"));

        //Espresso.onView(withId(R.id.loginPassword)).perform(typeText("1234567890"));

        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.loginBtn)).perform(click());

        Espresso.onView(withId(R.id.errorMsg)).check(matches(withText("Enter your Password")));
        //Espresso.onView(withId(R.id.loginPassword)).check(matches(withText("")));
    }


    @Test
    public void testNoPasswordAndEmail(){

        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.loginBtn)).perform(click());

        Espresso.onView(withId(R.id.loginPassword)).check(matches(withText("")));
        Espresso.onView(withId(R.id.loginEmail)).check(matches(withText("")));
    }

    @After
    public void tearDown() throws Exception {
        mActivity=null;
    }
}