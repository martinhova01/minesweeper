package persistance;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class HighScoreListTest {

    HighScoreList l;

    @Test
    public void testConstructor(){
        assertThrows(IllegalArgumentException.class, () -> {
            l = new HighScoreList(-1);

        });
    }
    
    @Test
    public void testReadingFromFile(){
        l = new HighScoreList(10);

        try {
            l.readFromFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertFalse(l.getList("easy").size() == 0);
    }

    @Test
    public void testWriteToFile(){
        l = new HighScoreList(10);

        try {
            l.readFromFile();
            HighScoreEntry testEntry = new HighScoreEntry("easy", "Test", 100, 100);
            l.addEntry(testEntry);
            l.writeToFile();

            HighScoreList newList = new HighScoreList(10);
            newList.readFromFile();
                //checks if the entry was added and saved to the file
            assertTrue(newList.contains(new HighScoreEntry("easy", "Test", 100, 100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
