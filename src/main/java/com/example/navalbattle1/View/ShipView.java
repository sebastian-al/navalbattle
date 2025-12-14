package com.example.navalbattle1.View;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ShipView extends Pane {

    /* ===================== ENUM TIPO ===================== */

    public enum Type {
        CARRIER(4),
        SUBMARINE(3),
        DESTROYER(2),
        FRIGATE(1);

        private final int size;

        Type(int size) {
            this.size = size;
        }

        public int getSize() {
            return size;
        }

        /**
         * 🔁 MÉTODO CLAVE REUTILIZABLE
         * Convierte tamaño lógico (modelo) → tipo visual (vista)
         */
        public static Type fromSize(int size) {
            return switch (size) {
                case 4 -> CARRIER;
                case 3 -> SUBMARINE;
                case 2 -> DESTROYER;
                case 1 -> FRIGATE;
                default ->
                        throw new IllegalArgumentException("Tamaño de barco inválido: " + size);
            };
        }
    }

    /* ===================== ORIENTACIÓN ===================== */

    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

    /* ===================== CONSTANTES ===================== */

    private static final int CELL_SIZE = BoardCell.SIZE;
    private static final int GAP = 2;

    /* ===================== CONSTRUCTOR ===================== */

    public ShipView(Type type, Orientation orientation) {

        int length = (type.getSize() * CELL_SIZE)
                + ((type.getSize() - 1) * GAP);
        int thickness = CELL_SIZE;

        Rectangle body;

        if (orientation == Orientation.HORIZONTAL) {
            body = new Rectangle(length, thickness);
        } else {
            body = new Rectangle(thickness, length);
        }

        body.setFill(Color.LIGHTGRAY);
        body.setStroke(Color.DARKGRAY);
        body.setStrokeWidth(1.5);
        body.setArcWidth(10);
        body.setArcHeight(10);

        getChildren().add(body);

        // ⚠️ Importante: permite posicionamiento exacto
        setPickOnBounds(false);
    }
}

