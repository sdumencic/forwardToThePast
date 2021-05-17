package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import breakout.GameView;

public class BreakoutActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Boolean audioState;
    public static ImageView close;
    ImageButton ibAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ibAudio = findViewById(R.id.ibAudio);
        sharedPreferences = getSharedPreferences("my_pref", 0);
        audioState = sharedPreferences.getBoolean("audioState", true);
        close = findViewById(R.id.close);
        if (audioState) {
            ibAudio.setImageResource(R.drawable.breakout_sound_on);
        } else {
            ibAudio.setImageResource(R.drawable.breakout_sound_off);
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeGame();
            }
        });

    }

    private void closeGame() {
        this.finish();
    }

    public void startGame(View view) {
        GameView gameView = new GameView(this);
        setContentView(gameView);
    }

    public void audioPref(View view) {
        if (audioState) {
            audioState = false;
            ibAudio.setBackgroundResource(0);
            ibAudio.setImageResource(R.drawable.breakout_sound_off);
        } else {
            audioState = true;
            ibAudio.setImageResource(R.drawable.breakout_sound_on);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("audioState", audioState);
        editor.commit();
    }
}