package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable{
    private Thread thread;
    private boolean isPlaying;
    private int screenX,screenY;
    private Paint paint;

    private Paddle paddle;
    private Ball ball;

    public static float screenRatioX, screenRatioY;

    private Background background1, background2;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        background1 = new Background(screenX,screenY,getResources());
        background2 = new Background(screenX,screenY,getResources());

        paddle = new Paddle(screenY, getResources());
        ball = new Ball(screenY, screenX, getResources());

        background2.x = screenX;

        paint = new Paint();
    }

    @Override
    public void run() {
        while(isPlaying){

            update();
            draw();
            sleep();
        }
    }

    private void update(){
        background1.x -= 10 * screenRatioX;
        background2.x -= 10 * screenRatioX;
        if(background1.x + background1.background.getWidth() < 0){
            background1.x = screenX;
        }

        if(background2.x + background2.background.getWidth() < 0){
            background2.x = screenX;
        }


        if(paddle.x + paddle.speed > 0 && paddle.x + paddle.speed < screenX - paddle.width){
            paddle.x += paddle.speed;
        }

        ball.y += ball.speedY;
        ball.x += ball.speedX;

        if(ball.y + ball.height > paddle.y && ball.x + ball.width > paddle.x && ball.x < paddle.x + paddle.width && ball.y < screenY - ball.height){
            //ako udari u paddle
            float Vkut = (ball.x + ball.width/2) - paddle.x;
            float kut = Vkut / paddle.width;
            System.out.println(Vkut+" "+kut);
            if(kut<0.5){
                ball.speedX= -5;
                ball.speedY= -5;
            }else if(kut>0.5){
                ball.speedX=5;
                ball.speedY=-5;
            }
        }

    }

    private void draw(){
        if(getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x,background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x,background2.y, paint);

            canvas.drawBitmap(paddle.paddle,paddle.x,paddle.y,paint);
            canvas.drawBitmap(ball.ball,ball.x,ball.y,paint);

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep(){
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume(){
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause(){
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                System.out.println("Pritisnuo");
                if(event.getX() < screenX / 2){
                    paddle.speed = -10;
                }
                else{
                    paddle.speed = 10;
                }
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("Pustio");
                paddle.speed = 0;
                break;
        }
        return true;

    }
}
