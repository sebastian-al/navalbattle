package com.example.navalbattle1.Controller;

import com.example.navalbattle1.Model.Player;
import com.example.navalbattle1.View.BoardCell;
import com.example.navalbattle1.View.ShipView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    /* ===================== CONSTANTES ===================== */

    private static final int BOARD_SIZE = 10;
    private static final int CELL_SIZE = BoardCell.SIZE;
    private static final int GAP = 2;          // hgap / vgap del GridPane
    private static final int GRID_PADDING = 5; // padding definido en el FXML

    /* ===================== MODELO ===================== */

    private Player player;

    /* ===================== UI ===================== */

    @FXML
    private Label playerNameLabel;

    @FXML
    private Label turnLabel;

    @FXML
    private GridPane positionBoardGrid; // Tablero jugador humano

    @FXML
    private GridPane mainBoardGrid; // Tablero máquina

    @FXML
    private Button newGameButton;

    @FXML
    private Button saveGameButton;

    @FXML
    private Button showEnemyBoardButton;

    @FXML
    private Button exitButton;

    /* ===== Capa de barcos 2D ===== */
    @FXML
    private Pane playerShipsLayer;

    /* ===================== MATRICES VISUALES ===================== */

    private BoardCell[][] playerBoard;
    private BoardCell[][] enemyBoard;

    /* ===================== INIT ===================== */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createBoards();
        drawPlayerShips();
        turnLabel.setText("🎯 Turno del jugador");
    }

    /* ===================== CREACIÓN DE TABLEROS ===================== */

    private void createBoards() {
        playerBoard = new BoardCell[BOARD_SIZE][BOARD_SIZE];
        enemyBoard = new BoardCell[BOARD_SIZE][BOARD_SIZE];

        positionBoardGrid.getChildren().clear();
        mainBoardGrid.getChildren().clear();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {

                // Tablero del jugador humano
                BoardCell playerCell = new BoardCell(row, col);
                playerCell.setWater();
                playerBoard[row][col] = playerCell;
                positionBoardGrid.add(playerCell, col, row);

                // Tablero de la máquina
                BoardCell enemyCell = new BoardCell(row, col);
                enemyCell.setWater();
                enemyCell.setOnMouseClicked(e -> handleEnemyShot(enemyCell));
                enemyBoard[row][col] = enemyCell;
                mainBoardGrid.add(enemyCell, col, row);
            }
        }
    }

    /* ===================== BARCOS DEL JUGADOR (VISTA 2D) ===================== */

    private void drawPlayerShips() {

        playerShipsLayer.getChildren().clear();

        /* 🚢 Portaaviones (4 casillas - horizontal) */
        ShipView carrier = new ShipView(
                ShipView.Type.CARRIER,
                ShipView.Orientation.HORIZONTAL
        );
        placeShip(carrier, 1, 1);
        playerShipsLayer.getChildren().add(carrier);

        /* 🛥️ Submarino (3 casillas - vertical) */
        ShipView submarine = new ShipView(
                ShipView.Type.SUBMARINE,
                ShipView.Orientation.VERTICAL
        );
        placeShip(submarine, 5, 3);
        playerShipsLayer.getChildren().add(submarine);

        /* 🚤 Destructor (2 casillas - horizontal) */
        ShipView destroyer = new ShipView(
                ShipView.Type.DESTROYER,
                ShipView.Orientation.HORIZONTAL
        );
        placeShip(destroyer, 2, 6);
        playerShipsLayer.getChildren().add(destroyer);

        /* ⛴️ Fragata (1 casilla) */
        ShipView frigate = new ShipView(
                ShipView.Type.FRIGATE,
                ShipView.Orientation.HORIZONTAL
        );
        placeShip(frigate, 8, 8);
        playerShipsLayer.getChildren().add(frigate);
    }

    /* ===================== POSICIONAMIENTO EXACTO ===================== */

    /**
     * Coloca un barco alineado exactamente a la celda (fila, columna)
     */
    private void placeShip(ShipView ship, int col, int row) {

        double x = GRID_PADDING + col * (CELL_SIZE + GAP);
        double y = GRID_PADDING + row * (CELL_SIZE + GAP);

        ship.setLayoutX(x);
        ship.setLayoutY(y);
    }

    /* ===================== EVENTOS DE BOTONES ===================== */

    @FXML
    void handleNewGame(ActionEvent event) {
        createBoards();
        drawPlayerShips();
        turnLabel.setText("🎯 Turno del jugador");
    }

    @FXML
    void handleSaveGame(ActionEvent event) {
        System.out.println("Guardar partida (pendiente)");
    }

    @FXML
    void handleShowEnemyBoard(ActionEvent event) {
        System.out.println("Modo debug: mostrar tablero enemigo");
    }

    @FXML
    void handleExit(ActionEvent event) {
        System.exit(0);
    }

    /* ===================== JUGADOR ===================== */

    public void setPlayer(Player player) {
        this.player = player;
        playerNameLabel.setText("Jugador: " + player.getNickname());
    }

    private void handleEnemyShot(BoardCell cell) {

        // Evitar disparar dos veces
        if (!cell.getChildren().isEmpty()) {
            return;
        }

        double random = Math.random();

        if (random < 0.6) {
            cell.setMiss();      // ❌ Agua
            turnLabel.setText("❌ Agua — turno de la máquina");
        } else if (random < 0.85) {
            cell.setHit();       // 💣 Tocado
            turnLabel.setText("💣 Tocado — dispara de nuevo");
        } else {
            cell.setSunk();      // 🔥 Hundido
            turnLabel.setText("🔥 Hundido — dispara de nuevo");
        }
    }

}
