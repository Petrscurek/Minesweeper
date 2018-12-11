package tamz.com.minesweeper;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class ScoreActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        ArrayList<ScoreObj> objects = new ArrayList<>();
        for (Integer id : Application.db.getAll()) {
            Cursor cur = Application.db.get(id);
            cur.moveToFirst();

            ScoreObj obj = new ScoreObj();
            obj.id = cur.getInt(cur.getColumnIndex(DBHelper.SCORE_COLUMN_ID));
            obj.size = cur.getInt(cur.getColumnIndex(DBHelper.SCORE_COLUMN_SIZE));
            obj.bomb = cur.getInt(cur.getColumnIndex(DBHelper.SCORE_COLUMN_BOMB));
            obj.time = cur.getInt(cur.getColumnIndex(DBHelper.SCORE_COLUMN_TIME));
            obj.result = cur.getInt(cur.getColumnIndex(DBHelper.SCORE_COLUMN_RESULT));

            objects.add(0, obj);
        }

        ListView lv = findViewById(R.id.listView);
        lv.setAdapter(new ScoreAdapter(this, objects));

        findViewById(R.id.button12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Application.db.clear();
                recreate();
            }
        });
    }
}
