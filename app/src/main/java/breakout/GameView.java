package breakout;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import com.example.myapplication.R;

import java.util.Random;

public class GameView extends View {
    Context context;
    float ballX, ballY;
    breakout.Velocity breakoutvelocity = new breakout.Velocity(25,40);
    Handler handler;
    final long UPDATE_MILLIS = 30; //delay used by handler to call the runnable
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint healthPaint = new Paint();
    float TEXT_SIZE = 120;
    float paddleX, paddleY;
    float oldX, oldPaddleX;
    int points = 0;
    int life = 3;
    Bitmap ball, paddle;
    int dWidth, dHeight;
    MediaPlayer mpHit, mpMiss;
    Random random;
    SharedPreferences sharedPreferences;
    Boolean audioState;

    public GameView(Context context) {
        super(context);
        this.context = context;
        ball = BitmapFactory.decodeResource(getResources(), R.drawable.breakout_ball);
        paddle = BitmapFactory.decodeResource(getResources(), R.drawable.breakout_paddle);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate(); //refreshes the view by indirectly calling onDraw
            }
        };
        mpHit = MediaPlayer.create(context, R.raw.breakout_ball_hit);
        mpMiss = MediaPlayer.create(context, R.raw.breakout_ball_miss);
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        healthPaint.setColor(Color.GREEN);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        random = new Random();
        ballX = random.nextInt(dWidth);
        paddleY = (dHeight * 4) / 5;
        paddleX = dWidth / 2 - paddle.getWidth() / 2; //positioning the pedal in the center
        sharedPreferences = context.getSharedPreferences("my_pref", 0);
        audioState = sharedPreferences.getBoolean("audioState", true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //draw the background
        canvas.drawColor(Color.rgb(50,70,150));

        //increase ball velocity
        ballX += breakoutvelocity.getX();
        ballY += breakoutvelocity.getY();

        //check if the ball hit the left or right wall
        if ((ballX >= dWidth - ball.getWidth()) || ballX <= 0) {
            breakoutvelocity.setX(breakoutvelocity.getX() * -1); //change direction
        }
        //check if the ball hit the top wall
        if (ballY <= 0) {
            breakoutvelocity.setY(breakoutvelocity.getY() * -1);
        }
        //check if the ball was missed
        if (ballY > paddleY + paddle.getHeight()){
            ballX = 1 + random.nextInt(dWidth - ball.getWidth() - 1); //get random X for next life
            ballY = 0; //put the ball at the top wall
            if(mpMiss != null & audioState) {
                mpMiss.start();
            }
            breakoutvelocity.setX(xVelocity());
            breakoutvelocity.setY(40);
            life--;
            if (life == 0) {
                //add game over
            }
        }
        /*
        4 hit conditions:
            1) The ball's right edge overlaps with the paddle's left edge
            2) The ball's left edge overlaps with the paddle's right edge
            3) The ball's bottom edge overlaps with the paddle's top edge
            4) The ball is not below the paddle
        */
        if (((ballX + ball.getWidth()) >= paddleX)
        && (ballX <= paddleX + paddle.getWidth())
        && (ballY + ball.getHeight() >= paddleY)
        && (ballY + ball.getHeight() <= paddleY + paddle.getHeight())) {
            if(mpHit != null && audioState) {
                mpHit.start();
            }
            breakoutvelocity.setX(breakoutvelocity.getX() + 5); //increase speed
            breakoutvelocity.setY(breakoutvelocity.getY() * -1); //change direction
            points++;
        }
        canvas.drawBitmap(ball, ballX, ballY, null);
        canvas.drawBitmap(paddle, paddleX, paddleY, null);
        canvas.drawText(""+points, 20, TEXT_SIZE, textPaint);
        if (life == 2) {
            healthPaint.setColor(Color.YELLOW);
        } else if (life == 1) {
            healthPaint.setColor(Color.RED);
        }
        //draw the life rectangle => 3 = green, 2 = yellow, 1 = red
        canvas.drawRect(dWidth-200, 30, dWidth - 200 + 60 * life, 60, healthPaint);
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        if (touchY >= paddleY) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                oldX = event.getX();
                oldPaddleX = paddleX;
            }
            if (action == MotionEvent.ACTION_MOVE) {
                float shift = oldX - touchX;
                float newPaddleX = oldPaddleX - shift;
                //adjust new paddle position - check if it goes off screen
                if (newPaddleX <= 0) {
                    paddleX = 0;
                } else if (newPaddleX >= dWidth - paddle.getWidth()) {
                    paddleX = dWidth - paddle.getWidth();
                } else {
                    paddleX = newPaddleX;
                }
            }
        }
        return true;
    }

    private int xVelocity() {
        int[] values = {-35, -30, -25, 25, 30, 35};
        int index = random.nextInt(6);
        return values[index];
    }
}
