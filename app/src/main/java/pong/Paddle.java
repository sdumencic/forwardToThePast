package pong;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.myapplication.R;

import static pong.GameView.screenRatioX;
import static pong.GameView.screenRatioY;

public class Paddle {
    int x,y, width, height, score = 0;
    Bitmap paddle;

    Paddle (int screenY, Resources res, int x, int y){
        paddle = BitmapFactory.decodeResource(res, R.drawable.paddle);

        width = paddle.getWidth();
        height = paddle.getHeight();
        //width /= 4;
        //height /= 4;

        screenRatioY *= 10;
        screenRatioX *= 10;

        width *= (int) screenRatioX;
        height *= (int) screenRatioY;

        height /= 10;
        width /= 10;

        System.out.println(width);

        screenRatioX/=10;
        screenRatioY/=10;


        paddle = Bitmap.createScaledBitmap(paddle, width, height, false);
        this.x = x;
        this.y = y;

        /*y = screenY - 15;
        x = 64 * (int) screenRatioX;*/
    }


}
