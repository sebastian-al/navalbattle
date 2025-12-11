package com.example.navalbattle1.Model;

import java.util.List;

public class Tablero {
    int[][] tablero = new int[10][10];
    List<Barco> barcos;

    public void agregarBarco(Barco barco) {
        barcos.add(barco);
        int tamano= barco.getTamano();
        int fila=barco.getFila();
        int columna=barco.getColumna();
        if(barco.isHorizontal()){
            for(int i = 0; i < tamano; i++){
                tablero[fila][columna+i] = barcos.size();
            }
        } else{
            for(int i = 0; i < tamano; i++){
                tablero[fila+i][columna] = barcos.size();
            }
        }
    }
    public void lanzarImpacto(int fila, int columna){
        int objetivo=tablero[fila][columna];
        if (objetivo==0){
            tablero[fila][columna]=barcos.size();
        } else {
            barcos.get(objetivo-1).agregarImpacto();
            if(barcos.get(objetivo-1).seHunde()){
                hundirBarco(objetivo-1);
            } else {
                tablero[fila][columna]=barcos.size()+1;
            }
        }
    }
    public void hundirBarco(int index){
        Barco barco=barcos.get(index);
        int tamano= barco.getTamano();
        int fila=barco.getFila();
        int columna=barco.getColumna();
        if(barco.isHorizontal()){
            for(int i = 0; i < tamano; i++){
                tablero[fila][columna+i] = barcos.size()+2;
            }
        } else{
            for(int i = 0; i < tamano; i++){
                tablero[fila+i][columna] = barcos.size()+2;
            }
        }
    }
}
