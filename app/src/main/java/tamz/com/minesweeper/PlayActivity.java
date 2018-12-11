package tamz.com.minesweeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlayActivity extends Activity {

    private static int MIN_SIZE = 9;
    private static int MAX_SIZE = 18;
    private static int MIN_BOMB = 1;
    private static int MAX_BOMB = 6;

    int bomb = MIN_BOMB;
    int size = MIN_SIZE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        findViewById(R.id.button6).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                bomb = MIN_BOMB;
                size = MIN_SIZE;
                updateViews();
            }
        });

        findViewById(R.id.button7).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                bomb = MIN_BOMB + (int) ((MAX_BOMB - MIN_BOMB) / 2.0f);
                size = MIN_SIZE + (int) ((MAX_SIZE - MIN_SIZE) / 2.0f);
                updateViews();
            }
        });

        findViewById(R.id.button8).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                bomb = MAX_BOMB;
                size = MAX_SIZE;
                updateViews();
            }
        });

        ((SeekBar) findViewById(R.id.seekBar)).setMax(MAX_SIZE - MIN_SIZE);
        ((SeekBar) findViewById(R.id.seekBar2)).setMax(MAX_BOMB - MIN_BOMB);

        ((SeekBar) findViewById(R.id.seekBar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                size = MIN_SIZE + progress;
                updateTexts();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ((SeekBar) findViewById(R.id.seekBar2)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bomb = MIN_BOMB + progress;
                updateTexts();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), GameActivity.class);
                intent.putExtra("bomb", bomb);
                intent.putExtra("size", size);
                intent.putExtra("continue", false);

                startActivity(intent);

                finish();
            }
        });

        updateViews();
    }

    void updateViews() {
        ((SeekBar) findViewById(R.id.seekBar)).setProgress(this.size - MIN_SIZE);
        ((SeekBar) findViewById(R.id.seekBar2)).setProgress(this.bomb - MIN_BOMB);

        updateTexts();
    }

    void updateTexts() {
        ((TextView) findViewById(R.id.textView7)).setText(Integer.toString((int)(this.size * this.size * (0.1f * this.bomb))));
        ((TextView) findViewById(R.id.textView8)).setText(Integer.toString((int)(this.size * this.size - Integer.parseInt(((TextView) findViewById(R.id.textView7)).getText().toString()))));
    }
}
