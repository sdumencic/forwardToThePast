package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {
    private Bitmap bmGrass1, bmGrass2, bmGrass3, bmSnake, bmMouse, bmFood1, bmFood2;
    private int h = 21;
    private int w = 12;
    private ArrayList<Grass> grassList = new ArrayList<>();
    private Snake snake;
    private boolean move = false;
    private Food food1, food2, food3;
    private float mx, my;
    private Context context;
    public static int sizeOfMap = 75 * Constants.SCREEN_WIDTH/1080;
    private Handler handler;
    private Runnable runnable;
    public static boolean isPlaying = false;
    public static int score = 0, bestScore = 0;
    private int soundEat, soundDie;
    private float volume;
    private boolean loadedSound;
    private SoundPool soundPool;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        bmGrass1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.snake_grass1);
        bmGrass1 = Bitmap.createScaledBitmap(bmGrass1, sizeOfMap, sizeOfMap, true);
        bmGrass2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.snake_grass3);
        bmGrass2 = Bitmap.createScaledBitmap(bmGrass2, sizeOfMap, sizeOfMap, true);
        bmSnake = BitmapFactory.decodeResource(this.getResources(), R.drawable.snake_4);
        bmSnake = Bitmap.createScaledBitmap(bmSnake, 14 * sizeOfMap, sizeOfMap, true);
        bmMouse = BitmapFactory.decodeResource(this.getResources(), R.drawable.snake_food3);
        bmMouse = Bitmap.createScaledBitmap(bmMouse, sizeOfMap, sizeOfMap, true);
        bmFood1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.snake_food1);
        bmFood1 = Bitmap.createScaledBitmap(bmFood1, sizeOfMap, sizeOfMap, true);
        bmFood2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.snake_food2);
        bmFood2 = Bitmap.createScaledBitmap(bmFood2, sizeOfMap, sizeOfMap, true);
        for(int i = 0; i < h; ++i) {
            for(int j = 0; j < w; ++j) {
                    grassList.add(new Grass(bmGrass2, j* bmGrass2.getWidth() + Constants.SCREEN_WIDTH / 2 - (w/2) * bmGrass2.getWidth(),
                            i * bmGrass2.getHeight() + 40 * Constants.SCREEN_HEIGHT/1920, bmGrass2.getHeight(), bmGrass2.getHeight()));
            }
        }

        snake = new Snake(bmSnake, grassList.get(125).getX(), grassList.get(125).getY(), 4);
        food1 = new Food(bmFood1, grassList.get(randomFood()[0]).getX(), grassList.get(randomFood()[1]).getY());
        food2 = new Food(bmFood2, grassList.get(randomFood()[0]).getX(), grassList.get(randomFood()[1]).getY());
        food3 = new Food(bmMouse, grassList.get(randomFood()[0]).getX(), grassList.get(randomFood()[1]).getY());
        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };

        if(Build.VERSION.SDK_INT>=21){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setAudioAttributes(audioAttributes).setMaxStreams(5);
            this.soundPool = builder.build();
        }else{
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loadedSound = true;
            }
        });
        soundEat = this.soundPool.load(context, R.raw.snake_eat, 1);
        soundDie = this.soundPool.load(context, R.raw.snake_hit, 1);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int e = event.getActionMasked();

        switch(e) {
            case MotionEvent.ACTION_MOVE: {
                if(move == false) {
                    mx = event.getX();
                    my = event.getY();
                    move = true;
                } else {
                    if(mx - event.getX() > 100 * Constants.SCREEN_WIDTH / 1080 && !snake.isMoveR()) {
                        mx = event.getX();
                        my = event.getY();
                        snake.setMoveL(true);
                        isPlaying = true;
                        MainActivity.txt_swipe.setVisibility(INVISIBLE);
                    } else if(event.getX() - mx > 100 * Constants.SCREEN_WIDTH / 1080 && !snake.isMoveL()) {
                        mx = event.getX();
                        my = event.getY();
                        snake.setMoveR(true);
                        isPlaying = true;
                        MainActivity.txt_swipe.setVisibility(INVISIBLE);
                    } else if(my - event.getY() > 100 * Constants.SCREEN_WIDTH / 1080 && !snake.isMoveD()) {
                        mx = event.getX();
                        my = event.getY();
                        snake.setMoveU(true);
                        isPlaying = true;
                        MainActivity.txt_swipe.setVisibility(INVISIBLE);
                    } else if(event.getY() - my > 100 * Constants.SCREEN_WIDTH / 1080 && !snake.isMoveU()) {
                        mx = event.getX();
                        my = event.getY();
                        snake.setMoveD(true);
                        isPlaying = true;
                        MainActivity.txt_swipe.setVisibility(INVISIBLE);
                    }
                }

                break;
            }

            case MotionEvent.ACTION_UP: {
                mx = 0;
                my = 0;
                move = false;
                break;
            }
        }

        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(0x237500);
        for(int i = 0; i < grassList.size(); ++i) {
            canvas.drawBitmap(grassList.get(i).getBm(), grassList.get(i).getX(), grassList.get(i).getY(), null);
        }

        if(isPlaying){
            snake.update();
            if(snake.getListPartSnake().get(0).getX() < this.grassList.get(0).getX()
                    ||snake.getListPartSnake().get(0).getY() < this.grassList.get(0).getY()
                    ||snake.getListPartSnake().get(0).getY()+sizeOfMap>this.grassList.get(this.grassList.size()-1).getY() + sizeOfMap
                    ||snake.getListPartSnake().get(0).getX()+sizeOfMap>this.grassList.get(this.grassList.size()-1).getX() + sizeOfMap){
                gameOver();
            }
            for (int i = 1; i < snake.getListPartSnake().size(); i++){
                if (snake.getListPartSnake().get(0).getBody().intersect(snake.getListPartSnake().get(i).getBody())){
                    gameOver();
                }
            }
        }

        snake.drawSnake(canvas);
        food1.draw(canvas);
        food2.draw(canvas);
        food3.draw(canvas);
        if(snake.getListPartSnake().get(0).getBody().intersect(food1.getRectangle())) {
            if(loadedSound){
                int streamId = this.soundPool.play(this.soundEat, (float)0.5, (float)0.5, 1, 0, 1f);
            }
            randomFood();
            food1.reset(grassList.get(randomFood()[0]).getX(), grassList.get(randomFood()[1]).getY());
            snake.addPart();
            score++;
            MainActivity.txt_score.setText(score+"");
        } else if(snake.getListPartSnake().get(0).getBody().intersect(food2.getRectangle())) {
            if(loadedSound){
                int streamId = this.soundPool.play(this.soundEat, (float)0.5, (float)0.5, 1, 0, 1f);
            }
            randomFood();
            food2.reset(grassList.get(randomFood()[0]).getX(), grassList.get(randomFood()[1]).getY());
            snake.addPart();
            score++;
            MainActivity.txt_score.setText(score+"");
        } else if(snake.getListPartSnake().get(0).getBody().intersect(food3.getRectangle())) {
            if(loadedSound){
                int streamId = this.soundPool.play(this.soundEat, (float)0.5, (float)0.5, 1, 0, 1f);
            }
            randomFood();
            food3.reset(grassList.get(randomFood()[0]).getX(), grassList.get(randomFood()[1]).getY());
            snake.addPart();
            score++;
            MainActivity.txt_score.setText(score+"");
        }

        if(score > bestScore){
            bestScore = score;
            SharedPreferences sp = context.getSharedPreferences("gamesetting", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("bestscore", bestScore);
            editor.apply();
            MainActivity.txt_best_score.setText(bestScore+"");
        }

        handler.postDelayed(runnable, 100);
    }

    private void gameOver() {
        isPlaying = false;
        MainActivity.dialogScore.show();
        MainActivity.txt_dialog_best_score.setText(bestScore+"");
        MainActivity.txt_dialog_score.setText(score+"");
        if(loadedSound){
            int streamId = this.soundPool.play(this.soundDie, (float)0.5, (float)0.5, 1, 0, 1f);
        }
    }

    public int[] randomFood() {
        int xy[] = new int[2];
        Random x = new Random();
        xy[0] = x.nextInt(grassList.size() -1);
        xy[1] = x.nextInt(grassList.size() - 1);
        Rect rectangle = new Rect(grassList.get(xy[0]).getX(), grassList.get(xy[1]).getY(), grassList.get(xy[0]).getX() + sizeOfMap, grassList.get(xy[1]).getY() + sizeOfMap);
        boolean check = true;
        while(check) {
            check = false;
            for(int i = 0; i < snake.getListPartSnake().size(); ++i) {
                if(rectangle.intersect(snake.getListPartSnake().get(i).getBody())) {
                    check = true;
                    xy[0] = x.nextInt(grassList.size() - 1);
                    xy[1] = x.nextInt(grassList.size() - 1);
                    rectangle = new Rect(grassList.get(xy[0]).getX(), grassList.get(xy[1]).getY(), grassList.get(xy[0]).getX() + sizeOfMap, grassList.get(xy[1]).getY() + sizeOfMap);
                }
            }
        }

        return xy;
    }

    public Bitmap randomFoodImage() {
        Bitmap foods[] = new Bitmap[3];
        int x = new Random().nextInt(foods.length);
        foods[0] = bmFood1;
        foods[1] = bmFood2;
        foods[2] = bmMouse;

        return foods[x];
    }

    public void reset(){
        for(int i = 0; i < h; i++){
            for (int j = 0; j < w; j++){
                grassList.add(new Grass(bmGrass2, j*bmGrass2.getWidth() + Constants.SCREEN_WIDTH/2 - (w/2)*bmGrass2.getWidth(), i*bmGrass2.getHeight()+50*Constants.SCREEN_HEIGHT/1920, bmGrass2.getWidth(), bmGrass2.getHeight()));
            }
        }
        snake = new Snake(bmSnake, grassList.get(126).getX(), grassList.get(126).getY(), 4);
        food1 = new Food(randomFoodImage(), grassList.get(randomFood()[0]).getX(), grassList.get(randomFood()[1]).getY());
        score = 0;
    }
}
