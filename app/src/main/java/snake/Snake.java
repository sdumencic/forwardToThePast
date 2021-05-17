package snake;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Snake {
    private boolean moveL, moveR, moveU, moveD;
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
        setMoveR(true);
    }

    public void drawSnake(Canvas canvas) {
        for(int i = 0; i < length; ++i) {
            canvas.drawBitmap(listPartSnake.get(i).getBm(), listPartSnake.get(i).getX(), listPartSnake.get(i).getY(), null);
        }
    }

    public void update() {
        for(int i = length - 1; i > 0; i--) {
            listPartSnake.get(i).setX(listPartSnake.get(i-1).getX());
            listPartSnake.get(i).setY(listPartSnake.get(i-1).getY());
        }

        if(moveR) {
            listPartSnake.get(0).setX(listPartSnake.get(0).getX() + GameView.sizeOfMap);
            listPartSnake.get(0).setBm(right);
        } else if(moveL) {
            listPartSnake.get(0).setX(listPartSnake.get(0).getX() - GameView.sizeOfMap);
            listPartSnake.get(0).setBm(left);
        } else if(moveU) {
            listPartSnake.get(0).setY(listPartSnake.get(0).getY() - GameView.sizeOfMap);
            listPartSnake.get(0).setBm(up);
        } else if(moveD) {
            listPartSnake.get(0).setY(listPartSnake.get(0).getY() + GameView.sizeOfMap);
            listPartSnake.get(0).setBm(down);
        }

        for(int i = 1; i < length - 1; ++i) {
            if(listPartSnake.get(i).getLeft().intersect(listPartSnake.get(i+1).getBody()) &&
                listPartSnake.get(i).getBottom().intersect(listPartSnake.get(i-1).getBody()) ||
                listPartSnake.get(i).getLeft().intersect(listPartSnake.get(i-1).getBody()) &&
                listPartSnake.get(i).getBottom().intersect(listPartSnake.get(i+1).getBody())) {
                    listPartSnake.get(i).setBm(bodyBottomLeft);
            } else if(listPartSnake.get(i).getRight().intersect(listPartSnake.get(i+1).getBody()) &&
                    listPartSnake.get(i).getBottom().intersect(listPartSnake.get(i-1).getBody()) ||
                    listPartSnake.get(i).getRight().intersect(listPartSnake.get(i-1).getBody()) &&
                            listPartSnake.get(i).getBottom().intersect(listPartSnake.get(i+1).getBody())) {
                listPartSnake.get(i).setBm(bodyBottomRight);
            } else if(listPartSnake.get(i).getLeft().intersect(listPartSnake.get(i+1).getBody()) &&
                    listPartSnake.get(i).getTop().intersect(listPartSnake.get(i-1).getBody()) ||
                    listPartSnake.get(i).getLeft().intersect(listPartSnake.get(i-1).getBody()) &&
                            listPartSnake.get(i).getTop().intersect(listPartSnake.get(i+1).getBody())) {
                listPartSnake.get(i).setBm(bodyTopLeft);
            } else if(listPartSnake.get(i).getRight().intersect(listPartSnake.get(i+1).getBody()) &&
                    listPartSnake.get(i).getTop().intersect(listPartSnake.get(i-1).getBody()) ||
                    listPartSnake.get(i).getRight().intersect(listPartSnake.get(i-1).getBody()) &&
                            listPartSnake.get(i).getTop().intersect(listPartSnake.get(i+1).getBody())) {
                listPartSnake.get(i).setBm(bodyTopRight);
            } else if(listPartSnake.get(i).getTop().intersect(listPartSnake.get(i+1).getBody()) &&
                    listPartSnake.get(i).getBottom().intersect(listPartSnake.get(i-1).getBody()) ||
                    listPartSnake.get(i).getTop().intersect(listPartSnake.get(i-1).getBody()) &&
                            listPartSnake.get(i).getBottom().intersect(listPartSnake.get(i+1).getBody())) {
                listPartSnake.get(i).setBm(bodyVertical);
            } else if(listPartSnake.get(i).getLeft().intersect(listPartSnake.get(i+1).getBody()) &&
                    listPartSnake.get(i).getRight().intersect(listPartSnake.get(i-1).getBody()) ||
                    listPartSnake.get(i).getLeft().intersect(listPartSnake.get(i-1).getBody()) &&
                            listPartSnake.get(i).getRight().intersect(listPartSnake.get(i+1).getBody())) {
                listPartSnake.get(i).setBm(bodyHorizontal);
            } else {
                if(moveR){
                    listPartSnake.get(i).setBm(bodyHorizontal);
                }else if(moveD){
                    listPartSnake.get(i).setBm(bodyVertical);
                }else if(moveU){
                    listPartSnake.get(i).setBm(bodyVertical);
                }else{
                    listPartSnake.get(i).setBm(bodyHorizontal);
                }
            }
        }

        if(listPartSnake.get(length-1).getRight().intersect(listPartSnake.get(length-2).getBody())){
            listPartSnake.get(length-1).setBm(tailRight);
        }else if(listPartSnake.get(length-1).getLeft().intersect(listPartSnake.get(length-2).getBody())){
            listPartSnake.get(length-1).setBm(tailLeft);
        }else if(listPartSnake.get(length-1).getBottom().intersect(listPartSnake.get(length-2).getBody())){
            listPartSnake.get(length-1).setBm(tailDown);
        }else{
            listPartSnake.get(length-1).setBm(tailUp);
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

    public boolean isMoveL() {
        return moveL;
    }

    public void setMoveL(boolean moveL) {
        stop();
        this.moveL = moveL;
    }

    public boolean isMoveR() {
        return moveR;
    }

    public void setMoveR(boolean moveR) {
        stop();
        this.moveR = moveR;
    }

    public boolean isMoveU() {
        return moveU;
    }

    public void setMoveU(boolean moveU) {
        stop();
        this.moveU = moveU;
    }

    public boolean isMoveD() {
        return moveD;
    }

    public void setMoveD(boolean moveD) {
        stop();
        this.moveD = moveD;
    }

    public void stop() {
        this.moveU = false;
        this.moveR = false;
        this.moveD = false;
        this.moveL = false;
    }

    public void addPart() {
        PartSnake p = this.listPartSnake.get(length - 1);
        this.length += 1;
        if(p.getBm() == tailRight) {
            this.listPartSnake.add(new PartSnake(tailRight, p.getX() - GameView.sizeOfMap, p.getY()));
        } else if(p.getBm() == tailLeft) {
            this.listPartSnake.add(new PartSnake(tailLeft, p.getX() + GameView.sizeOfMap, p.getY()));
        } else if(p.getBm() == tailUp) {
            this.listPartSnake.add(new PartSnake(tailUp, p.getX(), p.getY() + GameView.sizeOfMap));
        } else if(p.getBm() == tailDown) {
            this.listPartSnake.add(new PartSnake(tailDown, p.getX(), p.getY() - GameView.sizeOfMap));
        }
    }
}
