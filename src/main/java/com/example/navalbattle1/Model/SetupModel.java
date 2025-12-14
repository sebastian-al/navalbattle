package com.example.navalbattle1.Model;

import java.util.EnumMap;
import java.util.Map;

public class SetupModel {

    public static final int BOARD_SIZE = 10;

    /* ===================== TABLERO ===================== */

    private final Tablero tablero = new Tablero();

    /* ===================== FLOTA DISPONIBLE ===================== */

    public enum ShipType {
        CARRIER(4, 1),
        SUBMARINE(3, 2),
        DESTROYER(2, 3),
        FRIGATE(1, 4);

        public final int size;
        public int remaining;

        ShipType(int size, int remaining) {
            this.size = size;
            this.remaining = remaining;
        }
    }

    private final Map<ShipType, Integer> remainingFleet =
            new EnumMap<>(ShipType.class);

    public SetupModel() {
        for (ShipType type : ShipType.values()) {
            remainingFleet.put(type, type.remaining);
        }
    }

    /* ===================== FLOTA ===================== */

    public boolean hasRemaining(ShipType type) {
        return remainingFleet.get(type) > 0;
    }

    public int getRemaining(ShipType type) {
        return remainingFleet.get(type);
    }

    public boolean isFleetComplete() {
        return remainingFleet.values().stream().allMatch(v -> v == 0);
    }

    /* ===================== COLOCAR BARCO ===================== */

    public boolean placeShip(
            ShipType type,
            int row,
            int col,
            boolean horizontal
    ) {
        if (!hasRemaining(type)) return false;

        Barco barco = new Barco.Builder(type.name(), type.size)
                .fila(row)
                .columna(col)
                .horizontal(horizontal)
                .build();

        if (!tablero.puedeColocar(barco)) {
            return false;
        }

        tablero.agregarBarco(barco);
        remainingFleet.put(type, remainingFleet.get(type) - 1);
        return true;
    }

    /* ===================== FINAL ===================== */

    public Tablero getFinalBoard() {
        return tablero;
    }
}

