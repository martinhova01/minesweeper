package persistance;

import java.util.List;

public class HighScoreEntry implements Comparable<HighScoreEntry> {
    
    private String difficulty;
    private String name;
    private int minutes;
    private int seconds;

    public static final List<String> difficulties = List.of("easy", "medium", "hard", "expert"); 

    public HighScoreEntry(String difficulty, String name, int minutes, int seconds) {
        if (!validateDifficulty(difficulty)){
            throw new IllegalArgumentException("illegal difficulty");
        }
        if (name.contains(",")){
            throw new IllegalArgumentException("name cannot contain ,");
        }
        if (seconds < 0 || minutes < 0){
            throw new IllegalArgumentException("time cannot be neagtive");
        }
        this.name = name;
        this.difficulty = difficulty;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public static boolean validateDifficulty(String difficulty){
        if(difficulties.contains(difficulty)){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return difficulty + "," + name + ","+ minutes + "," + seconds;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getName() {
        return name;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    @Override
    public int compareTo(HighScoreEntry entry) {
        if (this.getMinutes() - entry.getMinutes() != 0){
            return this.getMinutes() - entry.getMinutes();
        }
        return this.getSeconds() - entry.getSeconds();
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof HighScoreEntry){
            HighScoreEntry entry = (HighScoreEntry) other;
            return this.difficulty.equals(entry.difficulty) 
                && this.name.equals(entry.name)
                && this.minutes == entry.minutes
                && this.seconds == entry.seconds;
        }
        else{
            return super.equals(other);
        }
    }


}
