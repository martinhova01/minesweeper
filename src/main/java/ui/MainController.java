package ui;



import java.io.IOException;
import java.util.Map;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Minesweeper;
import model.Square;
import persistance.HighScoreEntry;
import persistance.HighScoreList;

public class MainController{

    private Stage stage = MinesweeperApp.getPrimaryStage();
    private Scene scene;
    private Parent root;
    private GridPane grid;
    
    private boolean started = false;
    private int bombs;
    private int rows;
    private int cols;
    private String difficulty;
    private double sideLength;
    private Minesweeper game;
    private Pointer pointer = new Pointer(0, 0);

    private Image one = new Image("img/_1.png");
    private Image two = new Image("img/_2.png");
    private Image three = new Image("img/_3.png");
    private Image four = new Image("img/_4.png");
    private Image five = new Image("img/_5.png");
    private Image six = new Image("img/_6.png");
    private Image seven = new Image("img/_7.png");
    private Image eight = new Image("img/_8.png");
    private Image bomb = new Image("img/bomb.png");
    private Image bombExploded = new Image("img/bomb_exploded.jpg");
    private Image flag = new Image("img/flag.png");
    private Image unrevealed = new Image("img/square_unrevealed.png");
    private Image revealed = new Image("img/square_revealed.png");
    private Map<Integer, Image> imagesMap = Map.of(0, revealed, 1, one, 2, two, 3, three, 4, four, 5, five, 6, six, 7, seven, 8, eight);
    // private Map<Integer, Image> imagesMap = Map.of(0, revealed, 2, two, 3, three, 4, four, 5, five, 6, six, 7, seven, 8, eight);
    
    
    @FXML
    private AnchorPane gamePane, controlsPane;

    @FXML
    private Button btnRestart, btnMainMenu;

    @FXML
    private Label timerLabel, bombsLeftLabel;

    @FXML
    private void restart()throws Exception{
        if(game != null){
            game.stopTimer();
        }
        this.started = false;
        gamePane.getChildren().clear();
        timerLabel.setText("00 : 00");
        startGame(difficulty, rows, cols, bombs);
    }

    @FXML
    private void mainMenu() throws Exception{
        if(game != null){
            game.stopTimer();
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
        root = loader.load();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void startGame(String difficulty, int rows, int cols, int bombs) throws Exception{


            //testing
        // System.out.println(System.getProperty("user.dir"));
        // String dir = System.getProperty("user.dir");
        // String temp = dir + "src/main/resources/minesweeper/img/_1.png";
        // temp.replace("\\", "/");
        // one = new Image(temp);
        // imagesMap.put(1, one);


            /*
             * 
            */
        btnMainMenu.setFocusTraversable(false);
        btnRestart.setFocusTraversable(false);

            //set field values
        this.rows = rows;
        this.cols = cols;
        this.bombs = bombs;
        this.difficulty = difficulty;

        bombsLeftLabel.setText("" + bombs);

            //create the gameGrid with images
        grid = new GridPane();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                ImageView imageView = new ImageView();
                imageView.setId("" + i + "-" + j);
                
                sideLength = Math.min(gamePane.getWidth() / cols, gamePane.getWidth() / rows);
                imageView.setFitWidth(sideLength);
                imageView.setFitHeight(sideLength);
                imageView.setPreserveRatio(true);
                imageView.setImage(unrevealed);

                grid.add(imageView, j, i);
            }
        }
        grid.setOnMouseMoved(e -> {
            pointer.setX(e.getX());
            pointer.setY(e.getY());
        });
        grid.setOnMouseClicked(e -> {
            updateGameGrid(false, started);
        });
        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.SPACE){
                updateGameGrid(true, started);
            }
        });
        gamePane.getChildren().add(grid);
        
    }

    private void updateGameGrid(boolean isSpaceClick, boolean started) {
        
            //checks if the pointer is outside the game grid
        if(pointer.getX() > grid.getWidth() - 1 || pointer.getY() > grid.getHeight() - 1 || pointer.getX() < 1 || pointer.getY() < 1){
            return;
        }

        int targetRow = (int) (pointer.getY() / (sideLength)); 
        int targetCol = (int) (pointer.getX() / (sideLength));

            //initialize the game model first time a square is clicked
            //makes sure that the first square is not a bomb and has no neighbors
        if(!started){
            this.started = true;
            game = new Minesweeper(rows, cols, bombs, this);

            while(game.get(targetRow, targetCol).isBomb() || game.get(targetRow, targetCol).getNeighborBombs() != 0){
                game = new Minesweeper(rows, cols, bombs, this);
            }
            game.initTimer();
        }

            //the model updates based on what the player clicked
        if (isSpaceClick){
            game.spaceClick(targetRow, targetCol);
        }
        else{
            game.click(targetRow, targetCol);
        }

            //update the grid to be the same state as the game model
        for(int i = 0; i < grid.getRowCount(); i++){
            for(int j = 0; j < grid.getColumnCount(); j++){
                Square s = game.get(i, j);
                ImageView imageView = (ImageView)scene.lookup("#" + i + "-" + j);

                if(s.isFlagged()){
                    imageView.setImage(flag);
                }
                else if(!s.isRevealed()){
                    imageView.setImage(unrevealed);
                }
                else if(s.isRevealed() && !s.isBomb()){
                    imageView.setImage(imagesMap.get(s.getNeighborBombs()));
                }
                else if(s == game.getExplodedBomb()){
                    imageView.setImage(bombExploded);
                }
                else{
                    imageView.setImage(bomb);
                }
            }
        }
        bombsLeftLabel.setText("" + Math.max(0, game.getBombsLeft()));
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
        //updateTimer is called by the MinesweeperTimer every second
    public void updateTimer(int time){
        int minutes = time / 60;
        int seconds = time % 60;
        String minutesString = "";
        String secondsString = "";
        if(minutes < 10){
            minutesString += "0";
        }
        if(seconds < 10){
            secondsString += "0";
        }
        final String minutesDisplay = minutesString + minutes;
        final String secondsDisplay = secondsString + seconds;

            //the label must be updated in the FX thread, not the timer thread
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                timerLabel.setText(minutesDisplay + " : " + secondsDisplay);
            }
        });
        
    }
        //handleWin is called by  the Minesweeper class when a game is won
    public void handleWin() {
        grid.setOnMouseMoved(null);
        grid.setOnMouseClicked(null);
        scene.setOnKeyPressed(null);

            //create a popup box where player can enter name and save score
        AnchorPane box = new AnchorPane();
        box.setId("box");
        gamePane.getChildren().addAll(box);

        box.setPrefWidth(300);
        box.setPrefHeight(200);
        box.setStyle("-fx-background-color: #f2f2f2;");
        AnchorPane.setTopAnchor(box, gamePane.getHeight() / 2 - 100);
        AnchorPane.setLeftAnchor(box, gamePane.getWidth() / 2 - 150);

        Label label = new Label();
        label.setId("label");
        label.setText("You Win!! \nEnter name to save score.");
        box.getChildren().addAll(label);
        AnchorPane.setTopAnchor(label, 50.0);
        AnchorPane.setLeftAnchor(label, 75.0);

        TextField txtName = new TextField();
        txtName.setId("txtName");
        AnchorPane.setTopAnchor(txtName, 100.0);
        AnchorPane.setLeftAnchor(txtName, 75.0);
        txtName.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                saveScore();
            }
        });
        box.getChildren().add(txtName);

    }
        //handleGameOver is called by the Minesweeper class when a game is lost
    public void handleGameOver() {
            //disable the EventHandlers
        grid.setOnMouseMoved(null);
        grid.setOnMouseClicked(null);
        scene.setOnKeyPressed(null);
    }


    private void saveScore() {
        TextField txtName = (TextField)scene.lookup("#txtName");
        Label label = (Label)scene.lookup("#label");
        
        String name = txtName.getText();
        
        int minutes = Integer.parseInt(timerLabel.getText().split(" : ")[0]);
        int seconds = Integer.parseInt(timerLabel.getText().split(" : ")[1]);
        
        HighScoreList highscores = new HighScoreList(10);
        txtName.setText("Score saved");
        txtName.setDisable(true);
        try{
            highscores.readFromFile();
            highscores.addEntry(new HighScoreEntry(difficulty, name, minutes, seconds));
            highscores.writeToFile();
            
        }
        catch(Exception e){
            if (e instanceof IOException){
                e.printStackTrace();
            }
            else if (e instanceof IllegalArgumentException){
                txtName.setText("");
                txtName.setDisable(false);
                label.setText(label.getText() + "\nName connot contain ','");
            }
        }
    }
    
}
