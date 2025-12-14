package com.example.navalbattle1.Model;

import java.util.EnumMap;
import java.util.Map;

/**
 * Modelo que gestiona la lógica de colocación de barcos en el tablero durante la fase de setup.
 *
 * <p>Este modelo maneja:</p>
 * <ul>
 *   <li>Validación de posiciones de barcos</li>
 *   <li>Detección de colisiones entre barcos</li>
 *   <li>Conteo de barcos restantes por tipo</li>
 *   <li>Creación del tablero final con todos los barcos colocados</li>
 * </ul>
 *
 * <p>Reglas de colocación:</p>
 * <ul>
 *   <li>Los barcos no pueden superponerse</li>
 *   <li>Los barcos no pueden salirse del tablero</li>
 *   <li>Debe haber exactamente: 1 portaaviones, 2 submarinos, 3 destructores y 4 fragatas</li>
 * </ul>
 *
 * @author Tu Nombre
 * @version 2.0
 * @see Barco
 * @see Tablero
 * @see ShipType
 */
public class SetupModel {

    /* ===================== CONSTANTES ===================== */

    /** Tamaño del tablero (10x10) */
    public static final int BOARD_SIZE = 10;

    /* ===================== TABLERO ===================== */

    /** Tablero donde se colocan los barcos */
    private final Tablero tablero = new Tablero();

    /* ===================== ENUMERACIÓN DE TIPOS ===================== */

    /**
     * Enumeración que define los tipos de barcos disponibles en el juego.
     * Cada tipo tiene un tamaño específico y una cantidad máxima permitida.
     */
    public enum ShipType {
        /** Portaaviones - 4 casillas - cantidad: 1 */
        CARRIER(4, 1),

        /** Submarino - 3 casillas - cantidad: 2 */
        SUBMARINE(3, 2),

        /** Destructor - 2 casillas - cantidad: 3 */
        DESTROYER(2, 3),

        /** Fragata - 1 casilla - cantidad: 4 */
        FRIGATE(1, 4);

        /** Tamaño del barco en número de casillas */
        public final int size;

        /** Cantidad inicial de barcos de este tipo */
        public final int initialCount;

        /**
         * Constructor del enum ShipType.
         *
         * @param size tamaño del barco en casillas
         * @param initialCount cantidad inicial de este tipo de barco
         */
        ShipType(int size, int initialCount) {
            this.size = size;
            this.initialCount = initialCount;
        }

        /**
         * Obtiene el tamaño del barco.
         *
         * @return el tamaño en número de casillas
         */
        public int getSize() {
            return size;
        }

        /**
         * Obtiene la cantidad inicial de barcos de este tipo.
         *
         * @return la cantidad inicial
         */
        public int getInitialCount() {
            return initialCount;
        }
    }

    /* ===================== FLOTA DISPONIBLE ===================== */

    /** Mapa que mantiene el conteo de barcos restantes por tipo */
    private final Map<ShipType, Integer> remainingFleet = new EnumMap<>(ShipType.class);

    /* ===================== CONSTRUCTOR ===================== */

    /**
     * Crea un nuevo modelo de setup con el tablero vacío y la flota completa disponible.
     * Inicializa el contador de barcos restantes según las cantidades definidas en ShipType.
     */
    public SetupModel() {
        for (ShipType type : ShipType.values()) {
            remainingFleet.put(type, type.initialCount);
        }
    }

    /* ===================== CONSULTAS DE FLOTA ===================== */

    /**
     * Verifica si quedan barcos disponibles de un tipo específico.
     *
     * @param type el tipo de barco a verificar
     * @return true si quedan barcos de este tipo, false en caso contrario
     */
    public boolean hasRemaining(ShipType type) {
        return remainingFleet.get(type) > 0;
    }

    /**
     * Obtiene la cantidad de barcos restantes de un tipo específico.
     *
     * @param type el tipo de barco
     * @return la cantidad de barcos de ese tipo que aún no han sido colocados
     */
    public int getRemaining(ShipType type) {
        return remainingFleet.get(type);
    }

    /**
     * Verifica si todos los barcos han sido colocados en el tablero.
     *
     * @return true si la flota está completa (todos los contadores en 0), false en caso contrario
     */
    public boolean isFleetComplete() {
        return remainingFleet.values().stream().allMatch(v -> v == 0);
    }

    /**
     * Obtiene el número total de barcos que deben ser colocados.
     *
     * @return el número total de barcos (1+2+3+4 = 10)
     */
    public int getTotalShipsCount() {
        return ShipType.CARRIER.initialCount +
                ShipType.SUBMARINE.initialCount +
                ShipType.DESTROYER.initialCount +
                ShipType.FRIGATE.initialCount;
    }

    /**
     * Obtiene el número de barcos que ya han sido colocados.
     *
     * @return la cantidad de barcos colocados
     */
    public int getPlacedShipsCount() {
        int total = getTotalShipsCount();
        int remaining = remainingFleet.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
        return total - remaining;
    }

    /* ===================== VALIDACIÓN DE COLOCACIÓN ===================== */

    /**
     * Verifica si un barco puede ser colocado en la posición especificada.
     * Este método solo valida, no coloca el barco.
     *
     * <p>Validaciones realizadas:</p>
     * <ul>
     *   <li>Verifica que queden barcos de este tipo disponibles</li>
     *   <li>Verifica que el barco no se salga del tablero</li>
     *   <li>Verifica que no haya colisión con otros barcos</li>
     * </ul>
     *
     * @param type el tipo de barco a colocar
     * @param row la fila inicial del barco
     * @param col la columna inicial del barco
     * @param horizontal true si el barco es horizontal, false si es vertical
     * @return true si la colocación es válida, false en caso contrario
     */
    public boolean canPlaceShip(ShipType type, int row, int col, boolean horizontal) {
        // Verificar si quedan barcos de este tipo
        if (!hasRemaining(type)) {
            return false;
        }

        // Crear un barco temporal para validar
        Barco tempBarco = new Barco.Builder(type.name(), type.size)
                .fila(row)
                .columna(col)
                .horizontal(horizontal)
                .build();

        // Usar el método de validación del tablero
        return tablero.puedeColocar(tempBarco);
    }

    /* ===================== COLOCACIÓN DE BARCOS ===================== */

    /**
     * Intenta colocar un barco en la posición especificada.
     * Si la colocación es válida, el barco se añade al tablero y se actualiza
     * el conteo de barcos restantes.
     *
     * @param type el tipo de barco a colocar
     * @param row la fila inicial del barco
     * @param col la columna inicial del barco
     * @param horizontal true si el barco es horizontal, false si es vertical
     * @return true si el barco fue colocado exitosamente, false en caso contrario
     */
    public boolean placeShip(ShipType type, int row, int col, boolean horizontal) {
        // Verificar si quedan barcos de este tipo
        if (!hasRemaining(type)) {
            return false;
        }

        // Crear el barco usando el patrón Builder
        Barco barco = new Barco.Builder(type.name(), type.size)
                .fila(row)
                .columna(col)
                .horizontal(horizontal)
                .build();

        // Validar que el barco pueda ser colocado
        if (!tablero.puedeColocar(barco)) {
            return false;
        }

        // Colocar el barco en el tablero
        tablero.agregarBarco(barco);

        // Decrementar el contador de barcos restantes
        remainingFleet.put(type, remainingFleet.get(type) - 1);

        return true;
    }

    /* ===================== OBTENCIÓN DEL TABLERO FINAL ===================== */

    /**
     * Obtiene el tablero con todos los barcos colocados.
     * Este método debe ser llamado cuando isFleetComplete() retorna true.
     *
     * @return el tablero con la configuración final de barcos
     */
    public Tablero getFinalBoard() {
        return tablero;
    }

    /**
     * Reinicia el modelo al estado inicial, removiendo todos los barcos colocados
     * y restaurando la flota completa.
     *
     * Nota: Este método solo reinicia el contador de barcos. Si necesitas
     * limpiar el tablero completamente, deberás crear una nueva instancia de SetupModel.
     */
    public void reset() {
        // Reiniciar contadores
        for (ShipType type : ShipType.values()) {
            remainingFleet.put(type, type.initialCount);
        }
    }

    /**
     * Obtiene el tablero actual (útil para debugging o visualización parcial).
     *
     * @return el tablero actual
     */
    public Tablero getTablero() {
        return tablero;
    }
}