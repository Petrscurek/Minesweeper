package tamz.com.minesweeper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SeekBar bar = findViewById(R.id.seekBar3);
        bar.setProgress((int) (100.0f * Application.preferences.getFloat("volume", 1.0f)));
        bar.setMax(100);

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Application.preferences.edit().putFloat("volume", progress / 100.0f).apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
