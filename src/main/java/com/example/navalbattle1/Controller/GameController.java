package com.example.navalbattle1.Controller;

import com.example.navalbattle1.Model.Player;
import com.example.navalbattle1.Model.Tablero;
import com.example.navalbattle1.Model.Barco;
import com.example.navalbattle1.View.BoardCell;
import com.example.navalbattle1.View.ShipView;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import com.example.navalbattle1.util.CSVLogger;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Controlador principal del juego de Batalla Naval.
 * Los barcos se dibujan directamente en el GridPane.
 */
public class GameController implements Initializable {

    /* ===================== CONSTANTES ===================== */

    private static final int BOARD_SIZE = 10;
    private static final int CELL_SIZE = BoardCell.SIZE;
    private static final int GAP = 2;

    /* ===================== MODELO ===================== */

    private Player player;
    private Tablero playerFinalBoard;
    private Tablero enemyBoard;
    private final Random random = new Random();

    /* ===================== UI ===================== */

    @FXML private Label playerNameLabel;
    @FXML private Label turnLabel;
    @FXML private GridPane positionBoardGrid;
    @FXML private GridPane mainBoardGrid;
    @FXML private Button toggleEnemyShipsButton;

    /* ===================== MATRICES VISUALES ===================== */

    private BoardCell[][] playerBoardCells;
    private BoardCell[][] enemyBoardCells;

    // Mapa para controlar visibilidad de barcos enemigos
    private final Map<ShipView, Boolean> enemyShipVisibility = new HashMap<>();

    /* ===================== ESTADO DEL JUEGO ===================== */

    private boolean enemyShipsVisible = false;
    private boolean isPlayerTurn = true;
    private boolean gameOver = false;

    private int playerShipsSunk = 0;
    private int enemyShipsSunk = 0;
    private static final int TOTAL_SHIPS = 10;

    /* ===================== IA DE LA MÁQUINA ===================== */

    private List<Position> availableTargets;
    private boolean huntMode = false;
    private List<Position> huntTargets;
    private Position lastHit = null;

    private static class Position {
        int row;
        int col;

        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Position)) return false;
            Position p = (Position) obj;
            return this.row == p.row && this.col == p.col;
        }

        @Override
        public int hashCode() {
            return row * 100 + col;
        }
    }

    /* ===================== INIT ===================== */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createBoards();
        createEnemyFleet();
        initializeAI();
        turnLabel.setText("🎯 Turno del jugador");
    }

    private void initializeAI() {
        availableTargets = new ArrayList<>();
        huntTargets = new ArrayList<>();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                availableTargets.add(new Position(row, col));
            }
        }
    }

    /* ===================== INYECCIÓN DESDE SETUP ===================== */

    public void setInitialBoard(Tablero tablero) {
        this.playerFinalBoard = tablero;
        drawPlayerShips();
    }

    /* ===================== CREACIÓN DE TABLEROS ===================== */

    private void createBoards() {
        playerBoardCells = new BoardCell[BOARD_SIZE][BOARD_SIZE];
        enemyBoardCells = new BoardCell[BOARD_SIZE][BOARD_SIZE];

        positionBoardGrid.getChildren().clear();
        mainBoardGrid.getChildren().clear();

        // Configurar GAP en los GridPanes
        positionBoardGrid.setHgap(GAP);
        positionBoardGrid.setVgap(GAP);
        mainBoardGrid.setHgap(GAP);
        mainBoardGrid.setVgap(GAP);

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {

                // Tablero del jugador
                BoardCell playerCell = new BoardCell(row, col);
                playerCell.setWater();
                playerBoardCells[row][col] = playerCell;
                positionBoardGrid.add(playerCell, col, row);

                // Tablero enemigo
                BoardCell enemyCell = new BoardCell(row, col);
                enemyCell.setWater();

                final int r = row;
                final int c = col;
                enemyCell.setOnMouseClicked(e -> handlePlayerShot(r, c));

                enemyBoardCells[row][col] = enemyCell;
                mainBoardGrid.add(enemyCell, col, row);
            }
        }
    }

    /* ===================== CREACIÓN FLOTA ENEMIGA ===================== */

    private void createEnemyFleet() {
        enemyBoard = new Tablero();

        List<ShipConfig> fleet = new ArrayList<>();
        fleet.add(new ShipConfig("Portaaviones", 4, 1));
        fleet.add(new ShipConfig("Submarino", 3, 2));
        fleet.add(new ShipConfig("Destructor", 2, 3));
        fleet.add(new ShipConfig("Fragata", 1, 4));

        for (ShipConfig config : fleet) {
            for (int i = 0; i < config.count; i++) {
                placeRandomEnemyShip(config.name, config.size);
            }
        }

        drawEnemyShips();
        hideEnemyShips();
    }

    private void placeRandomEnemyShip(String name, int size) {
        int maxAttempts = 100;
        int attempts = 0;

        while (attempts < maxAttempts) {
            int row = random.nextInt(BOARD_SIZE);
            int col = random.nextInt(BOARD_SIZE);
            boolean horizontal = random.nextBoolean();

            Barco barco = new Barco.Builder(name, size)
                    .fila(row)
                    .columna(col)
                    .horizontal(horizontal)
                    .build();

            if (enemyBoard.puedeColocar(barco)) {
                enemyBoard.agregarBarco(barco);
                return;
            }

            attempts++;
        }

        System.err.println("No se pudo colocar el barco: " + name + " de tamaño " + size);
    }

    private static class ShipConfig {
        String name;
        int size;
        int count;

        ShipConfig(String name, int size, int count) {
            this.name = name;
            this.size = size;
            this.count = count;
        }
    }

    /* ===================== DIBUJO DE BARCOS ===================== */

    private void drawPlayerShips() {
        if (playerFinalBoard == null) return;

        for (Barco barco : playerFinalBoard.getBarcos()) {
            ShipView ship = new ShipView(
                    ShipView.Type.fromSize(barco.getTamano()),
                    barco.isHorizontal()
                            ? ShipView.Orientation.HORIZONTAL
                            : ShipView.Orientation.VERTICAL
            );

            int size = barco.getTamano();
            int columnSpan = barco.isHorizontal() ? size : 1;
            int rowSpan = barco.isHorizontal() ? 1 : size;

            // Añadir el barco directamente al GridPane
            positionBoardGrid.add(ship, barco.getColumna(), barco.getFila(), columnSpan, rowSpan);

            // Marcar las celdas como ocupadas
            for (int i = 0; i < size; i++) {
                int row = barco.isHorizontal() ? barco.getFila() : barco.getFila() + i;
                int col = barco.isHorizontal() ? barco.getColumna() + i : barco.getColumna();
                playerBoardCells[row][col].setShipReference(ship);
            }
        }
    }

    private void drawEnemyShips() {
        if (enemyBoard == null) return;

        for (Barco barco : enemyBoard.getBarcos()) {
            ShipView ship = new ShipView(
                    ShipView.Type.fromSize(barco.getTamano()),
                    barco.isHorizontal()
                            ? ShipView.Orientation.HORIZONTAL
                            : ShipView.Orientation.VERTICAL
            );

            int size = barco.getTamano();
            int columnSpan = barco.isHorizontal() ? size : 1;
            int rowSpan = barco.isHorizontal() ? 1 : size;

            // Añadir el barco directamente al GridPane
            mainBoardGrid.add(ship, barco.getColumna(), barco.getFila(), columnSpan, rowSpan);

            // Guardar referencia para controlar visibilidad
            enemyShipVisibility.put(ship, false);

            // Marcar las celdas como ocupadas
            for (int i = 0; i < size; i++) {
                int row = barco.isHorizontal() ? barco.getFila() : barco.getFila() + i;
                int col = barco.isHorizontal() ? barco.getColumna() + i : barco.getColumna();
                enemyBoardCells[row][col].setShipReference(ship);
            }
        }
    }

    /* ===================== VISIBILIDAD DE BARCOS ENEMIGOS ===================== */

    private void hideEnemyShips() {
        for (ShipView ship : enemyShipVisibility.keySet()) {
            ship.setVisible(false);
            enemyShipVisibility.put(ship, false);
        }
    }

    private void showEnemyShips() {
        for (ShipView ship : enemyShipVisibility.keySet()) {
            ship.setVisible(true);
            enemyShipVisibility.put(ship, true);
        }
    }

    @FXML
    private void handleToggleEnemyShips() {
        enemyShipsVisible = !enemyShipsVisible;

        if (enemyShipsVisible) {
            showEnemyShips();
            toggleEnemyShipsButton.setText("👁️ Ocultar barcos enemigos");
        } else {
            hideEnemyShips();
            toggleEnemyShipsButton.setText("👁️ Ver barcos enemigos");
        }
    }

    /* ===================== DISPAROS DEL JUGADOR ===================== */

    private void handlePlayerShot(int row, int col) {
        if (!isPlayerTurn || gameOver) return;

        BoardCell cell = enemyBoardCells[row][col];

        if (cell.isShot()) return;

        boolean hit = false;
        Barco hitBarco = null;

        for (Barco barco : enemyBoard.getBarcos()) {
            if (barco.occupiesCell(row, col)) {
                hit = true;
                hitBarco = barco;
                barco.agregarImpacto();
                break;
            }
        }

        if (hit) {
            if (hitBarco.seHunde()) {
                markShipAsSunk(hitBarco, enemyBoardCells);
                enemyShipsSunk++;
                turnLabel.setText("🔥 ¡Hundido! — Dispara de nuevo");

                if (enemyShipsSunk >= TOTAL_SHIPS) {
                    endGame(true);
                    return;
                }
            } else {
                cell.setHit();
                turnLabel.setText("💣 ¡Tocado! — Dispara de nuevo");
            }
        } else {
            cell.setMiss();
            CSVLogger.log(row, col, "MISS");

            turnLabel.setText("❌ Agua — Turno de la máquina");
            isPlayerTurn = false;

            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> machinePlay());
            pause.play();
        }
    }

    /**
     * Marca todas las celdas de un barco como hundido.
     */
    private void markShipAsSunk(Barco barco, BoardCell[][] cells) {
        int size = barco.getTamano();
        int startRow = barco.getFila();
        int startCol = barco.getColumna();

        for (int i = 0; i < size; i++) {
            int row = barco.isHorizontal() ? startRow : startRow + i;
            int col = barco.isHorizontal() ? startCol + i : startCol;

            cells[row][col].setSunk();
        }
    }

    /* ===================== IA DE LA MÁQUINA ===================== */

    private void machinePlay() {
        if (gameOver) return;

        Position target;

        if (huntMode && !huntTargets.isEmpty()) {
            target = huntTargets.remove(random.nextInt(huntTargets.size()));
        } else {
            if (availableTargets.isEmpty()) {
                return;
            }
            target = availableTargets.remove(random.nextInt(availableTargets.size()));
            huntMode = false;
        }

        executeMachineShot(target.row, target.col);
    }

    private void executeMachineShot(int row, int col) {
        BoardCell cell = playerBoardCells[row][col];

        if (cell.isShot()) {
            machinePlay();
            return;
        }

        boolean hit = false;
        Barco hitBarco = null;

        for (Barco barco : playerFinalBoard.getBarcos()) {
            if (barco.occupiesCell(row, col)) {
                hit = true;
                hitBarco = barco;
                barco.agregarImpacto();
                break;
            }
        }

        if (hit) {
            if (hitBarco.seHunde()) {
                markShipAsSunk(hitBarco, playerBoardCells);
                playerShipsSunk++;
                turnLabel.setText("🔥 ¡La máquina hundió tu barco! — Máquina dispara de nuevo");

                huntMode = false;
                huntTargets.clear();
                lastHit = null;

                if (playerShipsSunk >= TOTAL_SHIPS) {
                    endGame(false);
                    return;
                }
            } else {
                cell.setHit();
                turnLabel.setText("💣 ¡La máquina tocó tu barco! — Máquina dispara de nuevo");

                huntMode = true;
                lastHit = new Position(row, col);
                addAdjacentTargets(row, col);
            }

            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(e -> machinePlay());
            pause.play();
        } else {
            cell.setMiss();
            turnLabel.setText("❌ La máquina falló — Tu turno");
            isPlayerTurn = true;
        }
    }

    private void addAdjacentTargets(int row, int col) {
        if (row > 0) {
            Position p = new Position(row - 1, col);
            if (availableTargets.contains(p) && !huntTargets.contains(p)) {
                huntTargets.add(p);
                availableTargets.remove(p);
            }
        }

        if (row < BOARD_SIZE - 1) {
            Position p = new Position(row + 1, col);
            if (availableTargets.contains(p) && !huntTargets.contains(p)) {
                huntTargets.add(p);
                availableTargets.remove(p);
            }
        }

        if (col > 0) {
            Position p = new Position(row, col - 1);
            if (availableTargets.contains(p) && !huntTargets.contains(p)) {
                huntTargets.add(p);
                availableTargets.remove(p);
            }
        }

        if (col < BOARD_SIZE - 1) {
            Position p = new Position(row, col + 1);
            if (availableTargets.contains(p) && !huntTargets.contains(p)) {
                huntTargets.add(p);
                availableTargets.remove(p);
            }
        }
    }

    /* ===================== FIN DEL JUEGO ===================== */

    private void endGame(boolean playerWon) {
        gameOver = true;
        showEnemyShips();

        if (playerWon) {
            turnLabel.setText("🎉 ¡VICTORIA! Has hundido todos los barcos enemigos");
            turnLabel.setStyle("-fx-text-fill: #00FF00; -fx-font-size: 18px; -fx-font-weight: bold;");
        } else {
            turnLabel.setText("💀 DERROTA — La máquina hundió todos tus barcos");
            turnLabel.setStyle("-fx-text-fill: #FF0000; -fx-font-size: 18px; -fx-font-weight: bold;");
        }
    }

    /* ===================== BOTÓN SALIR ===================== */

    @FXML
    void handleExit(ActionEvent event) {
        System.exit(0);
    }

    /* ===================== JUGADOR ===================== */

    public void setPlayer(Player player) {
        this.player = player;
        if (playerNameLabel != null) {
            playerNameLabel.setText("Jugador: " + player.getNickname());
        }
    }







}