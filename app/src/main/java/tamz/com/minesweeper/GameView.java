package tamz.com.minesweeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameView extends View implements View.OnTouchListener {

    private static Rect rect = new Rect();

    private Game game = null;
    private boolean frozen = false;
    private boolean marking = false;

    private Boolean state = null;

    private Activity parent;

    public GameView(Context c) {
        super(c);
    }

    public GameView(Context c, AttributeSet a) {
        super(c, a);
    }
    public void setGame(Game game) {
        this.game = game;
    }

    public void setActivity(Activity parent) {
        this.parent = parent;

        setOnTouchListener(this);

        this.parent.findViewById(R.id.button9).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setMarking(false);
            }
        });

        this.parent.findViewById(R.id.button10).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setMarking(true);
            }
        });
    }

    public Boolean getState() {
        return state;
    }

    public void freeze() {
        this.frozen = true;
    }

    public void setMarking(boolean mark) {
        marking = mark;
    }

    @Override
    public void onDraw(Canvas c) {
        c.drawRGB(0xFF, 0xFF, 0xFF);

        if (game != null) {
            int size = 0;
            int offset = 0;

            if (c.getHeight() < c.getWidth()) {
                size = c.getHeight() / game.size;
                offset = (c.getWidth() - game.size * size) / 2;
            } else {
                size = c.getWidth() / game.size;
            }

            for (int i = 0; i < game.size; i++) {
                for (int j = 0; j < game.size; j++) {
                    rect.set(offset + i * size, j * size, offset + (i + 1) * size, (j + 1) * size);

                    if (game.tiles[j + i * game.size].marked) c.drawBitmap(Resources.getFlag(), null, rect, null);
                    else if (game.tiles[j + i * game.size].covered) c.drawBitmap(Resources.getNumber(0), null, rect, Resources.dark());
                    else {
                        if (game.tiles[j + i * game.size].bomb) c.drawBitmap(Resources.getMine(), null, rect, null);
                        else {
                            c.drawBitmap(Resources.getNumber(game.tiles[j + i * game.size].around), null, rect, null);
                        }
                    }
                }
            }

            if (this.parent != null && state == null) {
                updateGame();
            }
        }

        invalidate();
    }

    void updateGame() {
        game.time++;

        ((TextView) this.parent.findViewById(R.id.textView8)).setText(Integer.toString(game.time / 60));
        ((TextView) this.parent.findViewById(R.id.textView11)).setText(Integer.toString(game.bombs));
        ((TextView) this.parent.findViewById(R.id.textView12)).setText(Integer.toString(countCovered()));

        ((TextView) this.parent. findViewById(R.id.textView)).setText(marking ? "MARK" : "UNCOVER");
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN && !frozen) {
            clickTile((int) (e.getX() / (v.getWidth() / game.size)), (int) (e.getY() / (v.getWidth() / game.size)));
        }

        return true;
    }

    private boolean is(int x, int y) {
        return x >= 0 && y >= 0 && x < game.size && y < game.size;
    }

    private void clickTile(int x, int y) {
        if (this.marking) {
            markTile(x, y);
        } else {
            uncoverTile(x, y);

            if (countCovered() == game.bombs) {
                setState(true);
            }
        }
    }

    public int countCovered() {
        int count = 0;
        for (int i = 0; i < game.tiles.length; i++) {
            if (game.tiles[i].covered) {
                count++;
            }
        }

        return count;
    }

    private void markTile(int x, int y) {
        if (is(x, y) && this.game.tiles[x * game.size + y].covered) {
            game.tiles[x * game.size + y].marked ^= true;
        }
    }

    private void uncoverTile(int x, int y) {
        if (is(x, y) && !game.tiles[x * game.size + y].marked && game.tiles[x * game.size + y].covered) {
            uncoverAll(x, y);
        }
    }

    private void uncoverAll(int x, int y) {
        if (is(x, y) && game.tiles[x * game.size + y].covered) {
            game.tiles[x * game.size + y].covered = false;

            if (game.tiles[x * game.size + y].bomb) {
                setState(false);

                for (int i = 0; i < game.tiles.length; i++) {
                    game.tiles[i].covered = false;
                }

                Resources.playExplode();

            } else {
                if (game.tiles[x * game.size + y].around == 0) {
                    uncoverAll(x - 1, y);
                    uncoverAll(x + 1, y);
                    uncoverAll(x, y - 1);
                    uncoverAll(x, y + 1);
                }
            }
        }
    }

    private void setState(boolean state) {
        updateGame();

        this.state = state;

        AlertDialog.Builder builder = new AlertDialog.Builder(this.parent);
        builder.setTitle(!state ? "YOU LOST" : "YOU WON");
        builder.setMessage("It took you " + Integer.toString(game.time / 60) + " seconds!");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                parent.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        writeStatus();
                        parent.finish();
                    }
                });
            }
        });

        final AlertDialog alert = builder.create();
        this.parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alert.show();
            }
        });
    }

    private void writeStatus() {
        Application.db.insertScore(game.size, game.bombs, game.time, state ? 1 : -1);
        Application.preferences.edit().putBoolean("currentGame", false).apply();
    }

    public void saveStatus() {
        GameProvider.set(game);
        GameProvider.save(getContext());
        Application.preferences.edit().putBoolean("currentGame", true).apply();
    }

}
