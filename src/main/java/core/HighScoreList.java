package core;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class HighScoreList {

    private int limit;

    private List<HighScoreEntry> scores;

    public HighScoreList(int limit){
        scores = new ArrayList<>();
        if(limit <= 0){
            throw new IllegalArgumentException("limit cannot be 0 or negative");
        }
        this.limit = limit;
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

    public void writeToFile() throws IOException{
        FileWriter fileWriter = new FileWriter(new File("highscores.txt"));
        String data = "";
        for (HighScoreEntry entry : scores) {
            data += entry.toString() + "\n";
        }
        fileWriter.write(data);
        fileWriter.close();
        

    }

    public void readFromFile() throws IOException{
        InputStream is = HighScoreList.class.getResourceAsStream("highscores.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        
            while(br.ready()){
                String line = br.readLine();
                String[] values = line.split(",");
                String difficulty = values[0];
                String name = values[1];
                int minutes = Integer.parseInt(values[2]);
                int seconds = Integer.parseInt(values[3]);
                HighScoreEntry newEntry = new HighScoreEntry(difficulty, name, minutes, seconds);
                this.addEntry(newEntry);
            }
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
