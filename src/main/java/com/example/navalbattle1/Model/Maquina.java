package com.example.navalbattle1.Model;

/**
 * Clase que representa la máquina (IA) del juego de Batalla Naval.
 * Esta es la clase base que será adaptada para usar la interfaz Jugador.
 */
public class Maquina {
    private String nombre;
    private int[][] tablero;
    private int[][] tableroAtaques; // Para recordar dónde ha atacado
    private static final int TAMANIO_TABLERO = 10;

    public Maquina() {
        this.nombre = "Máquina";
        this.tablero = new int[TAMANIO_TABLERO][TAMANIO_TABLERO];
        this.tableroAtaques = new int[TAMANIO_TABLERO][TAMANIO_TABLERO];
        inicializarTableros();
    }

    public Maquina(String nombre) {
        this();
        this.nombre = nombre;
    }

    private void inicializarTableros() {
        for (int i = 0; i < TAMANIO_TABLERO; i++) {
            for (int j = 0; j < TAMANIO_TABLERO; j++) {
                tablero[i][j] = 0;
                tableroAtaques[i][j] = 0;
            }
        }
    }

    /**
     * Genera una posición aleatoria para atacar.
     * @return Array de 2 elementos [fila, columna]
     */
    public int[] generarAtaque() {
        int fila, columna;
        do {
            fila = (int) (Math.random() * TAMANIO_TABLERO);
            columna = (int) (Math.random() * TAMANIO_TABLERO);
        } while (tableroAtaques[fila][columna] != 0); // Repetir si ya atacó ahí

        tableroAtaques[fila][columna] = 1; // Marcar como atacado
        return new int[]{fila, columna};
    }

    /**
     * Coloca los barcos de la máquina de forma aleatoria.
     */
    public void colocarBarcosAleatoriamente() {
        // Tipos de barcos: Portaaviones(4), Submarino(3), Destructor(2), Fragata(1)
        int[] tamaños = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};

        for (int i = 0; i < tamaños.length; i++) {
            boolean colocado = false;
            int intentos = 0;

            while (!colocado && intentos < 100) {
                int fila = (int) (Math.random() * TAMANIO_TABLERO);
                int columna = (int) (Math.random() * TAMANIO_TABLERO);
                boolean horizontal = Math.random() < 0.5;

                if (puedeColocarBarco(fila, columna, tamaños[i], horizontal)) {
                    colocarBarco(fila, columna, tamaños[i], horizontal, i + 1);
                    colocado = true;
                }
                intentos++;
            }
        }
    }

    private boolean puedeColocarBarco(int fila, int columna, int tamaño, boolean horizontal) {
        if (horizontal) {
            if (columna + tamaño > TAMANIO_TABLERO) return false;
            for (int i = 0; i < tamaño; i++) {
                if (tablero[fila][columna + i] != 0) return false;
            }
        } else {
            if (fila + tamaño > TAMANIO_TABLERO) return false;
            for (int i = 0; i < tamaño; i++) {
                if (tablero[fila + i][columna] != 0) return false;
            }
        }
        return true;
    }

    private void colocarBarco(int fila, int columna, int tamaño, boolean horizontal, int idBarco) {
        if (horizontal) {
            for (int i = 0; i < tamaño; i++) {
                tablero[fila][columna + i] = idBarco;
            }
        } else {
            for (int i = 0; i < tamaño; i++) {
                tablero[fila + i][columna] = idBarco;
            }
        }
    }

    /**
     * Recibe un ataque en una posición específica.
     * @return true si hubo impacto, false si fue agua
     */
    public boolean recibirAtaque(int fila, int columna) {
        if (fila < 0 || fila >= TAMANIO_TABLERO ||
                columna < 0 || columna >= TAMANIO_TABLERO) {
            return false;
        }

        boolean impacto = tablero[fila][columna] > 0;

        if (tablero[fila][columna] > 0) {
            tablero[fila][columna] = -tablero[fila][columna]; // Marcar como impacto
        } else if (tablero[fila][columna] == 0) {
            tablero[fila][columna] = -1; // Marcar como agua atacada
        }

        return impacto;
    }

    /**
     * Verifica si la máquina tiene barcos restantes.
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int[][] getTablero() {
        return tablero;
    }

    public void setTablero(int[][] tablero) {
        if (tablero != null && tablero.length == TAMANIO_TABLERO) {
            for (int i = 0; i < TAMANIO_TABLERO; i++) {
                System.arraycopy(tablero[i], 0, this.tablero[i], 0, TAMANIO_TABLERO);
            }
        }
    }

    public int[][] getTableroAtaques() {
        return tableroAtaques;
    }
}