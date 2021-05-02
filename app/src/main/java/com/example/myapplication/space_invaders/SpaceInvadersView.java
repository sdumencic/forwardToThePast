package com.example.myapplication.space_invaders;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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
    private PlayerShip playerShip;
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

    // How menacing (fast) should the sound be?
    private long menaceInterval = 1000;
    // Which menace sound should play next
    private boolean uhOrOh;
    // When did we last play a menacing sound
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

        // This SoundPool is deprecated!!!
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);

        try{
            // Create objects of the 2 required classes
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // Load our fx in memory ready for use
            descriptor = assetManager.openFd("shoot.ogg");
            shootID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("invaderexplode.ogg");
            invaderExplodeID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("damageshelter.ogg");
            damageShelterID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("playerexplode.ogg");
            playerExplodeID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("damageshelter.ogg");
            damageShelterID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("uh.ogg");
            uhID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("oh.ogg");
            ohID = soundPool.load(descriptor, 0);

        }catch(IOException e){
            // Print an error message to the console
            Log.e("error", "failed to load sound files");
        }

        prepareLevel();
    }
    /**
     * Here will be initialized all of the game objects.
     */
    private void prepareLevel(){
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

            // Play a sound based on the menace level
            if(!paused) {
                if ((startFrameTime - lastMenaceTime) > menaceInterval) {
                    if (uhOrOh) {
                        // Play Uh
                        soundPool.play(uhID, 1, 1, 0, 0, 1);

                    } else {
                        // Play Oh
                        soundPool.play(ohID, 1, 1, 0, 0, 1);
                    }

                    // Reset the last menace time
                    lastMenaceTime = System.currentTimeMillis();
                    // Alter value of uhOrOh
                    uhOrOh = !uhOrOh;
                }
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
        // Make sure our drawing surface is valid or we crash
        if (ourHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = ourHolder.lockCanvas();

            // Draw the background color
            canvas.drawColor(Color.argb(255, 26, 128, 182));

            // Choose the brush color for drawing
            paint.setColor(Color.argb(255,  255, 255, 255));

            // Draw the score and remaining lives
            // Change the brush color
            paint.setColor(Color.argb(255,  249, 129, 0));
            paint.setTextSize(80);
            canvas.drawText("Score: " + score + "   Lives: " + lives, 20,90, paint);

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