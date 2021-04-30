package snake;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class PartSnake {
    private Bitmap bm;
    private int x, y;
    private Rect body, top, bottom, left, right;

    public PartSnake(Bitmap bm, int x, int y) {
        this.bm = bm;
        this.x = x;
        this.y = y;
    }

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rect getBody() {
        return new Rect(this.x, this.y, this.x + GameView.sizeOfMap, this.y + GameView.sizeOfMap);
    }

    public void setBody(Rect body) {
        this.body = body;
    }

    public Rect getTop() {
        return new Rect(this.x, this.y - 10 * Constants.SCREEN_HEIGHT / 1920, this.x + GameView.sizeOfMap, this.y);
    }

    public void setTop(Rect top) {
        this.top = top;
    }

    public Rect getBottom() {
        return new Rect(this.x, this.y + GameView.sizeOfMap, this.x + GameView.sizeOfMap, this.y + GameView.sizeOfMap + 10 * Constants.SCREEN_HEIGHT / 1920);
    }

    public void setBottom(Rect bottom) {
        this.bottom = bottom;
    }

    public Rect getLeft() {
        return new Rect(this.x - 10 * Constants.SCREEN_WIDTH / 1080, this.y, this.x, this.y + GameView.sizeOfMap);
    }

    public void setLeft(Rect left) {
        this.left = left;
    }

    public Rect getRight() {
        return new Rect(this.x + 10 * Constants.SCREEN_WIDTH / 1080, this.y, this.x + GameView.sizeOfMap + 10 * Constants.SCREEN_WIDTH / 1080, this.y + GameView.sizeOfMap);
    }

    public void setRight(Rect right) {
        this.right = right;
    }
}
