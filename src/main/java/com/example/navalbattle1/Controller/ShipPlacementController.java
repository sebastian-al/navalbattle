package com.example.navalbattle1.Controller;

import com.example.navalbattle1.Model.Player;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;

import com.example.navalbattle1.View.Ships;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Controlador para la pantalla de colocación de barcos (HU-1)
 * VERSIÓN OPTIMIZADA - Dimensiones compactas
 */
public class ShipPlacementController {

    // ==================== COMPONENTES FXML ====================

    @FXML private GridPane placementBoard;
    @FXML private VBox availableShipsContainer;
    @FXML private VBox shipPreviewContainer;

    @FXML private Label playerNameLabel;
    @FXML private Label shipsPlacedLabel;
    @FXML private Label statusLabel;
    @FXML private Label selectedShipLabel;
    @FXML private Label orientationLabel;
    @FXML private Label progressLabel;

    @FXML private Label carrierCountLabel;
    @FXML private Label submarineCountLabel;
    @FXML private Label destroyerCountLabel;
    @FXML private Label frigateCountLabel;

    @FXML private Button rotateButton;
    @FXML private Button randomPlacementButton;
    @FXML private Button clearBoardButton;
    @FXML private Button startGameButton;
    @FXML private Button cancelButton;

    // ==================== CONSTANTES ====================

    private static final int GRID_SIZE = 10;
    private static final double CELL_SIZE = 35.0; // ✅ REDUCIDO de 50.0 a 35.0
    private static final Color WATER_COLOR = Color.rgb(96, 165, 250);
    private static final Color VALID_COLOR = Color.rgb(134, 239, 172);
    private static final Color INVALID_COLOR = Color.rgb(252, 165, 165);

    // ==================== ESTADO DEL JUEGO ====================

    private boolean isHorizontal = true;
    private Ship selectedShip = null;
    private List<PlacedShip> placedShips = new ArrayList<>();
    private String[][] board = new String[GRID_SIZE][GRID_SIZE];

    private int carriersAvailable = 1;
    private int submarinesAvailable = 2;
    private int destroyersAvailable = 3;
    private int frigatesAvailable = 4;

    private com.example.navalbattle1.Model.Player player;

    /**
     * Establece el jugador actual y actualiza el label con su nombre
     */
    public void setPlayer(com.example.navalbattle1.Model.Player player) {
        this.player = player;
        if (player != null && player.getNickname() != null && !player.getNickname().isEmpty()) {
            playerNameLabel.setText("Jugador: " + player.getNickname());
        }
    }

    public com.example.navalbattle1.Model.Player getPlayer() {
        return this.player;
    }

    // ==================== CLASES INTERNAS ====================

    private static class Ship {
        String name;
        int size;
        String icon;
        Color color;
        int available;

        Ship(String name, int size, String icon, Color color, int available) {
            this.name = name;
            this.size = size;
            this.icon = icon;
            this.color = color;
            this.available = available;
        }
    }

    private static class PlacedShip {
        String shipType;
        int row;
        int col;
        boolean isHorizontal;
        int size;
        Group visualGroup;

        PlacedShip(String type, int row, int col, boolean horizontal, int size, Group visual) {
            this.shipType = type;
            this.row = row;
            this.col = col;
            this.isHorizontal = horizontal;
            this.size = size;
            this.visualGroup = visual;
        }
    }

    // ==================== INICIALIZACIÓN ====================

    @FXML
    public void initialize() {
        initializeBoard();
        createAvailableShips();
        updateUI();
        setupBoardDragAndDrop();
        orientationLabel.setText("Orientación: ➡️ Horizontal");
    }

    private void initializeBoard() {
        placementBoard.getChildren().clear();

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                board[row][col] = "water";

                Pane cell = new Pane();
                cell.setPrefSize(CELL_SIZE, CELL_SIZE);
                cell.setStyle("-fx-background-color: #60a5fa; -fx-border-color: #1e293b; -fx-border-width: 1;");

                placementBoard.add(cell, col, row);
            }
        }
    }

    private void createAvailableShips() {
        List<Ship> ships = List.of(
                new Ship("Portaaviones", 4, "🚢", Color.rgb(135, 206, 250), 1),
                new Ship("Submarino", 3, "🔱", Color.rgb(106, 90, 205), 2),
                new Ship("Destructor", 2, "⚓", Color.rgb(255, 140, 0), 3),
                new Ship("Fragata", 1, "⛵", Color.rgb(255, 182, 193), 4)
        );

        for (Ship ship : ships) {
            VBox shipCard = createShipCard(ship);
            availableShipsContainer.getChildren().add(shipCard);
        }
    }

    private VBox createShipCard(Ship ship) {
        VBox card = new VBox(4);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(160);
        card.setPrefHeight(70);

        card.setStyle(
                "-fx-background-color: rgba(65, 90, 119, 0.6);" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 8;" +
                        "-fx-border-color: " + toHex(ship.color) + ";" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 8;" +
                        "-fx-cursor: hand;"
        );

        Label iconLabel = new Label(ship.icon);
        iconLabel.setStyle("-fx-font-size: 20;");

        Label nameLabel = new Label(ship.name);
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 11;");

        Label sizeLabel = new Label("Tamaño: " + ship.size);
        sizeLabel.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 9;");

        Label countLabel = new Label("Disponibles: " + ship.available);
        countLabel.setStyle("-fx-text-fill: #fbbf24; -fx-font-size: 10; -fx-font-weight: bold;");

        card.getChildren().addAll(iconLabel, nameLabel, sizeLabel, countLabel);

        card.setOnMouseClicked(e -> {
            if (getAvailableCount(ship.name) > 0) {
                selectShip(ship);
                updateShipPreview(ship);
            }
        });

        setupDragSource(card, ship);

        return card;
    }

    private void setupDragSource(VBox card, Ship ship) {
        card.setOnDragDetected(event -> {
            if (getAvailableCount(ship.name) <= 0) {
                return;
            }

            selectShip(ship);

            Dragboard db = card.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            content.putString(ship.name + "," + ship.size);
            db.setContent(content);

            event.consume();
        });
    }

    // ==================== DRAG & DROP ====================

    private void setupBoardDragAndDrop() {
        placementBoard.setOnDragOver(event -> {
            if (event.getGestureSource() != placementBoard &&
                    event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY);
            }

            highlightValidCells(event);
            event.consume();
        });

        placementBoard.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString() && selectedShip != null) {
                int[] coords = getCellFromEvent(event);
                if (coords != null) {
                    success = placeShip(coords[0], coords[1]);
                }
            }

            event.setDropCompleted(success);
            clearHighlights();
            event.consume();
        });

        placementBoard.setOnDragExited(event -> {
            clearHighlights();
            event.consume();
        });

        placementBoard.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                int[] coords = getCellFromEvent(event);
                if (coords != null) {
                    removeShipAt(coords[0], coords[1]);
                }
            }
        });
    }

    private int[] getCellFromEvent(Event event) {
        if (event instanceof MouseEvent) {
            MouseEvent me = (MouseEvent) event;
            int col = (int) (me.getX() / CELL_SIZE);
            int row = (int) (me.getY() / CELL_SIZE);

            if (col >= 0 && col < GRID_SIZE && row >= 0 && row < GRID_SIZE) {
                return new int[]{row, col};
            }
        } else if (event instanceof DragEvent) {
            DragEvent de = (DragEvent) event;
            int col = (int) (de.getX() / CELL_SIZE);
            int row = (int) (de.getY() / CELL_SIZE);

            if (col >= 0 && col < GRID_SIZE && row >= 0 && row < GRID_SIZE) {
                return new int[]{row, col};
            }
        }

        return null;
    }

    private void highlightValidCells(DragEvent event) {
        clearHighlights();

        if (selectedShip == null) return;

        int[] coords = getCellFromEvent(event);
        if (coords == null) return;

        int row = coords[0];
        int col = coords[1];

        boolean isValid = canPlaceShip(row, col, selectedShip.size, isHorizontal);
        Color highlightColor = isValid ? VALID_COLOR : INVALID_COLOR;

        for (int i = 0; i < selectedShip.size; i++) {
            int r = isHorizontal ? row : row + i;
            int c = isHorizontal ? col + i : col;

            if (r >= 0 && r < GRID_SIZE && c >= 0 && c < GRID_SIZE) {
                Pane cell = getCellAt(r, c);
                if (cell != null) {
                    cell.setStyle(
                            "-fx-background-color: " + toHex(highlightColor) + ";" +
                                    "-fx-border-color: #1e293b; -fx-border-width: 1;"
                    );
                }
            }
        }
    }

    private void clearHighlights() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Pane cell = getCellAt(row, col);
                if (cell != null && board[row][col].equals("water")) {
                    cell.setStyle(
                            "-fx-background-color: #60a5fa;" +
                                    "-fx-border-color: #1e293b; -fx-border-width: 1;"
                    );
                }
            }
        }
    }

    // ==================== COLOCACIÓN Y REMOCIÓN ====================

    private boolean placeShip(int row, int col) {
        if (selectedShip == null) return false;
        if (!canPlaceShip(row, col, selectedShip.size, isHorizontal)) return false;

        for (int i = 0; i < selectedShip.size; i++) {
            int r = isHorizontal ? row : row + i;
            int c = isHorizontal ? col + i : col;
            board[r][c] = selectedShip.name;
        }

        Group shipVisual = createShipVisual(selectedShip, isHorizontal);

        Pane cell = getCellAt(row, col);
        if (cell != null) {
            cell.getChildren().add(shipVisual);
        }

        PlacedShip placed = new PlacedShip(
                selectedShip.name, row, col, isHorizontal, selectedShip.size, shipVisual
        );
        placedShips.add(placed);

        decrementShipCount(selectedShip.name);
        updateUI();

        statusLabel.setText("✅ " + selectedShip.name + " colocado exitosamente!");
        statusLabel.setStyle("-fx-text-fill: #10b981;");

        return true;
    }

    private boolean canPlaceShip(int row, int col, int size, boolean horizontal) {
        if (horizontal) {
            if (col + size > GRID_SIZE) return false;
        } else {
            if (row + size > GRID_SIZE) return false;
        }

        for (int i = 0; i < size; i++) {
            int r = horizontal ? row : row + i;
            int c = horizontal ? col + i : col;

            if (!board[r][c].equals("water")) {
                return false;
            }
        }

        return true;
    }

    private void removeShipAt(int row, int col) {
        String shipType = board[row][col];
        if (shipType.equals("water")) return;

        PlacedShip toRemove = null;
        for (PlacedShip ship : placedShips) {
            if (ship.shipType.equals(shipType)) {
                boolean inRange = false;
                for (int i = 0; i < ship.size; i++) {
                    int r = ship.isHorizontal ? ship.row : ship.row + i;
                    int c = ship.isHorizontal ? ship.col + i : ship.col;
                    if (r == row && c == col) {
                        inRange = true;
                        break;
                    }
                }
                if (inRange) {
                    toRemove = ship;
                    break;
                }
            }
        }

        if (toRemove != null) {
            for (int i = 0; i < toRemove.size; i++) {
                int r = toRemove.isHorizontal ? toRemove.row : toRemove.row + i;
                int c = toRemove.isHorizontal ? toRemove.col + i : toRemove.col;

                board[r][c] = "water";

                Pane cell = getCellAt(r, c);
                if (cell != null) {
                    cell.getChildren().clear();
                    cell.setStyle(
                            "-fx-background-color: #60a5fa;" +
                                    "-fx-border-color: #1e293b; -fx-border-width: 1;"
                    );
                }
            }

            placedShips.remove(toRemove);
            incrementShipCount(toRemove.shipType);
            updateUI();

            statusLabel.setText("🗑️ " + toRemove.shipType + " removido del tablero");
            statusLabel.setStyle("-fx-text-fill: #ef4444;");
        }
    }

    private Group createShipVisual(Ship ship, boolean horizontal) {
        Group visual = null;

        switch (ship.name) {
            case "Portaaviones":
                visual = new Ships.PortaavionesView(CELL_SIZE, horizontal);
                break;
            case "Submarino":
                visual = new Ships.SubMarinoView(CELL_SIZE, horizontal);
                break;
            case "Destructor":
                visual = new Ships.DestructorView(CELL_SIZE, horizontal);
                break;
            case "Fragata":
                visual = new Ships.FragataView(CELL_SIZE, !horizontal);
                break;
        }

        return visual != null ? visual : new Group();
    }

    // ==================== EVENT HANDLERS ====================

    @FXML
    private void handleRotateShip() {
        isHorizontal = !isHorizontal;

        String orientation = isHorizontal ? "➡️ Horizontal" : "⬇️ Vertical";
        orientationLabel.setText("Orientación: " + orientation);

        if (selectedShip != null) {
            updateShipPreview(selectedShip);
        }
    }

    @FXML
    private void handleRandomPlacement() {
        clearBoard();

        Random rand = new Random();

        List<Ship> ships = List.of(
                new Ship("Portaaviones", 4, "🚢", Color.rgb(100, 100, 120), 1),
                new Ship("Submarino", 3, "🔱", Color.rgb(50, 80, 120), 2),
                new Ship("Submarino", 3, "🔱", Color.rgb(50, 80, 120), 2),
                new Ship("Destructor", 2, "⚓", Color.rgb(80, 120, 80), 3),
                new Ship("Destructor", 2, "⚓", Color.rgb(80, 120, 80), 3),
                new Ship("Destructor", 2, "⚓", Color.rgb(80, 120, 80), 3),
                new Ship("Fragata", 1, "⛵", Color.rgb(230, 150, 70), 4),
                new Ship("Fragata", 1, "⛵", Color.rgb(230, 150, 70), 4),
                new Ship("Fragata", 1, "⛵", Color.rgb(230, 150, 70), 4),
                new Ship("Fragata", 1, "⛵", Color.rgb(230, 150, 70), 4)
        );

        for (Ship ship : ships) {
            boolean placed = false;
            int attempts = 0;

            while (!placed && attempts < 100) {
                int row = rand.nextInt(GRID_SIZE);
                int col = rand.nextInt(GRID_SIZE);
                boolean horizontal = rand.nextBoolean();

                selectedShip = ship;
                isHorizontal = horizontal;

                if (canPlaceShip(row, col, ship.size, horizontal)) {
                    placeShip(row, col);
                    placed = true;
                }

                attempts++;
            }
        }

        statusLabel.setText("🎲 Colocación aleatoria completada!");
        statusLabel.setStyle("-fx-text-fill: #8b5cf6;");
    }

    @FXML
    private void handleClearBoard() {
        clearBoard();

        statusLabel.setText("🗑️ Tablero limpiado - Comienza de nuevo");
        statusLabel.setStyle("-fx-text-fill: #ef4444;");
    }

    private void clearBoard() {
        for (PlacedShip ship : placedShips) {
            for (int i = 0; i < ship.size; i++) {
                int r = ship.isHorizontal ? ship.row : ship.row + i;
                int c = ship.isHorizontal ? ship.col + i : ship.col;

                Pane cell = getCellAt(r, c);
                if (cell != null) {
                    cell.getChildren().clear();
                }
            }
        }

        placedShips.clear();
        initializeBoard();

        carriersAvailable = 1;
        submarinesAvailable = 2;
        destroyersAvailable = 3;
        frigatesAvailable = 4;

        updateUI();
    }

    @FXML
    private void handleStartGame() {
        System.out.println("Iniciando batalla con " + placedShips.size() + " barcos colocados");

        statusLabel.setText("⚔️ ¡INICIANDO BATALLA!");
        statusLabel.setStyle("-fx-text-fill: #fbbf24;");
    }

    @FXML
    private void handleCancel() {
        System.out.println("Cancelando colocación");
    }

    // ==================== UTILIDADES ====================

    private void selectShip(Ship ship) {
        selectedShip = ship;
        selectedShipLabel.setText(ship.icon + " " + ship.name);
    }

    private void updateShipPreview(Ship ship) {
        shipPreviewContainer.getChildren().clear();
        Group preview = createShipVisual(ship, isHorizontal);
        shipPreviewContainer.getChildren().add(preview);
    }

    private void updateUI() {
        int totalPlaced = placedShips.size();

        shipsPlacedLabel.setText("Barcos colocados: " + totalPlaced + "/10");
        progressLabel.setText(totalPlaced + " / 10 barcos colocados");

        carrierCountLabel.setText("🚢 Portaaviones (4): " + carriersAvailable);
        submarineCountLabel.setText("🔱 Submarinos (3): " + submarinesAvailable);
        destroyerCountLabel.setText("⚓ Destructores (2): " + destroyersAvailable);
        frigateCountLabel.setText("⛵ Fragatas (1): " + frigatesAvailable);

        startGameButton.setDisable(totalPlaced < 10);

        if (totalPlaced == 10) {
            statusLabel.setText("✅ ¡Todos los barcos colocados! Listo para la batalla");
            statusLabel.setStyle("-fx-text-fill: #10b981;");
        }
    }

    private Pane getCellAt(int row, int col) {
        for (javafx.scene.Node node : placementBoard.getChildren()) {
            Integer r = GridPane.getRowIndex(node);
            Integer c = GridPane.getColumnIndex(node);

            if (r != null && c != null && r == row && c == col) {
                return (Pane) node;
            }
        }

        return null;
    }

    private int getAvailableCount(String shipName) {
        switch (shipName) {
            case "Portaaviones": return carriersAvailable;
            case "Submarino": return submarinesAvailable;
            case "Destructor": return destroyersAvailable;
            case "Fragata": return frigatesAvailable;
            default: return 0;
        }
    }

    private void decrementShipCount(String shipName) {
        switch (shipName) {
            case "Portaaviones": carriersAvailable--; break;
            case "Submarino": submarinesAvailable--; break;
            case "Destructor": destroyersAvailable--; break;
            case "Fragata": frigatesAvailable--; break;
        }
    }

    private void incrementShipCount(String shipName) {
        switch (shipName) {
            case "Portaaviones": carriersAvailable++; break;
            case "Submarino": submarinesAvailable++; break;
            case "Destructor": destroyersAvailable++; break;
            case "Fragata": frigatesAvailable++; break;
        }
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}