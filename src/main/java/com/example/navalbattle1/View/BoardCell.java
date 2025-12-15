package com.example.navalbattle1.View;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Representa una celda individual del tablero de Batalla Naval.
 * Puede contener agua, parte de un barco, y marcas de disparo.
 * Usa StackPane para superponer: fondo -> barco -> marca de disparo
 */
public class BoardCell extends StackPane {

    public static final int SIZE = 32;

    private final int row;
    private final int col;
    private final Rectangle background;
    private ShipView shipView = null; // Referencia al barco completo
    private boolean shot = false;
    private CellState state = CellState.WATER;
    private Text hitMarker;
    private Circle missMarker;

    public enum CellState {
        WATER,      // Agua (celda vacía)
        SHIP,       // Contiene parte de un barco
        MISS,       // Disparo fallido
        HIT,        // Disparo acertado
        SUNK        // Barco hundido
    }

    public BoardCell(int row, int col) {
        this.row = row;
        this.col = col;

        setPrefSize(SIZE, SIZE);
        setMaxSize(SIZE, SIZE);
        setMinSize(SIZE, SIZE);

        background = new Rectangle(SIZE, SIZE);
        background.setFill(Color.web("#2c5f7c"));
        background.setStroke(Color.web("#1a3a52"));
        background.setStrokeWidth(1);

        getChildren().add(background);
    }

    /**
     * Asocia esta celda con un barco completo (para referencia).
     * El barco se dibuja en el GridPane, no dentro de esta celda individual.
     */
    public void setShipReference(ShipView ship) {
        this.shipView = ship;
        this.state = CellState.SHIP;
    }

    /**
     * Verifica si esta celda es parte de un barco.
     */
    public boolean hasShip() {
        return shipView != null;
    }

    /**
     * Establece esta celda como agua (vacía).
     */
    public void setWater() {
        state = CellState.WATER;
        background.setFill(Color.web("#2c5f7c"));
        shot = false;
        removeMarkers();
    }

    /**
     * Marca esta celda como disparo fallido (agua).
     */
    public void setMiss() {
        state = CellState.MISS;
        background.setFill(Color.web("#4a7c9e"));
        shot = true;

        removeMarkers();

        // Añadir círculo blanco ENCIMA de todo
        missMarker = new Circle(SIZE / 2, SIZE / 2, 4);
        missMarker.setFill(Color.WHITE);
        missMarker.setStroke(Color.LIGHTGRAY);
        missMarker.setStrokeWidth(1);
        getChildren().add(missMarker);
    }

    /**
     * Marca esta celda como impacto en barco.
     * La X aparece ENCIMA del barco.
     */
    public void setHit() {
        state = CellState.HIT;
        background.setFill(Color.web("#d94a3d"));
        shot = true;

        removeMarkers();

        // La X se dibuja ENCIMA del barco
        hitMarker = new Text("✗");
        hitMarker.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        hitMarker.setFill(Color.RED);
        hitMarker.setStroke(Color.DARKRED);
        hitMarker.setStrokeWidth(2);

        // Asegurar que esté visible encima de todo
        getChildren().add(hitMarker);
    }

    /**
     * Marca esta celda como parte de un barco hundido.
     * La calavera aparece ENCIMA del barco.
     */
    public void setSunk() {
        state = CellState.SUNK;
        background.setFill(Color.web("#8b0000"));
        shot = true;

        removeMarkers();

        hitMarker = new Text("💀");
        hitMarker.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        // Asegurar que esté visible encima de todo
        getChildren().add(hitMarker);
    }

    /**
     * Remueve todas las marcas de disparo.
     */
    private void removeMarkers() {
        if (hitMarker != null) {
            getChildren().remove(hitMarker);
            hitMarker = null;
        }
        if (missMarker != null) {
            getChildren().remove(missMarker);
            missMarker = null;
        }
    }

    /**
     * Verifica si ya se disparó a esta celda.
     */
    public boolean isShot() {
        return shot;
    }

    /**
     * Obtiene el estado actual de la celda.
     */
    public CellState getState() {
        return state;
    }

    /**
     * Obtiene la fila de esta celda.
     */
    public int getRow() {
        return row;
    }

    /**
     * Obtiene la columna de esta celda.
     */
    public int getCol() {
        return col;
    }

    /**
     * Resalta la celda con un color específico (para preview de colocación).
     */
    public void setHighlight(boolean valid) {
        if (valid) {
            background.setFill(Color.web("#4ade80", 0.5));
        } else {
            background.setFill(Color.web("#f87171", 0.5));
        }
    }

    /**
     * Limpia cualquier resaltado y restaura el color según el estado.
     */
    public void clearHighlight() {
        switch (state) {
            case WATER, SHIP -> background.setFill(Color.web("#2c5f7c"));
            case MISS -> background.setFill(Color.web("#4a7c9e"));
            case HIT -> background.setFill(Color.web("#d94a3d"));
            case SUNK -> background.setFill(Color.web("#8b0000"));
        }
    }
}