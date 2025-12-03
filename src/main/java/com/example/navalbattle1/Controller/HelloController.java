package com.example.navalbattle1.Controller;

import com.example.navalbattle1.Model.Player;
import com.example.navalbattle1.View.GameView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {

    @FXML
    private Label errorLabel;

    @FXML
    private Button loadGameButton;

    @FXML
    private Button newGameButton;

    @FXML
    private TextField nicknameField;

    @FXML
    void handleLoadGame(ActionEvent event) {

    }

    @FXML
    void handleNewGame(ActionEvent event) throws IOException {
        String nickname = nicknameField.getText();
        Player player = new Player();
        player.setNickname(nickname);
        GameView gameView = GameView.getInstance();
        gameView.show();

        // Close stage
        Node source = (Node)event.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();

        GameController gameController = gameView.getGameController();
        gameController.setPlayer(player);
    }

    @FXML
    void onButtonExit(MouseEvent event) {

    }

    @FXML
    void onButtonHover(MouseEvent event) {

    }

}
