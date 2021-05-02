package com.example.myapplication;

import android.graphics.Point;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;;
import android.view.Display;

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

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();

        // Load the resolution into a Point object
        Point size = new Point();
        display.getSize(size);

        // Initialize gameView and set it as the view
        spaceInvadersView = new SpaceInvadersView(this, size.x, size.y);
        setContentView(spaceInvadersView);
    }

    /**
     * This method executes when the player starts the game.
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
}
