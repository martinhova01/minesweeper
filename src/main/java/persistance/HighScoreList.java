package persistance;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public class HighScoreList {

    private int limit;

    private List<HighScoreEntry> scores;

    private Gson gson;

    public HighScoreList(int limit){
        scores = new ArrayList<>();
        if(limit <= 0){
            throw new IllegalArgumentException("limit cannot be 0 or negative");
        }
        this.limit = limit;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void addEntry(HighScoreEntry entry){
        scores.add(entry);
    }

    public List<HighScoreEntry> getList(String difficulty){
        if (!HighScoreEntry.validateDifficulty(difficulty)){
            throw new IllegalArgumentException("illegal difficulty");
        }
        return scores.stream()
        .filter(e -> e.getDifficulty().equals(difficulty))
        .sorted().limit(limit)
        .toList();
    }

    public void setLimit(int limit){
        this.limit = limit;
    }

    public void writeToFile(String filename) throws IOException{
        FileWriter fileWriter = new FileWriter(new File("src/main/java/persistance/" + filename));
        String data = gson.toJson(scores);
        fileWriter.write(data);
        fileWriter.close();
    }

    public void readFromFile(String filename) throws IOException{
       
        InputStream is = HighScoreList.class.getResourceAsStream(filename);
        Reader r = new InputStreamReader(is);

        scores = gson.fromJson(r, new TypeToken<List<HighScoreEntry>>() {}.getType());
    }

    public int getLimit() {
        return limit;
    }

    public boolean contains(HighScoreEntry entry) {

        for(HighScoreEntry e : scores){
            if (e.equals(entry)){
                return true;
            }
        }
        return false;
    }
    
}
