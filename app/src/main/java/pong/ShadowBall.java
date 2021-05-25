package pong;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.myapplication.R;

import static pong.GameView.screenRatioX;
import static pong.GameView.screenRatioY;

public class ShadowBall {
    public static int quartet = 0;
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

    /***
     * Trail from the ball
     * @param ball
     */

    public void fade(Ball ball) {
        if (this.isVisible == false) {
            this.y = ball.y;
            this.x = ball.x;
            this.isVisible = true;
            this.width = ball.width;
            this.height = ball.height;
            this.ball = Bitmap.createScaledBitmap(ball.ball, (int) (ball.width / 1.2), (int) (ball.height / 1.2), false);
        } else {

            if (this.width <= 12 || this.width <= 12) {
                x = 2000;
                y = 2000;
                this.isVisible = false;
            } else {
                height /= 1.1;
                width /= 1.1;
                x += 1;
                y += 1;
                this.ball = Bitmap.createScaledBitmap(ball.ball, width, height, false);
            }
        }

    }
}
