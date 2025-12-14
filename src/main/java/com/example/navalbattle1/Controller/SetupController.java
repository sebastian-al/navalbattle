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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SetupController implements Initializable {

    /* ===================== CONSTANTES ===================== */

    private static final int BOARD_SIZE = 10;
    private static final int CELL_SIZE = BoardCell.SIZE;
    private static final int GAP = 2;
    private static final int GRID_PADDING = 5;

    /* ===================== MODELO ===================== */

    private final SetupModel model = new SetupModel();
    private JugadorHumano jugadorHumano;
    /* ===================== UI ===================== */

    @FXML private GridPane playerBoardGrid;
    @FXML private Pane shipsLayer;

    @FXML private Label carrierLabel;
    @FXML private Label submarineLabel;
    @FXML private Label destroyerLabel;
    @FXML private Label frigateLabel;

    @FXML private Button helpButton;
    @FXML private Button continueButton;

    /* ===================== ESTADO ===================== */

    private ShipType selectedShip = null;
    private ShipView.Orientation orientation = ShipView.Orientation.HORIZONTAL;

    private BoardCell[][] boardCells;

    /* ===================== INIT ===================== */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createBoard();
        updateFleetLabels();
        enableRotation();
        continueButton.setDisable(true);
    }

    /* ===================== TABLERO ===================== */

    private void createBoard() {

        boardCells = new BoardCell[BOARD_SIZE][BOARD_SIZE];
        playerBoardGrid.getChildren().clear();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {

                BoardCell cell = new BoardCell(row, col);
                cell.setWater();

                final int r = row;
                final int c = col;

                cell.setOnMouseClicked(e -> placeShip(r, c));

                boardCells[row][col] = cell;
                playerBoardGrid.add(cell, col, row);
            }
        }
    }

    /* ===================== SELECCIÓN DE FLOTA ===================== */

    @FXML private void selectCarrier()   { selectedShip = ShipType.CARRIER; }
    @FXML private void selectSubmarine() { selectedShip = ShipType.SUBMARINE; }
    @FXML private void selectDestroyer() { selectedShip = ShipType.DESTROYER; }
    @FXML private void selectFrigate()   { selectedShip = ShipType.FRIGATE; }

    /* ===================== COLOCACIÓN ===================== */

    private void placeShip(int row, int col) {

        if (selectedShip == null) return;

        boolean horizontal = (orientation == ShipView.Orientation.HORIZONTAL);

        // Validación lógica en el modelo
        if (!model.placeShip(selectedShip, row, col, horizontal)) return;

        ShipView shipView = new ShipView(
                ShipView.Type.valueOf(selectedShip.name()),
                orientation
        );

        double x = GRID_PADDING + col * (CELL_SIZE + GAP);
        double y = GRID_PADDING + row * (CELL_SIZE + GAP);

        shipView.setLayoutX(x);
        shipView.setLayoutY(y);

        shipsLayer.getChildren().add(shipView);

        selectedShip = null;
        updateFleetLabels();

        if (model.isFleetComplete()) {
            continueButton.setDisable(false);
        }
    }


    /* ===================== UI ===================== */

    private void updateFleetLabels() {
        carrierLabel.setText("Portaaviones (4)  x" + model.getRemaining(ShipType.CARRIER));
        submarineLabel.setText("Submarino (3)     x" + model.getRemaining(ShipType.SUBMARINE));
        destroyerLabel.setText("Destructor (2)    x" + model.getRemaining(ShipType.DESTROYER));
        frigateLabel.setText("Fragata (1)       x" + model.getRemaining(ShipType.FRIGATE));
    }

    private void enableRotation() {
        playerBoardGrid.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.R) {
                orientation = (orientation == ShipView.Orientation.HORIZONTAL)
                        ? ShipView.Orientation.VERTICAL
                        : ShipView.Orientation.HORIZONTAL;
            }
        });
        playerBoardGrid.setFocusTraversable(true);
    }

    /* ===================== CONTINUAR ===================== */

    @FXML
    private void handleContinue() throws IOException {

        GameView gameView = GameView.getInstance();
        gameView.show();

        GameController controller = gameView.getGameController();

        controller.setPlayer(jugadorHumano);
        controller.setInitialBoard(model.getFinalBoard());

        SetupView.getInstance().close();
    }


    /* ===================== JUGADOR ===================== */

    public void setPlayer(JugadorHumano jugadorHumano) {
        this.jugadorHumano = jugadorHumano;
    }
}

