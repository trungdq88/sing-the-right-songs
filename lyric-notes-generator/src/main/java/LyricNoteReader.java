import org.jfugue.midi.MidiParser;
import org.jfugue.parser.ParserListenerAdapter;
import org.jfugue.player.Player;
import org.jfugue.theory.Note;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LyricNoteReader {

    public static List<Integer> read(File midiFile) {
        Map<Integer,List<Integer>> map = new HashMap<Integer, List<Integer>>();

        MidiParser parser = new MidiParser();
        LyricNoteParser listener = new LyricNoteParser();
        parser.addParserListener(listener);
        Sequence sequence = null;
        try {
            sequence = MidiSystem.getSequence(midiFile);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (sequence != null) {
            parser.parse(sequence);
        } else {
            return null;
        }

        return listener.notes;
    }

    static class LyricNoteParser extends ParserListenerAdapter {
        private byte currentTrack;
        public StringBuilder lyricNotes = new StringBuilder();
        public List<Integer> notes = new ArrayList<Integer>();

        @Override
        public void onNoteParsed(Note note) {
            // The first track (number 0) is the lyric track
            if ((currentTrack + "").equals("0")
                    && !note.toStringWithoutDuration().equals("R")) {
                lyricNotes.append(note.toStringWithoutDuration()).append(" ");
                notes.add(Integer.valueOf(note.getValue() + ""));
            }
        }

        @Override
        public void onTrackChanged(byte track) {
            currentTrack = track;
        }

    }
}
