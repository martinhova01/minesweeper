package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ui.MainController;

public class Minesweeper {

    private static final int[] UP = {0, 1};
    private static final int[] DOWN = {0, -1};
    private static final int[] RIGHT = {1, 0};
    private static final int[] LEFT = {-1, 0};
    private static final int[] UP_LEFT = {-1, 1};
    private static final int[] UP_RIGHT = {1, 1};
    private static final int[] DOWN_LEFT = {-1, -1};
    private static final int[] DOWN_RIGHT = {1, -1};
    private static final int[][] DIRECTIONS = {UP_LEFT, UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT};
    
    private int rows;
    private int cols;
    private int bombsLeft;
    private boolean finished;
    private Square explodedBomb;
    private MinesweeperTimer timer;
    private MainController controller;

    private List<List<Square>> squares;

    public Minesweeper(int rows, int cols, int bombs, MainController controller) {
        if (bombs > rows * cols){
            throw new IllegalArgumentException("Too many bombs in grid.");
        }
        this.rows = rows;
        this.cols = cols;
        this.bombsLeft = bombs;
        this.finished = false;
        this.controller = controller;
        
            //fill grid with squares that are not bombs
        this.squares = new ArrayList<>();
        for (int i = 0; i < rows; i++){
            List<Square> newRow = new ArrayList<>();
            for (int j = 0; j < cols; j++){
                newRow.add(new Square());
            }
            this.squares.add(newRow);
        }

            //fills the grid with bombs at random positions
            Random r = new Random();
        while(bombs > 0){
            int row = r.nextInt(rows);
            int col = r.nextInt(cols);
            Square s = this.get(row, col);

            if(!s.isBomb()){
                s.setBomb(true);
                bombs--;
            }
        }
            //count neighbors
        for (int i = 0; i < squares.size(); i++) {
            List<Square> row = squares.get(i);
            for (int j = 0; j < row.size(); j++) {
                Square s = this.get(i, j);

                int neighborBombs = 0;
                for(int[] direction : DIRECTIONS){
                    int x = direction[0];
                    int y = direction[1];

                    if(this.get(i + x, j + y) == null){
                        continue;
                    }
                    else if(this.get(i + x, j + y).isBomb()){
                        neighborBombs++;
                    }
                }
                s.setNeighborBombs(neighborBombs);
            }
        }
    }

    public void reveal(int row, int col){
        Square s = this.get(row, col);
        if(s.reveal()){
            if(s.isBomb() && !finished){
                explodedBomb = s;
                gameOver();
                return;
            }
            else if (s.getNeighborBombs() == 0 && !finished){
                for (int[] direction : DIRECTIONS) {
                    int x = direction[0];
                    int y = direction[1];
                    
                    Square nextSquare = this.get(row + x, col + y);
                    if (nextSquare != null){
                        this.reveal(row + x, col + y);
                    }
                }
            }
        }
        if(this.checkWin() && !finished){
            win();
        }
    }

    public void spaceReveal(int row, int col){
        Square s = this.get(row, col);
        int flaggedNeighbors = 0;
        if(s.isRevealed()){
                //count flagged neighbors
            for (int[] direction : DIRECTIONS) {
                Square nextSquare = this.get(row + direction[0], col + direction[1]);
                if(nextSquare != null){
                    if(nextSquare.isFlagged()){
                        flaggedNeighbors++;
                    }

                }
            }
        }

        if(s.getNeighborBombs() == flaggedNeighbors){
            for (int[] direction : DIRECTIONS) {
                Square nextSquare = this.get(row + direction[0], col + direction[1]);
                if(nextSquare != null){
                    this.reveal(row + direction[0], col + direction[1]);
                }
            }
        }
        if(this.checkWin() && !finished){
            win();
        }
    }

    public int getBombsLeft(){
        return bombsLeft;
    }

    private void win() {
        finished = true;
        stopTimer();
        controller.handleWin();
        System.out.println("You Win!");
    }

    private void gameOver() {
        finished = true;
        revealBombs();
        stopTimer();
        controller.handleGameOver();
        System.out.println("game Over");
    }

        //changes flagged state while keeping count of remaining bombs
    public void flag(int row, int col){
        bombsLeft += this.get(row, col).flag();
    }

    public Square get(int row, int col){
        if(row < 0 || col < 0 || row >= rows || col >= cols){
            return null;
        }
        return squares.get(row).get(col);
    }

        //used for testing
    public void printGridToConsole(boolean show){
        String result = "";
        for (List<Square> row : squares) {
            for (Square s : row) {
                if(!s.isRevealed() && !show){
                    if(s.isFlagged()){
                        result += "F";
                    }
                    else{
                        result += "X";
                    }
                }
                else if(s.isBomb()){
                    result += "*";
                }
                else{
                    result += s.getNeighborBombs();
                }
            }
            result += "\n";
        }
        System.out.println(result);
    }

    public List<List<Square>> getSquares(){
        List<List<Square>> newList = new ArrayList<>(squares);
        return newList;
    }

        //this method is called by the controller
    public void click(int row, int col) {
        Square s = this.get(row, col);
        if(s.isFlagged() || s.isRevealed()){
            return;
        }
        this.reveal(row, col);
    }
    
        //this methid is called by the controller
    public void spaceClick(int row, int col){
        Square s = this.get(row, col);
        if (!s.isRevealed()){
            this.flag(row, col);
        }
        else{
            this.spaceReveal(row, col);
        }
    }

    private boolean checkWin(){
        for (int i = 0; i < rows; i++) {
            for(int j = 0; j <  cols; j++){
                Square s = this.get(i, j);
                if(!s.isBomb() && !s.isRevealed()){
                    return false;
                }
            }
        }
        return true;
    }

    private void revealBombs(){
        for (int i = 0; i < rows; i++) {
            for(int j = 0; j <  cols; j++){
                Square s = this.get(i, j);
                if(s.isBomb()){
                    this.reveal(i, j);
                }
            }
        }
    }

    public void stopTimer(){
        this.timer.stop();
    }

    public MinesweeperTimer getTimer(){
        return timer;
    }

    public Square getExplodedBomb(){
        return explodedBomb;
    }

    public void updateTimer(int time) {
        controller.updateTimer(time);
    }

    public void initTimer(){
        this.timer = new MinesweeperTimer(this);
    }
}
