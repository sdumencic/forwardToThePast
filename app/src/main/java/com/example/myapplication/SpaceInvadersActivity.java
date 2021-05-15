package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import space_invaders.Constants;
import space_invaders.SpaceInvadersView;



/**
 * SpaceInvadersActivity handles the game lifecycle.
 * It calls methods from the SpaceInvadersView when needed.
 */


public class SpaceInvadersActivity extends AppCompatActivity {
    SpaceInvadersView spaceInvadersView;

    /**
     *Initialization of the game instance.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        Constants.SCREEN_WIDTH = dm.widthPixels;

        // Initialize gameView and set it as the view
        spaceInvadersView = new SpaceInvadersView(this, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        setContentView(R.layout.space_invaders_start_game);
    }

    /**
     * This method executes when player starts the game.
     */
    @Override
    protected void onResume() {
        super.onResume();
        spaceInvadersView.resume();
    }

    /**
     * This method executes when the player quits the game
     */
    @Override
    protected void onPause() {
        super.onPause();
        spaceInvadersView.pause();
    }


    //This method detects screen touches
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:

                break;

            // Player has removed finger from screen
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }

    public void startSpaceInvaders(View view) {
        setContentView(spaceInvadersView);
    }
}
