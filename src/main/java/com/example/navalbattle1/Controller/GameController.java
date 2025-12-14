package com.example.navalbattle1.Controller;

import com.example.navalbattle1.Model.JugadorHumano;
import com.example.navalbattle1.Model.Tablero;
import com.example.navalbattle1.Model.Barco;
import com.example.navalbattle1.View.BoardCell;
import com.example.navalbattle1.View.ShipView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    /* ===================== CONSTANTES ===================== */

    private static final int BOARD_SIZE = 10;
    private static final int CELL_SIZE = BoardCell.SIZE;
    private static final int GAP = 2;
    private static final int GRID_PADDING = 5;

    /* ===================== MODELO ===================== */
    private JugadorHumano jugadorHumano;
    private Tablero playerFinalBoard; // viene desde Setup

    /* ===================== UI ===================== */

    @FXML
    private Label playerNameLabel;

    @FXML
    private Label turnLabel;

    @FXML
    private GridPane positionBoardGrid; // tablero jugador (solo visual)

    @FXML
    private GridPane mainBoardGrid; // tablero enemigo

    @FXML
    private Pane playerShipsLayer; // capa barcos 2D (bloqueada)

    /* ===================== MATRICES VISUALES ===================== */

    private BoardCell[][] playerBoard;
    private BoardCell[][] enemyBoard;

    /* ===================== INIT ===================== */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createBoards();
        turnLabel.setText("🎯 Turno del jugador");
    }

    /* ===================== INYECCIÓN DESDE SETUP ===================== */

    /**
     * Llamado desde SetupController
     */
    public void setInitialBoard(Tablero tablero) {
        this.playerFinalBoard = tablero;
        drawFinalPlayerShips();
    }

    /* ===================== CREACIÓN DE TABLEROS ===================== */

    private void createBoards() {

        playerBoard = new BoardCell[BOARD_SIZE][BOARD_SIZE];
        enemyBoard = new BoardCell[BOARD_SIZE][BOARD_SIZE];

        positionBoardGrid.getChildren().clear();
        mainBoardGrid.getChildren().clear();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {

                // === Tablero del jugador (solo observación) ===
                BoardCell playerCell = new BoardCell(row, col);
                playerCell.setWater();
                playerBoard[row][col] = playerCell;
                positionBoardGrid.add(playerCell, col, row);

                // === Tablero enemigo (clickeable) ===
                BoardCell enemyCell = new BoardCell(row, col);
                enemyCell.setWater();
                enemyCell.setOnMouseClicked(e -> handleEnemyShot(enemyCell));
                enemyBoard[row][col] = enemyCell;
                mainBoardGrid.add(enemyCell, col, row);
            }
        }
    }

    /* ===================== DIBUJO DE BARCOS DESDE SETUP ===================== */

    private void drawFinalPlayerShips() {

        if (playerFinalBoard == null) return;

        playerShipsLayer.getChildren().clear();

        for (Barco barco : playerFinalBoard.getBarcos()) {

            ShipView shipView = new ShipView(
                    ShipView.Type.fromSize(barco.getTamano()),
                    barco.isHorizontal()
                            ? ShipView.Orientation.HORIZONTAL
                            : ShipView.Orientation.VERTICAL
            );

            placeShip(shipView, barco.getColumna(), barco.getFila());
            playerShipsLayer.getChildren().add(shipView);
        }
    }

    /* ===================== POSICIONAMIENTO EXACTO ===================== */

    private void placeShip(ShipView ship, int col, int row) {

        double x = GRID_PADDING + col * (CELL_SIZE + GAP);
        double y = GRID_PADDING + row * (CELL_SIZE + GAP);

        ship.setLayoutX(x);
        ship.setLayoutY(y);
    }

    /* ===================== DISPAROS AL ENEMIGO ===================== */

    private void handleEnemyShot(BoardCell cell) {

        if (cell.isShot()) return;

        double random = Math.random();

        if (random < 0.6) {
            cell.setMiss(); // ❌
            turnLabel.setText("❌ Agua — turno de la máquina");
        } else if (random < 0.85) {
            cell.setHit(); // 💣
            turnLabel.setText("💣 Tocado — dispara de nuevo");
        } else {
            cell.setSunk(); // 🔥
            turnLabel.setText("🔥 Hundido — dispara de nuevo");
        }
    }

    /* ===================== BOTÓN SALIR ===================== */

    @FXML
    void handleExit(ActionEvent event) {
        System.exit(0);
    }

    /* ===================== JUGADOR ===================== */

    public void setPlayer(JugadorHumano jugador) {
        jugadorHumano = jugador;
        playerNameLabel.setText("Jugador: " + jugador.getNombre());
    }
}
