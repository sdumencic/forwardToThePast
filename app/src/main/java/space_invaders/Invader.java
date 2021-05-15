package space_invaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import java.util.Random;
import com.example.myapplication.R;


public class Invader {

    RectF rect;

    Random generator = new Random();

    private Bitmap bitmap;

    // Invader dimension
    private float length;
    private float height;

    // Rectangle coordinates
    private float x;
    private float y;

    private float shipSpeed;

    // Possible directions
    public final int LEFT = 1;
    public final int RIGHT = 2;

    // Detect ship movements
    private int shipMoving = RIGHT;

    // If invader is shot, it's not visible
    boolean isVisible;

    public Invader(Context context, int row, int column, int screenX, int screenY) {

        rect = new RectF();

        length = screenX / 12;
        height = screenY / 20;

        isVisible = true;

        int padding = screenX / 18;

        // Calculate rectangle dimensions
        x = column * (length + padding);
        y = row * (length + padding) + 100;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.space_invaders_enemy);

        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);

        shipSpeed = 50;
    }

    public void setInvisible(){
        isVisible = false;
    }

    public boolean getVisibility(){
        return isVisible;
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

    public float getY(){
        return y;
    }

    public float getLength(){
        return length;
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

    public void Swap(){
        if(shipMoving == LEFT){
            shipMoving = RIGHT;
        }else{
            shipMoving = LEFT;
        }

        y = y + height;

        shipSpeed = shipSpeed * 1.20f;
    }

    public boolean shootTheTarget(float spaceShipX, float spaceShipLength){

        int randomNumber = -1;

        if((spaceShipX + spaceShipLength > x &&
                spaceShipX + spaceShipLength < x + length) || (spaceShipX > x && spaceShipX < x + length)) {

            randomNumber = generator.nextInt(150);
            if(randomNumber == 0) {
                return true;
            }

        }

        randomNumber = generator.nextInt(2000);
        if(randomNumber == 0){
            return true;
        }

        return false;
    }
}
