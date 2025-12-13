package Interfaces;

public interface Jugador {
    Boolean pasarTurno();
    void atacar(int fila, int columna);
    String getNombre();
}
