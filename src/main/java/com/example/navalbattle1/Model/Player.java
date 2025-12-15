package com.example.navalbattle1.Model;

/**
 * Clase base que representa un jugador en el juego de Batalla Naval.
 * Esta clase puede ser extendida por diferentes tipos de jugadores.
 */
public class Player {
    protected String nickname;
    protected int[][] tablero;
    protected static final int TAMANIO_TABLERO = 10;

    /**
     * Constructor por defecto.
     */
    public Player() {
        this.nickname = "Jugador";
        this.tablero = new int[TAMANIO_TABLERO][TAMANIO_TABLERO];
        inicializarTablero();
    }

    /**
     * Constructor con nickname.
     * @param nickname El apodo del jugador
     */
    public Player(String nickname) {
        this();
        this.nickname = nickname;
    }

    /**
     * Inicializa el tablero con valores vacíos (0).
     */
    protected void inicializarTablero() {
        for (int i = 0; i < TAMANIO_TABLERO; i++) {
            for (int j = 0; j < TAMANIO_TABLERO; j++) {
                tablero[i][j] = 0;
            }
        }
    }

    /**
     * Obtiene el apodo del jugador.
     * @return El apodo del jugador
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Establece el apodo del jugador.
     * @param nickname El nuevo apodo
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Obtiene el tablero del jugador.
     * @return Matriz que representa el tablero
     */
    public int[][] getTablero() {
        return tablero;
    }

    /**
     * Establece el tablero del jugador.
     * @param tablero La nueva matriz del tablero
     */
    public void setTablero(int[][] tablero) {
        if (tablero != null && tablero.length == TAMANIO_TABLERO
                && tablero[0].length == TAMANIO_TABLERO) {
            // Copiar el tablero para evitar referencias compartidas
            for (int i = 0; i < TAMANIO_TABLERO; i++) {
                System.arraycopy(tablero[i], 0, this.tablero[i], 0, TAMANIO_TABLERO);
            }
        }
    }

    /**
     * Verifica si el jugador tiene barcos restantes.
     * @return true si quedan barcos, false si todos han sido hundidos
     */
    public boolean tieneBarcos() {
        for (int i = 0; i < TAMANIO_TABLERO; i++) {
            for (int j = 0; j < TAMANIO_TABLERO; j++) {
                if (tablero[i][j] > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Marca una celda como atacada.
     * @param fila Fila de la celda
     * @param columna Columna de la celda
     * @return true si hubo impacto, false si fue agua
     */
    public boolean recibirAtaque(int fila, int columna) {
        if (fila < 0 || fila >= TAMANIO_TABLERO ||
                columna < 0 || columna >= TAMANIO_TABLERO) {
            return false;
        }

        boolean impacto = tablero[fila][columna] > 0;

        if (tablero[fila][columna] > 0) {
            tablero[fila][columna] = -tablero[fila][columna];
        } else if (tablero[fila][columna] == 0) {
            tablero[fila][columna] = -1;
        }

        return impacto;
    }

    /**
     * Verifica si una celda ya fue atacada.
     * @param fila Fila de la celda
     * @param columna Columna de la celda
     * @return true si ya fue atacada
     */
    public boolean celdaAtacada(int fila, int columna) {
        if (fila < 0 || fila >= TAMANIO_TABLERO ||
                columna < 0 || columna >= TAMANIO_TABLERO) {
            return false;
        }
        return tablero[fila][columna] < 0;
    }

    /**
     * Obtiene el valor de una celda específica.
     * @param fila Fila de la celda
     * @param columna Columna de la celda
     * @return El valor de la celda
     */
    public int getCelda(int fila, int columna) {
        if (fila < 0 || fila >= TAMANIO_TABLERO ||
                columna < 0 || columna >= TAMANIO_TABLERO) {
            return 0;
        }
        return tablero[fila][columna];
    }

    /**
     * Establece el valor de una celda específica.
     * @param fila Fila de la celda
     * @param columna Columna de la celda
     * @param valor El nuevo valor
     */
    public void setCelda(int fila, int columna, int valor) {
        if (fila >= 0 && fila < TAMANIO_TABLERO &&
                columna >= 0 && columna < TAMANIO_TABLERO) {
            tablero[fila][columna] = valor;
        }
    }

    /**
     * Obtiene el tamaño del tablero.
     * @return El tamaño del tablero (10)
     */
    public int getTamanioTablero() {
        return TAMANIO_TABLERO;
    }

    @Override
    public String toString() {
        return "Player{" +
                "nickname='" + nickname + '\'' +
                ", barcos restantes=" + tieneBarcos() +
                '}';
    }
}