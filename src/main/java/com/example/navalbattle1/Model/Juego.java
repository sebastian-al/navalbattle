package com.example.navalbattle1.Model;

public class Juego {
    private Tablero tableroJugador;
    private Tablero tableroMaquina;
    private JugadorHumano jugador;
    private Maquina maquinaBase;
    private MaquinaAdapter maquina;
    public Juego() {
        tableroJugador = new Tablero();
        tableroMaquina = new Tablero();
        jugador = new JugadorHumano();
        maquinaBase = new Maquina();
        maquina = new MaquinaAdapter(maquinaBase);
    }
    public void iniciarJuego(){

    }
}
