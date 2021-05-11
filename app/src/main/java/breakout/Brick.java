package breakout;

import android.graphics.Bitmap;
import android.graphics.RectF;

public class Brick {
    private Bitmap color;
    private RectF rect;
    private boolean isVisible;
    private int left, top, right, bottom;

    Brick(Bitmap color, int row, int column, int width, int height){

        int padding = 4;
        this.color = color;
        this.left = column * width + padding;
        this.top = row * height + padding;
        this.right = column * width + width - padding;
        this.bottom = row * height + height - padding;
        isVisible = true;

        rect = new RectF(this.left, this.top, this.right, this.bottom);
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

    public int getLeft() {return left;}

    public int getTop() {return top;}

    public int getBottom() {return bottom;}

    public int getRight() {return right;}
}