package com.example.navalbattle1.View;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class BoardCell extends StackPane {

    public static final int SIZE = 35;

    private final int row;
    private final int col;

    /* ===================== ESTADO ===================== */

    private boolean shot = false; // ← CLAVE para el GameController

    /* ===================== CONSTRUCTOR ===================== */

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

    /* ===================== CONSULTA DE ESTADO ===================== */

    public boolean isShot() {
        return shot;
    }

    private void markShot() {
        this.shot = true;
    }

    /* ===================== ESTADO BASE ===================== */

    public void setWater() {
        getChildren().clear();
        shot = false;

        setStyle("""
            -fx-background-color: #1e3d59;
            -fx-border-color: black;
            -fx-border-width: 1;
        """);
    }

    /* ===================== DISPAROS (TABLERO ENEMIGO) ===================== */

    // ❌ Agua
    public void setMiss() {
        markShot();
        getChildren().clear();

        Label miss = new Label("✖");
        miss.setFont(Font.font(18));
        miss.setTextFill(Color.LIGHTBLUE);

        getChildren().add(miss);
    }

    // 💣 Tocado
    public void setHit() {
        markShot();
        getChildren().clear();

        Label hit = new Label("💣");
        hit.setFont(Font.font(18));

        getChildren().add(hit);
    }

    // 🔥 Hundido
    public void setSunk() {
        markShot();
        getChildren().clear();

        Label fire = new Label("🔥");
        fire.setFont(Font.font(18));

        getChildren().add(fire);
    }

    /* ===================== BARCOS (SOLO VISUAL LOCAL) ===================== */
    // ⚠️ Estos métodos NO se usan en el tablero enemigo

    public void setCarrier() {
        drawShip(Color.DARKGRAY, 28, 28);
    }

    public void setSubmarine() {
        drawShip(Color.DARKSEAGREEN, 26, 16);
    }

    public void setDestroyer() {
        drawShip(Color.LIGHTGRAY, 26, 14);
    }

    public void setFrigate() {
        drawShip(Color.LIGHTBLUE, 18, 18);
    }

    private void drawShip(Color color, double w, double h) {
        getChildren().clear();

        Rectangle body = new Rectangle(w, h);
        body.setFill(color);
        body.setArcWidth(6);
        body.setArcHeight(6);

        getChildren().add(body);
    }

    /* ===================== COORDENADAS ===================== */

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
