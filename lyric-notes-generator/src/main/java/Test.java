import java.io.File;
import java.util.List;

/**
 * FPT University - Capstone Project - Summer 2015 - lyric-notes-generator
 * Created by dinhquangtrung on 6/13/15.
 */
public class Test {
    public static void main(String[] args) {
        List<Integer> read = LyricNoteReader.read(new File("songs/829556.mid"));
        System.out.println(read);
    }
}
