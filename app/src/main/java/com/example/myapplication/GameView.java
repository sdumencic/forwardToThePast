package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.View;

import java.util.logging.LogRecord;

public class GameView extends View {


    android.os.Handler handler;
    Runnable runnable;
    final int UPDATE_MIL=30;
    Bitmap background;
    Display display;
    Point point;

    //custom view class

    public GameView(Context context) {
        super(context);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();//poziva ondraw()
            }
        };
        background = BitmapFactory.decodeResource(getResources(),R.drawable.background);
        display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //drawing here
        //draw the background on canvas
        canvas.drawBitmap(background,0,0,null);
        handler.postDelayed(runnable, UPDATE_MIL);
    }
}
