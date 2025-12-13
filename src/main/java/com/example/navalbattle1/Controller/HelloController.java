package com.example.navalbattle1.Controller;

import com.example.navalbattle1.Model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controlador de la pantalla de inicio (Hello/Welcome)
 *
 * Maneja:
 * - Entrada del nickname del jugador
 * - Creación de nueva partida
 * - Carga de partida guardada
 * - Transición a la pantalla de colocación de barcos
 */
public class HelloController {

    @FXML
    private Label errorLabel;

    @FXML
    private Button loadGameButton;

    @FXML
    private Button newGameButton;

    @FXML
    private TextField nicknameField;

    /**
     * Maneja el evento de cargar partida guardada
     * TODO: Implementar carga de partidas guardadas
     */
    @FXML
    void handleLoadGame(ActionEvent event) {
        // TODO: Implementar lógica de carga de partida
        errorLabel.setText("Función no implementada aún");
        errorLabel.setStyle("-fx-text-fill: #fbbf24;");
    }

    /**
     * Maneja el evento de nueva partida
     *
     * Flujo:
     * 1. Valida que el jugador haya ingresado un nickname
     * 2. Crea un objeto Player con el nickname
     * 3. Carga la vista de colocación de barcos (ShipPlacement.fxml)
     * 4. Pasa el jugador al controlador de colocación
     * 5. Cierra la ventana actual
     * 6. Muestra la ventana de colocación de barcos
     */
    @FXML
    void handleNewGame(ActionEvent event) {
        try {
            // PASO 1: Validar que el jugador haya ingresado un nickname
            String nickname = nicknameField.getText().trim();

            if (nickname.isEmpty()) {
                errorLabel.setText("⚠️ Por favor ingresa un nombre");
                errorLabel.setStyle("-fx-text-fill: #ef4444;");
                return;
            }

            if (nickname.length() < 3) {
                errorLabel.setText("⚠️ El nombre debe tener al menos 3 caracteres");
                errorLabel.setStyle("-fx-text-fill: #ef4444;");
                return;
            }

            // PASO 2: Crear el jugador con el nickname
            Player player = new Player();
            player.setNickname(nickname);

            // PASO 3: Cargar la vista de colocación de barcos
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/navalbattle1/ship-placement.fxml")
            );
            Parent root = loader.load();

            // PASO 4: Obtener el controlador y pasarle el jugador
            ShipPlacementController shipController = loader.getController();
            shipController.setPlayer(player);

            // PASO 5: Crear la nueva escena
            Scene scene = new Scene(root);

            // PASO 6: Obtener el Stage actual y cambiar la escena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Batalla Naval - Colocación de Barcos - " + nickname);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("❌ Error al cargar la pantalla de colocación");
            errorLabel.setStyle("-fx-text-fill: #ef4444;");
        }
    }

    /**
     * Maneja el evento de salir del juego
     */
    @FXML
    void onButtonExit(MouseEvent event) {
        // Obtener el Stage y cerrarlo
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Maneja el efecto hover de los botones
     * Cambia la apariencia visual cuando el mouse pasa sobre un botón
     */
    @FXML
    void onButtonHover(MouseEvent event) {
        Node node = (Node) event.getSource();

        if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
            // Mouse entra al botón - efecto de iluminación
            node.setStyle(node.getStyle() + "-fx-opacity: 0.8; -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
            // Mouse sale del botón - volver a normal
            node.setStyle(node.getStyle() + "-fx-opacity: 1.0; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
        }
    }
}