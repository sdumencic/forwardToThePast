package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import breakout.GameView;

public class BreakoutActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Boolean audioState;
    ImageButton ibAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ibAudio = findViewById(R.id.ibAudio);
        sharedPreferences = getSharedPreferences("my_pref", 0);
        audioState = sharedPreferences.getBoolean("audioState", true);
        if(audioState) {
            ibAudio.setImageResource(R.drawable.breakout_sound_on);
        } else {
            ibAudio.setImageResource(R.drawable.breakout_sound_off);
        }

    }

    public void startGame(View view) {
        GameView gameView = new GameView(this);
        setContentView(gameView);
    }

    public void audioPref(View view) {
        if(audioState){
            audioState = false;
            ibAudio.setBackgroundResource(0);
            ibAudio.setImageResource(R.drawable.breakout_sound_off);
        } else {
            audioState = true;
            ibAudio.setImageResource(R.drawable.breakout_sound_on);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("audioState",audioState);
        editor.commit();
    }
}