package tamz.com.minesweeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        int bomb = intent.getIntExtra("bomb", 1);
        int size = intent.getIntExtra("size", 9);
        boolean cont = intent.getBooleanExtra("continue", false);

        if (cont) {
            GameProvider.load(this);
        } else {
            GameProvider.create(size, bomb);
        }

        GameView gameView = findViewById(R.id.gameView2);
        gameView.setGame(GameProvider.get());
        gameView.setActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (((GameView) findViewById(R.id.gameView2)).getState() == null) {
            ((GameView) findViewById(R.id.gameView2)).saveStatus();
        }

        finish();
    }
}
