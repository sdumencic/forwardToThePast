package com.example.myapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.example.myapplication.GameView.screenRatioX;
import static com.example.myapplication.GameView.screenRatioY;

public class Ball {
    int x,y, width, height, speedX = 0, speedY = 10;
    Bitmap ball;

    Ball (int screenY, int screenX, Resources res){
        ball = BitmapFactory.decodeResource(res, R.drawable.ball);

        width = ball.getWidth();
        height = ball.getHeight();
        //width /= 4;
        //height /= 4;

        screenRatioY *= 10;
        screenRatioX *= 10;

        width *= (int) screenRatioX;
        height *= (int) screenRatioX;

        height /= 10;
        width /= 10;

        screenRatioX/=10;
        screenRatioY/=10;


        ball = Bitmap.createScaledBitmap(ball, width, height, false);
        y = screenY/2 - ball.getHeight()/2;
        x = screenX/2 - ball.getWidth()/2;
    }


}
