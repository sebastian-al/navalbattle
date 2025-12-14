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
    public String getNombre() { return nombre; }

    public void setNombre(String auxNombre){
        this.nombre = auxNombre;
    }

}
