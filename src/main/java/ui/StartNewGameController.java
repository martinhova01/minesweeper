package ui;


import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class StartNewGameController{
    
    private Stage stage = MinesweeperApp.getPrimaryStage();
    private Scene scene;
    private Parent root;
    ToggleGroup difficultyToggleGroup = new ToggleGroup();

    @FXML
    private RadioButton rbtnEasy, rbtnMedium, rbtnHard, rbtnExpert;
    private Map<RadioButton, List<Integer>> difficulties;

    @FXML
    private Button btnStartGame;
    @FXML
    private TextField txtRows, txtColums, txtBombs;

    @FXML
    private void startGame() throws Exception{
        int rows = Integer.parseInt(txtRows.getText());
        int cols = Integer.parseInt(txtColums.getText());
        int bombs = Integer.parseInt(txtBombs.getText());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        root = loader.load();
        
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
            //gets the controller for the main game scene and passes in the parameters for the game
        MainController controller = loader.getController();
        controller.setScene(scene);
        RadioButton selectedDifficulty = (RadioButton)difficultyToggleGroup.getSelectedToggle();
        String difficulty = selectedDifficulty.getId().substring(4).toLowerCase();
        controller.startGame(difficulty, rows, cols, bombs);

        
    }

    @FXML
    private void back() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
        root = loader.load();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    
    }

    @FXML
    private void update(){

        RadioButton selectedDifficulty = (RadioButton)difficultyToggleGroup.getSelectedToggle();
        int rows = difficulties.get(selectedDifficulty).get(0);
        int cols = difficulties.get(selectedDifficulty).get(1);
        int bombs = difficulties.get(selectedDifficulty).get(2);        
        
        txtRows.setText("" + rows);
        txtColums.setText("" + cols);
        txtBombs.setText("" + bombs);
    }

    @FXML
    public void initialize() {

        rbtnEasy.setToggleGroup(difficultyToggleGroup);
        rbtnMedium.setToggleGroup(difficultyToggleGroup);
        rbtnHard.setToggleGroup(difficultyToggleGroup);
        rbtnExpert.setToggleGroup(difficultyToggleGroup);

        rbtnEasy.setSelected(true);

        txtRows.setEditable(false);
        txtColums.setEditable(false);
        txtBombs.setEditable(false);

        txtRows.setText("" + 7);
        txtColums.setText("" + 7);
        txtBombs.setText("" + 7);

            //set difficulties settings
        difficulties = Map.of(
        rbtnEasy,   List.of(7, 7, 7),
        rbtnMedium, List.of(10, 10, 20),
        rbtnHard,   List.of(15, 15, 40),
        rbtnExpert, List.of(25, 25, 100));
    }
   
}
