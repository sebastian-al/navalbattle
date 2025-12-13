package com.example.navalbattle1.View;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ShipView extends Pane {

    public enum Type {
        CARRIER(4),
        SUBMARINE(3),
        DESTROYER(2),
        FRIGATE(1);

        public final int size;

        Type(int size) {
            this.size = size;
        }
    }

    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

    private static final int CELL_SIZE = BoardCell.SIZE;
    private static final int GAP = 2;

    public ShipView(Type type, Orientation orientation) {

        int length = (type.size * CELL_SIZE) + ((type.size - 1) * GAP);
        int thickness = CELL_SIZE;

        Rectangle body;

        if (orientation == Orientation.HORIZONTAL) {
            body = new Rectangle(length, thickness);
        } else {
            body = new Rectangle(thickness, length);
        }

        body.setFill(Color.LIGHTGRAY);
        body.setStroke(Color.DARKGRAY);
        body.setArcWidth(10);
        body.setArcHeight(10);

        getChildren().add(body);
    }
}

