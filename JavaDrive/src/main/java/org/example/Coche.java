package org.example;

public class Coche extends Vehiculo {
    private Tipo tipo;
    private int numPlazas;

    public Coche(String matricula, String marca, String modelo, boolean disponible, Tipo tipo, int numPlazas) {
        super(matricula, marca, modelo, disponible);
        this.tipo = tipo;
        this.numPlazas = numPlazas;
    }

    public String obtenerDetalles() {
        return "org.example.Coche ["+tipo+"], Plazas: ["+numPlazas+"]";
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public int getNumPlazas() {
        return numPlazas;
    }

    public void setNumPlazas(int numPlazas) {
        this.numPlazas = numPlazas;
    }
}