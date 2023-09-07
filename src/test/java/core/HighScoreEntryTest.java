package core;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import core.HighScoreEntry;

public class HighScoreEntryTest {

    HighScoreEntry entry;

    @Test
    public void testIllegalDifficulty(){
        assertThrows(IllegalArgumentException.class, () -> {
            entry = new HighScoreEntry("Easy", null, 1, 1);

        });
    }

    @Test
    public void testNegativeMinutesOrSeconds(){
        assertThrows(IllegalArgumentException.class, () -> {
            entry = new HighScoreEntry("easy", "martin", -1, 1);

        });

        assertThrows(IllegalArgumentException.class, () -> {
            entry = new HighScoreEntry("easy", "martin", 1, -1);

        });

    }

    @Test
    public void testNameWithComma(){
        assertThrows(IllegalArgumentException.class, () -> {
            entry = new HighScoreEntry("easy", "martin,1", 1, 1);
        });
    }
    
}
