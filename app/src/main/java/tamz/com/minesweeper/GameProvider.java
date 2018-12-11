package tamz.com.minesweeper;

import android.content.Context;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Random;


public class GameProvider {

    private static Game game;

    public static void load(Context context) {
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();

            parser.setInput(context.openFileInput("game.xml"), null);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);


            parseData(parser);
        } catch (Exception exception) {
            Toast.makeText(context, exception.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    public static void set(Game g) {
        game = g;
    }


    public static void create(int size, int bomb) {

        int count = (int) (size * size * bomb * 0.1f);

        game = new Game();
        game.size = size;
        game.bombs = count;
        game.time = 0;
        game.tiles = new Tile[size * size];

        for (int i = 0; i < game.tiles.length; i++) {
            game.tiles[i] = new Tile();
        }

        Random random = new Random();
        for (int i = 0; i < count;) {
            int rnd = random.nextInt(size * size);

            if (!game.tiles[rnd].bomb) {
                game.tiles[rnd].bomb = true;
                i++;
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int around = 0;

                for (int di = -1; di < 2; di++) {
                    for (int dj = -1; dj < 2; dj++) {
                        if (i + di >= 0 && i + di < size && j + dj >= 0 && j + dj < size) {
                            if (game.tiles[j + dj + (i + di) * size].bomb) {
                                around++;
                            }
                        }
                    }
                }

                game.tiles[j + i * size].around = around;
            }
        }
    }

    private static void parseData(XmlPullParser parser) throws Exception {
        game = new Game();

        int event = parser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            String title = null;

            switch (event) {
                case XmlPullParser.START_TAG: {
                    title = parser.getName();

                    if (title.equals("data")) {
                        game.size =  Integer.parseInt(parser.getAttributeValue(0));
                        game.bombs =  Integer.parseInt(parser.getAttributeValue(1));
                        game.time =  Integer.parseInt(parser.getAttributeValue(2));

                        game.tiles = new Tile[game.size * game.size];
                    } else if (title.equals("tile")) {
                        int position = Integer.parseInt(parser.getAttributeValue(0));

                        Tile tile = new Tile();
                        tile.covered = Boolean.parseBoolean(parser.getAttributeValue(1));
                        tile.bomb = Boolean.parseBoolean(parser.getAttributeValue(2));
                        tile.marked = Boolean.parseBoolean(parser.getAttributeValue(3));
                        tile.around = Integer.parseInt(parser.getAttributeValue(4));

                        game.tiles[position] = tile;
                    }
                }
            }

            event = parser.next();
        }
    }

    public static Game get() {
        return game;
    }

    public static void save(Context context) {
        XmlSerializer serializer = Xml.newSerializer();

        try {
            StringWriter writer = new StringWriter();

            serializer.setOutput(writer);

            serializer.startDocument("UTF-8", true);

            serializer.startTag("", "data");
            serializer.attribute("", "size", Integer.toString(game.size));
            serializer.attribute("", "bombs", Integer.toString(game.bombs));
            serializer.attribute("", "time", Integer.toString(game.time));

            for (int i = 0; i < game.size * game.size; i++) {
                serializer.startTag("","tile");

                serializer.attribute("", "position", Integer.toString(i));

                serializer.attribute("", "uncovered", Boolean.toString(game.tiles[i].covered));
                serializer.attribute("", "bomb", Boolean.toString(game.tiles[i].bomb));
                serializer.attribute("", "marked", Boolean.toString(game.tiles[i].marked));
                serializer.attribute("", "around", Integer.toString(game.tiles[i].around));

                serializer.endTag("", "tile");
            }

            serializer.endTag("", "data");

            serializer.endDocument();

            try (FileOutputStream fostream = context.openFileOutput("game.xml", Context.MODE_PRIVATE)) {
                fostream.write(writer.toString().getBytes());
            } catch (IOException exception) {
                Toast.makeText(context, exception.toString(), Toast.LENGTH_SHORT).show();
            }

        } catch (IOException exception) {
            Toast.makeText(context, exception.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
