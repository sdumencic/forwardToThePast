package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends AppCompatActivity {
    private CardView snake, tetris, spaceInvaders, breakout, pong, exit;

    /**
     * Shows CardViews for each game. When a card is clicked, it opens the game
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        snake = (CardView) findViewById(R.id.snake);
        snake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSnake();
            }
        });

        tetris = (CardView) findViewById(R.id.tetris);
        tetris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTetris();
            }
        });

        spaceInvaders = (CardView) findViewById(R.id.spaceinvaders);
        spaceInvaders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSpaceInvaders();
            }
        });

        breakout = (CardView) findViewById(R.id.breakout);
        breakout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBreakout();
            }
        });

        pong = (CardView) findViewById(R.id.pong);
        pong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPong();
            }
        });

        exit = (CardView) findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
    }

    public void openSnake() {
        Intent intent = new Intent(this, SnakeMain.class);
        startActivity(intent);
    }

    public void openTetris() {
        Intent intent = new Intent(this, TetrisActivity.class);
        startActivity(intent);
    }

    public void openSpaceInvaders() {
        Intent intent = new Intent(this, SpaceInvadersActivity.class);
        startActivity(intent);
    }

    public void openBreakout() {
        Intent intent = new Intent(this, BreakoutActivity.class);
        startActivity(intent);
    }

    public void openPong() {
        Intent intent = new Intent(this, PongActivity.class);
        startActivity(intent);
    }

    public void exit() {
        this.finishAffinity();
    }
}