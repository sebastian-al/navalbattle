package com.example.navalbattle1.Model;

import Interfaces.Jugador;

/**
 * Adaptador que permite que la clase Maquina implemente la interfaz Jugador.
 * Patrón Adapter para integrar la IA con el sistema de juego.
 */
public class MaquinaAdapter implements Jugador {
    private Maquina maquina;
    private int ultimaFila = -1;
    private int ultimaColumna = -1;

    public MaquinaAdapter(Maquina maquina) {
        this.maquina = maquina;
    }

    @Override
    public String getNombre() {
        return maquina.getNombre();
    }

    @Override
    public Boolean pasarTurno() {
        // La máquina siempre juega su turno
        return false;
    }

    @Override
    public void atacar(int fila, int columna) {
        // Este método se llama cuando la máquina ataca
        ultimaFila = fila;
        ultimaColumna = columna;
    }

    /**
     * Genera un ataque automático de la máquina.
     * @return Array con [fila, columna] del ataque
     */
    public int[] generarAtaqueAutomatico() {
        int[] posicion = maquina.generarAtaque();
        ultimaFila = posicion[0];
        ultimaColumna = posicion[1];
        return posicion;
    }

    /**
     * La máquina recibe un ataque.
     * @return true si fue impacto, false si fue agua
     */
    public boolean recibirAtaque(int fila, int columna) {
        return maquina.recibirAtaque(fila, columna);
    }

    /**
     * Coloca los barcos de la máquina aleatoriamente.
     */
    public void colocarBarcosAleatoriamente() {
        maquina.colocarBarcosAleatoriamente();
    }

    /**
     * Verifica si la máquina tiene barcos restantes.
     */
    public boolean tieneBarcos() {
        return maquina.tieneBarcos();
    }

    public int[][] getTablero() {
        return maquina.getTablero();
    }

    public void setTablero(int[][] tablero) {
        maquina.setTablero(tablero);
    }

    public int getUltimaFila() {
        return ultimaFila;
    }

    public int getUltimaColumna() {
        return ultimaColumna;
    }

    public Maquina getMaquinaBase() {
        return maquina;
    }
}