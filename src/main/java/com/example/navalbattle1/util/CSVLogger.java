package com.example.navalbattle1.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Utilidad para registrar estados del tablero enemigo en un archivo CSV.
 * Este archivo es solo informativo y NO afecta la lógica del juego.
 */
public class CSVLogger {

    private static final String FILE_NAME = "enemy_board_log.csv";
    private static boolean headerWritten = false;

    /**
     * Registra un disparo en el tablero enemigo.
     *
     * @param row fila atacada
     * @param col columna atacada
     * @param state estado resultante (MISS, HIT, SUNK)
     */
    public static void log(int row, int col, String state) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {

            if (!headerWritten) {
                writer.println("row,col,state");
                headerWritten = true;
            }

            writer.println(row + "," + col + "," + state);

        } catch (IOException e) {
            System.err.println("No se pudo escribir el CSV: " + e.getMessage());
        }
    }
}
