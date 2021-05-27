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
    private Paint paint3;
    private Paddle paddle;
    private Paddle paddleUp;
    private Ball ball;
    private ShadowBall shadowBall1, shadowBall2, shadowBall3, shadowBall4, shadowBall5, shadowBall6, shadowBall7, shadowBall8;



    public static float screenRatioX, screenRatioY;
    private Background background1;
    private boolean isBotPlaying = true;
    Bitmap bg_crta;
    MediaPlayer mp3, mp4;

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
        ShadowBall.quartet = 0;

        bg_crta = BitmapFactory.decodeResource(getResources(), R.drawable.bg_crta);


        paddle = new Paddle(screenY, getResources(), screenX / 2, screenY - 100);
        paddleUp = new Paddle(screenY, getResources(), screenX / 2, +100);
        ball = new Ball(screenY, screenX, getResources());

        shadowBall1 = new ShadowBall(screenX + 1000, screenY + 1000, getResources());
        shadowBall2 = new ShadowBall(screenX + 1000, screenY + 1000, getResources());
        shadowBall3 = new ShadowBall(screenX + 1000, screenY + 1000, getResources());
        shadowBall4 = new ShadowBall(screenX + 1000, screenY + 1000, getResources());
        shadowBall5 = new ShadowBall(screenX + 1000, screenY + 1000, getResources());
        shadowBall6 = new ShadowBall(screenX + 1000, screenY + 1000, getResources());
        shadowBall7 = new ShadowBall(screenX + 1000, screenY + 1000, getResources());
        shadowBall8 = new ShadowBall(screenX + 1000, screenY + 1000, getResources());


        paint = new Paint();
        paint2 = new Paint();
        paint2.setColor(Color.WHITE);
        paint2.setTextSize(80);

        paint3 = new Paint();
        paint3.setAlpha(100);

        Context context1 = getContext();
        mp3 = MediaPlayer.create(context1, R.raw.pong_ball);
        mp4 = MediaPlayer.create(context1, R.raw.pong_ball);
    }

    /**
     * Plays mp3 file for down paddle
     */

    public void startMp3() {
        mp3.start();
    }

    /**
     * Plays mp3 file for up paddle
     */

    public void startMp4() {
        mp4.start();
    }

    /**
     * Running function of an application
     * Calls update, draw and sleep methods
     */

    @Override
    public void run() {
        while (isPlaying) {

            update();
            draw();
            sleep();
        }
    }

    /**
     * Updates canvas that is rendered in draw method
     * Collision detection
     * Ball movement
     * AI movement
     * Ball trail animations
     */

    private void update() {
        if(ShadowBall.quartet >= 0){
            shadowBall1.fade(ball);
            if(ShadowBall.quartet==0 && shadowBall1.height <= ball.height / 1.2){
                ShadowBall.quartet++;
            }
        }
        if(ShadowBall.quartet >= 1){
            shadowBall2.fade(ball);
            if(ShadowBall.quartet==1 && shadowBall2.height <= ball.height / 1.2){
                ShadowBall.quartet++;
            }
        }
        if(ShadowBall.quartet >= 2){
            shadowBall3.fade(ball);
            if(ShadowBall.quartet==2 && shadowBall3.height <= ball.height / 1.2){
                ShadowBall.quartet++;
            }
        }
        if(ShadowBall.quartet >= 3){
            shadowBall4.fade(ball);
            if(ShadowBall.quartet==3 && shadowBall4.height <= ball.height / 1.2){
                ShadowBall.quartet++;
            }
        }
        if(ShadowBall.quartet >= 4){
            shadowBall5.fade(ball);
            if(ShadowBall.quartet==4 && shadowBall5.height <= ball.height / 1.2){
                ShadowBall.quartet++;
            }
        }
        if(ShadowBall.quartet >= 5){
            shadowBall6.fade(ball);
            if(ShadowBall.quartet==5 && shadowBall6.height <= ball.height / 1.2){
                ShadowBall.quartet++;
            }
        }
        if(ShadowBall.quartet >= 6){
            shadowBall7.fade(ball);
            if(ShadowBall.quartet==6 && shadowBall7.height <= ball.height / 1.2){
                ShadowBall.quartet++;
            }
        }
        if(ShadowBall.quartet >= 7){
            shadowBall8.fade(ball);
            if(ShadowBall.quartet==7 && shadowBall8.height <= ball.height / 1.2){
                ShadowBall.quartet++;
            }
        }

        if (ball.x < 0) {
            ball.speedX *= -1;
        }
        if (ball.x + ball.width > screenX) {
            ball.speedX *= -1;
        }

        ball.y += ball.speedY;
        ball.x += ball.speedX;

        if (ball.y + ball.height > paddle.y && ball.x + ball.width > paddle.x && ball.x < paddle.x + paddle.width && ball.y < screenY - ball.height) {
            startMp4();

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
    }

    /**
     * Renders updated changes to screen
     */

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(bg_crta, 0, screenY / 2, paint);
            canvas.drawBitmap(paddle.paddle, paddle.x, paddle.y, paint);
            canvas.drawBitmap(paddleUp.paddle, paddleUp.x, paddleUp.y, paint);
            canvas.drawBitmap(ball.ball, ball.x, ball.y, paint);

            canvas.drawBitmap(shadowBall1.ball, shadowBall1.x, shadowBall1.y, paint3);
            canvas.drawBitmap(shadowBall2.ball, shadowBall2.x, shadowBall2.y, paint3);
            canvas.drawBitmap(shadowBall3.ball, shadowBall3.x, shadowBall3.y, paint3);
            canvas.drawBitmap(shadowBall4.ball, shadowBall4.x, shadowBall4.y, paint3);
            canvas.drawBitmap(shadowBall5.ball, shadowBall5.x, shadowBall5.y, paint3);
            canvas.drawBitmap(shadowBall6.ball, shadowBall6.x, shadowBall6.y, paint3);
            canvas.drawBitmap(shadowBall7.ball, shadowBall7.x, shadowBall7.y, paint3);
            canvas.drawBitmap(shadowBall8.ball, shadowBall8.x, shadowBall8.y, paint3);

            canvas.drawText(String.valueOf(paddle.score), 0, screenY / 2 + 75, paint2);
            canvas.save();
            canvas.rotate(180f, screenX / 2, screenY / 2);
            canvas.drawText(String.valueOf(paddleUp.score), 0, screenY / 2 + 75, paint2);
            canvas.restore();
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    /**
     * Delay between frames
     * 17 milliseconds for ~60 fps
     */

    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resumes the game if it was paused
     */

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * pauses the game if it was playing
     */

    public void pause() {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes input from a screen
     * Top side of the screen for upper paddle movement
     * Bottom side of the screen for lower paddle movement
     *
     * @param event
     * @return
     */

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
