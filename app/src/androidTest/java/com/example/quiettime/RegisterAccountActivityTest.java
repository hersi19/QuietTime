package com.example.quiettime;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

//import static org.junit.Assert.*;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class RegisterAccountActivityTest {

    @Rule
    public ActivityTestRule<RegisterAccountActivity> mActivityTestRule= new ActivityTestRule<RegisterAccountActivity>(RegisterAccountActivity.class);

    private RegisterAccountActivity mActivity=null;

    @Before
    public void setUp() throws Exception {
        mActivity= mActivityTestRule.getActivity();
        Intents.init();
    }

    @Test
    public void testRegisterPage(){
        Espresso.onView(withId(R.id.textView4)).check(matches(withText("Create Your Account")));

    }


    @Test
    public void testMissingUsername(){

        Espresso.onView(withId(R.id.emailInput)).perform(typeText("moha1234@gmail.com"));
        Espresso.onView(withId(R.id.phoneinput)).perform(typeText("614768324687"));
        Espresso.onView(withId(R.id.passInput)).perform(typeText("1234567890"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.confirmPassInput)).perform(typeText("1234567890"));

        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.registerBtn)).perform(click());

        Espresso.onView(withId(R.id.register_error)).check(matches(withText("Please enter a username")));
    }

    @Test
    public void testMissingEmail(){
        Espresso.onView(withId(R.id.usernameInput)).perform(typeText("moha12345"));

        Espresso.onView(withId(R.id.phoneinput)).perform(typeText("614768324687"));
        Espresso.onView(withId(R.id.passInput)).perform(typeText("1234567890"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.confirmPassInput)).perform(typeText("1234567890"));

        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.registerBtn)).perform(click());
        Espresso.onView(withId(R.id.register_error)).check(matches(withText("Please enter your email")));
    }


    @Test
    public void testMissingPassword(){
        Espresso.onView(withId(R.id.usernameInput)).perform(typeText("moha12345"));
        Espresso.onView(withId(R.id.emailInput)).perform(typeText("moha1234@gmail.com"));
        Espresso.onView(withId(R.id.phoneinput)).perform(typeText("614768324687"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.confirmPassInput)).perform(typeText("1234567890"));

        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.registerBtn)).perform(click());
        Espresso.onView(withId(R.id.register_error)).check(matches(withText("Please enter a password")));
    }

    @Test
    public void testMissingConfirmPassword(){
        Espresso.onView(withId(R.id.usernameInput)).perform(typeText("moha12345"));
        Espresso.onView(withId(R.id.emailInput)).perform(typeText("moha1234@gmail.com"));
        Espresso.onView(withId(R.id.phoneinput)).perform(typeText("614768324687"));
        Espresso.onView(withId(R.id.passInput)).perform(typeText("1234567890"));
        Espresso.closeSoftKeyboard();


        Espresso.onView(withId(R.id.registerBtn)).perform(click());
        Espresso.onView(withId(R.id.register_error)).check(matches(withText("Please enter password again")));
    }

    public void inValidPass(){
        Espresso.onView(withId(R.id.usernameInput)).perform(typeText("moha12345"));
        Espresso.onView(withId(R.id.emailInput)).perform(typeText("moha1234@gmail.com"));
        Espresso.onView(withId(R.id.phoneinput)).perform(typeText("614768324687"));
        Espresso.onView(withId(R.id.passInput)).perform(typeText("123"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.confirmPassInput)).perform(typeText("1234"));

        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.registerBtn)).perform(click());
        Espresso.onView(withId(R.id.register_error)).check(matches(withText("Password must have 6 or more characters")));
    }

    public void testPassNotSame(){
        Espresso.onView(withId(R.id.usernameInput)).perform(typeText("moha12345"));
        Espresso.onView(withId(R.id.emailInput)).perform(typeText("moha1234@gmail.com"));
        Espresso.onView(withId(R.id.phoneinput)).perform(typeText("614768324687"));
        Espresso.onView(withId(R.id.passInput)).perform(typeText("1234567890"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.confirmPassInput)).perform(typeText("0000000000"));

        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.registerBtn)).perform(click());
        Espresso.onView(withId(R.id.register_error)).check(matches(withText("Enter same password")));
    }



    @After
    public void tearDown() throws Exception {
        mActivity=null;
        Intents.release();
    }
}