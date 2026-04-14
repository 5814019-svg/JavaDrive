package org.example;

public class Furgoneta extends Vehiculo {

    private boolean elDescarga;
    private int capacidad;

    public Furgoneta(String matricula, String marca, String modelo, boolean disponible, boolean elDescarga, int capacidad) {
        super(matricula, marca, modelo, disponible);
        this.capacidad = capacidad;
        this.elDescarga = elDescarga;
    }
    public String obtenerdetalles() {
        if (elDescarga == true) {
            return ("Fuergoneta de carga" + capacidad + "kg");
        } else
            return ("org.example.Furgoneta de pasajeros" + capacidad + "personas");
        }


    public boolean isElDescarga() {
        return elDescarga;
    }

    public void setElDescarga(boolean elDescarga) {
        this.elDescarga = elDescarga;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }
}

