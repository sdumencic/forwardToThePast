package pong;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.myapplication.R;

import static pong.GameView.screenRatioX;
import static pong.GameView.screenRatioY;

public class ShadowBall {
    int x, y, width, height, speedX = 0, speedY = -10;
    boolean isVisible = false;
    Bitmap ball;

    ShadowBall(int screenY, int screenX, Resources res) {
        ball = BitmapFactory.decodeResource(res, R.drawable.ball);

        width = ball.getWidth();
        height = ball.getHeight();

        screenRatioY *= 10;
        screenRatioX *= 10;

        width *= (int) screenRatioX;
        height *= (int) screenRatioX;

        height /= 17;
        width /= 17;

        screenRatioX /= 10;
        screenRatioY /= 10;


        ball = Bitmap.createScaledBitmap(ball, width, height, false);
        y = screenY / 2 - ball.getHeight() / 2;
        x = screenX / 2 - ball.getWidth() / 2;
    }

    public void shrink() {
        this.ball = Bitmap.createScaledBitmap(ball, (int) (this.width / 1.1), (int) (this.height / 1.1), false);
    }

    public void move(Ball ball1) {
        this.height = ball1.height;
        this.width = ball1.width;
        this.x = ball1.x;
        this.y = ball1.y;
        isVisible = true;
        this.ball = Bitmap.createScaledBitmap(ball, this.width, this.height, false);
    }
}
