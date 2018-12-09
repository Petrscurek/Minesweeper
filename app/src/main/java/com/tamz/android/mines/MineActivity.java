package com.tamz.android.mines;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MineActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();

        Mines mines = (Mines) findViewById(R.id.mines);
        mines.setup(this, intent.getIntExtra("GRIDS", 9), intent.getIntExtra("BOMBS", 15));
    }
}
