package com.example.navalbattle1.Controller;

import com.example.navalbattle1.Model.Player;
import com.example.navalbattle1.Model.Tablero;
import com.example.navalbattle1.Model.Barco;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Controlador principal del juego de Batalla Naval.
 *
 * <p>Gestiona la interfaz de juego incluyendo:</p>
 * <ul>
 *   <li>Tablero del jugador (solo visualización)</li>
 *   <li>Tablero enemigo (interactivo para disparar)</li>
 *   <li>Colocación automática de barcos enemigos</li>
 *   <li>Lógica de disparos y turnos</li>
 *   <li>Visualización opcional de barcos enemigos (modo trampa)</li>
 * </ul>
 *
 * @author Tu Nombre
 * @version 2.0
 */
public class GameController implements Initializable {

    /* ===================== CONSTANTES ===================== */

    private static final int BOARD_SIZE = 10;
    private static final int CELL_SIZE = BoardCell.SIZE;
    private static final int GAP = 2;
    private static final int GRID_PADDING = 5;

    /* ===================== MODELO ===================== */

    private Player player;
    private Tablero playerFinalBoard; // viene desde Setup
    private Tablero enemyBoard; // tablero con barcos enemigos
    private final Random random = new Random();

    /* ===================== UI ===================== */

    @FXML private Label playerNameLabel;
    @FXML private Label turnLabel;
    @FXML private GridPane positionBoardGrid; // tablero jugador (solo visual)
    @FXML private GridPane mainBoardGrid; // tablero enemigo
    @FXML private Pane playerShipsLayer; // capa barcos 2D jugador
    @FXML private Pane enemyShipsLayer; // capa barcos 2D enemigo
    @FXML private Button toggleEnemyShipsButton; // botón para mostrar/ocultar barcos enemigos

    /* ===================== MATRICES VISUALES ===================== */

    private BoardCell[][] playerBoardCells;
    private BoardCell[][] enemyBoardCells;

    /* ===================== ESTADO ===================== */

    private boolean enemyShipsVisible = false;

    /* ===================== INIT ===================== */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createBoards();
        createEnemyFleet();
        turnLabel.setText("🎯 Turno del jugador");
    }

    /* ===================== INYECCIÓN DESDE SETUP ===================== */

    /**
     * Llamado desde SetupController para establecer el tablero del jugador.
     *
     * @param tablero el tablero con los barcos del jugador colocados
     */
    public void setInitialBoard(Tablero tablero) {
        this.playerFinalBoard = tablero;
        drawFinalPlayerShips();
    }

    /* ===================== CREACIÓN DE TABLEROS ===================== */

    /**
     * Crea los dos tableros visuales: el del jugador y el del enemigo.
     */
    private void createBoards() {
        playerBoardCells = new BoardCell[BOARD_SIZE][BOARD_SIZE];
        enemyBoardCells = new BoardCell[BOARD_SIZE][BOARD_SIZE];

        positionBoardGrid.getChildren().clear();
        mainBoardGrid.getChildren().clear();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {

                // === Tablero del jugador (solo observación) ===
                BoardCell playerCell = new BoardCell(row, col);
                playerCell.setWater();
                playerBoardCells[row][col] = playerCell;
                positionBoardGrid.add(playerCell, col, row);

                // === Tablero enemigo (clickeable) ===
                BoardCell enemyCell = new BoardCell(row, col);
                enemyCell.setWater();

                final int r = row;
                final int c = col;
                enemyCell.setOnMouseClicked(e -> handleEnemyShot(r, c));

                enemyBoardCells[row][col] = enemyCell;
                mainBoardGrid.add(enemyCell, col, row);
            }
        }
    }

    /* ===================== CREACIÓN FLOTA ENEMIGA ===================== */

    /**
     * Crea y coloca aleatoriamente la flota enemiga en el tablero.
     * La flota consiste en:
     * - 1 Portaaviones (4 casillas)
     * - 2 Submarinos (3 casillas)
     * - 3 Destructores (2 casillas)
     * - 4 Fragatas (1 casilla)
     */
    private void createEnemyFleet() {
        enemyBoard = new Tablero();

        // Definir los barcos a colocar
        List<ShipConfig> fleet = new ArrayList<>();
        fleet.add(new ShipConfig("Portaaviones", 4, 1));
        fleet.add(new ShipConfig("Submarino", 3, 2));
        fleet.add(new ShipConfig("Destructor", 2, 3));
        fleet.add(new ShipConfig("Fragata", 1, 4));

        // Colocar cada tipo de barco
        for (ShipConfig config : fleet) {
            for (int i = 0; i < config.count; i++) {
                placeRandomEnemyShip(config.name, config.size);
            }
        }

        // Los barcos enemigos están ocultos por defecto
        if (enemyShipsLayer != null) {
            enemyShipsLayer.setVisible(false);
        }

        drawEnemyShips();
    }

    /**
     * Coloca un barco enemigo en una posición aleatoria válida.
     *
     * @param name nombre del barco
     * @param size tamaño del barco
     */
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

    /**
     * Clase auxiliar para configurar la flota.
     */
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

    /**
     * Dibuja los barcos del jugador en el tablero visual.
     */
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

            placeShipOnLayer(shipView, barco.getColumna(), barco.getFila(), playerShipsLayer);
        }
    }

    /**
     * Dibuja los barcos enemigos en el tablero visual (inicialmente ocultos).
     */
    private void drawEnemyShips() {
        if (enemyBoard == null || enemyShipsLayer == null) return;

        enemyShipsLayer.getChildren().clear();

        for (Barco barco : enemyBoard.getBarcos()) {
            ShipView shipView = new ShipView(
                    ShipView.Type.fromSize(barco.getTamano()),
                    barco.isHorizontal()
                            ? ShipView.Orientation.HORIZONTAL
                            : ShipView.Orientation.VERTICAL
            );

            placeShipOnLayer(shipView, barco.getColumna(), barco.getFila(), enemyShipsLayer);
        }
    }

    /**
     * Posiciona un barco en una capa específica.
     *
     * @param ship la vista del barco
     * @param col columna inicial
     * @param row fila inicial
     * @param layer la capa donde colocar el barco
     */
    private void placeShipOnLayer(ShipView ship, int col, int row, Pane layer) {
        // Calcular la posición exacta
        double x = col * (CELL_SIZE + GAP);
        double y = row * (CELL_SIZE + GAP);

        ship.setLayoutX(x);
        ship.setLayoutY(y);

        layer.getChildren().add(ship);
    }

    /* ===================== TOGGLE BARCOS ENEMIGOS ===================== */

    /**
     * Muestra u oculta los barcos enemigos (modo trampa/debug).
     */
    @FXML
    private void handleToggleEnemyShips() {
        enemyShipsVisible = !enemyShipsVisible;

        if (enemyShipsLayer != null) {
            enemyShipsLayer.setVisible(enemyShipsVisible);
        }

        if (toggleEnemyShipsButton != null) {
            if (enemyShipsVisible) {
                toggleEnemyShipsButton.setText("👁️ Ocultar barcos enemigos");
            } else {
                toggleEnemyShipsButton.setText("👁️ Ver barcos enemigos");
            }
        }
    }

    /* ===================== DISPAROS AL ENEMIGO ===================== */

    /**
     * Maneja un disparo del jugador al tablero enemigo.
     *
     * @param row fila del disparo
     * @param col columna del disparo
     */
    private void handleEnemyShot(int row, int col) {
        BoardCell cell = enemyBoardCells[row][col];

        if (cell.isShot()) return;

        // Verificar si hay un barco en esa posición
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
                cell.setSunk(); // 🔥
                turnLabel.setText("🔥 ¡Hundido! — Dispara de nuevo");
            } else {
                cell.setHit(); // 💣
                turnLabel.setText("💣 ¡Tocado! — Dispara de nuevo");
            }
        } else {
            cell.setMiss(); // ❌
            turnLabel.setText("❌ Agua — Turno de la máquina");
        }
    }

    /* ===================== BOTÓN SALIR ===================== */

    /**
     * Maneja el evento de salir del juego.
     */
    @FXML
    void handleExit(ActionEvent event) {
        System.exit(0);
    }

    /* ===================== JUGADOR ===================== */

    /**
     * Establece el jugador actual.
     *
     * @param player el jugador
     */
    public void setPlayer(Player player) {
        this.player = player;
        if (playerNameLabel != null) {
            playerNameLabel.setText("Jugador: " + player.getNickname());
        }
    }
}