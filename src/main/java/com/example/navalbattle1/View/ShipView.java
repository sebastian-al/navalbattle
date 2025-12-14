package com.example.navalbattle1.View;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;

/**
 * La clase ShipView proporciona representaciones gráficas modernas y únicas de diferentes
 * tipos de barcos utilizados en el juego de Batalla Naval. Cada barco tiene un diseño
 * distintivo y colorido que lo hace fácilmente identificable.
 *
 * <p>Tipos de barcos disponibles:</p>
 * <ul>
 *   <li>FRAGATA (rosada) - Tamaño 1 - Diseño compacto con forma de lágrima</li>
 *   <li>DESTRUCTOR (verde) - Tamaño 2 - Diseño angular y agresivo</li>
 *   <li>SUBMARINO (morado) - Tamaño 3 - Diseño redondeado tipo cápsula</li>
 *   <li>PORTAAVIONES (azul) - Tamaño 4 - Diseño rectangular con detalles de cubierta</li>
 * </ul>
 *
 * @author Tu Nombre
 * @version 3.0
 */
public class ShipView extends Pane {

    /* ===================== ENUM TIPO ===================== */

    /**
     * Enumeración que define los tipos de barcos disponibles en el juego.
     * Cada tipo tiene un tamaño específico que determina cuántas celdas ocupa.
     */
    public enum Type {
        /** Portaaviones - el barco más grande (4 celdas) - Color azul */
        CARRIER(4),
        /** Submarino - barco mediano-grande (3 celdas) - Color morado */
        SUBMARINE(3),
        /** Destructor - barco mediano (2 celdas) - Color verde */
        DESTROYER(2),
        /** Fragata - el barco más pequeño (1 celda) - Color rosado */
        FRIGATE(1);

        private final int size;

        Type(int size) {
            this.size = size;
        }

        /**
         * Obtiene el tamaño del barco en número de celdas.
         *
         * @return el tamaño del barco
         */
        public int getSize() {
            return size;
        }

        /**
         * Convierte un tamaño numérico en su tipo de barco correspondiente.
         * Método útil para convertir datos del modelo a la vista.
         *
         * @param size el tamaño del barco (1-4)
         * @return el tipo de barco correspondiente
         * @throws IllegalArgumentException si el tamaño no es válido
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

    /**
     * Enumeración que define las posibles orientaciones de un barco en el tablero.
     */
    public enum Orientation {
        /** Barco posicionado horizontalmente (de izquierda a derecha) */
        HORIZONTAL,
        /** Barco posicionado verticalmente (de arriba a abajo) */
        VERTICAL
    }

    /* ===================== CONSTANTES ===================== */

    private static final int CELL_SIZE = BoardCell.SIZE;
    private static final double SHIP_WIDTH = 28.0;  // Ancho fijo para todos los barcos
    private static final int GAP = 2;

    /* ===================== CONSTRUCTOR ===================== */

    /**
     * Crea una nueva vista de barco con el tipo y orientación especificados.
     * El constructor genera automáticamente la representación gráfica detallada
     * del barco usando formas 2D de JavaFX con diseños únicos.
     *
     * @param type el tipo de barco a crear (FRIGATE, DESTROYER, SUBMARINE, CARRIER)
     * @param orientation la orientación del barco (HORIZONTAL o VERTICAL)
     */
    public ShipView(Type type, Orientation orientation) {
        Group shipGroup = switch (type) {
            case FRIGATE -> drawFrigate(orientation == Orientation.VERTICAL);
            case DESTROYER -> drawDestroyer(orientation == Orientation.VERTICAL);
            case SUBMARINE -> drawSubmarine(orientation == Orientation.VERTICAL);
            case CARRIER -> drawAircraftCarrier(orientation == Orientation.VERTICAL);
        };

        getChildren().add(shipGroup);
        setPickOnBounds(false);
    }

    /* ===================== MÉTODOS DE DIBUJO ===================== */

    /**
     * Dibuja una fragata (barco pequeño de 1 celda) con color rosado.
     * Diseño en forma de lágrima con detalles brillantes.
     *
     * @param vertical si es true, el barco se dibuja verticalmente; si es false, horizontalmente
     * @return un Group de JavaFX con la representación gráfica de la fragata
     */
    private Group drawFrigate(boolean vertical) {
        double length = CELL_SIZE;
        double width = SHIP_WIDTH;

        // Casco principal - rectángulo redondeado
        Rectangle hull = new Rectangle(0, 0, length, width);
        hull.setArcWidth(10);
        hull.setArcHeight(10);
        hull.setFill(createGradient(Color.HOTPINK, Color.DEEPPINK, vertical));
        hull.setStroke(Color.rgb(139, 0, 139));
        hull.setStrokeWidth(2);

        // Cabina
        Rectangle cabin = new Rectangle(length * 0.3, width * 0.3, length * 0.4, width * 0.4);
        cabin.setArcWidth(6);
        cabin.setArcHeight(6);
        cabin.setFill(Color.rgb(255, 20, 147));
        cabin.setStroke(Color.WHITE);
        cabin.setStrokeWidth(1);

        // Ventana
        Circle window = new Circle(length * 0.5, width * 0.5, 3);
        window.setFill(Color.LIGHTCYAN);

        Group frigateGroup = new Group(hull, cabin, window);

        if (vertical) {
            frigateGroup.setRotate(90);
            // Centrar: mover a la derecha el ancho y ajustar verticalmente
            frigateGroup.setTranslateX((length - width) / 2 + width);
            frigateGroup.setTranslateY((length - width) / 2);
        }

        return frigateGroup;
    }

    /**
     * Dibuja un destructor (barco de 2 celdas) con color verde.
     * Diseño angular y agresivo estilo militar.
     *
     * @param vertical si es true, el barco se dibuja verticalmente; si es false, horizontalmente
     * @return un Group de JavaFX con la representación gráfica del destructor
     */
    private Group drawDestroyer(boolean vertical) {
        double length = CELL_SIZE * 2 + GAP;
        double width = SHIP_WIDTH;

        // Casco principal
        Rectangle hull = new Rectangle(0, 0, length, width);
        hull.setArcWidth(8);
        hull.setArcHeight(8);
        hull.setFill(createGradient(Color.LIMEGREEN, Color.DARKGREEN, vertical));
        hull.setStroke(Color.DARKGREEN);
        hull.setStrokeWidth(2);

        // Torre de comando
        Rectangle tower = new Rectangle(length * 0.35, width * 0.25, length * 0.15, width * 0.5);
        tower.setArcWidth(4);
        tower.setArcHeight(4);
        tower.setFill(Color.rgb(34, 139, 34));
        tower.setStroke(Color.WHITE);
        tower.setStrokeWidth(1);

        // Ventana de la torre
        Rectangle window = new Rectangle(length * 0.38, width * 0.35, 6, 4);
        window.setFill(Color.CYAN);
        window.setArcWidth(2);
        window.setArcHeight(2);

        // Cañón
        Rectangle cannon = new Rectangle(length * 0.65, width * 0.45, length * 0.25, 3);
        cannon.setFill(Color.DARKSLATEGRAY);
        cannon.setArcWidth(2);
        cannon.setArcHeight(2);

        Group destroyerGroup = new Group(hull, tower, window, cannon);

        if (vertical) {
            destroyerGroup.setRotate(90);
            destroyerGroup.setTranslateX((length - width) / 2 + width);
            destroyerGroup.setTranslateY((length - width) / 2);
        }

        return destroyerGroup;
    }

    /**
     * Dibuja un submarino (barco de 3 celdas) con color morado.
     * Diseño redondeado tipo cápsula con periscopio.
     *
     * @param vertical si es true, el barco se dibuja verticalmente; si es false, horizontalmente
     * @return un Group de JavaFX con la representación gráfica del submarino
     */
    private Group drawSubmarine(boolean vertical) {
        double length = CELL_SIZE * 3 + GAP * 2;
        double width = SHIP_WIDTH;

        // Casco tipo cápsula
        Rectangle body = new Rectangle(0, 0, length, width);
        body.setArcWidth(width);
        body.setArcHeight(width);
        body.setFill(createGradient(Color.MEDIUMPURPLE, Color.DARKVIOLET, vertical));
        body.setStroke(Color.INDIGO);
        body.setStrokeWidth(2);

        // Escotilla
        Circle hatch = new Circle(length * 0.35, width / 2, 5);
        hatch.setFill(Color.rgb(75, 0, 130));
        hatch.setStroke(Color.GOLD);
        hatch.setStrokeWidth(2);

        // Ventanilla
        Circle window = new Circle(length * 0.35, width / 2, 2.5);
        window.setFill(Color.LIGHTBLUE);

        // Periscopio
        Line periscope = new Line(length * 0.5, width / 2, length * 0.5, -4);
        periscope.setStroke(Color.DARKSLATEBLUE);
        periscope.setStrokeWidth(2.5);

        Rectangle periscopeTop = new Rectangle(length * 0.485, -6, 3, 3);
        periscopeTop.setFill(Color.RED);
        periscopeTop.setArcWidth(2);
        periscopeTop.setArcHeight(2);

        Group submarineGroup = new Group(body, hatch, window, periscope, periscopeTop);

        if (vertical) {
            submarineGroup.setRotate(90);
            submarineGroup.setTranslateX((length - width) / 2 + width);
            submarineGroup.setTranslateY((length - width) / 2);
        }

        return submarineGroup;
    }

    /**
     * Dibuja un portaaviones (barco de 4 celdas) con color azul.
     * Diseño rectangular con cubierta de aterrizaje y torre de control.
     *
     * @param vertical si es true, el barco se dibuja verticalmente; si es false, horizontalmente
     * @return un Group de JavaFX con la representación gráfica del portaaviones
     */
    private Group drawAircraftCarrier(boolean vertical) {
        double length = CELL_SIZE * 4 + GAP * 3;
        double width = SHIP_WIDTH;

        // Casco principal
        Rectangle hull = new Rectangle(0, 0, length, width);
        hull.setArcWidth(6);
        hull.setArcHeight(6);
        hull.setFill(createGradient(Color.DODGERBLUE, Color.DARKBLUE, vertical));
        hull.setStroke(Color.NAVY);
        hull.setStrokeWidth(2);

        // Cubierta
        Rectangle deck = new Rectangle(3, 3, length - 6, width - 6);
        deck.setFill(Color.rgb(70, 130, 180, 0.5));
        deck.setStroke(Color.WHITE);
        deck.setStrokeWidth(1);

        // Línea de pista
        Line deckLine = new Line(length * 0.25, width / 2, length * 0.75, width / 2);
        deckLine.setStroke(Color.YELLOW);
        deckLine.setStrokeWidth(1);
        deckLine.getStrokeDashArray().addAll(4d, 4d);

        // Torre de control
        Rectangle tower = new Rectangle(length * 0.7, 0, length * 0.15, width * 0.6);
        tower.setArcWidth(4);
        tower.setArcHeight(4);
        tower.setFill(Color.STEELBLUE);
        tower.setStroke(Color.WHITE);
        tower.setStrokeWidth(1.5);

        // Ventanas
        Rectangle window1 = new Rectangle(length * 0.73, 4, 3, 2);
        window1.setFill(Color.LIGHTCYAN);

        Rectangle window2 = new Rectangle(length * 0.79, 4, 3, 2);
        window2.setFill(Color.LIGHTCYAN);

        // Radar
        Line radar = new Line(length * 0.775, 0, length * 0.775, -4);
        radar.setStroke(Color.WHITE);
        radar.setStrokeWidth(2);

        Circle radarDish = new Circle(length * 0.775, -4, 2.5);
        radarDish.setFill(Color.RED);
        radarDish.setStroke(Color.WHITE);
        radarDish.setStrokeWidth(1);

        // Marcadores
        Circle marker1 = new Circle(length * 0.15, width / 2, 1.5);
        marker1.setFill(Color.ORANGE);

        Circle marker2 = new Circle(length * 0.55, width / 2, 1.5);
        marker2.setFill(Color.ORANGE);

        Group carrierGroup = new Group(hull, deck, deckLine, tower, window1, window2,
                radar, radarDish, marker1, marker2);

        if (vertical) {
            carrierGroup.setRotate(90);
            carrierGroup.setTranslateX((length - width) / 2 + width);
            carrierGroup.setTranslateY((length - width) / 2);
        }

        return carrierGroup;
    }

    /* ===================== MÉTODOS AUXILIARES ===================== */

    /**
     * Crea un gradiente lineal para dar profundidad a los barcos.
     *
     * @param startColor color inicial del gradiente
     * @param endColor color final del gradiente
     * @param vertical si el gradiente es vertical u horizontal
     * @return un LinearGradient
     */
    private LinearGradient createGradient(Color startColor, Color endColor, boolean vertical) {
        return new LinearGradient(
                vertical ? 0 : 0, vertical ? 0 : 0,
                vertical ? 0 : 1, vertical ? 1 : 0,
                true, CycleMethod.NO_CYCLE,
                new Stop(0, startColor),
                new Stop(1, endColor)
        );
    }
}