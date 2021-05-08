package breakout;

import android.graphics.Bitmap;
import android.graphics.RectF;

public class Brick {
    private Bitmap color;
    private RectF rect;
    private boolean isVisible;
    private int x, y;

    Brick(Bitmap color, int row, int column, int width, int height){

        int padding = 4;
        this.color = color;
        this.x = column * width + padding;
        this.y = row * height + padding;
        isVisible = true;


        rect = new RectF(column * width + padding,
                row * height + padding,
                column * width + width - padding,
                row * height + height - padding);
    }

    RectF getRect(){
        return this.rect;
    }

    void setInvisible(){
        isVisible = false;
    }

    boolean getVisibility(){
        return isVisible;
    }

    public Bitmap getColor() {return color;}

    public int getX() {return x;}

    public int getY() {return y;}
}