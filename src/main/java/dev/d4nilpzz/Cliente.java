package dev.d4nilpzz;

public class Cliente {
    private String dni;
    private String nombre;
    private String telefono;

    public Cliente(String dni, String nombre, String telefono) {
        this.dni = dni;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public void setDni(String dni) { this.dni = dni; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    @Override
    public String toString() {
        return "Cliente [DNI: " + dni + ", Nombre: " + nombre + ", Teléfono: " + telefono + "]";
    }
}
