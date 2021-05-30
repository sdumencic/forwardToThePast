package com.example.myapplication;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTestSnake {
    @Rule
    public ActivityScenarioRule<SnakeMain> activityTestRule = new ActivityScenarioRule<> (SnakeMain.class);

    @Test
    public void clickPlay() throws Exception {
        onView(withText("Play")).perform(click());
        onView(withId(R.id.gameview)).perform(swipeRight());
        onView(withId(R.id.gameview)).perform(swipeUp());
        onView(withId(R.id.gameview)).perform(swipeRight());
        onView(withId(R.id.gameview)).perform(swipeDown());
        onView(withId(R.id.gameview)).perform(swipeLeft());
    }
}