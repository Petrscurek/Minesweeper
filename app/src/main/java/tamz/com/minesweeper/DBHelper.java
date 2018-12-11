package tamz.com.minesweeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Scores.db";

    public static final String TABLE_NAME = "score";
    public static final String SCORE_COLUMN_ID = "id";
    public static final String SCORE_COLUMN_SIZE = "size";
    public static final String SCORE_COLUMN_BOMB = "bomb";
    public static final String SCORE_COLUMN_TIME = "time";
    public static final String SCORE_COLUMN_RESULT = "result";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + SCORE_COLUMN_ID + " INTEGER PRIMARY KEY, " + SCORE_COLUMN_SIZE + " INTEGER, " + SCORE_COLUMN_BOMB + " INTEGER, " + SCORE_COLUMN_TIME + " INTEGER, " + SCORE_COLUMN_RESULT + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertScore(int size, int bomb, int time, int result) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(SCORE_COLUMN_SIZE, size);
        cv.put(SCORE_COLUMN_BOMB, bomb);
        cv.put(SCORE_COLUMN_TIME, time);
        cv.put(SCORE_COLUMN_RESULT, result);

        db.insert(TABLE_NAME, null, cv);
        return true;
    }

    public ArrayList<Integer> getAll() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();

        ArrayList<Integer> list = new ArrayList<>();
        while (!res.isAfterLast()) {
            list.add(res.getInt(res.getColumnIndex(SCORE_COLUMN_ID)));
            res.moveToNext();
        }

        return list;
    }

    public Cursor get(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + TABLE_NAME + " where " + SCORE_COLUMN_ID + "=" + id + "", null);
    }

    public void clear()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "1", null);
    }
}
