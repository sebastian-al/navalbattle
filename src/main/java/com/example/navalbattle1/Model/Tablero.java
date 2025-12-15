package com.example.navalbattle1.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un tablero del juego de Batalla Naval.
 * Gestiona los barcos y las celdas del tablero.
 */
public class Tablero {
    private int[][] celdas;
    private List<Barco> barcos;
    private static final int TAMANIO = 10;

    public Tablero() {
        celdas = new int[TAMANIO][TAMANIO];
        barcos = new ArrayList<>();
        inicializar();
    }

    private void inicializar() {
        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                celdas[i][j] = 0;
            }
        }
    }

    /**
     * Agrega un barco al tablero.
     * @param barco El barco a agregar
     * @return true si se agregó exitosamente
     */
    public boolean agregarBarco(Barco barco) {
        if (!puedeColocar(barco)) {
            return false;
        }

        barcos.add(barco);
        marcarBarcoEnCeldas(barco);
        return true;
    }

    /**
     * Verifica si un barco puede ser colocado en el tablero.
     * @param barco El barco a verificar
     * @return true si se puede colocar
     */
    public boolean puedeColocar(Barco barco) {
        int fila = barco.getFila();
        int columna = barco.getColumna();
        int tamano = barco.getTamano();
        boolean horizontal = barco.isHorizontal();

        // Verificar límites
        if (horizontal) {
            if (columna + tamano > TAMANIO) return false;
        } else {
            if (fila + tamano > TAMANIO) return false;
        }

        // Verificar colisiones
        for (int i = 0; i < tamano; i++) {
            int r = horizontal ? fila : fila + i;
            int c = horizontal ? columna + i : columna;

            if (celdas[r][c] != 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Marca las celdas ocupadas por un barco.
     * @param barco El barco a marcar
     */
    private void marcarBarcoEnCeldas(Barco barco) {
        int fila = barco.getFila();
        int columna = barco.getColumna();
        int tamano = barco.getTamano();
        boolean horizontal = barco.isHorizontal();
        int idBarco = barcos.size(); // Usar el índice como ID

        for (int i = 0; i < tamano; i++) {
            int r = horizontal ? fila : fila + i;
            int c = horizontal ? columna + i : columna;
            celdas[r][c] = idBarco;
        }
    }

    public int getCelda(int fila, int columna) {
        if (fila >= 0 && fila < TAMANIO && columna >= 0 && columna < TAMANIO) {
            return celdas[fila][columna];
        }
        return 0;
    }

    public void setCelda(int fila, int columna, int valor) {
        if (fila >= 0 && fila < TAMANIO && columna >= 0 && columna < TAMANIO) {
            celdas[fila][columna] = valor;
        }
    }

    /**
     * Obtiene la matriz de celdas del tablero.
     * @return Matriz int[][] con el estado del tablero
     */
    public int[][] getCeldas() {
        return celdas;
    }

    public void setCeldas(int[][] nuevasCeldas) {
        if (nuevasCeldas != null && nuevasCeldas.length == TAMANIO) {
            for (int i = 0; i < TAMANIO; i++) {
                System.arraycopy(nuevasCeldas[i], 0, this.celdas[i], 0, TAMANIO);
            }
        }
    }

    /**
     * Obtiene la lista de barcos en el tablero.
     * @return Lista de barcos
     */
    public List<Barco> getBarcos() {
        return barcos;
    }

    public int getTamanio() {
        return TAMANIO;
    }

    public void limpiar() {
        inicializar();
        barcos.clear();
    }
}