package com.example.navalbattle1.Model;

import Interfaces.Jugador;

/**
 * Representa un jugador humano en el juego de Batalla Naval.
 * Extiende Player e implementa la interfaz Jugador.
 */
public class JugadorHumano extends Player implements Jugador {

    private String nombre;

    /**
     * Constructor por defecto.
     * Inicializa el tablero del jugador.
     */
    public JugadorHumano() {
        super();
        this.nombre = "Jugador";
        this.nickname = "Jugador";
    }

    /**
     * Constructor con nombre.
     * @param nombre El nombre del jugador
     */
    public JugadorHumano(String nombre) {
        super(nombre);
        this.nombre = nombre;
        this.nickname = nombre;
    }

    @Override
    public Boolean pasarTurno() {
        return true;
    }

    @Override
    public void atacar(int fila, int columna) {
        // Este método será implementado cuando se gestione el ataque del jugador
        // Por ahora está vacío ya que el jugador selecciona manualmente dónde atacar
    }

    @Override
    public String getNombre() {
        return nombre != null ? nombre : nickname;
    }

    /**
     * Establece el nombre del jugador.
     * @param nombre El nuevo nombre del jugador
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
        this.nickname = nombre;
    }

    @Override
    public String getNickname() {
        return nickname != null ? nickname : nombre;
    }

    @Override
    public void setNickname(String nickname) {
        this.nickname = nickname;
        this.nombre = nickname;
    }

    @Override
    public String toString() {
        return "JugadorHumano{" +
                "nombre='" + nombre + '\'' +
                ", nickname='" + nickname + '\'' +
                ", barcos restantes=" + tieneBarcos() +
                '}';
    }
}