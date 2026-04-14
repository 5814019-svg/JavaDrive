package dev.d4nilpzz;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reserva {
    private int idReserva;
    private Cliente cliente;
    private Vehiculo vehiculo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Reserva(int idReserva, Cliente cliente, Vehiculo vehiculo, LocalDate fechaInicio, LocalDate fechaFin) {
        this.idReserva = idReserva;
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public int getIdReserva() { return idReserva; }
    public Cliente getCliente() { return cliente; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }

    public String generarLineaTicket() {
        long dias = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        StringBuilder sb = new StringBuilder();
        sb.append("==================================================\n");
        sb.append("         CONTRATO DE ALQUILER - JAVADRIVE\n");
        sb.append("==================================================\n");
        sb.append("ID RESERVA: R").append(idReserva).append("\n");
        sb.append("FECHA EMISIÓN: ").append(LocalDate.now()).append("\n");
        sb.append("--------------------------------------------------\n");
        sb.append("DATOS DEL CLIENTE:\n");
        sb.append("Nombre: ").append(cliente.getNombre()).append("\n");
        sb.append("DNI: ").append(cliente.getDni()).append("\n");
        sb.append("Teléfono: ").append(cliente.getTelefono()).append("\n");
        sb.append("--------------------------------------------------\n");
        sb.append("DATOS DEL VEHÍCULO:\n");
        sb.append("Marca y Modelo: ").append(vehiculo.getMarca()).append(" ").append(vehiculo.getModelo()).append("\n");
        sb.append("Matrícula: ").append(vehiculo.getMatricula()).append("\n");
        sb.append("Detalles: ").append(vehiculo.obtenerDetalles()).append("\n");
        sb.append("--------------------------------------------------\n");
        sb.append("PERIODO DE ALQUILER:\n");
        sb.append("Fecha de recogida: ").append(fechaInicio).append("\n");
        sb.append("Fecha de devolución: ").append(fechaFin).append("\n");
        sb.append("Total días: ").append(dias).append("\n");
        sb.append("--------------------------------------------------\n");
        sb.append("ESTADO: Confirmado y Pendiente de pago\n");
        sb.append("Firma del cliente:          Firma JavaDrive:\n");
        sb.append("____________________        ____________________\n");
        sb.append("==================================================\n");
        return sb.toString();
    }
}
