package com.example.navalbattle1.Controller;

import com.example.navalbattle1.Model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class GameController {
    private Player player;

    @FXML
    private Label carrierLabel;

    @FXML
    private Label computerScoreLabel;

    @FXML
    private Label destroyerLabel;

    @FXML
    private Button exitButton;

    @FXML
    private Label frigateLabel;

    @FXML
    private Label hitsLabel;

    @FXML
    private GridPane mainBoardGrid;

    @FXML
    private Label missesLabel;

    @FXML
    private Button newGameButton;

    @FXML
    private Label playerNameLabel;

    @FXML
    private Label playerScoreLabel;

    @FXML
    private GridPane positionBoardGrid;

    @FXML
    private Button saveGameButton;

    @FXML
    private Label shotsLabel;

    @FXML
    private Button showEnemyBoardButton;

    @FXML
    private Label submarineLabel;

    @FXML
    private Label turnLabel;

    @FXML
    void handleExit(ActionEvent event) {

    }

    @FXML
    void handleNewGame(ActionEvent event) {

    }

    @FXML
    void handleSaveGame(ActionEvent event) {


    }

    @FXML
    void handleShowEnemyBoard(ActionEvent event) {

    }

    public void setPlayer(Player player) {
        this.player = player;
        this.playerNameLabel.setText("Jugador :"+" "+player.getNickname());
    }

}
