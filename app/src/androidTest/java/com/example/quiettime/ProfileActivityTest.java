package com.example.quiettime;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ProfileActivityTest {

    @Rule
    public ActivityTestRule<ProfileActivity> mActivityTestRule= new ActivityTestRule<ProfileActivity>(ProfileActivity.class);

//    @Rule
//    public IntentsTestRule<EmailActivity> mActivityRule =
//            new IntentsTestRule<>(EmailActivity.class);

    private ProfileActivity mActivity=null;


    @Before
    public void setUp() throws Exception{
        Intents.init();
        mActivity= mActivityTestRule.getActivity();

    }

    @Test
    public void onCreate() {

    }



    @Test
    public void testLaunchUpdateEmail(){

        Espresso.onView(withId(R.id.profile_email)).perform(click());

        intended(hasComponent(EmailActivity.class.getName()));
    }

    @Test
    public void testLaunchPhone(){

        Espresso.onView(withId(R.id.profile_phone)).perform(click());

        intended(hasComponent(PhoneActivity.class.getName()));
    }

    @Test
    public void testLaunchPassword(){

        Espresso.onView(withId(R.id.profile_password)).perform(click());

        intended(hasComponent(PasswordActivity.class.getName()));
    }

    @After
    public void tearDown() throws Exception{

        mActivity=null;
        Intents.release();
    }
}