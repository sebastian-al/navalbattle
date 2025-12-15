package com.example.navalbattle1.Model;

/**
 * Clase principal que gestiona el flujo del juego de Batalla Naval.
 */
public class Juego {
    private Tablero tableroJugador;
    private Tablero tableroMaquina;
    private JugadorHumano jugador;
    private Maquina maquinaBase;
    private MaquinaAdapter maquina;
    private boolean turnoJugador;

    public Juego() {
        tableroJugador = new Tablero();
        tableroMaquina = new Tablero();
        jugador = new JugadorHumano("Jugador 1");
        maquinaBase = new Maquina("Computadora");
        maquina = new MaquinaAdapter(maquinaBase);
        turnoJugador = true;
    }

    public void iniciarJuego() {
        // Colocar barcos de la máquina aleatoriamente
        maquina.colocarBarcosAleatoriamente();
        tableroMaquina.setCeldas(maquina.getTablero());

        System.out.println("Juego iniciado!");
        System.out.println("Jugador: " + jugador.getNombre());
        System.out.println("Oponente: " + maquina.getNombre());
    }

    /**
     * Procesa el ataque del jugador.
     */
    public boolean jugadorAtaca(int fila, int columna) {
        if (!turnoJugador) return false;

        boolean impacto = maquina.recibirAtaque(fila, columna);
        tableroMaquina.setCeldas(maquina.getTablero());

        turnoJugador = false;
        return impacto;
    }

    /**
     * Procesa el ataque de la máquina.
     */
    public int[] maquinaAtaca() {
        if (turnoJugador) return null;

        int[] ataque = maquina.generarAtaqueAutomatico();
        boolean impacto = jugador.recibirAtaque(ataque[0], ataque[1]);
        tableroJugador.setCeldas(jugador.getTablero());

        turnoJugador = true;
        return ataque;
    }

    /**
     * Verifica si el juego ha terminado.
     */
    public boolean juegoTerminado() {
        return !jugador.tieneBarcos() || !maquina.tieneBarcos();
    }

    /**
     * Obtiene el ganador del juego.
     */
    public String getGanador() {
        if (!jugador.tieneBarcos()) {
            return maquina.getNombre();
        } else if (!maquina.tieneBarcos()) {
            return jugador.getNombre();
        }
        return null;
    }

    // Getters y Setters
    public Tablero getTableroJugador() {
        return tableroJugador;
    }

    public Tablero getTableroMaquina() {
        return tableroMaquina;
    }

    public JugadorHumano getJugador() {
        return jugador;
    }

    public void setJugador(JugadorHumano jugador) {
        this.jugador = jugador;
        tableroJugador.setCeldas(jugador.getTablero());
    }

    public MaquinaAdapter getMaquina() {
        return maquina;
    }

    public boolean isTurnoJugador() {
        return turnoJugador;
    }

    public void setTurnoJugador(boolean turnoJugador) {
        this.turnoJugador = turnoJugador;
    }
}