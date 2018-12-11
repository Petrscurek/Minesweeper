package tamz.com.minesweeper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ScoreAdapter extends ArrayAdapter<ScoreObj> {

    public ScoreAdapter(Context context, ArrayList<ScoreObj> objects) {
        super(context,0, objects);
    }

    @Override
    public View getView(int position, View convert, ViewGroup parent) {
        ScoreObj obj = getItem(position);

        if (convert == null) {
            convert = LayoutInflater.from(getContext()).inflate(R.layout.score_layout, parent, false);
        }

        ((TextView) convert.findViewById(R.id.textView15)).setText(obj.size + "x" + obj.size);
        ((TextView) convert.findViewById(R.id.textView16)).setText(Integer.toString(obj.bomb));
        ((TextView) convert.findViewById(R.id.textView17)).setText(Integer.toString(obj.time / 60));
        ((TextView) convert.findViewById(R.id.textView18)).setText(obj.result == -1 ? "LOST" : "WON");

        return convert;
    }

}
