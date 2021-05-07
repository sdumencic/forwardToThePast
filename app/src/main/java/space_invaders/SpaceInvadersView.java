package space_invaders;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.myapplication.R;

import java.io.IOException;

/**
 * SpaceInvadersView class represents the view of the game.
 * It also holds the logic of the game.
 */
public class SpaceInvadersView extends SurfaceView implements Runnable {
    Context context;

    private Thread gameThread = null;

    // SurfaceHolder will lock the surface before we draw the graphics.
    private SurfaceHolder ourHolder;

    // A boolean which we will set and unset
    // when the game is running- or not.
    private volatile boolean playing;

    // Game is paused at the start.
    private boolean paused = true;

    private Canvas canvas;
    private Paint paint;

    // This variable tracks the game frame rate.
    private long fps;

    // This is used to help calculate the fps.
    private long timeThisFrame;

    // The size of the screen in pixels
    private int screenX;
    private int screenY;

    //The player's ship and bullet
    private SpaceShip spaceShip;
    private Bullet bullet;

    // The invaders bullets
    private Bullet[] invadersBullets = new Bullet[200];
    private int nextBullet;
    private int maxInvaderBullets = 10;

    // Up to 60 invaders
    Invader[] invaders = new Invader[60];
    int numInvaders = 0;

    // The player's shelters are built from bricks
    private DefenceBrick[] bricks = new DefenceBrick[400];
    private int numBricks;

    // For sound FX
    private SoundPool soundPool;
    private int playerExplodeID = -1;
    private int invaderExplodeID = -1;
    private int shootID = -1;
    private int damageShelterID = -1;
    private int uhID = -1;
    private int ohID = -1;

    // The score
    int score = 0;

    // Lives
    private int lives = 3;


    private long lastMenaceTime = System.currentTimeMillis();

    /**
     * When we initialize (call new()) on gameView
     * This special constructor method runs
     */
    public SpaceInvadersView(Context context, int x, int y) {

        // The next line of code asks the
        // SurfaceView class to set up our object.
        super(context);

        // Make a globally available copy of the context so we can use it in another method
        this.context = context;

        // Initialize ourHolder and paint objects
        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        prepareLevel();
    }


    /**
     * Here will be initialized all of the game objects.
     */
    private void prepareLevel(){

        spaceShip = new SpaceShip(context, screenX, screenY);

        //canvas.drawBitmap(spaceShip.getBitmap(), spaceShip.getX(), screenY - 50, paint);

    }



    /**
     * Run the game thread.
     */
    @Override
    public void run() {
        while (playing) {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Update the frame
            if (!paused) {
                update();
            }

            // Draw the frame
            draw();

            // Calculate the fps this frame
            // We can then use the result to
            // time animations and more.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }

            
        }


    }

    /**
     * Update() updates game state after resume.
     */
    private void update(){
        // Did an invader bump into the side of the screen
        boolean bumped = false;

        // Has the player lost
        boolean lost = false;

        if(lost) {
            prepareLevel();
        }
    }


    /**
     * Draw() sets the screen graphics.
     */
    private void draw(){

        if (ourHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = ourHolder.lockCanvas();

            // Set the background picture
            Bitmap background = BitmapFactory.decodeResource(this.getResources(), R.drawable.space_invaders_background);
            background = Bitmap.createScaledBitmap(background, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, true);
            canvas.drawBitmap(background, 0, 0, null);

            // Choose the brush color for drawing
            paint.setColor(Color.argb(255,  255, 255, 255));

            // Draw the score and remaining lives
            paint.setColor(Color.argb(255,  249, 129, 0));
            paint.setTextSize(80);
            canvas.drawText("Score: " + score + "   Lives: " + lives, 20,90, paint);

            //Draw the spaceship
            canvas.drawBitmap(spaceShip.getBitmap(), spaceShip.getX(), screenY - spaceShip.getHeight(), paint);

            // Draw everything to the screen
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }


    /**
     * If SpaceInvadersActivity is paused/stopped
     * shutdown our thread.
     */
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    /**
     *If user resums the game, we call resume method.
     */
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}