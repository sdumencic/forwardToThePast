package space_invaders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.content.Context;
import android.media.MediaPlayer;

import com.example.myapplication.R;


public class Bullet {

    //rectangle object in which the bullet will be drawn and its dimensions
    private float x;
    private float y;

    private RectF rect;
    private Bitmap bitmap;

    //possible shooting directions
    public final int UP = 0;
    public final int DOWN = 1;

    int heading = -1;

    float speed = 700;


    //height is relative to the screen resolution
    private int width = 1;
    private int laser_width = Constants.SCREEN_WIDTH/30;
    private int height;
    private int laser_height;



    private boolean isActive;

    public Bullet(Context context, int screenY) {
        height = screenY / 20;
        laser_height = screenY / 15;
        isActive = false;

        rect = new RectF();

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.space_invaders_laser);

        // Synchronize the bitmap with the screen resolution
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (laser_width),
                (int) (laser_height),
                false);

    }

    public RectF getRect() {
        return rect;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public float getWidth () {
        return x;
    }

    public float getHeight () {
        return y;
    }

    public boolean getStatus() {
        return isActive;
    }

    public void setInactive() {
        isActive = false;
    }

    public float getImpactPointY() {

        if (heading == DOWN) {
            return y + height;
        } else {
            return y;
        }
    }

    public boolean shoot(float startX, float startY, int direction) {
        if (!isActive) {
            x = startX;
            y = startY;
            heading = direction;
            isActive = true;
            return true;
        }

        return false;
    }

    public void update(long fps) {

        if (heading == UP) {
            y = y - speed / fps;
        } else {
            y = y + speed / fps;
        }

        // Update rectangle coordinates
        rect.left = x;
        rect.right = x + width;
        rect.top = y;
        rect.bottom = y + height;

    }
}