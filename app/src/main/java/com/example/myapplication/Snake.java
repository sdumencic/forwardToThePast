package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Snake {
    private Bitmap bm, up, down, left, right,
            bodyVertical, bodyHorizontal, bodyBottomLeft, bodyBottomRight, bodyTopLeft, bodyTopRight,
            tailLeft, tailUp, tailDown, tailRight;
    private int x, y, length;
    private ArrayList<PartSnake> listPartSnake = new ArrayList<>();

    public Snake(Bitmap bm, int x, int y, int length) {
        this.bm = bm;
        this.x = x;
        this.y = y;
        this.length = length;
        bodyBottomLeft = Bitmap.createBitmap(bm, 0, 0, bm.getWidth()/14, GameView.sizeOfMap);
        bodyBottomRight = Bitmap.createBitmap(bm, bm.getWidth()/14, 0, bm.getWidth()/14, bm.getWidth()/14);
        bodyHorizontal = Bitmap.createBitmap(bm, 2 * bm.getWidth()/14, 0, bm.getWidth()/14, bm.getWidth()/14);
        bodyTopLeft = Bitmap.createBitmap(bm, 3 * bm.getWidth()/14, 0, bm.getWidth()/14, bm.getWidth()/14);
        bodyTopRight = Bitmap.createBitmap(bm, 4 * bm.getWidth()/14, 0, bm.getWidth()/14, bm.getWidth()/14);
        bodyVertical = Bitmap.createBitmap(bm, 5 * bm.getWidth()/14, 0, bm.getWidth()/14, bm.getWidth()/14);
        down = Bitmap.createBitmap(bm, 6 * bm.getWidth()/14, 0, bm.getWidth()/14, bm.getWidth()/14);
        left = Bitmap.createBitmap(bm, 7 * bm.getWidth()/14, 0, bm.getWidth()/14, bm.getWidth()/14);
        right = Bitmap.createBitmap(bm, 8 * bm.getWidth()/14, 0, bm.getWidth()/14, bm.getWidth()/14);
        up = Bitmap.createBitmap(bm, 9 * bm.getWidth()/14, 0, bm.getWidth()/14, bm.getWidth()/14);
        tailUp = Bitmap.createBitmap(bm, 10 * bm.getWidth()/14, 0, bm.getWidth()/14, bm.getWidth()/14);
        tailRight = Bitmap.createBitmap(bm, 11 * bm.getWidth()/14, 0, bm.getWidth()/14, bm.getWidth()/14);
        tailLeft = Bitmap.createBitmap(bm, 12 * bm.getWidth()/14, 0, bm.getWidth()/14, bm.getWidth()/14);
        tailDown = Bitmap.createBitmap(bm, 13 * bm.getWidth()/14, 0, bm.getWidth()/14, bm.getWidth()/14);

        listPartSnake.add(new PartSnake(right, x, y));
        for(int i = 1; i < length - 1; ++i) {
            listPartSnake.add(new PartSnake(bodyHorizontal, listPartSnake.get(i-1).getX() - GameView.sizeOfMap, y));
        }
        listPartSnake.add(new PartSnake(tailRight, listPartSnake.get(length-2).getX() - GameView.sizeOfMap, listPartSnake.get(length-2).getY()));
    }

    public void drawSnake(Canvas canvas) {
        for(int i = 0; i < length; ++i) {
            canvas.drawBitmap(listPartSnake.get(i).getBm(), listPartSnake.get(i).getX(), listPartSnake.get(i).getY(), null);
        }
    }

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }

    public Bitmap getUp() {
        return up;
    }

    public void setUp(Bitmap up) {
        this.up = up;
    }

    public Bitmap getDown() {
        return down;
    }

    public void setDown(Bitmap down) {
        this.down = down;
    }

    public Bitmap getLeft() {
        return left;
    }

    public void setLeft(Bitmap left) {
        this.left = left;
    }

    public Bitmap getRight() {
        return right;
    }

    public void setRight(Bitmap right) {
        this.right = right;
    }

    public Bitmap getBodyVertical() {
        return bodyVertical;
    }

    public void setBodyVertical(Bitmap bodyVertical) {
        this.bodyVertical = bodyVertical;
    }

    public Bitmap getBodyHorizontal() {
        return bodyHorizontal;
    }

    public void setBodyHorizontal(Bitmap bodyHorizontal) {
        this.bodyHorizontal = bodyHorizontal;
    }

    public Bitmap getBodyBottomLeft() {
        return bodyBottomLeft;
    }

    public void setBodyBottomLeft(Bitmap bodyBottomLeft) {
        this.bodyBottomLeft = bodyBottomLeft;
    }

    public Bitmap getBodyBottomRight() {
        return bodyBottomRight;
    }

    public void setBodyBottomRight(Bitmap bodyBottomRight) {
        this.bodyBottomRight = bodyBottomRight;
    }

    public Bitmap getBodyTopLeft() {
        return bodyTopLeft;
    }

    public void setBodyTopLeft(Bitmap bodyTopLeft) {
        this.bodyTopLeft = bodyTopLeft;
    }

    public Bitmap getBodyTopRight() {
        return bodyTopRight;
    }

    public void setBodyTopRight(Bitmap bodyTopRight) {
        this.bodyTopRight = bodyTopRight;
    }

    public Bitmap getTailLeft() {
        return tailLeft;
    }

    public void setTailLeft(Bitmap tailLeft) {
        this.tailLeft = tailLeft;
    }

    public Bitmap getTailUp() {
        return tailUp;
    }

    public void setTailUp(Bitmap tailUp) {
        this.tailUp = tailUp;
    }

    public Bitmap getTailDown() {
        return tailDown;
    }

    public void setTailDown(Bitmap tailDown) {
        this.tailDown = tailDown;
    }

    public Bitmap getTailRight() {
        return tailRight;
    }

    public void setTailRight(Bitmap tailRight) {
        this.tailRight = tailRight;
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

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public ArrayList<PartSnake> getListPartSnake() {
        return listPartSnake;
    }

    public void setListPartSnake(ArrayList<PartSnake> listPartSnake) {
        this.listPartSnake = listPartSnake;
    }
}
