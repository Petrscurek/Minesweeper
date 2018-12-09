package com.tamz.android.mines;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;



@SuppressWarnings("all")
public class Mines extends View implements View.OnTouchListener {


    class Tile {
        boolean bomb = false;
        boolean covered = true;
        boolean marked = false;
        int bombs = 0;
    }


    private Tile grid[][];
    private int bombsTotal;
    private int time;
    private int gameState;

    private Activity activity;

    private boolean markMode;

    public Mines(Context c) {
        super(c);
    }

    public Mines(Context c, AttributeSet a) {
        super(c, a);
    }

    public void setup(Activity a, int size, int count) {
        this.bombsTotal = count;
        this.grid = new Tile[size][size];

        this.activity = a;

        setOnTouchListener(this);

        this.time = 0;
        this.gameState = 0;
        this.markMode = false;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.grid[i][j] = new Tile();
            }
        }

        Random rnd = new Random();
        for (int i = 0; i < count;) {
            int rx = rnd.nextInt(size);
            int ry = rnd.nextInt(size);
            if(!this.grid[rx][ry].bomb) {
                this.grid[rx][ry].bomb = true;
                i++;
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.grid[i][j].bombs = countAround(i, j);
            }
        }

        ((Button) this.activity.findViewById(R.id.button2)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setMarkMode(false);
            }
        });

        ((Button) this.activity.findViewById(R.id.button3)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setMarkMode(true);
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            clickTile((int) (e.getX() / (v.getWidth() / this.grid.length)), (int) (e.getY() / (v.getWidth() / this.grid.length)));
        }

        return true;
    }

    public void setMarkMode(boolean value) {
        this.markMode = value;
    }

    private void clickTile(int x, int y) {
        if (this.markMode) {
            markTile(x, y);
        } else {
            uncoverTile(x, y);

            if (countCovered() == bombsTotal) {
                this.gameState = 1;
            }
        }
    }

    private boolean is(int x, int y) {
        return x >= 0 && y >= 0 && x < this.grid.length && y < this.grid.length;
    }

    private void markTile(int x, int y) {
        if (is(x, y) && this.grid[x][y].covered) {
            this.grid[x][y].marked ^= true;
        }
    }

    public boolean isWon() {
        return this.gameState == 1;
    }

    public boolean isLost() {
        return this.gameState == -1;
    }

    public int countCovered() {
        int count = 0;
        for (int i = 0; i < this.grid.length; i++) for (int j = 0; j < this.grid.length; j++) {
            if (this.grid[i][j].covered) {
                count++;
            }
        }

        return count;
    }

    public int countTime() {
        return this.time / 60;
    }

    public int countTotal() {
        return this.grid.length * this.grid.length;
    }

    private void uncoverTile(int x, int y) {
        if (is(x, y) && !this.grid[x][y].marked && this.grid[x][y].covered) {
            uncoverAll(x, y);
        }
    }

    private void uncoverAll(int x, int y) {
        if (is(x, y) && this.grid[x][y].covered) {
            if (this.grid[x][y].bomb) {
                this.gameState = -1;
            } else {
                this.grid[x][y].covered = false;
                if (this.grid[x][y].bombs == 0) {
                    uncoverAll(x - 1, y);
                    uncoverAll(x + 1, y);
                    uncoverAll(x, y - 1);
                    uncoverAll(x, y + 1);
                }
            }
        }
    }

    private int countAround(int x, int y) {
        int count = 0;
        for (int i = -1; i <= 1; i++) for (int j = -1; j <= 1; j++) {
            if (is(x + i, y + j) && this.grid[x + i][y + j].bomb) {
                count++;
            }
        }

        return count;
    }

    private void tick() {
        ((TextView) this.activity.findViewById(R.id.textView8)).setText(Integer.toString(countTime()));
        ((TextView) this.activity.findViewById(R.id.textView11)).setText(Integer.toString(bombsTotal));
        ((TextView) this.activity.findViewById(R.id.textView12)).setText(Integer.toString(countCovered()));
        ((TextView) this.activity.findViewById(R.id.textView)).setText(this.markMode ? "MARK" : "UNCOVER");
    }

    @Override
    protected void onMeasure(int w, int h) {
        super.onMeasure(w, h);

        setMeasuredDimension(w, w);
    }

    private static Bitmap bitmaps[] = new Bitmap[11];
    private static boolean loaded = false;

    public static void load(Resources r) {
        if (!loaded ) {
            loaded = true;

            bitmaps[9] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.mine), 50, 50, false);
            bitmaps[10] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.flag), 50, 50, false);

            bitmaps[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.def), 50, 50, false);
            for (int i = 1; i < 9; i++) {//nacist obrazky poli s cislama
                bitmaps[i] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(r, R.drawable.s1 + i - 1), 50, 50, false);
            }
        }
    }

    private static Paint blackPaint = new Paint();
    private static Rect rect = new Rect();
    static {
        blackPaint.setColorFilter(new PorterDuffColorFilter(Color.argb(100, 0, 0, 0), PorterDuff.Mode.DARKEN));
    }

    @Override
    public void onDraw(Canvas c) {
        if (this.grid != null && this.gameState == 0) {
            this.time++;

            c.drawRGB(0xFF, 0xFF, 0xFF);

            int tile = c.getWidth() / this.grid.length;

            for (int i = 0; i < this.grid.length; i++) {
                for (int j = 0; j < this.grid.length; j++) {
                    rect.set(i * tile, j * tile, (i + 1) * tile, (j + 1) * tile);

                    if (this.grid[i][j].marked) c.drawBitmap(bitmaps[10], null, rect, null);
                    else if (this.grid[i][j].covered) c.drawBitmap(bitmaps[0], null, rect, blackPaint);
                    else {
                        if (this.grid[i][j].bomb) c.drawBitmap(bitmaps[9], null, rect, null);
                        else {
                            c.drawBitmap(bitmaps[this.grid[i][j].bombs], null, rect, null);
                        }
                    }
                }
            }

            tick();
            invalidate();
        }

        if (this.grid != null && this.gameState != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
            builder.setTitle(this.gameState == -1 ? "YOU LOST" : "YOU WON");
            builder.setMessage("It took you " + Integer.toString(countTime()) + " seconds!");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.finish();
                        }
                    });
                }
            });

            SharedPreferences prefs = this.activity.getSharedPreferences("com.tamz.android.mines", Context.MODE_PRIVATE);
            prefs.edit().putInt("grid", this.grid.length).apply();
            prefs.edit().putInt("bomb", this.bombsTotal).apply();
            prefs.edit().putInt("time", countTime()).apply();
            prefs.edit().putInt("stat", this.gameState).apply();

            this.grid = null;

            final AlertDialog alert = builder.create();
            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alert.show();
                }
            });
        }
    }
}