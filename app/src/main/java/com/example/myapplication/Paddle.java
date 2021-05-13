package com.example.myapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.example.myapplication.GameView.screenRatioX;
import static com.example.myapplication.GameView.screenRatioY;

public class Paddle {
    int x,y, width, height, speed = 0, score = 0;
    Bitmap paddle;

    Paddle (int screenY, Resources res/*, int x, int y*/){
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
        y = screenY - 15;
        x = 64 * (int) screenRatioX;
    }


}
