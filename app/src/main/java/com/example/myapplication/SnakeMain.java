package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import snake.Constants;
import snake.GameView;

public class SnakeMain extends AppCompatActivity {
    public static TextView txt_score, txt_best_score, txt_dialog_score, txt_dialog_best_score;
    public static Button txt_swipe;
    public static Dialog dialogScore;
    private GameView gameview;

    /**
     * Gets display metrics so that it can scale Bitmaps
     * Shows dialog with score
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); manifest android theme @style/Theme.Design.NoActionBar @style/Theme.MyApplication
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        Constants.SCREEN_WIDTH = dm.widthPixels;
        setContentView(R.layout.activity_snake);
        txt_swipe = findViewById(R.id.txt_swipe);
        gameview = findViewById(R.id.gameview);
        txt_score = findViewById(R.id.txt_score);
        txt_best_score = findViewById(R.id.txt_highscore);
        dialogScore();
    }

    /**
     * Shows score.
     * Creates dialog.
     */

    private void dialogScore() {
        int bestScore = 0;
        SharedPreferences sp = this.getSharedPreferences("gamesetting", Context.MODE_PRIVATE);
        if(sp != null){
            bestScore = sp.getInt("bestscore",0);
        }

        SnakeMain.txt_best_score.setText(bestScore+"");
        dialogScore = new Dialog(this);
        dialogScore.setContentView(R.layout.snake_dialog);
        txt_dialog_score = dialogScore.findViewById(R.id.txt_dialog_score);
        txt_dialog_best_score = dialogScore.findViewById(R.id.txt_dialog_best_score);
        txt_dialog_best_score.setText(bestScore + "");
        dialogScore.setCanceledOnTouchOutside(false);
        RelativeLayout start = dialogScore.findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_swipe.setVisibility(View.VISIBLE);
                gameview.reset();
                dialogScore.dismiss();
            }
        });

        dialogScore.show();
    }
}
