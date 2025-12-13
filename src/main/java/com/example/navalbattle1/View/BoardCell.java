package com.example.navalbattle1.View;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class BoardCell extends StackPane {

    public static final int SIZE = 35;

    private final int row;
    private final int col;

    public BoardCell(int row, int col) {
        this.row = row;
        this.col = col;

        setPrefSize(SIZE, SIZE);
        setMinSize(SIZE, SIZE);
        setMaxSize(SIZE, SIZE);

        setAlignment(Pos.CENTER);

        setStyle("""
            -fx-background-color: #1e3d59;
            -fx-border-color: black;
            -fx-border-width: 1;
        """);
    }

    /* ===================== ESTADOS BASE ===================== */

    public void setWater() {
        getChildren().clear();
        setStyle("""
            -fx-background-color: #1e3d59;
            -fx-border-color: black;
            -fx-border-width: 1;
        """);
    }

    /* ===================== BARCOS (JUGADOR HUMANO) ===================== */

    // 🚢 Portaaviones (4)
    public void setCarrier() {
        getChildren().clear();

        Rectangle body = new Rectangle(28, 28);
        body.setFill(Color.DARKGRAY);
        body.setArcWidth(6);
        body.setArcHeight(6);

        getChildren().add(body);
    }

    // 🛥️ Submarino (3)
    public void setSubmarine() {
        getChildren().clear();

        Rectangle body = new Rectangle(26, 16);
        body.setFill(Color.DARKSEAGREEN);
        body.setArcWidth(10);
        body.setArcHeight(10);

        Rectangle tower = new Rectangle(6, 10);
        tower.setFill(Color.DARKGREEN);
        tower.setTranslateY(-8);

        getChildren().addAll(body, tower);
    }

    // 🚤 Destructor (2)
    public void setDestroyer() {
        getChildren().clear();

        Rectangle body = new Rectangle(26, 14);
        body.setFill(Color.LIGHTGRAY);
        body.setArcWidth(4);
        body.setArcHeight(4);

        getChildren().add(body);
    }

    // ⛴️ Fragata (1)
    public void setFrigate() {
        getChildren().clear();

        Rectangle body = new Rectangle(18, 18);
        body.setFill(Color.LIGHTBLUE);
        body.setArcWidth(4);
        body.setArcHeight(4);

        getChildren().add(body);
    }

    /* ===================== DISPAROS (TABLERO ENEMIGO) ===================== */

    // ❌ Agua
    public void setMiss() {
        getChildren().clear();

        Label miss = new Label("✖");
        miss.setFont(Font.font(18));
        miss.setTextFill(Color.LIGHTBLUE);

        getChildren().add(miss);
    }

    // 💣 Tocado
    public void setHit() {
        getChildren().clear();

        Label hit = new Label("O");
        hit.setFont(Font.font(18));

        getChildren().add(hit);
    }

    // 🔥 Hundido
    public void setSunk() {
        getChildren().clear();

        Label fire = new Label("H");
        fire.setFont(Font.font(18));

        getChildren().add(fire);
    }

    /* ===================== COORDENADAS ===================== */

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
