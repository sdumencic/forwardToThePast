package space_invaders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.example.myapplication.SpaceInvadersActivity;

/**
 * SpaceInvadersView class represents the view of the game.
 * It also holds the game logic and initialized instances of other classes.
 */
public class SpaceInvadersView extends SurfaceView implements Runnable {
    Context context;

    private Thread gameThread = null;

    private SurfaceHolder screenHolder;

    private volatile boolean gameOn;

    // Game is paused until the player touches the screen.
    private boolean paused = true;

    //toggle sound
    public boolean sound = true;

    private Canvas canvas;
    private Paint paint;

    private long fps;

    private long frame;

    // Screen size in pixels
    private int screenX;
    private int screenY;

    public boolean lost;

    private SpaceShip spaceShip;
    private Bullet laser;

    private Bullet[] invadersBullets = new Bullet[200];
    private int nextBullet;
    private int maxInvaderBullets = 10;


    // Invaders army set as array
    Invader[] invaders = new Invader[60];
    int killed = 0;


    // The player's shelters are built from bricks
    private DefenceBrick[] defence = new DefenceBrick[400];
    private int remainingBricks;


    int score = 0;
    private int lives = 3;

    /**
     * SpaceInvadersView() constructor is called everytime
     * the new game instance is created.
     *
     * @param context
     * @param x
     * @param y
     */
    public SpaceInvadersView(Context context, int x, int y) {
        super(context);

        this.context = context;

        screenHolder = getHolder();
        paint = new Paint();

        // Get screen coordinates
        screenX = x;
        screenY = y;

        play();
    }


    /**
     * Play() initializes all of the game objects.
     */
    private void play() {
        // Instance all of the game objects.
        spaceShip = new SpaceShip(context, screenX, screenY);
        laser = new Bullet(context, screenY);

        for (int i = 0; i < invadersBullets.length; i++) {
            invadersBullets[i] = new Bullet(context, screenY);
        }

        killed = 0;
        for (int column = 0; column < 4; column++) {
            for (int row = 0; row < 5; row++) {
                invaders[killed] = new Invader(context, row, column, screenX, screenY);
                killed++;
            }
        }

        remainingBricks = 0;
        for (int shelterNumber = 0; shelterNumber < 4; shelterNumber++) {
            for (int column = 0; column < 10; column++) {
                for (int row = 0; row < 4; row++) {
                    defence[remainingBricks] = new DefenceBrick(row, column, shelterNumber, screenX, screenY);
                    remainingBricks++;
                }
            }
        }
    }


    /**
     * Run the game thread.
     */
    @Override
    public void run() {
        while (gameOn) {
            long startFrameTime = System.currentTimeMillis();

            if (!paused) {
                update();
            }

            // Draw all of the object on screen.
            draw();

            frame = System.currentTimeMillis() - startFrameTime;
            if (frame >= 1) {
                fps = 1000 / frame;
            }
        }
    }

    /**
     * Update() updates screen context and coordinates of all objects
     * after every game action.
     */
    private void update() {
        boolean bumped = false;

        lost = false;

        // Update spaceship coordinates
        spaceShip.update(fps);

        for (int i = 0; i < killed; i++) {
            // Update invaders army and draw all the visible invaders.
            if (invaders[i].getVisibility()) {
                invaders[i].update(fps);

                if (invaders[i].shootTheTarget(spaceShip.getX(),
                        spaceShip.getLength())) {

                    if (invadersBullets[nextBullet].shoot(invaders[i].getX() + invaders[i].getLength() / 2, invaders[i].getY(), laser.DOWN)) {
                        nextBullet++;

                        if (nextBullet == maxInvaderBullets) {
                            nextBullet = 0;
                        }
                    }
                }

                // Turn the army in the other direction
                if (invaders[i].getX() > screenX - invaders[i].getLength()
                        || invaders[i].getX() < 0) {
                    bumped = true;
                }
            }
        }

        // Update laser position
        if (laser.getStatus()) {
            laser.update(fps);
        }

        // Update bullet position
        for (int i = 0; i < invadersBullets.length; i++) {
            if (invadersBullets[i].getStatus()) {
                invadersBullets[i].update(fps);
            }
        }


        if (bumped) {
            for (int i = 0; i < killed; i++) {
                invaders[i].Swap();

            }
        }

        if (laser.getRange() < 0) {
            laser.setInactive();
        }

        for (int i = 0; i < invadersBullets.length; i++) {
            if (invadersBullets[i].getRange() > screenY) {
                invadersBullets[i].setInactive();
            }
        }

        // Check if the laser managed to shoot the invader.
        if (laser.getStatus()) {
            for (int i = 0; i < killed; i++) {
                if (invaders[i].getVisibility()) {
                    if (RectF.intersects(laser.getRect(), invaders[i].getRect())) {
                        invaders[i].setInvisible();

                        // Shoot and pause
                        laser.setInactive();

                        score = score + 10;

                        if (score == killed * 10) {
                            // Player has won the game, get back to start view.
                            if (sound) {
                                MediaPlayer endSound = MediaPlayer.create(context, R.raw.space_invaders_end);
                                endSound.start();
                            }
                            gameOver();
                        }
                    }
                }
            }
        }

        // Check if the bullets destroyed the bricks.
        for (int i = 0; i < invadersBullets.length; i++) {
            if (invadersBullets[i].getStatus()) {
                for (int j = 0; j < remainingBricks; j++) {
                    if (defence[j].getVisibility()) {
                        if (RectF.intersects(invadersBullets[i].getRect(), defence[j].getRect())) {
                            invadersBullets[i].setInactive();
                            defence[j].setInvisible();
                        }
                    }
                }
            }
        }

        if (laser.getStatus()) {
            for (int i = 0; i < remainingBricks; i++) {
                if (defence[i].getVisibility()) {

                    // Laser has been fired at the spaceship defence wall.
                    if (RectF.intersects(laser.getRect(), defence[i].getRect())) {
                        laser.setInactive();
                        defence[i].setInvisible();
                    }
                }
            }
        }

        for (int i = 0; i < invadersBullets.length; i++) {
            if (invadersBullets[i].getStatus()) {

                // Check if the bullets shot the ship.
                if (RectF.intersects(spaceShip.getRect(), invadersBullets[i].getRect())) {
                    invadersBullets[i].setInactive();
                    lives--;

                    if (lives == 0) {
                        if (sound) {
                            MediaPlayer endSound = MediaPlayer.create(context, R.raw.space_invaders_end);
                            endSound.start();
                        }
                        gameOver();
                    }
                }
            }
        }


        for (int i = 0; i < killed; i++) {
            for (int j = 0; j < remainingBricks; j++) {
                if (invaders[i].getVisibility() && defence[i].getVisibility()) {

                    // Army reached the brick wall.
                    // Game is lost.
                    if (RectF.intersects(defence[j].getRect(), invaders[i].getRect())) {
                        invadersBullets[i].setInactive();
                        if (sound) {
                            MediaPlayer endSound = MediaPlayer.create(context, R.raw.space_invaders_end);
                            endSound.start();
                        }
                        gameOver();
                    }
                }
            }
        }
    }


    /**
     * Draw() sets the screen graphics.
     */
    private void draw() {

        if (screenHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = screenHolder.lockCanvas();

            // Set the background picture
            Bitmap background = BitmapFactory.decodeResource(this.getResources(), R.drawable.space_invaders_background);
            background = Bitmap.createScaledBitmap(background, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, true);
            canvas.drawBitmap(background, 0, 0, null);

            // Choose the brush color for drawing
            paint.setColor(Color.argb(255, 255, 255, 255));


            //Draw the spaceship
            canvas.drawBitmap(spaceShip.getBitmap(), spaceShip.getX(), screenY - spaceShip.getHeight(), paint);


            // Draw the army of invaders
            for (int i = 0; i < killed; i++) {
                if (invaders[i].getVisibility()) {
                    canvas.drawBitmap(invaders[i].getBitmap(), invaders[i].getX(), invaders[i].getY(), paint);
                }
            }


            // Bricks graphics
            paint.setColor(Color.argb(255, 103, 207, 252));
            for (int i = 0; i < remainingBricks; i++) {
                if (defence[i].getVisibility()) {
                    canvas.drawRect(defence[i].getRect(), paint);
                }
            }


            if (laser.getStatus()) {
                canvas.drawBitmap(laser.getBitmap(), laser.getWidth(), laser.getHeight(), paint);
            }

            for (int i = 0; i < invadersBullets.length; i++) {
                if (invadersBullets[i].getStatus()) {
                    paint.setColor(Color.argb(255, 255, 153, 51));
                    canvas.drawRect(invadersBullets[i].getRect(), paint);
                }
            }

            paint.setColor(Color.argb(130, 0, 51, 102));
            canvas.drawRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT / 20, paint);


            Drawable remaining_lives = ContextCompat.getDrawable(context, R.drawable.space_invaders_lives);
            remaining_lives.setBounds(Constants.SCREEN_WIDTH - 180, 10, Constants.SCREEN_WIDTH - 100, Constants.SCREEN_HEIGHT / 20 - 10);
            remaining_lives.draw(canvas);
            paint.setColor(Color.argb(255, 255, 255, 255));
            paint.setTextSize(40);
            canvas.drawText(String.valueOf(lives), Constants.SCREEN_WIDTH - 90, Constants.SCREEN_HEIGHT / 30, paint);

            Drawable coins = ContextCompat.getDrawable(context, R.drawable.space_invaders_killed);
            coins.setBounds(Constants.SCREEN_WIDTH - 375, 10, Constants.SCREEN_WIDTH - 300, Constants.SCREEN_HEIGHT / 20 - 10);
            coins.draw(canvas);
            paint.setColor(Color.argb(255, 255, 255, 255));
            paint.setTextSize(40);
            canvas.drawText(String.valueOf(score), Constants.SCREEN_WIDTH - 290, Constants.SCREEN_HEIGHT / 30, paint);

            screenHolder.unlockCanvasAndPost(canvas);
        }
    }


    // Overridden method from java SurfaceView
    // Tap beyond defence blocks to fire the laser bullet.
    // Tap or hold finger on the bottom left side of the screen to move the ship left.
    // Tap or hold finger on the bottom right side of the screen to move the ship right.
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() > 0) {
            // Player has touched the screen
            paused = false;
        }

        // Detects motions in the lower part of the screen where the spaceship is
        // LEFT and RIGHT controls
        if (event.getY() > screenY / 2) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    spaceShip.x = (int) event.getX() - spaceShip.length / 2;
                    break;
            }
        }

        // Spaceship bullet touch control
        if (event.getY() < Constants.SCREEN_HEIGHT - Constants.SCREEN_HEIGHT / 8) {
            // Calculate shooting start coordinates and add sound
            if (laser.shoot(spaceShip.getX() +
                    spaceShip.getLength() / 2 - Constants.SCREEN_WIDTH / 60, Constants.SCREEN_HEIGHT - spaceShip.getHeight() -
                    spaceShip.getHeight() / 4, laser.UP)) {
                if (sound) {
                    MediaPlayer laserSound = MediaPlayer.create(context, R.raw.space_invaders_shoot);
                    laserSound.start();
                }
            }
        }

        return true;
    }


    /**
     * If SpaceInvadersActivity is paused/stopped,
     * shutdown game thread.
     */
    public void pause() {
        gameOn = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    /**
     * If user resums the game, create new game instance
     */
    public void resume() {
        gameOn = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void gameOver() {
        Intent intent = new Intent(context, SpaceInvadersActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }
}