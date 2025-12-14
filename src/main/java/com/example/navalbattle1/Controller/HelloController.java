package com.example.navalbattle1.Controller;

import com.example.navalbattle1.Model.Player;
import com.example.navalbattle1.View.HelloView;
import com.example.navalbattle1.View.SetupView;
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

    /* ================= NUEVO JUEGO ================= */

    @FXML
    void handleNewGame(ActionEvent event) throws IOException {

        String nickname = nicknameField.getText().trim();

        if (nickname.isEmpty()) {
            errorLabel.setText("⚠️ Debes ingresar un nombre de comandante");
            errorLabel.setVisible(true);
            return;
        }

        Player player = new Player();
        player.setNickname(nickname);

        // Abrir SETUP VIEW (segunda vista)
        SetupView setupView = SetupView.getInstance();
        setupView.show();
        setupView.getController().setPlayer(player);

        // Cerrar HelloView
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /* ================= CARGAR PARTIDA (PENDIENTE) ================= */

    @FXML
    void handleLoadGame(ActionEvent event) {
        errorLabel.setText("📂 Cargar partida (pendiente)");
        errorLabel.setVisible(true);
    }

    /* ================= EFECTOS UI ================= */

    @FXML
    void onButtonHover(MouseEvent event) {
        ((Button) event.getSource()).setScaleX(1.05);
        ((Button) event.getSource()).setScaleY(1.05);
    }

    @FXML
    void onButtonExit(MouseEvent event) {
        ((Button) event.getSource()).setScaleX(1.0);
        ((Button) event.getSource()).setScaleY(1.0);
    }
}

