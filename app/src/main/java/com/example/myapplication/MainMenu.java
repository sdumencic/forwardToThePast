package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {
    private CardView snake, tetris, spaceInvaders, breakout, pong, exit;

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

        /*tetris = (CardView) findViewById(R.id.tetris);
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
        });*/

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

    /*public void openTetris() {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    public void openSpaceInvaders() {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    public void openBreakout() {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    public void openPong() {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }*/

    public void exit() {
        this.finishAffinity();
    }
}