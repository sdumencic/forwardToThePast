package space_invaders;

import android.graphics.RectF;

public class DefenceBrick {
    private RectF rect;

    // If certain brick is hit, it's invisible.
    private boolean isVisible;

    public DefenceBrick(int row, int column, int shelterNumber, int screenX, int screenY){
        int width = screenX / 70;
        int height = screenY / 50;

        isVisible = true;

        int brickPadding = 1;

        int shelterPadding = screenX / 9;
        int startHeight = screenY - (screenY / 3);

        // Calculate dimensions of the whole brick wall.
        rect = new RectF(column * width + brickPadding +
                (shelterPadding * shelterNumber) +
                shelterPadding + shelterPadding * shelterNumber,
                row * height + brickPadding + startHeight,
                column * width + width - brickPadding +
                        (shelterPadding * shelterNumber) +
                        shelterPadding + shelterPadding * shelterNumber,
                row * height + height - brickPadding + startHeight);
    }

    public RectF getRect(){
        return this.rect;
    }

    public void setInvisible(){
        isVisible = false;
    }

    public boolean getVisibility(){
        return isVisible;
    }
}