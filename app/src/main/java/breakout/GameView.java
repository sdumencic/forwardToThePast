package breakout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {
    Context context;
    float ballX, ballY;
    Velocity breakoutvelocity = new Velocity(25,-30);
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
    Brick[] bricks = new Brick[100];
    int numBricks = 0;
    int brickWidth, brickHeight;
    ArrayList<Bitmap> brickTypes = new ArrayList<Bitmap>();
    Bitmap brick1, brick2, brick3;

    public GameView(Context context) {
        super(context);
        this.context = context;
        ball = BitmapFactory.decodeResource(getResources(), R.drawable.breakout_ball);
        paddle = BitmapFactory.decodeResource(getResources(), R.drawable.breakout_paddle);
        brick1 = BitmapFactory.decodeResource(getResources(), R.drawable.breakout_brick1);
        brick2 = BitmapFactory.decodeResource(getResources(), R.drawable.breakout_brick2);
        brick3 = BitmapFactory.decodeResource(getResources(), R.drawable.breakout_brick3);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate(); //refreshes the view by indirectly calling onDraw
            }
        };
        mpHit = MediaPlayer.create(context, R.raw.breakout_ball_hit);
        mpMiss = MediaPlayer.create(context, R.raw.breakout_ball_miss);
        textPaint.setColor(Color.WHITE);
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
        ballY = dHeight * 3 / 4 ;
        paddleY = (dHeight * 17) / 20;
        paddleX = dWidth / 2 - paddle.getWidth() / 2; //positioning the pedal in the center
        brickWidth = dWidth / 6;
        brickHeight = dHeight / 30;
        brick1 = Bitmap.createScaledBitmap(brick1, brickWidth-5, brickHeight-5, true);
        brick2 = Bitmap.createScaledBitmap(brick2, brickWidth-5, brickHeight-5, true);
        brick3 = Bitmap.createScaledBitmap(brick3, brickWidth-5, brickHeight-5, true);
        brickTypes.add(brick1);
        brickTypes.add(brick2);
        brickTypes.add(brick3);
        numBricks = 0;

        for(int column = 0; column < 6; column ++ ){
            for(int row = 0; row < 9; row ++ ){
                bricks[numBricks] = new Brick(brickTypes.get(row%3), row, column, brickWidth, brickHeight);
                numBricks ++;
            }
        }
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

        //check if the ball hit a brick   /////////////////////////////////////////////////////////////////////
        for(int i = 0; i < numBricks; i++){

            if (bricks[i].getVisibility()){
                RectF ballRect = new RectF(ballX, ballY, ballX + ball.getWidth(), ballY + ball.getHeight());
                //if the ball hits a brick
                if(RectF.intersects(bricks[i].getRect(),ballRect)) {
                    bricks[i].setInvisible();

                    int ballCenterY = (int) (ballY - ball.getHeight() / 2);

                    if (ballCenterY < bricks[i].getY() - brickHeight //if the center of the ball is above the top of the brick - the ball hit the brick from the top
                    || ballCenterY > bricks[i].getY()) { //if the center of the ball is below the bottom of the brick - the ball hit the brick from the bottom
                        breakoutvelocity.setY(breakoutvelocity.getY() * -1);
                        points += 10;
                    } else { //the ball hit the brick from the side
                        breakoutvelocity.setX(breakoutvelocity.getX() * -1);
                        points += 10;
                    }

                    boolean gameOver = true; //all the bricks have been hit until proven otherwise
                    for (int j = 0; j < numBricks; j++) {
                        if (bricks[j].getVisibility()) {
                            gameOver = false;
                        }
                    }
                    if (gameOver) {
                        Intent intent = new Intent(context, GameOver.class);
                        intent.putExtra("points", points);
                        context.startActivity(intent);
                        ((Activity)context).finish();
                    }
                }
            }
        }

        //check if the ball hit the left or right wall
        if ((ballX >= dWidth - ball.getWidth()) || ballX <= 0) {
            breakoutvelocity.setX(breakoutvelocity.getX() * -1); //change direction left/right
        }
        //check if the ball hit the top wall
        if (ballY <= 0) {
            breakoutvelocity.setY(breakoutvelocity.getY() * -1); //change direction up/down
        }
        //check if the ball was missed
        if (ballY > paddleY + paddle.getHeight()){
            ballX = 1 + random.nextInt(dWidth - ball.getWidth() - 1); //get random X for next life
            ballY = dHeight * 3 / 4;
            if(mpMiss != null && audioState) {
                mpMiss.start();
            }
            breakoutvelocity.setX(xVelocity());
            breakoutvelocity.setY(-30);
            life--;
            if (life == 0) {
                //game over
                Intent intent = new Intent(context, GameOver.class);
                intent.putExtra("points", points);
                context.startActivity(intent);
                ((Activity)context).finish();
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
            breakoutvelocity.setX(breakoutvelocity.getX() + 1); //increase speed
            breakoutvelocity.setY(breakoutvelocity.getY() * -1); //change direction up/down
            points--;
        }
        canvas.drawBitmap(ball, ballX, ballY, null);
        canvas.drawBitmap(paddle, paddleX, paddleY, null);

        // Draw the bricks if visible
        for(int i = 0; i < numBricks; i++){
            if(bricks[i].getVisibility()) {
                canvas.drawBitmap(bricks[i].getColor(), bricks[i].getX(), bricks[i].getY(), null);
            }
        }
        canvas.drawText(""+points, 20, dHeight*19/20, textPaint);
        if (life == 2) {
            healthPaint.setColor(Color.YELLOW);
        } else if (life == 1) {
            healthPaint.setColor(Color.RED);
        }
        //draw the life rectangle => 3 = green, 2 = yellow, 1 = red
        canvas.drawRect(dWidth-200, dHeight*9/10, dWidth - 200 + 60 * life, dHeight*19/20, healthPaint);
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
