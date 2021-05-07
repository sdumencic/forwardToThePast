package space_invaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import com.example.myapplication.R;

public class SpaceShip {

    RectF rect;

    private Bitmap bitmap;

    // Spaceship dimensions
    private float length;
    private float height;

   //Dimensions of rectangle in which the spaceship will be drawn
    private float x;
    private float y;

    private float shipSpeed;

    // Spaceship directions
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    // This variable holds spaceship direction
    private int shipMoving = STOPPED;


    public SpaceShip(Context context, int screenX, int screenY){

        //Initializes blank rectangle for spaceship object and scale dimensions
        rect = new RectF();
        length = screenX/3;
        height = screenY/5;


        // Start ship rectangle in the center of the screen
        x = (float) (screenX / 3);
        y = screenY - height;


        // Initialize the bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.space_invaders_spaceship);

        // Synchronize the bitmap with the screen resolution
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);

        shipSpeed = 200;
    }

    public RectF getRect(){
        return rect;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public float getX(){
        return x;
    }

    public float getHeight(){
        return height;
    }

    public float getLength(){
        return length;
    }

    public void setMovementState(int state){
        shipMoving = state;
    }


    public void update(long fps){
        if(shipMoving == LEFT){
            x = x - shipSpeed / fps;
        }

        if(shipMoving == RIGHT){
            x = x + shipSpeed / fps;
        }

        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

    }

}

