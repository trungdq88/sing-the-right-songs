import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FPT University - Capstone Project - Summer 2015 - midi-reader
 * Created by dinhquangtrung on 6/13/15.
 */
public class Main {
    public static void main(String[] args) {

        Map<Integer, List<Integer>> songLyrics = new HashMap<Integer, List<Integer>>();

        File dir = new File("songs");

        File[] songs = dir.listFiles();

        if (songs != null) {
            int counter = 0;
            for (File song : songs) {
                counter++;
                System.out.println("Parsing... " + song.getName()
                        + " (" + counter + "/" + songs.length + ")");
                int songId = Integer.parseInt(song.getName().replace(".mid", ""));
                List<Integer> read = LyricNoteReader.read(song);
                if (read != null) {
                    songLyrics.put(songId, read);
                } else {
                    System.out.println("^ Failed");
                }
            }
        }


        // Write data to json file
        Gson gson = new Gson();
        String data = gson.toJson(songLyrics);

        PrintWriter writer = null;
        try {
            writer = new PrintWriter("songs.json", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (writer != null) {
            System.out.println("Writing JSON file...");
            writer.print(data);
            writer.close();
        }

        System.out.println("Done");

    }
}
