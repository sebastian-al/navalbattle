/** Esta clase se usa para manejar los tableros del jugador y enemigo */
package com.example.navalbattle1.Controller;

import com.example.navalbattle1.Model.JugadorHumano;
import com.example.navalbattle1.Model.SetupModel;
import com.example.navalbattle1.Model.SetupModel.ShipType;
import com.example.navalbattle1.Model.Tablero;
import com.example.navalbattle1.View.BoardCell;
import com.example.navalbattle1.View.GameView;
import com.example.navalbattle1.View.SetupView;
import com.example.navalbattle1.View.ShipView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador para la vista de configuración de la flota en el juego de Batalla Naval.
 * Los barcos se colocan directamente en el GridPane.
 */
public class SetupController implements Initializable {

    /* ===================== CONSTANTES ===================== */

    private static final int BOARD_SIZE = 10;
    private static final int CELL_SIZE = BoardCell.SIZE;
    private static final int GAP = 2;

    /* ===================== MODELO ===================== */

    private final SetupModel model = new SetupModel();
    private JugadorHumano jugadorHumano;

    /* ===================== COMPONENTES UI (FXML) ===================== */

    @FXML private GridPane playerBoardGrid;

    @FXML private Label carrierLabel;
    @FXML private Label submarineLabel;
    @FXML private Label destroyerLabel;
    @FXML private Label frigateLabel;

    @FXML private Button helpButton;
    @FXML private Button continueButton;
    @FXML private Button rotateButton;
    @FXML private ProgressBar progressBar;

    /* ===================== ESTADO DEL CONTROLADOR ===================== */

    private ShipType selectedShip = null;
    private ShipView.Orientation orientation = ShipView.Orientation.HORIZONTAL;
    private BoardCell[][] boardCells;
    private ShipView previewShip = null;

    /* ===================== INICIALIZACIÓN ===================== */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createBoard();
        updateFleetLabels();
        updateOrientationButton();
        continueButton.setDisable(true);

        // Inicializar jugador humano si no existe
        if (jugadorHumano == null) {
            jugadorHumano = new JugadorHumano("Jugador 1");
        }
    }

    /* ===================== CREACIÓN DEL TABLERO ===================== */

    private void createBoard() {
        boardCells = new BoardCell[BOARD_SIZE][BOARD_SIZE];
        playerBoardGrid.getChildren().clear();

        // Configurar el GridPane
        playerBoardGrid.setHgap(GAP);
        playerBoardGrid.setVgap(GAP);

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                BoardCell cell = new BoardCell(row, col);
                cell.setWater();

                final int r = row;
                final int c = col;

                cell.setOnMouseClicked(e -> {
                    if (selectedShip != null) {
                        placeShip(r, c);
                    }
                });

                cell.setOnMouseEntered(e -> {
                    if (selectedShip != null) {
                        highlightPlacement(r, c);
                    }
                });

                boardCells[row][col] = cell;
                playerBoardGrid.add(cell, col, row);
            }
        }

        playerBoardGrid.setOnMouseExited(e -> clearHighlights());
    }

    /* ===================== SELECCIÓN DE BARCOS ===================== */

    @FXML
    private void selectCarrier() {
        if (model.hasRemaining(ShipType.CARRIER)) {
            selectedShip = ShipType.CARRIER;
            updateShipSelection();
        }
    }

    @FXML
    private void selectSubmarine() {
        if (model.hasRemaining(ShipType.SUBMARINE)) {
            selectedShip = ShipType.SUBMARINE;
            updateShipSelection();
        }
    }

    @FXML
    private void selectDestroyer() {
        if (model.hasRemaining(ShipType.DESTROYER)) {
            selectedShip = ShipType.DESTROYER;
            updateShipSelection();
        }
    }

    @FXML
    private void selectFrigate() {
        if (model.hasRemaining(ShipType.FRIGATE)) {
            selectedShip = ShipType.FRIGATE;
            updateShipSelection();
        }
    }

    private void updateShipSelection() {
        carrierLabel.setStyle(selectedShip == ShipType.CARRIER ?
                "-fx-background-color: #4ade80; -fx-text-fill: white; -fx-padding: 5;" : "");
        submarineLabel.setStyle(selectedShip == ShipType.SUBMARINE ?
                "-fx-background-color: #4ade80; -fx-text-fill: white; -fx-padding: 5;" : "");
        destroyerLabel.setStyle(selectedShip == ShipType.DESTROYER ?
                "-fx-background-color: #4ade80; -fx-text-fill: white; -fx-padding: 5;" : "");
        frigateLabel.setStyle(selectedShip == ShipType.FRIGATE ?
                "-fx-background-color: #4ade80; -fx-text-fill: white; -fx-padding: 5;" : "");
    }

    /* ===================== COLOCACIÓN DE BARCOS ===================== */

    private void placeShip(int row, int col) {
        if (selectedShip == null) return;

        boolean horizontal = (orientation == ShipView.Orientation.HORIZONTAL);
        boolean canPlace = model.placeShip(selectedShip, row, col, horizontal);

        if (!canPlace) {
            clearHighlights();
            return;
        }

        // Crear el barco completo
        ShipView ship = new ShipView(
                ShipView.Type.fromSize(selectedShip.getSize()),
                orientation
        );

        // Calcular la posición en el GridPane y añadirlo directamente
        int size = selectedShip.getSize();
        int columnSpan = horizontal ? size : 1;
        int rowSpan = horizontal ? 1 : size;

        // Añadir el barco al GridPane en la posición correcta
        playerBoardGrid.add(ship, col, row, columnSpan, rowSpan);

        // Marcar las celdas como ocupadas por el barco
        for (int i = 0; i < size; i++) {
            int r = horizontal ? row : row + i;
            int c = horizontal ? col + i : col;
            boardCells[r][c].setShipReference(ship);
        }

        selectedShip = null;
        clearHighlights();
        updateFleetLabels();
        updateShipSelection();

        if (model.isFleetComplete()) {
            continueButton.setDisable(false);
        }
    }

    private void highlightPlacement(int row, int col) {
        clearHighlights();

        if (selectedShip == null) return;

        boolean horizontal = (orientation == ShipView.Orientation.HORIZONTAL);
        int size = selectedShip.getSize();

        boolean isValid = model.canPlaceShip(selectedShip, row, col, horizontal);

        for (int i = 0; i < size; i++) {
            int r = horizontal ? row : row + i;
            int c = horizontal ? col + i : col;

            if (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE) {
                boardCells[r][c].setHighlight(isValid);
            }
        }
    }

    private void clearHighlights() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                boardCells[i][j].clearHighlight();
            }
        }
    }

    /* ===================== ROTACIÓN ===================== */

    @FXML
    private void handleRotate() {
        orientation = (orientation == ShipView.Orientation.HORIZONTAL)
                ? ShipView.Orientation.VERTICAL
                : ShipView.Orientation.HORIZONTAL;

        updateOrientationButton();
        clearHighlights();
    }

    private void updateOrientationButton() {
        if (rotateButton != null) {
            String orientationText = (orientation == ShipView.Orientation.HORIZONTAL)
                    ? "Horizontal ↔"
                    : "Vertical ↕";
            rotateButton.setText("Orientación: " + orientationText);
        }
    }

    /* ===================== ACTUALIZACIÓN DE UI ===================== */

    private void updateFleetLabels() {
        carrierLabel.setText("🛳️ Portaaviones (4)  x" + model.getRemaining(ShipType.CARRIER));
        submarineLabel.setText("🚇 Submarino (3)     x" + model.getRemaining(ShipType.SUBMARINE));
        destroyerLabel.setText("⚡ Destructor (2)    x" + model.getRemaining(ShipType.DESTROYER));
        frigateLabel.setText("🎀 Fragata (1)       x" + model.getRemaining(ShipType.FRIGATE));

        if (progressBar != null) {
            int totalShips = model.getTotalShipsCount();
            int placedShips = model.getPlacedShipsCount();
            progressBar.setProgress((double) placedShips / totalShips);
        }
    }

    /* ===================== NAVEGACIÓN ===================== */

    @FXML
    private void handleContinue() throws IOException {
        try {
            // Obtener el tablero final del modelo
            Tablero tableroFinal = model.getFinalBoard();

            // Transferir al jugador
            if (jugadorHumano != null && tableroFinal != null) {
                int[][] celdas = tableroFinal.getCeldas();
                jugadorHumano.setTablero(celdas);
            }

            GameView gameView = GameView.getInstance();
            gameView.show();

            GameController controller = gameView.getGameController();

            if (controller != null) {
                controller.setPlayer(jugadorHumano);
                controller.setInitialBoard(tableroFinal);
            }

            SetupView.getInstance().close();

        } catch (Exception e) {
            System.err.println("Error al continuar al juego: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Error al iniciar el juego", e);
        }
    }

    /* ===================== CONFIGURACIÓN DEL JUGADOR ===================== */

    public void setPlayer(JugadorHumano jugadorHumano) {
        this.jugadorHumano = jugadorHumano;
    }

    public JugadorHumano getPlayer() {
        return jugadorHumano;
    }
}