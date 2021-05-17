package tetris;

import android.graphics.Point;

public class Rotation {

    public static Point point1 = new Point(0, 0);
    public static Point point2 = new Point(0, 0);
    public static Point point3 = new Point(0, 0);
    public static Point point4 = new Point(0, 0);
    public static Point pp1 = new Point();
    public static Point pp2 = new Point();
    public static Point pp3 = new Point();
    public static Point pp4 = new Point();
    public static Point ppp1 = new Point();
    public static Point ppp2 = new Point();
    public static Point ppp3 = new Point();
    public static Point ppp4 = new Point();
    public int pointTemp = 0;
    public static int rotateAmount = 0;
    TetrisGame parent;

    public Rotation(TetrisGame parent) {
        this.parent = parent;
        meventListen = parent;
    }

    public void rotateLeft() {
        rotateMove(false);
    }

    public void rotateRight() {
        rotateMove(true);
    }

    public void rotateMove(Boolean rotateRight) {
        if (parent.rotate180) {
        } else {
            pp1.set(point1.x, point1.y);
            pp2.set(point2.x, point2.y);
            pp3.set(point3.x, point3.y);
            pp4.set(point4.x, point4.y);
        }
        if (parent.random != 3 && parent.canRotate && parent.notLose) {
            double placeholder;
            double rounded = (point1.x + point2.x + point3.x + point4.x);
            double roundedX = rounded / 4;
            rounded = (point1.y + point2.y + point3.y + point4.y); //popravak greske dijeljenja
            double roundedY = rounded / 4; //popravak na tocni rounding

            if (rotateAmount == 0) {
                roundedY *= 100;
                placeholder = roundedY;
                roundedY -= placeholder % 100;
                roundedY /= 100;
                roundedX *= 100;
                if (roundedX % 100 != 0) {
                    placeholder = roundedX;
                    if (rotateRight) {
                        if (roundedX % 100 >= 50) {
                            roundedX += 100 - (placeholder % 100);
                        } else {
                            roundedX -= placeholder % 100;
                        }
                    } else {
                        if (roundedX % 100 > 50) {
                            roundedX += 100 - (placeholder % 100);
                        } else {
                            roundedX -= placeholder % 100;
                        }
                    }
                }
                roundedX /= 100;
            } else if (rotateAmount == 1) {
                roundedX *= 100;
                placeholder = roundedX;
                roundedX -= placeholder % 100;
                roundedX /= 100;
                roundedY *= 100;
                if (roundedY % 100 != 0) {
                    placeholder = roundedY;
                    if (rotateRight) {
                        if (roundedY % 100 <= 50) {
                            roundedY -= placeholder % 100;
                        } else {
                            roundedY += 100 - (placeholder % 100);
                        }
                    } else {
                        if (roundedY % 100 < 50) {
                            roundedY -= placeholder % 100;
                        } else {
                            roundedY += 100 - (placeholder % 100);
                        }
                    }
                }
                roundedY /= 100;
            } else if (rotateAmount == 2) {
                roundedY *= 100;
                placeholder = roundedY;
                if (100 - (placeholder % 100) == 100 || 100 - (placeholder % 100) == 0) {
                } else {
                    roundedY += 100 - (placeholder % 100);
                }
                roundedY /= 100;
                roundedX *= 100;
                if (roundedX % 100 != 0) {
                    placeholder = roundedX;
                    if (rotateRight) {
                        if (roundedX % 100 <= 50) {
                            roundedX -= placeholder % 100;
                        } else {
                            roundedX += 100 - (placeholder % 100);
                        }
                    } else {
                        if (roundedX % 100 < 50) {
                            roundedX -= placeholder % 100;
                        } else {
                            roundedX += 100 - (placeholder % 100);
                        }
                    }
                }
                roundedX /= 100;
            } else if (rotateAmount == 3) {
                roundedX *= 100;
                placeholder = roundedX;
                if (100 - (placeholder % 100) == 100 || 100 - (placeholder % 100) == 0) {
                } else {
                    roundedX += 100 - (placeholder % 100);
                }
                roundedX /= 100;
                roundedY *= 100;
                if (roundedY % 100 != 0) {
                    placeholder = roundedY;
                    if (rotateRight) {
                        if (roundedY % 100 >= 50) {
                            roundedY += 100 - (placeholder % 100);
                        } else {
                            roundedY -= placeholder % 100;
                        }
                    } else {
                        if (roundedY % 100 > 50) {
                            roundedY += 100 - (placeholder % 100);
                        } else {
                            roundedY -= placeholder % 100;
                        }
                    }
                }
                roundedY /= 100;
            }

            point1.x -= roundedX;
            point1.y -= roundedY;
            point2.x -= roundedX;
            point2.y -= roundedY;
            point3.x -= roundedX;
            point3.y -= roundedY;
            point4.x -= roundedX;
            point4.y -= roundedY;
            if (rotateRight) { // vrtnja u desno
                if (parent.random != 4) {
                    pointTemp = point1.x;
                    point1.x = point1.y;
                    point1.y = 0 - pointTemp;
                    pointTemp = point2.x;
                    point2.x = point2.y;
                    point2.y = 0 - pointTemp;
                    pointTemp = point3.x;
                    point3.x = point3.y;
                    point3.y = 0 - pointTemp;
                    pointTemp = point4.x;
                    point4.x = point4.y;
                    point4.y = 0 - pointTemp;
                } else {
                    pointTemp = point1.y;
                    point1.y = point1.x;
                    point1.x = 0 - pointTemp;
                    pointTemp = point2.y;
                    point2.y = point2.x;
                    point2.x = 0 - pointTemp;
                    pointTemp = point3.y;
                    point3.y = point3.x;
                    point3.x = 0 - pointTemp;
                    pointTemp = point4.y;
                    point4.y = point4.x;
                    point4.x = 0 - pointTemp;
                }
            } else { // vrtnja u lijevo
                if (parent.random != 4) {
                    pointTemp = point1.y;
                    point1.y = point1.x;
                    point1.x = 0 - pointTemp;
                    pointTemp = point2.y;
                    point2.y = point2.x;
                    point2.x = 0 - pointTemp;
                    pointTemp = point3.y;
                    point3.y = point3.x;
                    point3.x = 0 - pointTemp;
                    pointTemp = point4.y;
                    point4.y = point4.x;
                    point4.x = 0 - pointTemp;
                } else {
                    pointTemp = point1.x;
                    point1.x = point1.y;
                    point1.y = 0 - pointTemp;
                    pointTemp = point2.x;
                    point2.x = point2.y;
                    point2.y = 0 - pointTemp;
                    pointTemp = point3.x;
                    point3.x = point3.y;
                    point3.y = 0 - pointTemp;
                    pointTemp = point4.x;
                    point4.x = point4.y;
                    point4.y = 0 - pointTemp;
                }
            }
            point1.x += roundedX;
            point1.y += roundedY;
            point2.x += roundedX;
            point2.y += roundedY;
            point3.x += roundedX;
            point3.y += roundedY;
            point4.x += roundedX;
            point4.y += roundedY;
            if (parent.rotationType == 0) {
                if (parent.random == 4 || parent.random == 5 || parent.random == 6) {
                    if (rotateRight) {
                        if (rotateAmount == 3) {
                            point1.y -= 1;
                            point2.y -= 1;
                            point3.y -= 1;
                            point4.y -= 1;
                        }
                        if (rotateAmount == 0) {
                            point1.y += 1;
                            point2.y += 1;
                            point3.y += 1;
                            point4.y += 1;
                        }
                        if (rotateAmount == 3) {
                            point1.x -= 1;
                            point2.x -= 1;
                            point3.x -= 1;
                            point4.x -= 1;
                        }
                        if (rotateAmount == 2) {
                            point1.x += 1;
                            point2.x += 1;
                            point3.x += 1;
                            point4.x += 1;
                        }
                    } else {
                        if (rotateAmount == 1) {
                            point1.y -= 1;
                            point2.y -= 1;
                            point3.y -= 1;
                            point4.y -= 1;
                        }
                        if (rotateAmount == 0) {
                            point1.y += 1;
                            point2.y += 1;
                            point3.y += 1;
                            point4.y += 1;
                        }
                        if (rotateAmount == 0) {
                            point1.x += 1;
                            point2.x += 1;
                            point3.x += 1;
                            point4.x += 1;
                        }
                        if (rotateAmount == 3) {
                            point1.x -= 1;
                            point2.x -= 1;
                            point3.x -= 1;
                            point4.x -= 1;
                        }
                    }
                }
            }
            if (!rotateRight) {
                rotateAmount--;
                if (rotateAmount == -1) {
                    rotateAmount += 4;
                }
            } else {
                rotateAmount++;
                if (rotateAmount == 4) {
                    rotateAmount -= 4;
                }
            }
            ppp1.set(point1.x, point1.y);
            ppp2.set(point2.x, point2.y);
            ppp3.set(point3.x, point3.y);
            ppp4.set(point4.x, point4.y);
            if (parent.rotate180 == true) {
            } else {
                parent.rotationEffortTries = 0;
                meventListen.onGetRotation(point1, point2, point3, point4);
            }
        }
    }

    public eventListen meventListen;

    public interface eventListen {
        void onGetRotation(Point pt1, Point pt2, Point pt3, Point pt4);
    }
}
