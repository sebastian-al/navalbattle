package com.example.navalbattle1.Controller;

import com.example.navalbattle1.Model.JugadorHumano;
import com.example.navalbattle1.Model.SetupModel;
import com.example.navalbattle1.Model.SetupModel.ShipType;
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
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador para la vista de configuración de la flota en el juego de Batalla Naval.
 *
 * <p>Este controlador gestiona la interfaz de usuario donde los jugadores colocan sus barcos
 * en el tablero antes de iniciar el juego. Proporciona funcionalidades para:</p>
 * <ul>
 *   <li>Crear y mostrar el tablero de juego de 10x10 celdas</li>
 *   <li>Seleccionar diferentes tipos de barcos de la flota disponible</li>
 *   <li>Arrastrar y soltar barcos en el tablero usando el mouse</li>
 *   <li>Cambiar la orientación de los barcos (horizontal/vertical) con un botón</li>
 *   <li>Reubicar barcos ya colocados arrastrándolos nuevamente</li>
 *   <li>Validar la colocación de barcos según las reglas del juego</li>
 *   <li>Continuar al juego una vez que todos los barcos estén colocados</li>
 * </ul>
 *
 * @author Tu Nombre
 * @version 2.0
 */
public class SetupController implements Initializable {

    /* ===================== CONSTANTES ===================== */

    private static final int BOARD_SIZE = 10;
    private static final int CELL_SIZE = BoardCell.SIZE;
    private static final int GAP = 2;
    private static final int GRID_PADDING = 5;

    /* ===================== MODELO ===================== */

    private final SetupModel model = new SetupModel();
<<<<<<< HEAD
    private Player player;

    /* ===================== COMPONENTES UI (FXML) ===================== */
=======
    private JugadorHumano jugadorHumano;
    /* ===================== UI ===================== */
>>>>>>> origin/main

    @FXML private GridPane playerBoardGrid;
    @FXML private Pane shipsLayer;

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

    private ShipView draggedShip = null;

    /* ===================== INICIALIZACIÓN ===================== */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createBoard();
        updateFleetLabels();
        updateOrientationButton();
        continueButton.setDisable(true);
    }

    /* ===================== CREACIÓN DEL TABLERO ===================== */

    private void createBoard() {
        boardCells = new BoardCell[BOARD_SIZE][BOARD_SIZE];
        playerBoardGrid.getChildren().clear();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                BoardCell cell = new BoardCell(row, col);
                cell.setWater();

                final int r = row;
                final int c = col;

                cell.setOnMouseReleased(e -> {
                    if (draggedShip != null) {
                        dropShip(r, c);
                    }
                });

                boardCells[row][col] = cell;
                playerBoardGrid.add(cell, col, row);
            }
        }
    }

    /* ===================== SELECCIÓN DE BARCOS ===================== */

    @FXML
    private void selectCarrier() {
        if (model.hasRemaining(ShipType.CARRIER)) {
            startDraggingShip(ShipType.CARRIER);
        }
    }

    @FXML
    private void selectSubmarine() {
        if (model.hasRemaining(ShipType.SUBMARINE)) {
            startDraggingShip(ShipType.SUBMARINE);
        }
    }

    @FXML
    private void selectDestroyer() {
        if (model.hasRemaining(ShipType.DESTROYER)) {
            startDraggingShip(ShipType.DESTROYER);
        }
    }

    @FXML
    private void selectFrigate() {
        if (model.hasRemaining(ShipType.FRIGATE)) {
            startDraggingShip(ShipType.FRIGATE);
        }
    }

    /* ===================== SISTEMA DE ARRASTRE ===================== */

    private void startDraggingShip(ShipType shipType) {
        selectedShip = shipType;

        draggedShip = new ShipView(
                ShipView.Type.valueOf(shipType.name()),
                orientation
        );

        draggedShip.setOpacity(0.7);
        draggedShip.setMouseTransparent(true);

        shipsLayer.getChildren().add(draggedShip);
        setupDragHandlers();
    }

    private void setupDragHandlers() {
        playerBoardGrid.setOnMouseMoved(e -> {
            if (draggedShip != null) {
                double x = e.getX();
                double y = e.getY();
                draggedShip.setLayoutX(x - draggedShip.getWidth() / 2);
                draggedShip.setLayoutY(y - draggedShip.getHeight() / 2);

                updateHighlightFromMousePosition(x, y);
            }
        });

        playerBoardGrid.setOnMouseDragged(e -> {
            if (draggedShip != null) {
                double x = e.getX();
                double y = e.getY();
                draggedShip.setLayoutX(x - draggedShip.getWidth() / 2);
                draggedShip.setLayoutY(y - draggedShip.getHeight() / 2);

                updateHighlightFromMousePosition(x, y);
            }
        });
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

            if (r < BOARD_SIZE && c < BOARD_SIZE && r >= 0 && c >= 0) {
                if (isValid) {
                    boardCells[r][c].setStyle("-fx-background-color: rgba(0, 255, 0, 0.5); -fx-border-color: #1a3a52; -fx-border-width: 1;");
                } else {
                    boardCells[r][c].setStyle("-fx-background-color: rgba(255, 0, 0, 0.5); -fx-border-color: #1a3a52; -fx-border-width: 1;");
                }
            }
        }
    }

    private void updateHighlightFromMousePosition(double mouseX, double mouseY) {
        int col = (int) ((mouseX - GRID_PADDING) / (CELL_SIZE + GAP));
        int row = (int) ((mouseY - GRID_PADDING) / (CELL_SIZE + GAP));

        if (row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE) {
            highlightPlacement(row, col);
        } else {
            clearHighlights();
        }
    }

    private void clearHighlights() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                // Restaurar el estilo original de la celda
                boardCells[i][j].setStyle("");
                boardCells[i][j].setWater(); // Restaurar el color de agua
            }
        }
    }

    private void dropShip(int row, int col) {
        if (selectedShip == null || draggedShip == null) return;

        boolean horizontal = (orientation == ShipView.Orientation.HORIZONTAL);
        boolean canPlace = model.placeShip(selectedShip, row, col, horizontal);

        if (!canPlace) {
            // Colocación inválida - remover el barco arrastrado
            shipsLayer.getChildren().remove(draggedShip);

            draggedShip = null;
            selectedShip = null;
            clearHighlights();

            playerBoardGrid.setOnMouseMoved(null);
            playerBoardGrid.setOnMouseDragged(null);
            return;
        }

        // Colocación válida - posicionar el barco
        double x = GRID_PADDING + col * (CELL_SIZE + GAP);
        double y = GRID_PADDING + row * (CELL_SIZE + GAP);

        draggedShip.setLayoutX(x);
        draggedShip.setLayoutY(y);
        draggedShip.setOpacity(1.0);

        // IMPORTANTE: El barco colocado DEBE permanecer mouseTransparent=true
        // para que no bloquee los clics en las celdas
        draggedShip.setMouseTransparent(true);

        // Limpiar estado
        draggedShip = null;
        selectedShip = null;
        clearHighlights();

        playerBoardGrid.setOnMouseMoved(null);
        playerBoardGrid.setOnMouseDragged(null);

        updateFleetLabels();

        if (model.isFleetComplete()) {
            continueButton.setDisable(false);
        }
    }

    /* ===================== ROTACIÓN ===================== */

    @FXML
    private void handleRotate() {
        orientation = (orientation == ShipView.Orientation.HORIZONTAL)
                ? ShipView.Orientation.VERTICAL
                : ShipView.Orientation.HORIZONTAL;

        updateOrientationButton();

        if (draggedShip != null && selectedShip != null) {
            shipsLayer.getChildren().remove(draggedShip);

            playerBoardGrid.setOnMouseMoved(null);
            playerBoardGrid.setOnMouseDragged(null);

            startDraggingShip(selectedShip);
        }
    }

    private void updateOrientationButton() {
        if (rotateButton != null) {
            String orientationText = (orientation == ShipView.Orientation.HORIZONTAL)
                    ? "Horizontal"
                    : "Vertical";
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
        GameView gameView = GameView.getInstance();
        gameView.show();

        GameController controller = gameView.getGameController();

        controller.setPlayer(jugadorHumano);
        controller.setInitialBoard(model.getFinalBoard());

        SetupView.getInstance().close();
    }

    /* ===================== CONFIGURACIÓN DEL JUGADOR ===================== */

    public void setPlayer(JugadorHumano jugadorHumano) {
        this.jugadorHumano = jugadorHumano;
    }
}