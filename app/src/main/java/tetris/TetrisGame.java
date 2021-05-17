package tetris;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

public class TetrisGame extends AppCompatActivity implements Rotation.eventListen {

    TextView TV, TV2, TV3, TV4, TV5;
    ImageView IV, IV2, IV3, IV4;
    ImageButton button, DASright, DASleft;
    Point pc1 = new Point();
    Point pc2 = new Point();
    Point pc3 = new Point();
    Point pc4 = new Point();
    Point ptTouch = new Point(0, 0);
    MainView mainView;

    int[][] tetrisBoardColor = new int[10][30];
    int[] bag7 = new int[7];
    Boolean[][] tetrisBoard = new Boolean[10][30];
    Boolean[][] tetrisBoardLock = new Boolean[10][30];
    Boolean[][] tetrisGhostPiece = new Boolean[10][25];
    Bitmap[] tetrisPieceImage = new Bitmap[10];

    SharedPreferences mPref = null;
    Rotation rotation = new Rotation(this);
    Bitmap tetrisGhost;
    public static String playMode = "Marathon";
    public static int sprintUpdate = 50;
    long pausedSprintTimeStart = 0;
    String sprintTimeString = "";
    public static String sprintHighscore = "";

    public Boolean lockInAir = false;
    public Boolean canRotate = true;
    public Boolean notLose = true;
    public Boolean dontWorryAboutIt = true;
    public Boolean rotate180 = false;
    public Boolean rotationRight = false;
    public Boolean canLock = false;
    public Boolean ableToHardDrop = true;
    public Boolean swipeSoftDrop = false;
    public Boolean paused = false;
    public Boolean softDrop = false, DASR = false, DASL = false;
    public Boolean canMotionEventRotate = true;
    public Boolean TSpin = false;
    public Boolean B2B = false;
    public Boolean ARRRun = false;
    public static Boolean showPointsLog = true;
    public static int DAS = 100;
    public static int ARR = 50;
    public static int softDropSpeed = 10;
    public int random = 8, random2 = 8, random3 = 8, random4 = 8;
    public int rotationEffortTries = 0;
    int lines = 0;
    int linesNeeded = 10;
    int level = 0;
    int linesClearedAtOnce = 0;
    int bag7number = 0;
    int bag7numberCheck = 0;
    int holdPieceNumber = 8;
    int timesHoldPieceUsed = 0;
    int combo = 0;
    int piecesSinceCombo = 0;
    int lockDelay = 250; // ms
    int lockTries = 0;
    int hardDropTest = 0;
    int centerX = 0;
    int centerY = 0;
    public static int rotationType = 0;
    public static int randomType = 1;
    public static int mapNum = 0;
    long startTime = 0;
    long sprintTime = 0;
    long dropSpeed = 1000;
    long score = 0;
    public static long highscore = 0;

    public void onGetRotation(Point pt1, Point pt2, Point pt3, Point pt4) {
        pc1.set(pt1.x, pt1.y);
        pc2.set(pt2.x, pt2.y);
        pc3.set(pt3.x, pt3.y);
        pc4.set(pt4.x, pt4.y);
        timer.sendEmptyMessage(2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tetris);

        mainView = new MainView(this);
        FrameLayout frame = (FrameLayout) findViewById(R.id.mainLayout);
        frame.addView(mainView, 0);

        mPref = getSharedPreferences("setup", MODE_PRIVATE);
        readPreferences(); // hajskor

        //redPiece
        tetrisPieceImage[5] = BitmapFactory.decodeResource(getResources(), R.drawable.tetris_red);
        //orangePiece
        tetrisPieceImage[1] = BitmapFactory.decodeResource(getResources(), R.drawable.tetris_orange);
        //yellowPiece
        tetrisPieceImage[3] = BitmapFactory.decodeResource(getResources(), R.drawable.tetris_yellow);
        //greenPiece
        tetrisPieceImage[6] = BitmapFactory.decodeResource(getResources(), R.drawable.tetris_green);
        //lightBluePiece
        tetrisPieceImage[4] = BitmapFactory.decodeResource(getResources(), R.drawable.tetris_cyan);
        //bluePiece
        tetrisPieceImage[2] = BitmapFactory.decodeResource(getResources(), R.drawable.tetris_blue);
        //purplePiece
        tetrisPieceImage[0] = BitmapFactory.decodeResource(getResources(), R.drawable.tetris_purple);
        //grayPiece
        tetrisPieceImage[7] = BitmapFactory.decodeResource(getResources(), R.drawable.tetrisgray);
        //solid piece
        tetrisPieceImage[9] = BitmapFactory.decodeResource(getResources(), R.drawable.tetrisgray);
        tetrisGhost = BitmapFactory.decodeResource(getResources(), R.drawable.tetrisghost);

        TV = (TextView) findViewById(R.id.TV);
        TV2 = (TextView) findViewById(R.id.TV2);
        TV3 = (TextView) findViewById(R.id.TV3);
        TV4 = (TextView) findViewById(R.id.TV4);
        TV5 = (TextView) findViewById(R.id.TV5);
        IV = (ImageView) findViewById(R.id.IV);
        IV2 = (ImageView) findViewById(R.id.IV2);
        IV3 = (ImageView) findViewById(R.id.IV3);
        IV4 = (ImageView) findViewById(R.id.IV4);
        button = (ImageButton) findViewById(R.id.ButtonSoftDrop);
        DASright = (ImageButton) findViewById(R.id.ButtonRight);
        DASleft = (ImageButton) findViewById(R.id.ButtonLeft);

        if (showPointsLog) {
            TV5.setVisibility(View.VISIBLE);
        } else {
            TV5.setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 30; j++) {
                tetrisBoard[i][j] = false;
                tetrisBoardLock[i][j] = false;
                tetrisBoardColor[i][j] = 7;
            }
        }

        for (int i = 0; i <= 6; i++) {
            bag7[i] = 8;
        }

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    softDrop = true;
                    timer.removeMessages(0);
                    timer.sendEmptyMessage(0);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    softDrop = false;
                }

                return true;
            }
        });

        DASleft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    moveHorizontalOnce(0);
                    DASL = true;
                    timer.removeMessages(4);
                    timer.sendEmptyMessageDelayed(4, DAS);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    DASL = false;
                    timer.removeMessages(4);
                }

                return true;
            }
        });

        DASright.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    moveHorizontalOnce(1);
                    DASR = true;
                    timer.removeMessages(4);
                    timer.sendEmptyMessageDelayed(4, DAS);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    DASR = false;
                    timer.removeMessages(4);
                }

                return true;
            }
        });

        startGame();
    }

    void startGame() {
        random = getRandom(7, 0);
        bag7[0] = random;
        random2 = getRandom(7, 0);
        bag7numberCheck++;
        bag7number++;
        check7bag(2);
        random3 = getRandom(7, 0);
        bag7numberCheck++;
        bag7number++;
        check7bag(3);
        random4 = getRandom(7, 0);
        bag7numberCheck++;
        bag7number++;
        check7bag(4);
        showBoard();
        getPiece();
        TV5.setVisibility(View.VISIBLE);
        TV4.setVisibility(View.VISIBLE);
    }

    protected class MainView extends View {
        public MainView(Context context) {
            super(context);
        }

        public void onDraw(Canvas canvas) {
            if (notLose) {
                canvas.drawColor(Color.WHITE);
                Paint pnt = new Paint();
                drawScreen(canvas);
            } else {
                clearCanvas(canvas);
            }
        }

        public void clearCanvas(Canvas canvas) {
            Paint clearPaint = new Paint();
            clearPaint.setARGB(255, 100, 100, 100);
            canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), clearPaint);
        }

        public Rect getScreenRect() {
            Rect rtScreen = new Rect();
            rtScreen.left = this.getWidth() / 20;
            rtScreen.top = this.getHeight() / 50;
            rtScreen.right = this.getWidth() - (this.getWidth() / 100);
            rtScreen.bottom = this.getHeight() - (this.getHeight() / 4);

            return rtScreen;
        }

        public void drawScreen(Canvas canvas) {
            Rect screen = getScreenRect();
            int height = (int) (screen.height() / 20);
            int width = height;
            int startX = screen.left;
            int startY = screen.bottom;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 20; j++) {
                    int posX = startX + i * width;
                    int posY = startY - j * height;
                    if (tetrisBoardColor[i][j] == 10) {
                        canvas.drawBitmap(tetrisGhost, null, new Rect(posX, posY, posX + width, posY + width), null);
                    } else {
                        canvas.drawBitmap(tetrisPieceImage[tetrisBoardColor[i][j]], null, new Rect(posX, posY, posX + width, posY + width), null);
                    }
                }
            }
        }
    }

    void showBoard() {
        if (notLose) {
            int point = 0;
            for (int i = 29; i >= 0; i--) {
                for (int j = 0; j < 10; j++) {
                    if (tetrisBoard[j][i] || tetrisBoardLock[j][i]) {
                        if (tetrisBoard[j][i]) {
                            if (point == 0) {
                                Rotation.point1.set(j, i);
                            } else if (point == 1) {
                                Rotation.point2.set(j, i);
                            } else if (point == 2) {
                                Rotation.point3.set(j, i);
                            } else {
                                Rotation.point4.set(j, i);
                                point = -1;
                            }
                            point++;
                        }
                    }
                }
            }
            mainView.invalidate();
        }
    }

    public void lose() {
        notLose = false;
        for (int i = 0; i <= 7; i++)
            timer.removeMessages(i);

        if (score < 1000) {
            TV.setText("You lose! \nFinal Score:" + "\n" + score + " Points...");
        } else if (score < 5000) {
            TV.setText("You lose! \nFinal Score:" + "\n" + score + " Points");
        } else if (score < 15000) {
            TV.setText("You lose! \nFinal Score:" + "\n" + score + " Points!");
        } else if (score < 35000) {
            TV.setText("You lose! \nFinal Score:" + "\n" + score + " Points!!!");
        } else {
            TV.setText("You lose! \nFinal Score:" + "\nINCREDIBLE\n" + score + " POINTS!!!");
        }

        readPreferences();
        mainView.invalidate();
        if (score > highscore) {
            highscore = score;
        }

        savePreferences();

        TV.append("\nHighscore: " + highscore);
    }

    public void savePreferences() {
        SharedPreferences.Editor editor = mPref.edit();

        String strvalue = Long.toString(highscore);
        editor.putString("highscore", strvalue);

        editor.commit();
        Toast toast = Toast.makeText(getApplication(), "Score is saved", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void readPreferences() {
        try {
            String strvalue = mPref.getString("highscore", "0");
            highscore = Long.parseLong(strvalue);
        } catch (Exception PrefEmpty) {
            highscore = 0;
        }

        try {
            randomType = mPref.getInt("Random Type", 3);
        } catch (Exception PrefEmpty) {
            randomType = 2;
        }

        try {
            rotationType = mPref.getInt("Rotation Type", 3);
        } catch (Exception PrefEmpty) {
            rotationType = 1;
        }

        try {
            softDropSpeed = mPref.getInt("Soft Drop Type", 3);
        } catch (Exception PrefEmpty) {
            softDropSpeed = 10;
        }
    }

    public void onPause(View v) {
        paused = true;
        if (paused) {
            timer.removeMessages(0);
            timer.removeMessages(1);
            timer.removeMessages(2);
            timer.removeMessages(3);
            timer.removeMessages(4);
            timer.removeMessages(5);
            timer.removeMessages(6);
            timer.removeMessages(7);
            new AlertDialog.Builder(this).setTitle("PAUSED!").setMessage("This game is paused.").setIcon(R.drawable.ic_baseline_pause_circle_filled_24).setPositiveButton("Resume", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    paused = false;

                    timer.sendEmptyMessageDelayed(0, dropSpeed / 2);
                }
            })
                    .setNegativeButton("Restart", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            paused = false;
                            level = 0;
                            score = 0;
                            lines = 0;
                            for (int i = 0; i < 10; i++) {
                                for (int j = 0; j < 30; j++) {
                                    tetrisBoard[i][j] = false;
                                    tetrisBoardLock[i][j] = false;
                                    tetrisBoardColor[i][j] = 7;
                                }
                            }
                            holdPieceNumber = 7;
                            IV4.setImageResource(R.drawable.tetrisgray);
                            TV.setText("");
                            TV2.setText("Score : 0");
                            TV3.setText("Level : 0");
                            TV4.setText("Lines : 0");
                            TV5.setText("Points!");
                            canRotate = true;
                            notLose = true;
                            dontWorryAboutIt = true;
                            rotate180 = false;
                            rotationRight = false;
                            canLock = false;
                            ableToHardDrop = true;
                            swipeSoftDrop = false;
                            softDrop = false;
                            DASR = false;
                            DASL = false;
                            level = 0;
                            random = 8;
                            random2 = 8;
                            random3 = 8;
                            random4 = 8;
                            linesNeeded = 10;
                            for (int i = 0; i <= 6; i++) {
                                bag7[i] = 8;
                            }
                            bag7number = 0;
                            bag7numberCheck = 0;
                            holdPieceNumber = 8;
                            timesHoldPieceUsed = 0;

                            startGame();
                        }
                    })
                    .setNeutralButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            paused = false;
                            level = 0;
                            score = 0;
                            lines = 0;
                            for (int i = 0; i < 10; i++) {
                                for (int j = 0; j < 30; j++) {
                                    tetrisBoard[i][j] = false;
                                    tetrisBoardLock[i][j] = false;
                                    tetrisBoardColor[i][j] = 7;
                                }
                            }
                            holdPieceNumber = 7;
                            IV4.setImageResource(R.drawable.tetrisgray);
                            TV.setText("");
                            TV2.setText("Score : 0");
                            TV3.setText("Level : 0");
                            TV4.setText("Lines : 0");
                            TV5.setText("Points!");
                            canRotate = true;
                            notLose = true;
                            dontWorryAboutIt = true;
                            rotate180 = false;
                            rotationRight = false;
                            canLock = false;
                            ableToHardDrop = true;
                            swipeSoftDrop = false;
                            softDrop = false;
                            DASR = false;
                            DASL = false;
                            linesNeeded = 10;
                            level = 0;
                            random = 8;
                            random2 = 8;
                            random3 = 8;
                            random4 = 8;
                            for (int i = 0; i <= 6; i++) {
                                bag7[i] = 8;
                            }
                            bag7number = 0;
                            bag7numberCheck = 0;
                            holdPieceNumber = 8;
                            timesHoldPieceUsed = 0;
                            finish();
                        }
                    })
                    .show();
        }
    }

    public void blockPlace(int x, int y, int color) {
        tetrisBoardLock[x][y] = true;
        tetrisBoardColor[x][y] = color;
    }

    public void place2x2(int x, int y, int color) {
        blockPlace(x, y, color);
        blockPlace(x + 1, y, color);
        blockPlace(x + 1, y + 1, color);
        blockPlace(x, y + 1, color);
    }

    public void getRotation() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 30; j++) {
                if (tetrisBoard[i][j]) {
                    tetrisBoard[i][j] = false;
                    tetrisBoardColor[i][j] = 7;
                }
            }
        }
        SRS(pc1, pc2, pc3, pc4);
    }

    public void copyPoints() {
        pc1.set(Rotation.ppp1.x, Rotation.ppp1.y);
        pc2.set(Rotation.ppp2.x, Rotation.ppp2.y);
        pc3.set(Rotation.ppp3.x, Rotation.ppp3.y);
        pc4.set(Rotation.ppp4.x, Rotation.ppp4.y);
    }

    public void translatePoints(int x, int y) {
        pc1.set(pc1.x + x, pc1.y + y);
        pc2.set(pc2.x + x, pc2.y + y);
        pc3.set(pc3.x + x, pc3.y + y);
        pc4.set(pc4.x + x, pc4.y + y);
    }


    public void SRS(Point a, Point b, Point c, Point d) {
        rotationEffortTries++;
        try {
            if (tetrisBoardLock[a.x][a.y] || tetrisBoardLock[b.x][b.y] || tetrisBoardLock[c.x][c.y] || tetrisBoardLock[d.x][d.y]) {
                tetrisBoard[500][500] = true;
            } else {
                tetrisBoard[a.x][a.y] = true;
                tetrisBoard[b.x][b.y] = true;
                tetrisBoard[c.x][c.y] = true;
                tetrisBoard[d.x][d.y] = true;
                tetrisBoardColor[a.x][a.y] = random;
                tetrisBoardColor[b.x][b.y] = random;
                tetrisBoardColor[c.x][c.y] = random;
                tetrisBoardColor[d.x][d.y] = random;
            }
            ghostPiece();
            showBoard();
            rotationEffortTries = 0;
            return;
        } catch (Exception cannotRotate) {

            if (rotationType == 0) {
                SRS(Rotation.pp1, Rotation.pp2, Rotation.pp3, Rotation.pp4);
                if (rotationRight) {
                    rotation.rotateAmount -= 1;
                    if (rotation.rotateAmount <= -1) {
                        rotation.rotateAmount += 4;
                    }
                } else {
                    rotation.rotateAmount += 1;
                    if (rotation.rotateAmount >= 4) {
                        rotation.rotateAmount -= 4;
                    }
                }
            } else {
                copyPoints();
                if (rotationType == 2) {
                    if (rotationEffortTries == 1) {
                        if (rotationRight) {
                            translatePoints(-1, 0);
                        } else {
                            translatePoints(1, 0);
                        }
                        SRS(pc1, pc2, pc3, pc4);
                    } else if (rotationEffortTries == 2) {
                        if (!rotationRight) {
                            translatePoints(1, 0);
                        } else {
                            translatePoints(-1, 0);
                        }
                        SRS(pc1, pc2, pc3, pc4);
                    } else if (rotationEffortTries == 3) {
                        translatePoints(0, -1);
                        SRS(pc1, pc2, pc3, pc4);
                    } else if (rotationEffortTries == 4) {
                        if (rotationRight) {
                            translatePoints(-1, -1);
                        } else {
                            translatePoints(1, -1);
                        }
                        SRS(pc1, pc2, pc3, pc4);
                    } else if (rotationEffortTries == 5) {
                        if (rotationRight) {
                            translatePoints(1, -1);
                        } else {
                            translatePoints(-1, -1);
                        }
                        SRS(pc1, pc2, pc3, pc4);
                    } else if (rotationEffortTries == 6) {
                        translatePoints(0, -2);
                        SRS(pc1, pc2, pc3, pc4);
                    } else if (rotationEffortTries == 7) {
                        if (rotationRight) {
                            translatePoints(-1, -2);
                        } else {
                            translatePoints(1, -2);
                        }
                        SRS(pc1, pc2, pc3, pc4);
                    } else if (rotationEffortTries == 8) {
                        if (rotationRight) {
                            translatePoints(1, -2);
                        } else {
                            translatePoints(-1, -2);
                        }
                        SRS(pc1, pc2, pc3, pc4);
                    } else if (rotationEffortTries == 9) {
                        translatePoints(0, 1);
                        SRS(pc1, pc2, pc3, pc4);
                    } else if (rotationEffortTries == 10) {
                        if (rotationRight) {
                            translatePoints(-1, 1);
                        } else {
                            translatePoints(1, 1);
                        }
                        SRS(pc1, pc2, pc3, pc4);
                    } else if (rotationEffortTries == 11) {
                        if (rotationRight) {
                            translatePoints(1, 1);
                        } else {
                            translatePoints(-1, 1);
                        }
                        SRS(pc1, pc2, pc3, pc4);
                    } else if (rotationEffortTries == 12) {
                        if (rotationRight) {
                            translatePoints(-2, 0);
                        } else {
                            translatePoints(2, 0);
                        }
                        SRS(pc1, pc2, pc3, pc4);
                    } else if (rotationEffortTries == 13) {
                        if (rotationRight) {
                            translatePoints(2, 0);
                        } else {
                            translatePoints(-2, 0);
                        }
                        SRS(pc1, pc2, pc3, pc4);
                    } else {
                        SRS(Rotation.pp1, Rotation.pp2, Rotation.pp3, Rotation.pp4);
                    }
                } else {
                    if (rotationEffortTries >= 5) {
                        SRS(Rotation.pp1, Rotation.pp2, Rotation.pp3, Rotation.pp4);
                    } else {
                        int rotationAmount;
                        if (rotationRight) {
                            rotationAmount = rotation.rotateAmount - 1;
                            if (rotationAmount == -1) {
                                rotationAmount += 4;
                            }
                        } else {
                            rotationAmount = rotation.rotateAmount + 1;
                            if (rotationAmount == 4) {
                                rotationAmount -= 4;
                            }
                        }
                        if (random != 4) {
                            if (rotationAmount == 0) {
                                if (rotationRight) {
                                    if (rotationEffortTries == 1) {
                                        translatePoints(-1, 0);
                                    } else if (rotationEffortTries == 2) {
                                        translatePoints(-1, 1);
                                    } else if (rotationEffortTries == 3) {
                                        translatePoints(0, -2);
                                    } else {
                                        translatePoints(-1, -2);
                                    }
                                } else {
                                    if (rotationEffortTries == 1) {
                                        translatePoints(1, 0);
                                    } else if (rotationEffortTries == 2) {
                                        translatePoints(1, 1);
                                    } else if (rotationEffortTries == 3) {
                                        translatePoints(0, -2);
                                    } else {
                                        translatePoints(1, -2);
                                    }
                                }
                            } else if (rotationAmount == 1) {
                                if (rotationEffortTries == 1) {
                                    translatePoints(1, 0);
                                } else if (rotationEffortTries == 2) {
                                    translatePoints(1, -1);
                                } else if (rotationEffortTries == 3) {
                                    translatePoints(0, 2);
                                } else {
                                    translatePoints(1, 2);
                                }
                            } else if (rotationAmount == 2) {
                                if (rotationRight) {
                                    if (rotationEffortTries == 1) {
                                        translatePoints(1, 0);
                                    } else if (rotationEffortTries == 2) {
                                        translatePoints(1, 1);
                                    } else if (rotationEffortTries == 3) {
                                        translatePoints(0, -2);
                                    } else {
                                        translatePoints(1, -2);
                                    }
                                } else {
                                    if (rotationEffortTries == 1) {
                                        translatePoints(-1, 0);
                                    } else if (rotationEffortTries == 2) {
                                        translatePoints(-1, 1);
                                    } else if (rotationEffortTries == 3) {
                                        translatePoints(0, -2);
                                    } else {
                                        translatePoints(-1, -2);
                                    }
                                }
                            } else {
                                if (rotationEffortTries == 1) {
                                    translatePoints(-1, 0);
                                } else if (rotationEffortTries == 2) {
                                    translatePoints(-1, -1);
                                } else if (rotationEffortTries == 3) {
                                    translatePoints(0, 2);
                                } else {
                                    translatePoints(-1, 2);
                                }
                            }
                            SRS(pc1, pc2, pc3, pc4);
                        } else if (random == 4) {
                            if (rotationAmount == 0) {
                                if (rotationRight) {
                                    if (rotationEffortTries == 1) {
                                        translatePoints(-2, 0);
                                    } else if (rotationEffortTries == 2) {
                                        translatePoints(1, 0);
                                    } else if (rotationEffortTries == 3) {
                                        translatePoints(-2, -1);
                                    } else {
                                        translatePoints(1, 2);
                                    }
                                } else {
                                    if (rotationEffortTries == 1) {
                                        translatePoints(-1, 0);
                                    } else if (rotationEffortTries == 2) {
                                        translatePoints(2, 0);
                                    } else if (rotationEffortTries == 3) {
                                        translatePoints(-1, 2);
                                    } else {
                                        translatePoints(2, -1);
                                    }
                                }
                            } else if (rotationAmount == 1) {
                                if (rotationRight) {
                                    if (rotationEffortTries == 1) {
                                        translatePoints(-1, 0);
                                    } else if (rotationEffortTries == 2) {
                                        translatePoints(2, 0);
                                    } else if (rotationEffortTries == 3) {
                                        translatePoints(-1, 2);
                                    } else {
                                        translatePoints(2, -1);
                                    }
                                } else {
                                    if (rotationEffortTries == 1) {
                                        translatePoints(2, 0);
                                    } else if (rotationEffortTries == 2) {
                                        translatePoints(-1, 0);
                                    } else if (rotationEffortTries == 3) {
                                        translatePoints(2, 1);
                                    } else {
                                        translatePoints(-1, -2);
                                    }

                                }
                            } else if (rotationAmount == 2) {
                                if (rotationRight) {
                                    if (rotationEffortTries == 1) {
                                        translatePoints(2, 0);
                                    } else if (rotationEffortTries == 2) {
                                        translatePoints(-1, 0);
                                    } else if (rotationEffortTries == 3) {
                                        translatePoints(2, 1);
                                    } else {
                                        translatePoints(-1, -2);
                                    }
                                } else {
                                    if (rotationEffortTries == 1) {
                                        translatePoints(1, 0);
                                    } else if (rotationEffortTries == 2) {
                                        translatePoints(-2, 0);
                                    } else if (rotationEffortTries == 3) {
                                        translatePoints(1, -2);
                                    } else {
                                        translatePoints(-2, 1);
                                    }
                                }
                            } else {
                                if (rotationRight) {
                                    if (rotationEffortTries == 1) {
                                        translatePoints(1, 0);
                                    } else if (rotationEffortTries == 2) {
                                        translatePoints(-2, 0);
                                    } else if (rotationEffortTries == 3) {
                                        translatePoints(1, -2);
                                    } else {
                                        translatePoints(-2, 1);
                                    }
                                } else {
                                    if (rotationEffortTries == 1) {
                                        translatePoints(-2, 0);
                                    } else if (rotationEffortTries == 2) {
                                        translatePoints(1, 0);
                                    } else if (rotationEffortTries == 3) {
                                        translatePoints(-2, -1);
                                    } else {
                                        translatePoints(1, 2);
                                    }
                                }
                            }
                            SRS(pc1, pc2, pc3, pc4);
                        }
                    }
                }
            }
        }
    }

    public void holdPiece() {
        Rotation.rotateAmount = 0;
        if (timesHoldPieceUsed == 0 && !paused) {
            for (int i = 0; i < 30; i++) {
                for (int j = 0; j < 10; j++) {
                    if (tetrisBoard[j][i]) {
                        tetrisBoard[j][i] = false;
                        tetrisBoardColor[j][i] = 7;
                    }
                }
            }
            if (holdPieceNumber == 8) {
                timer.removeMessages(0);
                timer.removeMessages(3);
                holdPieceNumber = random;
                getPiece();
            } else {
                timer.removeMessages(0);
                timer.removeMessages(3);
                int temp = random;
                random = holdPieceNumber;
                holdPieceNumber = temp;
                dontWorryAboutIt = true;
                getPiece();
            }
            if (holdPieceNumber == 0) {
                IV4.setImageResource(R.drawable.block_purple);
            } else if (holdPieceNumber == 1) {
                IV4.setImageResource(R.drawable.block_orange);
            } else if (holdPieceNumber == 2) {
                IV4.setImageResource(R.drawable.block_blue);
            } else if (holdPieceNumber == 3) {
                IV4.setImageResource(R.drawable.block_yellow);
            } else if (holdPieceNumber == 4) {
                IV4.setImageResource(R.drawable.block_cyan);
            } else if (holdPieceNumber == 5) {
                IV4.setImageResource(R.drawable.block_red);
            } else if (holdPieceNumber == 6) {
                IV4.setImageResource(R.drawable.block_green);
            } else {
                IV4.setImageResource(R.drawable.tetrisgray);
            }
            timesHoldPieceUsed++;
        }
    }

    void check7bag(int rand) {
        if (rand == 4) {
            for (int i = bag7numberCheck; i >= 0; i--) {
                if (bag7[i] == random4) {
                    random4 = getRandom(7, 0);
                    check7bag(4);
                }
            }
            bag7[bag7number] = random4;
        } else if (rand == 3) {
            if (bag7[0] == random3 || bag7[1] == random3) {
                random3 = getRandom(7, 0);
                check7bag(3);
            }
            bag7[2] = random3;
        } else if (rand == 2) {
            if (bag7[0] == random2) {
                random2 = getRandom(7, 0);
                check7bag(2);
            }
            bag7[1] = random2;
        }
    }

    void getPiece() {
        lockInAir = true;
        TSpin = false;
        if (!dontWorryAboutIt) {
            random = random2;
            random2 = random3;
            random3 = random4;
            random4 = getRandom(7, 0);
            if (randomType == 2) {
                bag7numberCheck++;
                if (bag7numberCheck > 6)
                    bag7numberCheck = 6;
                bag7number++;
                if (bag7number > 6) {
                    for (int i = 0; i <= 6; i++) {
                        bag7[i] = 8;
                    }
                    bag7number = 0;
                }
                check7bag(4);
            } else if (randomType == 1) {
                if (random4 == random3) {
                    random4 = getRandom(7, 0);
                }
            }
        }
        dontWorryAboutIt = false;
        if (random == 0) {
            tetrisBoard[4][22 - 1] = true; // T
            Rotation.point1.set(4, 22 - 1);
            tetrisBoard[3][21 - 1] = true;
            Rotation.point2.set(3, 21 - 1);
            tetrisBoard[4][21 - 1] = true;
            Rotation.point3.set(4, 21 - 1);                      //Center
            tetrisBoard[5][21 - 1] = true;
            Rotation.point4.set(5, 21 - 1);
            tetrisBoardColor[4][22 - 1] = random;
            tetrisBoardColor[3][21 - 1] = random;
            tetrisBoardColor[4][21 - 1] = random;
            tetrisBoardColor[5][21 - 1] = random;
        } else if (random == 1) {
            tetrisBoard[3][21 - 1] = true;
            Rotation.point1.set(3, 21 - 1); // L
            tetrisBoard[4][21 - 1] = true;
            Rotation.point2.set(4, 21 - 1);                        //Center
            tetrisBoard[5][21 - 1] = true;
            Rotation.point3.set(5, 21 - 1);
            tetrisBoard[5][22 - 1] = true;
            Rotation.point4.set(5, 22 - 1);
            tetrisBoardColor[3][21 - 1] = random;
            tetrisBoardColor[4][21 - 1] = random;
            tetrisBoardColor[5][21 - 1] = random;
            tetrisBoardColor[5][22 - 1] = random;
        } else if (random == 2) {
            tetrisBoard[3][21 - 1] = true;
            Rotation.point1.set(3, 21 - 1); // J
            tetrisBoard[4][21 - 1] = true;
            Rotation.point2.set(4, 21 - 1);                        //Center
            tetrisBoard[5][21 - 1] = true;
            Rotation.point3.set(5, 21 - 1);
            tetrisBoard[3][22 - 1] = true;
            Rotation.point4.set(3, 22 - 1);
            tetrisBoardColor[3][21 - 1] = random;
            tetrisBoardColor[4][21 - 1] = random;
            tetrisBoardColor[5][21 - 1] = random;
            tetrisBoardColor[3][22 - 1] = random;
        } else if (random == 3) {
            tetrisBoard[4][22 - 1] = true; // O
            tetrisBoard[5][22 - 1] = true;
            tetrisBoard[4][21 - 1] = true;
            tetrisBoard[5][21 - 1] = true;
            tetrisBoardColor[4][22 - 1] = random;  // O
            tetrisBoardColor[5][22 - 1] = random;
            tetrisBoardColor[4][21 - 1] = random;
            tetrisBoardColor[5][21 - 1] = random;
        } else if (random == 4) {
            tetrisBoard[3][21 - 1] = true;
            Rotation.point1.set(3, 21 - 1); // I
            tetrisBoard[4][21 - 1] = true;
            Rotation.point2.set(3, 21 - 1);                        //Center
            tetrisBoard[5][21 - 1] = true;
            Rotation.point3.set(5, 21 - 1);
            tetrisBoard[6][21 - 1] = true;
            Rotation.point4.set(6, 21 - 1);
            tetrisBoardColor[3][21 - 1] = random;
            tetrisBoardColor[4][21 - 1] = random;
            tetrisBoardColor[5][21 - 1] = random;
            tetrisBoardColor[6][21 - 1] = random;
        } else if (random == 5) {
            tetrisBoard[3][22 - 1] = true;
            Rotation.point1.set(3, 22 - 1); // Z
            tetrisBoard[4][22 - 1] = true;
            Rotation.point2.set(4, 22 - 1);                          //Center
            tetrisBoard[4][21 - 1] = true;
            Rotation.point3.set(4, 21 - 1);
            tetrisBoard[5][21 - 1] = true;
            Rotation.point4.set(5, 21 - 1);
            tetrisBoardColor[3][22 - 1] = random;
            tetrisBoardColor[4][22 - 1] = random;
            tetrisBoardColor[4][21 - 1] = random;
            tetrisBoardColor[5][21 - 1] = random;
        } else if (random == 6) {
            tetrisBoard[5][22 - 1] = true;
            Rotation.point1.set(5, 22 - 1); // S PIECE *
            tetrisBoard[4][22 - 1] = true;
            Rotation.point2.set(4, 22 - 1);                         //Center
            tetrisBoard[4][21 - 1] = true;
            Rotation.point3.set(4, 21 - 1);
            tetrisBoard[3][21 - 1] = true;
            Rotation.point4.set(3, 21 - 1);
            tetrisBoardColor[5][22 - 1] = random;
            tetrisBoardColor[4][22 - 1] = random;
            tetrisBoardColor[4][21 - 1] = random;
            tetrisBoardColor[3][21 - 1] = random;
        }

        nextPieceShow();
        ghostPiece();
        showBoard();
        usePiece();
    }

    public void nextPieceShow() {
        if (random2 == 0) {
            IV.setImageResource(R.drawable.block_purple);
        } else if (random2 == 1) {
            IV.setImageResource(R.drawable.block_orange);
        } else if (random2 == 2) {
            IV.setImageResource(R.drawable.block_blue);
        } else if (random2 == 3) {
            IV.setImageResource(R.drawable.block_yellow);
        } else if (random2 == 4) {
            IV.setImageResource(R.drawable.block_cyan);
        } else if (random2 == 5) {
            IV.setImageResource(R.drawable.block_red);
        } else if (random2 == 6) {
            IV.setImageResource(R.drawable.block_green);
        }
        if (random3 == 0) {
            IV2.setImageResource(R.drawable.block_purple);
        } else if (random3 == 1) {
            IV2.setImageResource(R.drawable.block_orange);
        } else if (random3 == 2) {
            IV2.setImageResource(R.drawable.block_blue);
        } else if (random3 == 3) {
            IV2.setImageResource(R.drawable.block_yellow);
        } else if (random3 == 4) {
            IV2.setImageResource(R.drawable.block_cyan);
        } else if (random3 == 5) {
            IV2.setImageResource(R.drawable.block_red);
        } else if (random3 == 6) {
            IV2.setImageResource(R.drawable.block_green);
        }
        if (random4 == 0) {
            IV3.setImageResource(R.drawable.block_purple);
        } else if (random4 == 1) {
            IV3.setImageResource(R.drawable.block_orange);
        } else if (random4 == 2) {
            IV3.setImageResource(R.drawable.block_blue);
        } else if (random4 == 3) {
            IV3.setImageResource(R.drawable.block_yellow);
        } else if (random4 == 4) {
            IV3.setImageResource(R.drawable.block_cyan);
        } else if (random4 == 5) {
            IV3.setImageResource(R.drawable.block_red);
        } else if (random4 == 6) {
            IV3.setImageResource(R.drawable.block_green);
        }
    }

    public boolean onTouchEvent(MotionEvent e) {
        super.onTouchEvent(e);
        int dir = -1;

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                softDrop = false;
                swipeSoftDrop = false;
                ptTouch.set((int) e.getX(), (int) e.getY());
                canMotionEventRotate = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!paused)
                    dir = touchDir(ptTouch.x, ptTouch.y, (int) e.getX(), (int) e.getY());
                break;
            case MotionEvent.ACTION_UP:
                softDrop = false;
                swipeSoftDrop = false;
                DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
                if (canMotionEventRotate) {
                    if (Math.abs(ptTouch.x - (int) e.getX()) < 15 && Math.abs(ptTouch.y - (int) e.getY()) < 15 && ableToHardDrop && !softDrop && !paused) {
                        if (ptTouch.x > dm.widthPixels / 2) {
                            rotationRight = true;
                            lockTries++;
                            canLock = false;
                            rotation.rotateRight();
                        } else {
                            rotationRight = false;
                            ;
                            lockTries++;
                            canLock = false;
                            rotation.rotateLeft();
                        }
                    }
                }
                ableToHardDrop = true;
                break;
        }

        if (dir >= 0 && !paused) {
            if (dir <= 1) {
                softDrop = false;
                swipeSoftDrop = false;
                canMotionEventRotate = false;
                moveHorizontalOnce(dir);
            } else {
                swipeSoftDrop = true;
                canMotionEventRotate = false;
                if (dir == 3) {
                    if (ableToHardDrop) {
                        hardDropTest++;
                    } else {
                        hardDropTest = 0;
                    }
                    if (hardDropTest > 2.5) {
                        ableToHardDrop = false;
                        hardDrop();
                    } else {
                        timer.sendEmptyMessageDelayed(5, 120);
                    }
                    if (ableToHardDrop && swipeSoftDrop) {
                        softDrop = true;
                        swipeSoftDrop = false;
                        timer.removeMessages(0);
                        timer.removeMessages(3);
                        timer.sendEmptyMessageDelayed(3, (dropSpeed / 10));
                    }
                    showBoard();
                } else {
                    holdPiece();
                }
            }
            ptTouch.set((int) e.getX(), (int) e.getY());
        }
        return true;
    }

    int touchDir(int x1, int y1, int x2, int y2) {
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int moveLength = dm.widthPixels / 10;
        if (Math.abs(x2 - x1) > moveLength) {
            if (x2 < x1)
                return 0;
            else
                return 1;
        } else if (Math.abs(y2 - y1) > moveLength) {
            if (y2 < y1)
                return 2;
            else
                return 3;
        }
        return -1;
    }

    public void moveHorizontal(int a) {
        TSpin = false;
        lockTries++;
        canLock = false;
        if (ARR != 0) {
            if (a == 0 && !paused) {
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 30; j++) {
                        if (tetrisBoard[i][j]) {
                            if (i == 0) {
                                return;
                            } else {
                                if (tetrisBoardLock[i - 1][j])
                                    return;
                            }
                        }
                    }
                }
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 30; j++) {
                        if (tetrisBoard[i][j]) {
                            tetrisBoard[i][j] = false;
                            tetrisBoardColor[i - 1][j] = tetrisBoardColor[i][j];
                            tetrisBoardColor[i][j] = 7;
                            tetrisBoard[i - 1][j] = true;
                        }
                    }
                }
                ghostPiece();
                showBoard();
            } else if (a == 1 && !paused) {
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 30; j++) {
                        if (tetrisBoard[i][j]) {
                            if (i == 9) {
                                return;
                            } else {
                                if (tetrisBoardLock[i + 1][j])
                                    return;
                            }
                        }
                    }
                }
                for (int i = 9; i >= 0; i--) {
                    for (int j = 0; j < 30; j++) {
                        if (tetrisBoard[i][j]) {
                            tetrisBoard[i][j] = false;
                            tetrisBoardColor[i + 1][j] = tetrisBoardColor[i][j];
                            tetrisBoardColor[i][j] = 7;
                            tetrisBoard[i + 1][j] = true;
                        }
                    }
                }
                ghostPiece();
                showBoard();
            }
        } else {
            if (a == 0 && !paused) {
                for (; ; ) {
                    for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 30; j++) {
                            if (tetrisBoard[i][j]) {
                                if (i == 0) {
                                    return;
                                } else {
                                    if (tetrisBoardLock[i - 1][j])
                                        return;
                                }
                            }
                        }
                    }
                    for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 30; j++) {
                            if (tetrisBoard[i][j]) {
                                tetrisBoard[i][j] = false;
                                tetrisBoardColor[i - 1][j] = tetrisBoardColor[i][j];
                                tetrisBoardColor[i][j] = 7;
                                tetrisBoard[i - 1][j] = true;
                            }
                        }
                    }
                    ghostPiece();
                    showBoard();
                }
            } else if (a == 1 && !paused) {
                for (; ; ) {
                    for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 30; j++) {
                            if (tetrisBoard[i][j]) {
                                if (i == 9) {
                                    return;
                                } else {
                                    if (tetrisBoardLock[i + 1][j])
                                        return;
                                }
                            }
                        }
                    }
                    for (int i = 9; i >= 0; i--) {
                        for (int j = 0; j < 30; j++) {
                            if (tetrisBoard[i][j]) {
                                tetrisBoard[i][j] = false;
                                tetrisBoardColor[i + 1][j] = tetrisBoardColor[i][j];
                                tetrisBoardColor[i][j] = 7;
                                tetrisBoard[i + 1][j] = true;
                            }
                        }
                    }
                    ghostPiece();
                    showBoard();
                }
            }
        }
    }

    public void moveHorizontalOnce(int a) {
        TSpin = false;
        lockTries++;
        canLock = false;
        if (a == 0 && !paused) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 30; j++) {
                    if (tetrisBoard[i][j]) {
                        if (i == 0) {
                            return;
                        } else {
                            if (tetrisBoardLock[i - 1][j])
                                return;
                        }
                    }
                }
            }
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 30; j++) {
                    if (tetrisBoard[i][j]) {
                        tetrisBoard[i][j] = false;
                        tetrisBoardColor[i - 1][j] = tetrisBoardColor[i][j];
                        tetrisBoardColor[i][j] = 7;
                        tetrisBoard[i - 1][j] = true;
                    }
                }
            }
            ghostPiece();
            showBoard();
        } else if (a == 1 && !paused) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 30; j++) {
                    if (tetrisBoard[i][j]) {
                        if (i == 9) {
                            return;
                        } else {
                            if (tetrisBoardLock[i + 1][j])
                                return;
                        }
                    }
                }
            }
            for (int i = 9; i >= 0; i--) {
                for (int j = 0; j < 30; j++) {
                    if (tetrisBoard[i][j]) {
                        tetrisBoard[i][j] = false;
                        tetrisBoardColor[i + 1][j] = tetrisBoardColor[i][j];
                        tetrisBoardColor[i][j] = 7;
                        tetrisBoard[i + 1][j] = true;
                    }
                }
            }
            ghostPiece();
            showBoard();
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ButtonLeft: {
                if (!paused) {
                    lockTries++;
                    canLock = false;
                }
                break;
            }
            case R.id.ButtonRight: {
                if (!paused) {
                    lockTries++;
                    canLock = false;
                }
                break;
            }
        }
    }

    public void onDropClick(View v) {
        if (!paused) {
            switch (v.getId()) {
                case R.id.ButtonSoftDrop: {
                    break;
                }
                case R.id.ButtonHardDrop: {
                    hardDrop();
                    break;
                }
                case R.id.ButtonRotate180: {
                    if (rotationType != 0) {
                        TSpin = true;
                        lockTries++;
                        canLock = false;
                        rotate180 = true;
                        rotation.rotateLeft();
                        rotate180 = false;
                        rotation.rotateLeft();
                    }
                    break;
                }
                case R.id.ButtonHold: {
                    lockTries++;
                    canLock = false;
                    holdPiece();
                    break;
                }
            }
        }
    }

    public void hardDrop() {
        TSpin = false;
        if (!paused) {
            bigloop:
            for (; ; ) {
                for (int i = 0; i <= 29; i++) {
                    for (int j = 0; j < 10; j++) {
                        if (tetrisBoard[j][i]) {
                            if (i != 0) {
                                if (tetrisBoardLock[j][i - 1]) {
                                    lockPiece();
                                    break bigloop;
                                }
                            } else {
                                lockPiece();
                                break bigloop;
                            }
                        }

                    }
                }
                for (int i = 0; i <= 29; i++) {
                    for (int j = 0; j < 10; j++) {
                        if (!tetrisBoardLock[j][i]) {
                            if (tetrisBoard[j][i]) {

                                tetrisBoard[j][i - 1] = true;
                                if (tetrisBoardColor[j][i] == 10) {
                                    tetrisBoardColor[j][i] = 7;
                                } else {
                                    tetrisBoardColor[j][i - 1] = tetrisBoardColor[j][i];
                                    tetrisBoardColor[j][i] = 7;
                                }
                                tetrisBoard[j][i] = false;
                            }
                        }
                    }
                }
            }
        }
    }

    public void onRotateClick(View v) {
        if (!paused) {
            switch (v.getId()) {
                case R.id.ButtonRotateLeft: {
                    rotationRight = false;
                    TSpin = true;
                    lockTries++;
                    canLock = false;
                    rotation.rotateLeft();
                    break;
                }
                case R.id.ButtonRotateRight: {
                    rotationRight = true;
                    TSpin = true;
                    lockTries++;
                    canLock = false;
                    rotation.rotateRight();
                    break;
                }
            }
        }
    }

    public void ghostPiece() {
        try {
            for (int i = 0; i < 25; i++) {
                for (int j = 0; j < 10; j++) {
                    if (tetrisBoard[j][i]) {
                        tetrisGhostPiece[j][i] = true;
                    } else {
                        tetrisGhostPiece[j][i] = false;
                    }
                }
            }

            bigloop:
            for (; ; ) {
                for (int i = 0; i <= 24; i++) {
                    for (int j = 0; j < 10; j++) {
                        if (tetrisGhostPiece[j][i]) {
                            if (i != 0) {
                                if (tetrisBoardLock[j][i - 1]) {
                                    break bigloop;
                                }
                            } else {
                                break bigloop;
                            }
                        }

                    }
                }
                for (int i = 0; i <= 24; i++) {
                    for (int j = 0; j < 10; j++) {
                        if (tetrisGhostPiece[j][i]) {
                            tetrisGhostPiece[j][i - 1] = true;
                            tetrisGhostPiece[j][i] = false;
                        }
                    }
                }
            }

            for (int i = 0; i < 25; i++) {
                for (int j = 0; j < 10; j++) {
                    if (tetrisGhostPiece[j][i]) {
                        if (tetrisBoardColor[j][i] == 7) {
                            tetrisBoardColor[j][i] = 10;
                        }
                    } else {
                        if (tetrisBoardColor[j][i] == 10) {
                            tetrisBoardColor[j][i] = 7;
                        }
                    }
                }
            }
        } catch (Exception badGhost) {

        }
    }

    public void getCenter() {
        double rounded = (rotation.point1.x + rotation.point2.x + rotation.point3.x + rotation.point4.x);
        double roundedX = rounded / 4;
        rounded = (rotation.point1.y + rotation.point2.y + rotation.point3.y + rotation.point4.y);
        double roundedY = rounded / 4;

        if (roundedX % 1 > 0.5) {
            roundedX += 1 - (roundedX % 1);
        } else {
            roundedX -= (roundedX % 1);
        }
        if (roundedY % 1 > 0.5) {
            roundedY += 1 - (roundedY % 1);
        } else {
            roundedY -= (roundedY % 1);
        }

        centerX = (int) roundedX;
        centerY = (int) roundedY;
    }

    public void checkLine() {
        int tSpinCorners = 0;
        int tSpinTopCorners = 0;
        linesClearedAtOnce = 0;
        if (random != 0) {
            for (int runs = 0; runs < 4; runs++) {
                for (int j = 0; j < 30; j++) {
                    bigif:
                    if (tetrisBoardLock[0][j]) {
                        for (int i = 0; i < 10; i++) {
                            if (tetrisBoardLock[i][j]) {
                            } else {
                                break bigif;
                            }
                        }

                        lines += 1;
                        linesNeeded--;
                        linesClearedAtOnce++;
                        for (int i = 0; i < 10; i++) {
                            tetrisBoardLock[i][j] = false;
                            tetrisBoardColor[i][j] = 7;
                        }
                        for (int i = 0; i < 10; i++) {
                            for (int k = j; k < 30; k++) {
                                if (tetrisBoardLock[i][k]) {
                                    tetrisBoardLock[i][k] = false;
                                    tetrisBoardLock[i][k - 1] = true;
                                    tetrisBoardColor[i][k - 1] = tetrisBoardColor[i][k];
                                    tetrisBoardColor[i][k] = 7;
                                }
                            }
                        }
                    }
                }
            }

            if (tSpinCorners <= 2) {//tpsin//
                TSpin = false;
            }

            if (linesNeeded <= 0) {
                level++;
                linesNeeded += 10;
                TV4.setText("Level : " + level);
            }

            Boolean PC = true;

            PC:
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 30; j++) {
                    if (tetrisBoardLock[i][j]) {
                        PC = false;
                        break PC;
                    }
                }
            }

            if (PC) {
                if (linesClearedAtOnce == 1) {
                    score += 100 * (level + 1) + combo * 50 + 4000 * (level + 1);
                    piecesSinceCombo = 0;
                    combo++;
                    TV5.append("Perfect\nClear!!");
                } else if (linesClearedAtOnce == 2) {
                    score += 300 * (level + 1) + combo * 50 + 4000 * (level + 1);
                    piecesSinceCombo = 0;
                    combo++;
                    TV5.append("Perfect\nClear!!");
                } else if (linesClearedAtOnce == 3) {
                    score += 500 * (level + 1) + combo * 50 + 4000 * (level + 1);
                    piecesSinceCombo = 0;
                    combo++;
                    TV5.append("Perfect\nClear!!");
                } else if (linesClearedAtOnce == 4) {
                    if (B2B)
                        score += 1200 * (level + 1) + combo * 50 + 4000 * (level + 1);
                    else
                        score += 800 * (level + 1) + combo * 50 + 4000 * (level + 1);
                    piecesSinceCombo = 0;
                    combo++;
                    TV5.append("Perfect\nClear!!");
                    B2B = true;
                }
                if (TSpin) {
                    if (linesClearedAtOnce == 1) {
                        if (B2B) {
                            if (tSpinTopCorners == 1) {
                                score += 1200 * (level + 1) + combo * 50;
                                piecesSinceCombo = 0;
                                combo++;
                                TV5.append("T-S S\n");
                            } else {
                                score += 300 * (level + 1) + combo * 50;
                                piecesSinceCombo = 0;
                                combo++;
                                TV5.append("T-SM S\n");
                            }
                        } else {
                            if (tSpinTopCorners == 1) {
                                score += 800 * (level + 1) + combo * 50;
                                piecesSinceCombo = 0;
                                combo++;
                                TV5.append("T-S S\n");
                            } else {
                                score += 200 * (level + 1) + combo * 50;
                                piecesSinceCombo = 0;
                                combo++;
                                TV5.append("T-SM S\n");
                            }
                        }
                    } else if (linesClearedAtOnce == 2) {
                        if (B2B) {
                            if (tSpinTopCorners == 1) {
                                score += 1800 * (level + 1) + combo * 50;
                                piecesSinceCombo = 0;
                                combo++;
                                TV5.append("T-S D\n");
                            } else {
                                score += 600 * (level + 1) + combo * 50;
                                piecesSinceCombo = 0;
                                combo++;
                                TV5.append("T-SM D\n");
                            }
                        } else {
                            if (tSpinTopCorners == 1) {
                                score += 1200 * (level + 1) + combo * 50;
                                piecesSinceCombo = 0;
                                combo++;
                                TV5.append("T-S D\n");
                            } else {
                                score += 400 * (level + 1) + combo * 50;
                                piecesSinceCombo = 0;
                                combo++;
                                TV5.append("T-SM D\n");
                            }
                        }
                    } else if (linesClearedAtOnce == 3) {
                        if (B2B) {
                            score += 2400 * (level + 1) + combo * 50;
                            piecesSinceCombo = 0;
                            combo++;
                            TV5.append("T-S T\n");
                        } else {
                            score += 1600 * (level + 1) + combo * 50;
                            piecesSinceCombo = 0;
                            combo++;
                            TV5.append("T-S T\n");
                        }
                    } else if (linesClearedAtOnce == 0) {
                        if (B2B) {
                            if (tSpinTopCorners == 1) {
                                score += 600 * (level + 1) + combo * 50;
                                TV5.append("T-S\n");
                            } else {
                                score += 150 * (level + 1) + combo * 50;
                                TV5.append("T-SM\n");
                            }
                        } else {
                            if (tSpinTopCorners == 1) {
                                score += 400 * (level + 1) + combo * 50;
                                TV5.append("T-S\n");
                            } else {
                                score += 100 * (level + 1) + combo * 50;
                                TV5.append("T-SM\n");
                            }
                        }
                    }
                    B2B = true;
                } else {
                    if (linesClearedAtOnce == 1) {
                        score += 100 * (level + 1) + combo * 50;
                        piecesSinceCombo = 0;
                        combo++;
                        TV5.append("Single\n");
                        B2B = false;
                    } else if (linesClearedAtOnce == 2) {
                        score += 300 * (level + 1) + combo * 50;
                        piecesSinceCombo = 0;
                        combo++;
                        TV5.append("Double\n");
                        B2B = false;
                    } else if (linesClearedAtOnce == 3) {
                        score += 500 * (level + 1) + combo * 50;
                        piecesSinceCombo = 0;
                        combo++;
                        TV5.append("Triple\n");
                        B2B = false;
                    } else if (linesClearedAtOnce == 4) {
                        if (B2B)
                            score += 1200 * (level + 1) + combo * 50;
                        else
                            score += 800 * (level + 1) + combo * 50;
                        piecesSinceCombo = 0;
                        combo++;
                        TV5.append("Tetris\n");
                        B2B = true;
                    }
                }
            } else if (TSpin) {
                if (linesClearedAtOnce == 1) {
                    if (B2B) {
                        if (tSpinTopCorners == 1) {
                            score += 1200 * (level + 1) + combo * 50;
                            piecesSinceCombo = 0;
                            combo++;
                            TV5.append("T-S S\n");
                        } else {
                            score += 300 * (level + 1) + combo * 50;
                            piecesSinceCombo = 0;
                            combo++;
                            TV5.append("T-SM S\n");
                        }
                    } else {
                        if (tSpinTopCorners == 1) {
                            score += 800 * (level + 1) + combo * 50;
                            piecesSinceCombo = 0;
                            combo++;
                            TV5.append("T-S S\n");
                        } else {
                            score += 200 * (level + 1) + combo * 50;
                            piecesSinceCombo = 0;
                            combo++;
                            TV5.append("T-SM S\n");
                        }
                    }
                } else if (linesClearedAtOnce == 2) {
                    if (B2B) {
                        if (tSpinTopCorners == 1) {
                            score += 1800 * (level + 1) + combo * 50;
                            piecesSinceCombo = 0;
                            combo++;
                            TV5.append("T-S D\n");
                        } else {
                            score += 600 * (level + 1) + combo * 50;
                            piecesSinceCombo = 0;
                            combo++;
                            TV5.append("T-SM D\n");
                        }
                    } else {
                        if (tSpinTopCorners == 1) {
                            score += 1200 * (level + 1) + combo * 50;
                            piecesSinceCombo = 0;
                            combo++;
                            TV5.append("T-S D\n");
                        } else {
                            score += 400 * (level + 1) + combo * 50;
                            piecesSinceCombo = 0;
                            combo++;
                            TV5.append("T-SM D\n");
                        }
                    }
                } else if (linesClearedAtOnce == 3) {
                    if (B2B) {
                        score += 2400 * (level + 1) + combo * 50;
                        piecesSinceCombo = 0;
                        combo++;
                        TV5.append("T-S T\n");
                    } else {
                        score += 1600 * (level + 1) + combo * 50;
                        piecesSinceCombo = 0;
                        combo++;
                        TV5.append("T-S T\n");
                    }
                } else if (linesClearedAtOnce == 0) {
                    if (B2B) {
                        if (tSpinTopCorners == 1) {
                            score += 600 * (level + 1) + combo * 50;
                            TV5.append("T-S\n");
                        } else {
                            score += 150 * (level + 1) + combo * 50;
                            TV5.append("T-SM\n");
                        }
                    } else {
                        if (tSpinTopCorners == 1) {
                            score += 400 * (level + 1) + combo * 50;
                            TV5.append("T-S\n");
                        } else {
                            score += 100 * (level + 1) + combo * 50;
                            TV5.append("T-SM\n");
                        }
                    }
                }
                B2B = true;
            } else {
                if (linesClearedAtOnce == 1) {
                    score += 100 * (level + 1) + combo * 50;
                    piecesSinceCombo = 0;
                    combo++;
                    TV5.append("Single\n");
                    B2B = false;
                } else if (linesClearedAtOnce == 2) {
                    score += 300 * (level + 1) + combo * 50;
                    piecesSinceCombo = 0;
                    combo++;
                    TV5.append("Double\n");
                    B2B = false;
                } else if (linesClearedAtOnce == 3) {
                    score += 500 * (level + 1) + combo * 50;
                    piecesSinceCombo = 0;
                    combo++;
                    TV5.append("Triple\n");
                    B2B = false;
                } else if (linesClearedAtOnce == 4) {
                    if (B2B)
                        score += 1200 * (level + 1) + combo * 50;
                    else
                        score += 800 * (level + 1) + combo * 50;
                    piecesSinceCombo = 0;
                    combo++;
                    TV5.append("Tetris\n");
                    B2B = true;
                }
            }
            if (combo > 1) {
                TV5.append((combo - 1) + " Com\n");
            }
            TV3.setText("Lines : " + lines);
            TV2.setText("Score : " + score);

        } else if (playMode.equals("Sprint")) {

            for (int runs = 0; runs < 3; runs++) {
                for (int j = 0; j < 26; j++) {
                    bigif:
                    if (tetrisBoardLock[0][j]) {
                        for (int i = 0; i < 10; i++) {
                            if (tetrisBoardLock[i][j]) {
                            } else {
                                break bigif;
                            }
                        }
                        lines += 1;
                        for (int i = 0; i < 10; i++) {
                            tetrisBoardLock[i][j] = false;
                            tetrisBoardColor[i][j] = 7;
                        }
                        for (int i = 0; i < 10; i++) {
                            for (int k = j; k < 30; k++) {
                                if (tetrisBoardLock[i][k]) {
                                    tetrisBoardLock[i][k] = false;
                                    tetrisBoardLock[i][k - 1] = true;
                                    tetrisBoardColor[i][k - 1] = tetrisBoardColor[i][k];
                                    tetrisBoardColor[i][k] = 7;
                                }
                            }
                        }
                    }
                }
            }

            TV3.setText("Lines : " + lines);
            if (lines >= 40) {/////////////////////////////////////////////////////////////////////////////////////SPRINT WIN CONDITION/////////////////////////////
                lose();
            }
        }
    }

    public int getRandom(int a, int b) {
        return (int) (Math.random() * a) + b;
    }

    public void lockPiece() {
        timer.removeMessages(0);
        canRotate = false;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 30; j++) {
                if (tetrisBoard[i][j]) {
                    tetrisBoard[i][j] = false;
                    tetrisBoardLock[i][j] = true;
                    tetrisBoardColor[i][j] = random;
                }
            }
        }
        for (int times = 4; times < 7; times++) {
            if (tetrisBoardLock[times][21] || tetrisBoard[times][21])
                lose();
        }
        piecesSinceCombo++;
        if (piecesSinceCombo > 1) {
            combo = 0;
        }
        timesHoldPieceUsed = 0;
        lockTries = 0;
        checkLine();
        Rotation.rotateAmount = 0; // https://github.com/DonggeunJung/Tetris
        getPiece();
    }

    Handler timer = new Handler() {
        public void handleMessage(Message msg) {

            if (!paused) {

                bigloop:
                switch (msg.what) {

                    case 0: {

                        if (!softDrop) {
                            Log.d("Muffin", "Pie");
                            for (int i = 0; i <= 29; i++) {
                                for (int j = 0; j < 10; j++) {
                                    if (!tetrisBoardLock[j][i]) {
                                        if (tetrisBoard[j][i]) {
                                            if (i != 0) {
                                                if (tetrisBoardLock[j][i - 1]) {
                                                    lockInAir = false;
                                                    if (lockTries > 15) {
                                                        lockPiece();
                                                        break bigloop;
                                                    } else {
                                                        if (canLock) {
                                                            lockPiece();
                                                            break bigloop;
                                                        }
                                                        canLock = true;
                                                        timer.removeMessages(0);
                                                        timer.sendEmptyMessageDelayed(0, lockDelay);
                                                        break bigloop;
                                                    }
                                                }
                                            } else {
                                                lockInAir = false;
                                                if (lockTries > 15) {
                                                    lockPiece();
                                                    break bigloop;
                                                } else {
                                                    if (canLock) {
                                                        lockPiece();
                                                        break bigloop;
                                                    }
                                                    canLock = true;
                                                    timer.removeMessages(0);
                                                    timer.sendEmptyMessageDelayed(0, lockDelay);
                                                    break bigloop;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (lockInAir)
                                lockTries = 0;
                            for (int i = 0; i <= 29; i++) {
                                for (int j = 0; j < 10; j++) {
                                    if (!tetrisBoardLock[j][i]) {
                                        if (tetrisBoard[j][i]) {

                                            Log.d("tag", "handleMessage-1 " + j + " / " + i);
                                            tetrisBoard[j][i - 1] = true;
                                            tetrisBoardColor[j][i - 1] = random;
                                            tetrisBoard[j][i] = false;
                                            tetrisBoardColor[j][i] = 7;
                                        }
                                    }
                                }
                            }
                            TSpin = false;
                            ghostPiece();
                            showBoard();
                            usePiece();
                        } else {
                            timer.removeMessages(0);
                            timer.sendEmptyMessageDelayed(3, (dropSpeed / 10));
                        }
                        break;

                    }
                    case 1: { //This case is the one that decides
                        break;
                    }
                    case 2: {
                        getRotation();
                        break;
                    }
                    case 3: {
                        Log.d("Muffin", "Pie");
                        for (int i = 0; i <= 29; i++) {
                            for (int j = 0; j < 10; j++) {
                                if (!tetrisBoardLock[j][i]) {
                                    if (tetrisBoard[j][i]) {
                                        if (i != 0) {
                                            if (tetrisBoardLock[j][i - 1]) {
                                                lockInAir = false;
                                                if (lockTries > 15) {
                                                    lockPiece();
                                                    break bigloop;
                                                } else {
                                                    if (canLock) {
                                                        lockPiece();
                                                        break bigloop;
                                                    }
                                                    canLock = true;
                                                    timer.removeMessages(0);
                                                    timer.sendEmptyMessageDelayed(0, lockDelay);
                                                    break bigloop;
                                                }
                                            }
                                        } else {
                                            lockInAir = false;
                                            if (lockTries > 15) {
                                                lockPiece();
                                                break bigloop;
                                            } else {
                                                if (canLock) {
                                                    lockPiece();
                                                    break bigloop;
                                                }
                                                canLock = true;
                                                timer.removeMessages(0);
                                                timer.sendEmptyMessageDelayed(0, lockDelay);
                                                break bigloop;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (lockInAir)
                            lockTries = 0;
                        for (int i = 0; i <= 29; i++) {
                            for (int j = 0; j < 10; j++) {
                                if (!tetrisBoardLock[j][i]) {
                                    if (tetrisBoard[j][i]) {

                                        Log.d("tag", "handleMessage-1 " + j + " / " + i);
                                        tetrisBoard[j][i - 1] = true;
                                        tetrisBoardColor[j][i - 1] = random;
                                        tetrisBoard[j][i] = false;
                                        tetrisBoardColor[j][i] = 7;
                                    }
                                }
                            }
                        }
                        ghostPiece();
                        showBoard();
                        timer.removeMessages(0);
                        if (softDrop) {
                            timer.sendEmptyMessageDelayed(3, (dropSpeed / softDropSpeed));
                        } else {
                            timer.removeMessages(3);
                            timer.sendEmptyMessageDelayed(0, dropSpeed);
                        }
                        break;
                    }
                    case 4: {
                        ARRRun = true;
                        if (DASL && DASR) {
                        } else {
                            if (DASL) {
                                if (ARR != 0) {
                                    if (ARRRun) {
                                        moveHorizontal(0);
                                        timer.sendEmptyMessageDelayed(4, ARR);
                                        ARRRun = false;
                                    } else {
                                        timer.sendEmptyMessageDelayed(4, DAS);
                                    }
                                } else {
                                    if (ARRRun) {
                                        moveHorizontal(0);
                                        ARRRun = false;
                                    } else {
                                        timer.sendEmptyMessageDelayed(4, DAS);
                                    }
                                }
                            }
                            if (DASR) {
                                if (ARR != 0) {
                                    if (ARRRun) {
                                        moveHorizontal(1);
                                        timer.sendEmptyMessageDelayed(4, ARR);
                                        ARRRun = false;
                                    } else {
                                        timer.sendEmptyMessageDelayed(4, DAS);
                                    }
                                } else {
                                    if (ARRRun) {
                                        moveHorizontal(1);
                                        ARRRun = false;
                                    } else {
                                        timer.sendEmptyMessageDelayed(4, DAS);
                                    }
                                }
                            }
                        }
                        //ARRRun = true;
                        break;
                    }
                    case 5: {
                        hardDropTest--;
                        if (hardDropTest < 0) {
                            hardDropTest = 0;
                        }
                        break;
                    }
                    case 6: {
                        break;
                    }
                    case 7: {
                        sprintTime = (long) (SystemClock.uptimeMillis() - (long) startTime);
                        int ms = (int) (sprintTime % 1000);
                        int s = (int) ((sprintTime % 60000) / 1000);
                        int m = (int) ((sprintTime % 360000) / 60000);
                        sprintTimeString = String.format("%02d : %02d : %03d", m, s, ms);
                        TV2.setText(sprintTimeString);
                        timer.sendEmptyMessageDelayed(7, sprintUpdate);
                    }
                }   //End of the switch  statement
            }
        }

    };

    void usePiece() {
        if (notLose) {
            if (level < 17) {
                if (level % 2 == 0) {
                    if (level % 4 == 0) {
                        if (level == 0) {
                            dropSpeed = 1000;
                        } else if (level == 4) {
                            dropSpeed = 700;
                        } else if (level == 8) {
                            dropSpeed = 500;
                        } else if (level == 12) {
                            dropSpeed = 300;
                        } else if (level == 16) {
                            dropSpeed = 100;
                        }
                    } else {
                        if (level == 2) {
                            dropSpeed = 800;
                        } else if (level == 6) {
                            dropSpeed = 600;
                        } else if (level == 10) {
                            dropSpeed = 400;
                        } else if (level == 14) {
                            dropSpeed = 200;
                        }
                    }
                } else {
                    if (level % 4 == 1) {
                        if (level == 1) {
                            dropSpeed = 900;
                        } else if (level == 5) {
                            dropSpeed = 650;
                        } else if (level == 9) {
                            dropSpeed = 450;
                        } else if (level == 13) {
                            dropSpeed = 250;
                        }
                    } else {
                        if (level == 3) {
                            dropSpeed = 750;
                        } else if (level == 7) {
                            dropSpeed = 550;
                        } else if (level == 11) {
                            dropSpeed = 350;
                        } else if (level == 15) {
                            dropSpeed = 150;
                        }
                    }
                }
            } else {
                lockDelay = 500;
                dropSpeed = 80;
            }
            timer.sendEmptyMessageDelayed(0, dropSpeed);
            canRotate = true;
        }
    }
}