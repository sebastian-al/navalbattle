package com.example.navalbattle1.View;

import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;

/**
 * Clase contenedora para todos los tipos de barcos del juego
 * VERSIÓN CORREGIDA: Los barcos ahora se dibujan completamente dentro de sus límites
 */
public class Ships {

    // ==================== PORTAAVIONES (4 CELDAS) ====================

    public static class PortaavionesView extends Group {

        public PortaavionesView(double cellSize, boolean horizontal) {
            double width = horizontal ? cellSize * 4 : cellSize;
            double height = horizontal ? cellSize : cellSize * 4;

            // Ajustar márgenes para que quepa todo
            double margin = 2;

            // Cuerpo principal
            Rectangle body = new Rectangle(
                    margin, margin,
                    width - (margin * 2),
                    height - (margin * 2)
            );
            LinearGradient bodyGradient = new LinearGradient(
                    0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.LIGHTSKYBLUE),
                    new Stop(1, Color.STEELBLUE)
            );
            body.setFill(bodyGradient);
            body.setStroke(Color.DARKBLUE);
            body.setStrokeWidth(1.5);
            body.setArcWidth(8);
            body.setArcHeight(8);

            // Cubierta
            Rectangle deck = new Rectangle(
                    horizontal ? cellSize * 0.3 : cellSize * 0.15,
                    horizontal ? cellSize * 0.15 : cellSize * 0.3,
                    horizontal ? cellSize * 3.4 : cellSize * 0.7,
                    horizontal ? cellSize * 0.7 : cellSize * 3.4
            );
            deck.setFill(Color.LIGHTBLUE);
            deck.setStroke(Color.STEELBLUE);
            deck.setStrokeWidth(1);

            // Torre
            Rectangle tower = new Rectangle(
                    horizontal ? cellSize * 1.8 : cellSize * 0.25,
                    horizontal ? cellSize * 0.25 : cellSize * 1.8,
                    horizontal ? cellSize * 0.8 : cellSize * 0.5,
                    horizontal ? cellSize * 0.5 : cellSize * 0.8
            );
            tower.setFill(Color.STEELBLUE);
            tower.setStroke(Color.DARKBLUE);
            tower.setStrokeWidth(1);

            getChildren().addAll(body, deck, tower);

            // Ventanas (2 ventanas en lugar de 3 para que quepan)
            for (int i = 0; i < 2; i++) {
                Rectangle window = new Rectangle(
                        horizontal ? cellSize * (1.9 + i * 0.3) : cellSize * 0.35,
                        horizontal ? cellSize * 0.35 : cellSize * (1.9 + i * 0.3),
                        horizontal ? cellSize * 0.15 : cellSize * 0.25,
                        horizontal ? cellSize * 0.25 : cellSize * 0.15
                );
                window.setFill(Color.LIGHTYELLOW);
                window.setStroke(Color.GOLD);
                window.setStrokeWidth(0.5);
                getChildren().add(window);
            }
        }
    }

    // ==================== SUBMARINO (3 CELDAS) ====================

    public static class SubMarinoView extends Group {

        public SubMarinoView(double cellSize, boolean horizontal) {
            double width = horizontal ? cellSize * 3 : cellSize;
            double height = horizontal ? cellSize : cellSize * 3;
            double margin = 2;

            // Cuerpo
            Rectangle body = new Rectangle(
                    margin, margin,
                    width - (margin * 2),
                    height - (margin * 2)
            );
            LinearGradient bodyGradient = new LinearGradient(
                    0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.DARKSLATEBLUE),
                    new Stop(1, Color.MEDIUMPURPLE)
            );
            body.setFill(bodyGradient);
            body.setStroke(Color.DARKSLATEBLUE.darker());
            body.setStrokeWidth(1.5);
            body.setArcWidth(15);
            body.setArcHeight(15);

            // Casco
            Ellipse hull = new Ellipse(
                    horizontal ? cellSize * 1.5 : cellSize * 0.5,
                    horizontal ? cellSize * 0.5 : cellSize * 1.5,
                    horizontal ? cellSize * 1.1 : cellSize * 0.35,
                    horizontal ? cellSize * 0.35 : cellSize * 1.1
            );
            hull.setFill(Color.SLATEBLUE);
            hull.setStroke(Color.DARKSLATEBLUE);
            hull.setStrokeWidth(1);

            // Torre
            Rectangle tower = new Rectangle(
                    horizontal ? cellSize * 1.3 : cellSize * 0.3,
                    horizontal ? cellSize * 0.3 : cellSize * 1.3,
                    horizontal ? cellSize * 0.4 : cellSize * 0.4,
                    horizontal ? cellSize * 0.4 : cellSize * 0.4
            );
            tower.setFill(Color.MEDIUMPURPLE);
            tower.setStroke(Color.DARKSLATEBLUE);
            tower.setStrokeWidth(1);

            // Periscopio (más corto)
            Line periscope = new Line(
                    horizontal ? cellSize * 1.5 : cellSize * 0.5,
                    horizontal ? cellSize * 0.3 : cellSize * 1.2,
                    horizontal ? cellSize * 1.5 : cellSize * 0.5,
                    horizontal ? cellSize * 0.1 : cellSize * 0.9
            );
            periscope.setStroke(Color.DARKSLATEBLUE);
            periscope.setStrokeWidth(2);

            // Lente
            Circle lens = new Circle(
                    horizontal ? cellSize * 1.5 : cellSize * 0.5,
                    horizontal ? cellSize * 0.1 : cellSize * 0.9,
                    cellSize * 0.06
            );
            lens.setFill(Color.LIGHTCYAN);
            lens.setStroke(Color.DARKSLATEBLUE);
            lens.setStrokeWidth(1);

            getChildren().addAll(body, hull, tower, periscope, lens);
        }
    }

    // ==================== DESTRUCTOR (2 CELDAS) ====================

    public static class DestructorView extends Group {

        public DestructorView(double cellSize, boolean horizontal) {
            double width = horizontal ? cellSize * 2 : cellSize;
            double height = horizontal ? cellSize : cellSize * 2;
            double margin = 2;

            // Cuerpo
            Rectangle body = new Rectangle(
                    margin, margin,
                    width - (margin * 2),
                    height - (margin * 2)
            );
            LinearGradient bodyGradient = new LinearGradient(
                    0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.ORANGE),
                    new Stop(1, Color.DARKORANGE)
            );
            body.setFill(bodyGradient);
            body.setStroke(Color.ORANGERED);
            body.setStrokeWidth(1.5);
            body.setArcWidth(6);
            body.setArcHeight(6);

            // Proa triangular (más pequeña)
            Polygon bow = new Polygon();
            if (horizontal) {
                bow.getPoints().addAll(
                        cellSize * 0.05, cellSize * 0.35,
                        cellSize * 0.25, cellSize * 0.5,
                        cellSize * 0.05, cellSize * 0.65
                );
            } else {
                bow.getPoints().addAll(
                        cellSize * 0.35, cellSize * 0.05,
                        cellSize * 0.5, cellSize * 0.25,
                        cellSize * 0.65, cellSize * 0.05
                );
            }
            bow.setFill(Color.DARKORANGE);
            bow.setStroke(Color.ORANGERED);
            bow.setStrokeWidth(1);

            // Cabina
            Rectangle cabin = new Rectangle(
                    horizontal ? cellSize * 0.8 : cellSize * 0.3,
                    horizontal ? cellSize * 0.3 : cellSize * 0.8,
                    horizontal ? cellSize * 0.5 : cellSize * 0.4,
                    horizontal ? cellSize * 0.4 : cellSize * 0.5
            );
            cabin.setFill(Color.ORANGERED);
            cabin.setStroke(Color.DARKRED);
            cabin.setStrokeWidth(1);
            cabin.setArcWidth(4);
            cabin.setArcHeight(4);

            // Ventana
            Circle window = new Circle(
                    horizontal ? cellSize * 1.05 : cellSize * 0.5,
                    horizontal ? cellSize * 0.5 : cellSize * 1.05,
                    cellSize * 0.12
            );
            window.setFill(Color.LIGHTBLUE);
            window.setStroke(Color.STEELBLUE);
            window.setStrokeWidth(1);

            getChildren().addAll(body, bow, cabin, window);
        }
    }

    // ==================== FRAGATA (1 CELDA) ====================

    public static class FragataView extends Group {

        public FragataView(double cellSize, boolean isVertical) {
            double margin = 3;

            // Cuerpo ovalado
            Ellipse body = new Ellipse(
                    cellSize * 0.5, cellSize * 0.5,
                    isVertical ? cellSize * 0.35 : cellSize * 0.4,
                    isVertical ? cellSize * 0.4 : cellSize * 0.35
            );

            RadialGradient bodyGradient = new RadialGradient(
                    0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.LIGHTPINK),
                    new Stop(1, Color.PINK)
            );
            body.setFill(bodyGradient);
            body.setStroke(Color.HOTPINK);
            body.setStrokeWidth(1.5);

            // Cabina
            Circle cabin = new Circle(
                    cellSize * 0.5, cellSize * 0.5,
                    cellSize * 0.18
            );
            cabin.setFill(Color.HOTPINK);
            cabin.setStroke(Color.DEEPPINK);
            cabin.setStrokeWidth(1);

            // Ventana
            Circle window = new Circle(
                    cellSize * 0.5, cellSize * 0.45,
                    cellSize * 0.07
            );
            window.setFill(Color.LIGHTYELLOW);
            window.setStroke(Color.GOLD);
            window.setStrokeWidth(0.5);

            // Antena (más corta)
            Line antenna = new Line(
                    cellSize * 0.5, cellSize * 0.35,
                    cellSize * 0.5, cellSize * 0.2
            );
            antenna.setStroke(Color.DEEPPINK);
            antenna.setStrokeWidth(1.5);

            // Punta de la antena
            Circle antennaTop = new Circle(
                    cellSize * 0.5, cellSize * 0.18,
                    cellSize * 0.04
            );
            antennaTop.setFill(Color.RED);
            antennaTop.setStroke(Color.DARKRED);
            antennaTop.setStrokeWidth(0.5);

            getChildren().addAll(body, cabin, window, antenna, antennaTop);
        }
    }
}