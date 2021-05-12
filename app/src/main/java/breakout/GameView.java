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
    Velocity breakoutvelocity = new Velocity(25, -10);
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
    int dWidth, dHeight; //width and height of screen
    MediaPlayer mpHit, mpMiss, mpBrickHit;
    Random random;
    SharedPreferences sharedPreferences;
    Boolean audioState;
    Brick[] bricks = new Brick[100];
    int numBricks = 0;
    int brickWidth, brickHeight;
    ArrayList<Bitmap> brickTypes = new ArrayList<Bitmap>();
    Bitmap brick1, brick2, brick3;

    /*remember which side the previously hit brick got hit from -
    1 is hit from top, 2 is hit from right, 3 is hit from bottom, 4 is hit from left

    Solves the problem of double-direction change on the hit of two bricks at the same time */
    int HitFromSide = 0;

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
        mpBrickHit = MediaPlayer.create(context, R.raw.breakout_brick_hit);
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

        //fixes the bug of the ball spawning on the left or right wall (making the ball stuck) by having it spawn somewhere in the center of the screen
        ballX = random.nextInt(dWidth / 2) + dWidth / 4;
        ballY = dHeight * 3 / 4;
        paddleY = (dHeight * 17) / 20;
        paddleX = dWidth / 2 - paddle.getWidth() / 2; //positioning the pedal in the center
        brickWidth = dWidth / 6;
        brickHeight = dHeight / 25;
        brick1 = Bitmap.createScaledBitmap(brick1, brickWidth - 5, brickHeight - 5, true);
        brick2 = Bitmap.createScaledBitmap(brick2, brickWidth - 5, brickHeight - 5, true);
        brick3 = Bitmap.createScaledBitmap(brick3, brickWidth - 5, brickHeight - 5, true);
        brickTypes.add(brick1);
        brickTypes.add(brick2);
        brickTypes.add(brick3);
        numBricks = 0;

        for (int column = 0; column < 6; column++) {
            for (int row = 0; row < 6; row++) {
                bricks[numBricks] = new Brick(brickTypes.get(row / 2), row, column, brickWidth, brickHeight);
                numBricks++;
            }
        }
        sharedPreferences = context.getSharedPreferences("my_pref", 0);
        audioState = sharedPreferences.getBoolean("audioState", true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //draw the background
        canvas.drawColor(Color.rgb(50, 70, 150));

        //increase ball velocity
        ballX += breakoutvelocity.getX();
        ballY += breakoutvelocity.getY();

        //check if the ball hit a brick   /////////////////////////////////////////////////////////////////////
        for (int i = 0; i < numBricks; i++) {

            if (bricks[i].getVisibility()) {
                RectF ballRect = new RectF(ballX, ballY, ballX + ball.getWidth(), ballY + ball.getHeight());
                //if the ball hits a brick
                if (RectF.intersects(bricks[i].getRect(), ballRect)) {
                    if (mpBrickHit != null && audioState) {
                        mpBrickHit.start();
                    }
                    bricks[i].setInvisible();
                    points += 10;

                    int ballCenterY = (int) (ballY + ball.getHeight() / 2);
                    int ballCenterX = (int) (ballX + ball.getWidth() / 2);
                    int top = 0, right = 0, bottom = 0, left = 0;

                    //find center of the ball in relation to the brick
                    //the Y axis is reversed - the bottom of the brick is actually the upper side of the brick on our screen
                    if (ballCenterY <= bricks[i].getTop()) top = 1;
                    else if (ballCenterY >= bricks[i].getBottom()) bottom = 1;
                    if (ballCenterX <= bricks[i].getLeft()) left = 1;
                    else if (ballCenterX >= bricks[i].getRight()) right = 1;

                    //solve bouncing off the brick for all cases
                    /*
                    Case 1) The center of the ball is above the brick.
                    This can mean that:
                    a) The center of the ball is directly above the brick
                    b) The center of the ball is above the brick, but to the left of the left edge of the brick
                    c) The center of the ball is above the brick, but to the right of the right edge of the brick

                    Case 2) The center of the ball is below the brick.
                    This can mean that:
                    a) The center of the ball is directly below the brick
                    b) The center of the ball is below the brick, but to the left of the left edge of the brick
                    c) The center of the ball is below the brick, but to the right of the right edge of the brick

                    Case 3) The center of the ball is:
                    a) directly to the left of the left edge of the brick
                    b) directly to the right of the right edge of the brick
                    */
                    if (top == 1) { //Case 1
                        if (right == 0 && left == 0) { //1.a)
                            if (HitFromSide != 1) {
                                breakoutvelocity.setY(breakoutvelocity.getY() * -1);
                                HitFromSide = 1;
                            }
                        } else if (left == 1) { //1.b)
                            if (bricks[i].getTop() - ballCenterY >= bricks[i].getLeft() - ballCenterX) { //it's more top than left
                                if (HitFromSide != 1) {
                                    breakoutvelocity.setY(breakoutvelocity.getY() * -1);
                                    HitFromSide = 1;
                                }
                            } else { //it's more left than top
                                if (HitFromSide != 4) {
                                    breakoutvelocity.setX(breakoutvelocity.getX() * -1);
                                    HitFromSide = 4;
                                }
                            }
                        } else { //1.c)
                            if (bricks[i].getTop() - ballCenterY >= ballCenterX - bricks[i].getRight()) { //it's more top than right
                                if (HitFromSide != 1) {
                                    breakoutvelocity.setY(breakoutvelocity.getY() * -1);
                                    HitFromSide = 1;
                                }
                            } else { //it's more right than top
                                if (HitFromSide != 2) {
                                    breakoutvelocity.setX(breakoutvelocity.getX() * -1);
                                    HitFromSide = 2;
                                }
                            }
                        }
                    } else if (bottom == 1) { //Case 2
                        if (right == 0 && left == 0) { //2.a)
                            if (HitFromSide != 3) {
                                breakoutvelocity.setY(breakoutvelocity.getY() * -1);
                                HitFromSide = 3;
                            }
                        } else if (left == 1) { //2.b)
                            if (ballCenterY - bricks[i].getBottom() >= bricks[i].getLeft() - ballCenterX) { //it's more bottom than left
                                if (HitFromSide != 3) {
                                    breakoutvelocity.setY(breakoutvelocity.getY() * -1);
                                    HitFromSide = 3;
                                }
                            } else { //it's more left than bottom
                                if (HitFromSide != 4) {
                                    breakoutvelocity.setX(breakoutvelocity.getX() * -1);
                                    HitFromSide = 4;
                                }
                            }
                        } else { //2.c)
                            if (ballCenterY - bricks[i].getBottom() >= ballCenterX - bricks[i].getRight()) { //it's more bottom than right
                                if (HitFromSide != 3) {
                                    breakoutvelocity.setY(breakoutvelocity.getY() * -1);
                                    HitFromSide = 3;
                                }
                            } else { //it's more right than top
                                if (HitFromSide != 2) {
                                    breakoutvelocity.setX(breakoutvelocity.getX() * -1);
                                    HitFromSide = 2;
                                }
                            }
                        }
                    } else { //Case 3
                        if (left == 1) {
                            if (HitFromSide != 4) {
                                breakoutvelocity.setX(breakoutvelocity.getX() * -1);
                                HitFromSide = 4;
                            }
                        } else if (right == 1) {
                            if (HitFromSide != 2) {
                                breakoutvelocity.setX(breakoutvelocity.getX() * -1);
                                HitFromSide = 2;
                            }
                        }
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
                        ((Activity) context).finish();
                    }
                }
            }
        }

        //check if the ball hit the left or right wall
        if ((ballX >= dWidth - ball.getWidth()) || ballX <= 0) {
            breakoutvelocity.setX(breakoutvelocity.getX() * -1); //change direction left/right
            HitFromSide = 0;
            if (mpHit != null && audioState) {
                mpHit.start();
            }
        }
        //check if the ball hit the top wall
        if (ballY <= 0) {
            breakoutvelocity.setY(breakoutvelocity.getY() * -1); //change direction up/down
            HitFromSide = 0;
            if (mpHit != null && audioState) {
                mpHit.start();
            }
        }
        //check if the ball was missed
        if (ballY > paddleY + paddle.getHeight()) {
            //fixes the bug of the ball spawning on the left or right wall (making the ball stuck) by having it spawn somewhere in the center of the screen
            ballX = random.nextInt(dWidth / 2) + dWidth / 4;
            ballY = dHeight * 3 / 4;
            if (mpMiss != null && audioState) {
                mpMiss.start();
            }
            breakoutvelocity.setX(xVelocity());
            breakoutvelocity.setY(-10);
            life--;
            if (life == 0) {
                //game over
                Intent intent = new Intent(context, GameOver.class);
                intent.putExtra("points", points);
                context.startActivity(intent);
                ((Activity) context).finish();
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
            if (mpHit != null && audioState) {
                mpHit.start();
            }

            if (breakoutvelocity.getY() > 0) { // only change direction from down to up - fixes ball getting stuck in paddle
                breakoutvelocity.setY(breakoutvelocity.getY() * -1); //change direction up/down
            }
            HitFromSide = 0;
        }
        canvas.drawBitmap(ball, ballX, ballY, null);
        canvas.drawBitmap(paddle, paddleX, paddleY, null);

        // Draw the bricks if visible
        for (int i = 0; i < numBricks; i++) {
            if (bricks[i].getVisibility()) {
                canvas.drawBitmap(bricks[i].getColor(), bricks[i].getLeft(), bricks[i].getTop(), null);
            }
        }
        canvas.drawText("" + points, 20, dHeight * 19 / 20, textPaint);
        if (life == 2) {
            healthPaint.setColor(Color.YELLOW);
        } else if (life == 1) {
            healthPaint.setColor(Color.RED);
        }
        //draw the life rectangle => 3 = green, 2 = yellow, 1 = red
        canvas.drawRect(dWidth - 200, dHeight * 9 / 10, dWidth - 200 + 60 * life, dHeight * 19 / 20, healthPaint);
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
        int[] values = {-25, -20, -15, 15, 20, 25};
        int index = random.nextInt(6);
        return values[index];
    }
}
