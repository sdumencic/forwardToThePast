package pong;

import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    private GameView gameView;

    /**
     * Gets display size to properly scale Bitmaps
     * Starts a game
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        gameView = new GameView(this, point.x, point.y);

        setContentView(gameView);
    }

    /**
     * Pausing the game
     */

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    /**
     * Resuming the game
     */

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}