package com.example.navalbattle1.Model;

import Interfaces.Jugador;

public class JugadorHumano implements Jugador {
    private String nombre;
    @Override
    public Boolean pasarTurno() {
        return true;
    }
    @Override
    public void atacar(int fila, int columna) {

    }

    @Override
    public String getNombre() { return "Jugador Humano"; }

    public void setNombre(String auxNombre){
        this.nombre = auxNombre;
    }

    public void agregarSubmarino(Tablero tablero, Posicion posicion) {
        Barco barco = new Barco.Builder("Submarino", 4).fila(4).columna(4).build();
    }
}
