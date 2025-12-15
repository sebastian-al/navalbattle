package com.example.navalbattle1.Model;

/**
 * Representa un barco en el juego de Batalla Naval.
 *
 * <p>Esta clase encapsula toda la información de un barco, incluyendo:</p>
 * <ul>
 *   <li>Identificación: nombre y tamaño</li>
 *   <li>Posición: fila, columna y orientación</li>
 *   <li>Estado: impactos recibidos y si está hundido</li>
 * </ul>
 *
 * <p>La clase utiliza el patrón de diseño <b>Builder</b> para su construcción,
 * lo que permite crear barcos de manera flexible y legible.</p>
 *
 * <h2>Ejemplo de uso:</h2>
 * <pre>
 * Barco portaaviones = new Barco.Builder("Portaaviones", 4)
 *     .fila(0)
 *     .columna(0)
 *     .horizontal(true)
 *     .build();
 * </pre>
 *
 * @author Tu Nombre
 * @version 2.0
 * @see Builder
 */
public class Barco {

    /* ===================== ATRIBUTOS ===================== */

    /** Nombre descriptivo del barco (ej: "Portaaviones", "Submarino") */
    private final String nombre;

    /** Tamaño del barco en número de casillas (1-4) */
    private final int tamano;

    /** Fila inicial del barco en el tablero (0-9) */
    private final int fila;

    /** Columna inicial del barco en el tablero (0-9) */
    private final int columna;

    /** Número de impactos que ha recibido el barco */
    private int impactos;

    /** Orientación del barco: true = horizontal, false = vertical */
    private final boolean horizontal;

    /* ===================== CONSTRUCTOR PRIVADO ===================== */

    /**
     * Constructor privado que es llamado por el Builder.
     * Este constructor garantiza que los barcos solo pueden ser creados
     * mediante el patrón Builder.
     *
     * @param builder el builder con todos los parámetros configurados
     */
    private Barco(Builder builder) {
        this.nombre = builder.nombre;
        this.tamano = builder.tamano;
        this.fila = builder.fila;
        this.columna = builder.columna;
        this.horizontal = builder.horizontal;
        this.impactos = 0;
    }

    /* ===================== GETTERS ===================== */

    /**
     * Obtiene el nombre del barco.
     *
     * @return el nombre del barco
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el tamaño del barco en número de casillas.
     *
     * @return el tamaño del barco (1-4)
     */
    public int getTamano() {
        return tamano;
    }

    /**
     * Obtiene la fila inicial del barco en el tablero.
     *
     * @return la fila (0-9)
     */
    public int getFila() {
        return fila;
    }

    /**
     * Obtiene la columna inicial del barco en el tablero.
     *
     * @return la columna (0-9)
     */
    public int getColumna() {
        return columna;
    }

    /**
     * Verifica si el barco está orientado horizontalmente.
     *
     * @return true si el barco es horizontal, false si es vertical
     */
    public boolean isHorizontal() {
        return horizontal;
    }

    /**
     * Obtiene el número de impactos que ha recibido el barco.
     *
     * @return el número de impactos (0 al tamaño del barco)
     */
    public int getImpactos() {
        return impactos;
    }

    /* ===================== MÉTODOS DE ESTADO ===================== */

    /**
     * Verifica si el barco está completamente hundido.
     * Un barco se hunde cuando el número de impactos recibidos
     * es igual a su tamaño.
     *
     * @return true si el barco está hundido, false en caso contrario
     */
    public boolean seHunde() {
        return impactos >= tamano;
    }

    /**
     * Registra un impacto en el barco.
     * Incrementa el contador de impactos en 1.
     * Si el barco ya está hundido, este método no tiene efecto adicional.
     */
    public void agregarImpacto() {
        if (!seHunde()) {
            impactos++;
        }
    }

    /**
     * Verifica si una celda específica del tablero pertenece a este barco.
     *
     * @param row la fila de la celda a verificar
     * @param col la columna de la celda a verificar
     * @return true si la celda pertenece al barco, false en caso contrario
     */
    public boolean occupiesCell(int row, int col) {
        if (horizontal) {
            return row == fila && col >= columna && col < columna + tamano;
        } else {
            return col == columna && row >= fila && row < fila + tamano;
        }
    }

    /* ===================== MÉTODOS UTILITARIOS ===================== */

    /**
     * Calcula el porcentaje de daño del barco.
     *
     * @return un valor entre 0.0 (sin daño) y 1.0 (hundido)
     */
    public double getDamagePercentage() {
        return (double) impactos / tamano;
    }

    /**
     * Obtiene el estado actual del barco como texto.
     *
     * @return una cadena describiendo el estado ("Intacto", "Dañado", "Hundido")
     */
    public String getEstado() {
        if (impactos == 0) {
            return "Intacto";
        } else if (seHunde()) {
            return "Hundido";
        } else {
            return "Dañado (" + impactos + "/" + tamano + ")";
        }
    }

    @Override
    public String toString() {
        return String.format("%s [%d casillas] en (%d,%d) %s - %s",
                nombre, tamano, fila, columna,
                horizontal ? "Horizontal" : "Vertical",
                getEstado());
    }

    /* ===================== CLASE BUILDER (PATRÓN BUILDER) ===================== */

    /**
     * Builder para crear instancias de Barco de manera flexible y legible.
     *
     * <p>Este patrón de diseño permite:</p>
     * <ul>
     *   <li>Construir objetos complejos paso a paso</li>
     *   <li>Crear objetos inmutables de manera clara</li>
     *   <li>Proporcionar valores por defecto opcionales</li>
     * </ul>
     *
     * <h2>Ejemplo de uso:</h2>
     * <pre>
     * Barco destructor = new Barco.Builder("Destructor", 2)
     *     .fila(3)
     *     .columna(5)
     *     .horizontal(false)
     *     .build();
     * </pre>
     */
    public static class Builder {

        /* ===================== ATRIBUTOS DEL BUILDER ===================== */

        /** Nombre del barco (obligatorio) */
        private final String nombre;

        /** Tamaño del barco (obligatorio) */
        private final int tamano;

        /** Fila inicial del barco (opcional, por defecto 0) */
        private int fila = 0;

        /** Columna inicial del barco (opcional, por defecto 0) */
        private int columna = 0;

        /** Orientación del barco (opcional, por defecto horizontal) */
        private boolean horizontal = true;

        /* ===================== CONSTRUCTOR ===================== */

        /**
         * Crea un nuevo Builder con los parámetros obligatorios.
         *
         * @param nombre el nombre del barco
         * @param tamano el tamaño del barco en casillas
         * @throws IllegalArgumentException si el tamaño no está entre 1 y 4
         */
        public Builder(String nombre, int tamano) {
            if (tamano < 1 || tamano > 4) {
                throw new IllegalArgumentException(
                        "El tamaño del barco debe estar entre 1 y 4, se recibió: " + tamano
                );
            }
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "El nombre del barco no puede ser nulo o vacío"
                );
            }
            this.nombre = nombre;
            this.tamano = tamano;
        }

        /* ===================== MÉTODOS BUILDER ===================== */

        /**
         * Establece la fila inicial del barco.
         *
         * @param fila la fila (0-9)
         * @return este Builder para encadenamiento de métodos
         * @throws IllegalArgumentException si la fila no está entre 0 y 9
         */
        public Builder fila(int fila) {
            if (fila < 0 || fila > 9) {
                throw new IllegalArgumentException(
                        "La fila debe estar entre 0 y 9, se recibió: " + fila
                );
            }
            this.fila = fila;
            return this;
        }

        /**
         * Establece la columna inicial del barco.
         *
         * @param columna la columna (0-9)
         * @return este Builder para encadenamiento de métodos
         * @throws IllegalArgumentException si la columna no está entre 0 y 9
         */
        public Builder columna(int columna) {
            if (columna < 0 || columna > 9) {
                throw new IllegalArgumentException(
                        "La columna debe estar entre 0 y 9, se recibió: " + columna
                );
            }
            this.columna = columna;
            return this;
        }

        /**
         * Establece la orientación del barco.
         *
         * @param horizontal true para horizontal, false para vertical
         * @return este Builder para encadenamiento de métodos
         */
        public Builder horizontal(boolean horizontal) {
            this.horizontal = horizontal;
            return this;
        }

        /**
         * Construye y retorna la instancia final de Barco.
         *
         * @return un nuevo objeto Barco con los parámetros configurados
         */
        public Barco build() {
            return new Barco(this);
        }
    }
}