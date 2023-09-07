package core;

public class Square {
    
    private boolean revealed;
    private boolean isBomb;
    private boolean flagged;
    private int neighborBombs;


    public Square() {
        this.revealed = false;
        this.flagged = false;
        this.isBomb = false;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public boolean reveal(){
        if (flagged || revealed){
            return false;
        }
        revealed = true;
        return true;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void setBomb(boolean isBomb) {
        this.isBomb = isBomb;
    }

    public int getNeighborBombs() {
        return neighborBombs;
    }

    public void setNeighborBombs(int neighborBombs) {
        this.neighborBombs = neighborBombs;
    }

    public int flag() {
        if(revealed){
            return 0;
        }
        if (flagged){
            flagged = false;
            return 1;
        }
        else {
            flagged = true;
            return -1;
        }
    }

    public boolean isFlagged(){
        return flagged;
    }


}
