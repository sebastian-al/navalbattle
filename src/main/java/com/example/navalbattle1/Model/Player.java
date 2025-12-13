package com.example.navalbattle1.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelo que representa a un jugador en el juego de Batalla Naval
 *
 * Contiene:
 * - Información del jugador (nickname)
 * - Tablero del jugador
 * - Lista de barcos colocados
 * - Estadísticas del juego
 */
public class Player {

    /** Nombre o nickname del jugador */
    private String nickname;

    /** Tablero del jugador (10x10) */
    private String[][] board;

    /** Lista de barcos colocados por el jugador */
    private List<Ship> ships;

    /** Número de disparos realizados */
    private int shotsFired;

    /** Número de impactos acertados */
    private int hits;

    /** Número de barcos hundidos */
    private int shipsSunk;

    /** Indica si es el turno de este jugador */
    private boolean isTurn;

    /**
     * Constructor por defecto
     * Inicializa el tablero vacío y las listas
     */
    public Player() {
        this.board = new String[10][10];
        this.ships = new ArrayList<>();
        this.shotsFired = 0;
        this.hits = 0;
        this.shipsSunk = 0;
        this.isTurn = false;

        // Inicializar tablero con agua
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = "water";
            }
        }
    }

    /**
     * Constructor con nickname
     * @param nickname Nombre del jugador
     */
    public Player(String nickname) {
        this();
        this.nickname = nickname;
    }

    // ==================== GETTERS Y SETTERS ====================

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String[][] getBoard() {
        return board;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    public void addShip(Ship ship) {
        this.ships.add(ship);
    }

    public int getShotsFired() {
        return shotsFired;
    }

    public void setShotsFired(int shotsFired) {
        this.shotsFired = shotsFired;
    }

    public void incrementShotsFired() {
        this.shotsFired++;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void incrementHits() {
        this.hits++;
    }

    public int getShipsSunk() {
        return shipsSunk;
    }

    public void setShipsSunk(int shipsSunk) {
        this.shipsSunk = shipsSunk;
    }

    public void incrementShipsSunk() {
        this.shipsSunk++;
    }

    public boolean isTurn() {
        return isTurn;
    }

    public void setTurn(boolean turn) {
        isTurn = turn;
    }

    /**
     * Calcula la precisión del jugador (hits / shots)
     * @return Porcentaje de precisión (0-100)
     */
    public double getAccuracy() {
        if (shotsFired == 0) return 0.0;
        return (hits * 100.0) / shotsFired;
    }

    /**
     * Verifica si todos los barcos del jugador han sido hundidos
     * @return true si todos los barcos están hundidos, false en caso contrario
     */
    public boolean hasLost() {
        return shipsSunk >= 10; // 10 barcos en total
    }

    @Override
    public String toString() {
        return "Player{" +
                "nickname='" + nickname + '\'' +
                ", shotsFired=" + shotsFired +
                ", hits=" + hits +
                ", shipsSunk=" + shipsSunk +
                ", accuracy=" + String.format("%.2f", getAccuracy()) + "%" +
                '}';
    }

    // ==================== CLASE INTERNA SHIP ====================

    /**
     * Clase interna que representa un barco colocado
     */
    public static class Ship {
        private String type;        // Tipo: Portaaviones, Submarino, Destructor, Fragata
        private int row;            // Fila inicial (0-9)
        private int col;            // Columna inicial (0-9)
        private int size;           // Tamaño del barco (1-4)
        private boolean horizontal; // Orientación
        private int hitCount;       // Número de impactos recibidos

        public Ship(String type, int row, int col, int size, boolean horizontal) {
            this.type = type;
            this.row = row;
            this.col = col;
            this.size = size;
            this.horizontal = horizontal;
            this.hitCount = 0;
        }

        public boolean isSunk() {
            return hitCount >= size;
        }

        public void hit() {
            if (!isSunk()) {
                hitCount++;
            }
        }

        // Getters y Setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public int getRow() { return row; }
        public void setRow(int row) { this.row = row; }

        public int getCol() { return col; }
        public void setCol(int col) { this.col = col; }

        public int getSize() { return size; }
        public void setSize(int size) { this.size = size; }

        public boolean isHorizontal() { return horizontal; }
        public void setHorizontal(boolean horizontal) { this.horizontal = horizontal; }

        public int getHitCount() { return hitCount; }
        public void setHitCount(int hitCount) { this.hitCount = hitCount; }

        @Override
        public String toString() {
            return type + " at (" + row + "," + col + ") " +
                    (horizontal ? "H" : "V") +
                    " [" + hitCount + "/" + size + "]";
        }
    }
}