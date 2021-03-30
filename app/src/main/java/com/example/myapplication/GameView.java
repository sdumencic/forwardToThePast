package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GameView extends View {
    private Bitmap bmGrass1, bmGrass2, bmGrass3, bmSnake;
    private int h = 21;
    private int w = 12;
    private ArrayList<Grass> grassList = new ArrayList<>();
    private Snake snake;
    private Context context;
    public static int sizeOfMap = 75 * Constants.SCREEN_WIDTH/1080;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        bmGrass1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.grass1);
        bmGrass1 = Bitmap.createScaledBitmap(bmGrass1, sizeOfMap, sizeOfMap, true);
        bmGrass2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.grass3);
        bmGrass2 = Bitmap.createScaledBitmap(bmGrass2, sizeOfMap, sizeOfMap, true);
        bmGrass3 = BitmapFactory.decodeResource(this.getResources(), R.drawable.grass4);
        bmGrass3 = Bitmap.createScaledBitmap(bmGrass3, sizeOfMap, sizeOfMap, true);
        bmSnake = BitmapFactory.decodeResource(this.getResources(), R.drawable.snake_4);
        bmSnake = Bitmap.createScaledBitmap(bmSnake, 14 * sizeOfMap, sizeOfMap, true);
        for(int i = 0; i < h; ++i) {
            for(int j = 0; j < w; ++j) {
                //if(i % 2 == 0) {
                    grassList.add(new Grass(bmGrass2, j*sizeOfMap + Constants.SCREEN_WIDTH / 2 - (w/2) * sizeOfMap,
                            i * sizeOfMap + 100 * Constants.SCREEN_HEIGHT/1920, sizeOfMap, sizeOfMap));
                /*} else if(i % 3 == 0) {
                    grassList.add(new Grass(bmGrass3, j*sizeOfMap + Constants.SCREEN_WIDTH / 2 - (w/2) * sizeOfMap,
                            i * sizeOfMap + 100 * Constants.SCREEN_HEIGHT/1920, sizeOfMap, sizeOfMap));
                } else {
                    grassList.add(new Grass(bmGrass2, j*sizeOfMap + Constants.SCREEN_WIDTH / 2 - (w/2) * sizeOfMap,
                            i * sizeOfMap + 100 * Constants.SCREEN_HEIGHT/1920, sizeOfMap, sizeOfMap));
                }*/
            }
        }

        snake = new Snake(bmSnake, grassList.get(125).getX(), grassList.get(125).getY(), 4);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(0x237500);
        for(int i = 0; i < grassList.size(); ++i) {
            canvas.drawBitmap(grassList.get(i).getBm(), grassList.get(i).getX(), grassList.get(i).getY(), null);
        }
        snake.drawSnake(canvas);
    }
}
