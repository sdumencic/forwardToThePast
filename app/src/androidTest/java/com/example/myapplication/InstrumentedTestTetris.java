package com.example.myapplication;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTestTetris {
    @Rule
    public ActivityScenarioRule<TetrisActivity> activityTestRule = new ActivityScenarioRule<> (TetrisActivity.class);

    @Test
    public void clickPlay() throws Exception {
        onView(withText("Start Game!")).perform(click());
    }

    @Test
    public void clickMove() throws Exception {
        onView(withText("Start Game!")).perform(click());
        onView(withId(R.id.ButtonLeft)).perform(click());
        onView(withId(R.id.ButtonLeft)).perform(click());
        onView(withId(R.id.ButtonRotateLeft)).perform(click());
        onView(withId(R.id.ButtonRotateRight)).perform(click());
        onView(withId(R.id.ButtonHardDrop)).perform(click());
    }
}