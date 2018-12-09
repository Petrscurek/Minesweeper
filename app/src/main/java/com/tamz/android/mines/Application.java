package com.tamz.android.mines;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class Application extends Activity {

    private SeekBar seekBarGridSize;
    private SeekBar seekBarBombPerc;

    private TextView textGridSize;
    private TextView textBombSize;

    private Button buttonPlay;

    private static int MIN_GRID_SIZE = 9;
    private static int MAX_GRID_SIZE = 18;

    private static float MIN_BOMB_PERC = 0.1f;
    private static float MAX_BOMB_PERC = 0.6f;

    private int gridSize;
    private int bombCount;

    private int getGridSize() {
        return this.seekBarGridSize.getProgress() + MIN_GRID_SIZE;
    }

    private int getBombCount() {
        return (int) ((MIN_BOMB_PERC + ((float) this.seekBarBombPerc.getProgress()) / 100.0f) * Math.pow(getGridSize(), 2.0f));
    }

    {
        gridSize = MIN_GRID_SIZE;
        bombCount = (int) (MIN_BOMB_PERC * (float) (gridSize * gridSize));
    }

    private void showCounts() {
        this.textGridSize.setText("x" + Integer.toString(this.gridSize * this.gridSize - this.bombCount));
        this.textBombSize.setText("x" + Integer.toString(this.bombCount));
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);

        Mines.load(getResources());

        this.seekBarGridSize = (SeekBar) findViewById(R.id.seekBar2);
        this.seekBarBombPerc = (SeekBar) findViewById(R.id.seekBar4);
        this.textGridSize = (TextView) findViewById(R.id.textView7);
        this.textBombSize = (TextView) findViewById(R.id.textView6);
        this.buttonPlay = (Button) findViewById(R.id.button);

        this.seekBarBombPerc.setMax((int) ((MAX_BOMB_PERC - MIN_BOMB_PERC) * 100.0f));
        this.seekBarGridSize.setMax(MAX_GRID_SIZE - MIN_GRID_SIZE);

        this.seekBarBombPerc.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bombCount = getBombCount();
                showCounts();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        this.seekBarGridSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                gridSize = getGridSize();
                bombCount = getBombCount();
                showCounts();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        this.buttonPlay.setOnClickListener(new Button.OnClickListener() {
            @Override public void onClick(View v) { play(); }
        });

        showOld();

        showCounts();
    }

    void showOld() {
        SharedPreferences prefs = getSharedPreferences("com.tamz.android.mines", Context.MODE_PRIVATE);
        int ogrid = prefs.getInt("grid", -1);
        int obomb = prefs.getInt("bomb", -1);
        int otime = prefs.getInt("time", -1);
        int ostat = prefs.getInt("stat", 0);


        ((TextView) findViewById(R.id.textView10)).setText(ogrid == -1 ? "" : Integer.toString(ogrid) + "x" + Integer.toString(ogrid));
        ((TextView) findViewById(R.id.textView15)).setText(obomb == -1 ? "NO GAME PLAYED YET" : "Bombs: " + Integer.toString(obomb));
        ((TextView) findViewById(R.id.textView16)).setText(otime == -1 ? "" : "Took " + Integer.toString(otime) + "s");
        ((TextView) findViewById(R.id.textView17)).setText(ostat == 0 ? "" : (ostat == 1 ? "WIN" : "LOSS"));

    }

    void play() {
        Intent intent = new Intent(this, MineActivity.class);
        intent.putExtra("BOMBS", bombCount);
        intent.putExtra("GRIDS", gridSize);

        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showOld();
    }
}
