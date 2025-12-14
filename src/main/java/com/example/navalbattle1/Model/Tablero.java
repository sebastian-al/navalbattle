package com.example.navalbattle1.Model;

import java.util.ArrayList;
import java.util.List;

public class Tablero {

    private final int[][] tablero = new int[10][10];
    private final List<Barco> barcos;

    public Tablero() {
        this.barcos = new ArrayList<>();
    }

    public void agregarBarco(Barco barco) {
        barcos.add(barco);

        int tamano = barco.getTamano();
        int fila = barco.getFila();
        int columna = barco.getColumna();

        if (barco.isHorizontal()) {
            for (int i = 0; i < tamano; i++) {
                tablero[fila][columna + i] = barcos.size();
            }
        } else {
            for (int i = 0; i < tamano; i++) {
                tablero[fila + i][columna] = barcos.size();
            }
        }
    }
    public List<Barco> getBarcos() {
        return barcos;
    }


    /* ================= VALIDACIÓN ================= */

    public boolean puedeColocar(Barco barco) {
        int tamano = barco.getTamano();
        int fila = barco.getFila();
        int columna = barco.getColumna();

        if (barco.isHorizontal()) {
            if (columna + tamano > 10) return false;
            for (int i = 0; i < tamano; i++) {
                if (tablero[fila][columna + i] != 0) return false;
            }
        } else {
            if (fila + tamano > 10) return false;
            for (int i = 0; i < tamano; i++) {
                if (tablero[fila + i][columna] != 0) return false;
            }
        }
        return true;
    }

    public int[][] getSnapshot() {
        int[][] copy = new int[10][10];
        for (int i = 0; i < 10; i++) {
            System.arraycopy(tablero[i], 0, copy[i], 0, 10);
        }
        return copy;
    }
}
