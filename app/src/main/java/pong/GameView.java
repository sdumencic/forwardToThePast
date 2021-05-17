package pong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.example.myapplication.R;

public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    private boolean isPlaying;
    private int screenX, screenY;
    private Paint paint;
    private Paint paint2;

    MediaPlayer mp3, mp4;


    private Paddle paddle;
    private Paddle paddleUp;
    private Ball ball;
    private ShadowBall shadowBall1;

    Bitmap bg_crta;

    public static float screenRatioX, screenRatioY;

    private Background background1, background2;

    private boolean isBotPlaying = true;

    public int getScreenY() {
        return this.screenY;
    }

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        bg_crta = BitmapFactory.decodeResource(getResources(), R.drawable.bg_crta);
        //_________________________________________________________________________________________________!!!

        paddle = new Paddle(screenY, getResources(), screenX / 2, screenY - 100);
        paddleUp = new Paddle(screenY, getResources(), screenX / 2, +100);
        ball = new Ball(screenY, screenX, getResources());

        shadowBall1 = new ShadowBall(screenX + 1000, screenY + 1000, getResources());


        background2.x = screenX;

        paint = new Paint();
        paint2 = new Paint();
        paint2.setColor(Color.WHITE);
        paint2.setTextSize(80);

        Context context1 = getContext();
        mp3 = MediaPlayer.create(context1, R.raw.pong_ball);
        mp4 = MediaPlayer.create(context1, R.raw.pong_ball);
    }

    public void startMp3() {
        mp3.start();
    }

    public void stopMp3() {
        mp3.stop();
    }

    public void startMp4() {
        mp4.start();
    }

    @Override
    public void run() {
        while (isPlaying) {

            update();
            draw();
            sleep();
        }
    }

    private void update() {

        if (ball.x < 0) {
            ball.speedX *= -1;
        }
        if (ball.x + ball.width > screenX) {
            ball.speedX *= -1;
        }

        ball.y += ball.speedY;
        ball.x += ball.speedX;
//_____________________________________________________________________
        /*if(shadowBall1.isVisible){
            shadowBall1.shrink();
        }else{
            shadowBall1.move(ball);
        }*/


        if (ball.y + ball.height > paddle.y && ball.x + ball.width > paddle.x && ball.x < paddle.x + paddle.width && ball.y < screenY - ball.height) {
            startMp4();
            //ako udari u paddle
            float Vkut = (ball.x + ball.width / 2) - paddle.x;
            float kut = Vkut / paddle.width;
            System.out.println(Vkut + " " + kut);
            if (kut < 0.2) {
                ball.speedX = -15;
                ball.speedY = -15;
            } else if (kut < 0.4 && kut >= 0.2) {
                ball.speedX = -10;
                ball.speedY = -20;
            } else if (kut < 0.6 && kut >= 0.4) {
                if (kut > 0.5) {
                    ball.speedX = -4;
                } else {
                    ball.speedX = 4;
                }

                ball.speedY = -26;
            } else if (kut < 0.8 && kut >= 0.6) {
                ball.speedX = 10;
                ball.speedY = -20;
            } else if (kut >= 0.8) {
                ball.speedX = 15;
                ball.speedY = -15;
            }
        }

        if (ball.y + ball.height / 2 < paddleUp.y + paddleUp.height && ball.x + ball.width > paddleUp.x && ball.x < paddleUp.x + paddleUp.width && ball.y < screenY - ball.height) {
            startMp3();
            //ako udari u paddle
            float Vkut = (ball.x + ball.width / 2) - paddleUp.x;
            float kut = Vkut / paddleUp.width;
            System.out.println(Vkut + " " + kut);
            if (kut < 0.2) {
                ball.speedX = -15;
                ball.speedY = 15;
            } else if (kut < 0.4 && kut >= 0.2) {
                ball.speedX = -10;
                ball.speedY = 20;
            } else if (kut < 0.6 && kut >= 0.4) {
                ball.speedX = 4;
                ball.speedY = 26;
            } else if (kut < 0.8 && kut >= 0.6) {
                ball.speedX = 10;
                ball.speedY = 20;
            } else if (kut > 0.8) {
                ball.speedX = 15;
                ball.speedY = 15;
            }
        }
        if (ball.y < -ball.height || ball.y > screenY - ball.height) {

            //ball.speedY *= 2;
            if (ball.y < -ball.height) {
                paddle.score++;
            } else {
                paddleUp.score++;
            }
            ball.y = screenY / 2;
        }

        if (isBotPlaying) {
            if (ball.y + ball.height / 2 < screenY / 2) {
                if (ball.x + ball.width / 2 > paddleUp.x + paddleUp.width / 2) {
                    paddleUp.x += 15;
                }
                if (ball.x + ball.width / 2 < paddleUp.x + paddleUp.width / 2) {
                    paddleUp.x -= 15;
                }
            }
        }
        if ((ball.y > screenY / 2 - 100) && (ball.y < screenY / 2 + 100)) {
            //stopMp3();
        }
    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            canvas.drawBitmap(bg_crta, 0, screenY / 2, paint);


            canvas.drawBitmap(paddle.paddle, paddle.x, paddle.y, paint);
            canvas.drawBitmap(paddleUp.paddle, paddleUp.x, paddleUp.y, paint);
            canvas.drawBitmap(ball.ball, ball.x, ball.y, paint);

            canvas.drawBitmap(shadowBall1.ball, shadowBall1.x, shadowBall1.y, paint);

            canvas.drawText(String.valueOf(paddle.score), 0, screenY / 2 + 75, paint2);

            canvas.save();
            canvas.rotate(180f, screenX / 2, screenY / 2);
            canvas.drawText(String.valueOf(paddleUp.score), 0, screenY / 2 + 75, paint2);
            canvas.restore();

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getY() > getScreenY() / 2) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    System.out.println("Pustio");
                    paddle.x = (int) event.getX() - paddle.width / 2;//__________________1
                    break;
            }
        }

        if (event.getY() < getScreenY() / 2) {
            isBotPlaying = false;
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    System.out.println("Pustio");
                    paddleUp.x = (int) event.getX() - paddle.width / 2;//____________________1
                    break;
            }
        }

        return true;

    }
}
