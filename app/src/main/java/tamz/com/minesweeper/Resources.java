package tamz.com.minesweeper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.MediaPlayer;

public class Resources {

    private static Bitmap images[] = new Bitmap[11];
    private static Paint darken = new Paint();
    private static MediaPlayer explosion;

    public static void load(Context context) {
        images[9] = BitmapFactory.decodeResource(context.getResources(), R.drawable.mine);
        images[10] = BitmapFactory.decodeResource(context.getResources(), R.drawable.flag);

        images[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.def);
        for (int i = 1; i < 9; i++) {
            images[i] = BitmapFactory.decodeResource(context.getResources(), R.drawable.s1 + i - 1);
        }

        darken.setColorFilter(new PorterDuffColorFilter(Color.argb(100, 0, 0, 0), PorterDuff.Mode.DARKEN));

        explosion = MediaPlayer.create(context, R.raw.explosion);
    }

    public static Bitmap getMine() {
        return images[9];
    }

    public static void playExplode() {
        float volume = Application.preferences.getFloat("volume", 1.0f);

        explosion.setVolume(volume, volume);
        explosion.start();
    }

    public static Paint dark() {
        return darken;
    }

    public static Bitmap getFlag() {
        return images[10];
    }

    public static Bitmap getNumber(int number) {
        if (number < 0 || number > 8) {
            return images[0];
        } else {
            return images[number];
        }
    }

}
