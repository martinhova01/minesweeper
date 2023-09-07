package ui;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuController {

    private Stage stage = MinesweeperApp.getPrimaryStage();
    private Scene scene;
    private Parent root;
    
    @FXML
    private Button btnStartNewGame, btnHighscores;
    
    @FXML
    private void newGame() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("startNewGame.fxml"));
        root = loader.load();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void highscores() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("highscores.fxml"));
        root = loader.load();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
