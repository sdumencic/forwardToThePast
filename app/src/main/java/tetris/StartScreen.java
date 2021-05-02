package tetris;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.example.myapplication.R;

public class StartScreen extends AppCompatActivity {

    TetrisGame parent;
    SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);
        readPreferences();
        mPref = getSharedPreferences("setup", MODE_PRIVATE);
    }

    public void readPreferences(){
        try {
            parent.randomType = mPref.getInt("Random Type", 1); 
        } catch (Exception PrefEmpty) {
            parent.randomType = 1;
        }
        try {
            parent.rotationType = mPref.getInt("Rotation Type", 1); 
        } catch (Exception PrefEmpty) {
            parent.rotationType = 2;
        }
        try {
            parent.softDropSpeed = mPref.getInt("Soft Drop Type", 1); 
        } catch (Exception PrefEmpty) {
            parent.softDropSpeed = 1;
        }
    }

    public void onStart(View v){
        Intent intent = new Intent(this, tetris.TetrisGame.class);
        startActivity(intent);
    }

    public void onMaps(View v){
        Intent intent = new Intent(this, Maps.class);
        startActivity(intent);
    }

    public void onExit(View v){
        finish();
    }
}
