package tetris;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

public class Maps extends AppCompatActivity {

    TetrisGame parent;
    RadioButton[] RB = new RadioButton[7];

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        parent.mapNum = 1;
    }

    public void onExit(View v){
        finish();
    }
}
