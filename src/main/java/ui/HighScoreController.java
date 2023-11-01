package ui;


import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import persistance.HighScoreEntry;
import persistance.HighScoreList;

public class HighScoreController {
    
    private Stage stage = MinesweeperApp.getPrimaryStage();
    private Scene scene;
    private Parent root;
    private HighScoreList highScoreList;
    private GridPane grid;
    private ToggleGroup toggleGroup;

    @FXML
    private Button btnBack;

    @FXML
    private RadioButton rbtnEasy, rbtnMedium, rbtnHard, rbtnExpert;

    @FXML
    private AnchorPane HighScorePane;

    @FXML
    private void back() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
        root = loader.load();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize(){
        grid = new GridPane();
        highScoreList = new HighScoreList(10);

        toggleGroup = new ToggleGroup();
        rbtnEasy.setToggleGroup(toggleGroup);
        rbtnMedium.setToggleGroup(toggleGroup);
        rbtnHard.setToggleGroup(toggleGroup);
        rbtnExpert.setToggleGroup(toggleGroup);
        rbtnEasy.setSelected(true);

        try {
            highScoreList.readFromFile("highscores.json");
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < highScoreList.getList("easy").size(); i++){
            HighScoreEntry entry = highScoreList.getList("easy").get(i);
            createRow(entry, i);
        }

        HighScorePane.getChildren().add(grid); 
    }

    

    @FXML
    private void updateHighScoreList(){
        ToggleButton selcted = (ToggleButton)toggleGroup.getSelectedToggle();
        HighScorePane.getChildren().clear();
        grid.getChildren().clear();
        
        String difficulty = selcted.getId().substring(4).toLowerCase();

        for(int i = 0; i < highScoreList.getList(difficulty).size(); i++){
            HighScoreEntry entry = highScoreList.getList(difficulty).get(i);
            createRow(entry, i);
        }
        HighScorePane.getChildren().add(grid);

    }

    private void createRow(HighScoreEntry entry, int row) {
        
        String name = entry.getName();
        String minutes = "" + entry.getMinutes();
        String seconds = "" + entry.getSeconds();

        double width = HighScorePane.getPrefWidth() / 3;
        Label nameLabel = createLabel((row + 1) + ") " + name, width);
        Label minutesLabel = createLabel(minutes, width);
        Label secondsLabel = createLabel(seconds, width);


        grid.add(nameLabel, 0, row);
        grid.add(minutesLabel, 1, row);
        grid.add(secondsLabel, 2, row);
}

    private Label createLabel(String text, double width){

        Label label = new Label();
        label.setText(text);
        label.setPrefWidth(width);
        label.setFont(Font.font(20));

        return label;
    }



    
}
