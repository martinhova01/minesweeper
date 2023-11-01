package persistance;

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
    public void testWriteToFile(){
        l = new HighScoreList(10);

        try {
            HighScoreEntry testEntry = new HighScoreEntry("easy", "Test", 100, 100);
            l.addEntry(testEntry);
            l.writeToFile("testHighscorelist.json");

            HighScoreList newList = new HighScoreList(10);
            newList.readFromFile("testHighscorelist.json");

            assertTrue(newList.getList("easy").size() == 1);
            assertTrue(newList.contains(testEntry));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
