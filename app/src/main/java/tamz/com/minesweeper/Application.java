package tamz.com.minesweeper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Application extends Activity {


    public static SharedPreferences preferences;
    public static DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        preferences = getSharedPreferences("com.tamz.minesweeper", Context.MODE_PRIVATE);
        db = new DBHelper(this);

        findViewById(R.id.button).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), PlayActivity.class));
            }
        });

        findViewById(R.id.button3).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), ScoreActivity.class));
            }
        });

        findViewById(R.id.button4).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), SettingsActivity.class));
            }
        });

        findViewById(R.id.button2).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), GameActivity.class);
                intent.putExtra("continue", true);

                startActivity(intent);
            }
        });

        Resources.load(this);

        updateViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    void updateViews() {
        boolean currentGame = preferences.getBoolean("currentGame", false);

        if (currentGame) {
            GameProvider.load(this);

            GameView gameView = findViewById(R.id.gameView1);
            gameView.setGame(GameProvider.get());
            gameView.freeze();

            Game game = GameProvider.get();
            ((TextView) findViewById(R.id.textView6)).setText(Integer.toString(game.bombs));
            ((TextView) findViewById(R.id.textView5)).setText(Integer.toString(game.size * game.size - game.bombs));
        }

        ConstraintLayout layout = findViewById(R.id.linearLayout);
        layout.setVisibility(currentGame ? View.VISIBLE : View.GONE);
        ((Button) findViewById(R.id.button2)).setEnabled(currentGame);
    }
}
