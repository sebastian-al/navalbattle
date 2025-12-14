package com.example.navalbattle1.Model;

public class Posicion {
    int fila;
    int columna;
    boolean horizontal;
    public Posicion(int fila, int columna, boolean horizontal) {
        this.fila = fila;
        this.columna = columna;
        this.horizontal = horizontal;
    }
    public int getFila() {
        return fila;
    }
    public int getColumna() {
        return columna;
    }
    public boolean isHorizontal() {
        return horizontal;
    }

}
