package com.example.navalbattle1.Controller;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Controlador de la vista Flota Enemiga.
 * Muestra un tablero 10x10 solo de visualización.
 */
public class EnemyFleetController {

    @FXML
    private GridPane enemyGrid;

    /**
     * Se ejecuta automáticamente al cargar el FXML
     */
    @FXML
    public void initialize() {
        crearTablero();
    }

    /**
     * Crea una matriz 10x10 visual
     */
    private void crearTablero() {
        enemyGrid.getChildren().clear();

        for (int fila = 0; fila < 10; fila++) {
            for (int col = 0; col < 10; col++) {

                Rectangle celda = new Rectangle(35, 35);
                celda.setFill(Color.web("#1e3a5f")); // azul agua
                celda.setStroke(Color.BLACK);

                StackPane contenedor = new StackPane(celda);
                enemyGrid.add(contenedor, col, fila);
            }
        }
    }

    /**
     * Cierra la ventana y vuelve al setup
     */
    @FXML
    private void handleBack() {
        Stage stage = (Stage) enemyGrid.getScene().getWindow();
        stage.close();
    }
}
