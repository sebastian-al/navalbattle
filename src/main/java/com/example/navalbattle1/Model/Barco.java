package com.example.navalbattle1.Model;

public class Barco {
    private String nombre;
    private int tamano;
    private int fila;
    private int columna;
    private int impactos=0;
    private boolean horizontal;

    private Barco(Builder builder) {
        this.nombre = builder.nombre;
        this.tamano = builder.tamano;
        this.fila = builder.fila;
        this.columna = builder.columna;
        this.horizontal = builder.horizontal;
    }
    public String getNombre() {
        return nombre;
    }
    public int getTamano() {
        return tamano;
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
    public boolean seHunde(){
        return impactos == tamano;
    }
    public void agregarImpacto(){
        impactos++;
    }
    /**
     * Implementacion del patron de diseño "Builder"
     */
    public static class Builder {
        private String nombre;
        private int tamano;

        private int fila = 0;
        private int columna=0;
        private boolean horizontal = false;

        public Builder(String nombre, int tamano) {
            this.nombre = nombre;
            this.tamano = tamano;
        }

        public Builder fila(int fila) {
            this.fila = fila;
            return this;
        }

        public Builder columna(int columna) {
            this.columna = columna;
            return this;
        }

        public Builder horizontal(boolean horizontal) {
            this.horizontal = horizontal;
            return this;
        }

        public Barco build() {
            return new Barco(this);
        }
    }
}
