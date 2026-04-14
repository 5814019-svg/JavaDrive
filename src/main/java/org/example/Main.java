package org.example;





import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    // Atributos estáticos
    private static List<Vehiculo> flota = new ArrayList<>();
    private static List<Cliente> clientes = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    private static final String FICHERO_CLIENTES = "clientes.txt";
    private static final String FICHERO_VEHICULOS = "vehiculos.txt";

    public static void main(String[] args) {
        cargarDatos();

        int opcion;
        do {
            opcion = mostrarMenu();

            switch (opcion) {
                case 1:
                    crearCliente();
                    break;
                case 2:
                    crearVehiculo();
                    break;
                case 3:
                    listarVehiculosDisponibles();
                    break;
                case 4:
                    pedirDatosReserva();
                    break;
                case 5:
                    guardarDatos();
                    System.out.println("Datos guardados correctamente. Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 5);

        sc.close();
    }


    public static void cargarDatos() {
        cargarClientes();
        cargarVehiculos();
    }

    private static void cargarClientes() {
        try (BufferedReader br = new BufferedReader(new FileReader(FICHERO_CLIENTES))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");

                if (partes.length == 3) {
                    String dni = partes[0];
                    String nombre = partes[1];
                    String telefono = partes[2];

                    clientes.add(new Cliente(dni, nombre, telefono));
                }
            }

        } catch (IOException e) {
            System.out.println("No se pudieron cargar los clientes: " + e.getMessage());
        }
    }

    private static void cargarVehiculos() {
        try (BufferedReader br = new BufferedReader(new FileReader(FICHERO_VEHICULOS))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");

                if (partes[0].equalsIgnoreCase("COCHE")) {
                    // COCHE;1234ABC;Seat;Leon;true;Familiar;5
                    String matricula = partes[1];
                    String marca = partes[2];
                    String modelo = partes[3];
                    boolean disponible = Boolean.parseBoolean(partes[4]);
                    Tipo tipo = Tipo.valueOf(partes[5]);
                    int numPlazas = Integer.parseInt(partes[6]);

                    flota.add(new Coche(matricula, marca, modelo, disponible, tipo, numPlazas));

                } else if (partes[0].equalsIgnoreCase("FURGONETA")) {
                    // FURGONETA;1122JKL;Ford;Transit;true;true;1200
                    String matricula = partes[1];
                    String marca = partes[2];
                    String modelo = partes[3];
                    boolean disponible = Boolean.parseBoolean(partes[4]);
                    boolean esDeCarga = Boolean.parseBoolean(partes[5]);
                    int capacidad = Integer.parseInt(partes[6]);

                    flota.add(new Furgoneta(matricula, marca, modelo, disponible, esDeCarga, capacidad));
                }
            }

        } catch (IOException e) {
            System.out.println("No se pudieron cargar los vehículos: " + e.getMessage());
        }
    }

    public static void guardarDatos() {
        guardarClientes();
        guardarVehiculos();
    }

    private static void guardarClientes() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FICHERO_CLIENTES))) {
            for (Cliente c : clientes) {
                pw.println(c.getDni() + ";" + c.getNombre() + ";" + c.getTelefono());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar clientes: " + e.getMessage());
        }
    }

    private static void guardarVehiculos() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FICHERO_VEHICULOS))) {
            for (Vehiculo v : flota) {
                if (v instanceof Coche) {
                    Coche c = (Coche) v;
                    pw.println("COCHE;" + c.getMatricula() + ";" + c.getMarca() + ";" + c.getModelo() + ";"
                            + c.isDisponible() + ";" + c.getTipo() + ";" + c.getNumPlazas());
                } else if (v instanceof Furgoneta) {
                    Furgoneta f = (Furgoneta) v;
                    pw.println("FURGONETA;" + f.getMatricula() + ";" + f.getMarca() + ";" + f.getModelo() + ";"
                            + f.isDisponible() + ";" + f.isElDescarga() + ";" + f.getCapacidad());
                }
            }
        } catch (IOException e) {
            System.out.println("Error al guardar vehículos: " + e.getMessage());
        }
    }

    public static void exportarTicket(Reserva r) {
        String nombreFichero = "reserva_" + r.getIdReserva() + ".txt";

        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreFichero))) {
            long totalDias = ChronoUnit.DAYS.between(r.getFechaInicio(), r.getFechaFin());

            pw.println("==================================================");
            pw.println("CONTRATO DE ALQUILER - JAVADRIVE");
            pw.println("==================================================");
            pw.println("ID RESERVA: " + r.getIdReserva());
            pw.println("FECHA EMISIÓN: " + LocalDate.now());
            pw.println("--------------------------------------------------");
            pw.println("DATOS DEL CLIENTE:");
            pw.println("Nombre: " + r.getCliente().getNombre());
            pw.println("DNI: " + r.getCliente().getDni());
            pw.println("Teléfono: " + r.getCliente().getTelefono());
            pw.println("--------------------------------------------------");
            pw.println("DATOS DEL VEHÍCULO:");
            pw.println("Marca y Modelo: " + r.getVehiculo().getMarca() + " " + r.getVehiculo().getModelo());
            pw.println("Matrícula: " + r.getVehiculo().getMatricula());
            pw.println("Detalles: " + r.getVehiculo().toString());
            pw.println("--------------------------------------------------");
            pw.println("PERIODO DE ALQUILER:");
            pw.println("Fecha de recogida: " + r.getFechaInicio());
            pw.println("Fecha de devolución: " + r.getFechaFin());
            pw.println("Total días: " + totalDias);
            pw.println("--------------------------------------------------");
            pw.println("ESTADO: Confirmado y Pendiente de pago");
            pw.println();
            pw.println("Firma del cliente:        Firma JavaDrive:");
            pw.println("____________________      ____________________");
            pw.println("==================================================");

            System.out.println("Ticket exportado correctamente en el fichero: " + nombreFichero);

        } catch (IOException e) {
            System.out.println("Error al exportar el ticket: " + e.getMessage());
        }
    }

    // =========================
    // GESTIÓN DE NEGOCIO
    // =========================

    public static void crearCliente() {
        System.out.print("Introduce el DNI: ");
        String dni = sc.nextLine();

        if (buscarCliente(dni) != null) {
            System.out.println("Ya existe un cliente con ese DNI.");
            return;
        }

        System.out.print("Introduce el nombre completo: ");
        String nombre = sc.nextLine();

        System.out.print("Introduce el teléfono: ");
        String telefono = sc.nextLine();

        Cliente nuevo = new Cliente(dni, nombre, telefono);
        clientes.add(nuevo);

        System.out.println("Cliente creado correctamente.");
    }

    public static void crearVehiculo() {
        System.out.println("Tipo de vehículo a crear:");
        System.out.println("1. Coche");
        System.out.println("2. Furgoneta");
        System.out.print("Elige una opción: ");
        int opcion = Integer.parseInt(sc.nextLine());

        System.out.print("Introduce la matrícula: ");
        String matricula = sc.nextLine();

        if (buscarVehiculo(matricula) != null) {
            System.out.println("Ya existe un vehículo con esa matrícula.");
            return;
        }

        System.out.print("Introduce la marca: ");
        String marca = sc.nextLine();

        System.out.print("Introduce el modelo: ");
        String modelo = sc.nextLine();

        if (opcion == 1) {
            System.out.print("Introduce el tipo (Pequeño, Familiar o Deportivo): ");
            Tipo tipo = Tipo.valueOf(sc.nextLine());

            System.out.print("Introduce el número de plazas: ");
            int numPlazas = Integer.parseInt(sc.nextLine());

            Vehiculo coche = new Coche(matricula, marca, modelo, true, tipo, numPlazas);
            flota.add(coche);

            System.out.println("Coche creado correctamente.");

        } else if (opcion == 2) {
            System.out.print("¿Es de carga? (true/false): ");
            boolean esDeCarga = Boolean.parseBoolean(sc.nextLine());

            System.out.print("Introduce la capacidad: ");
            int capacidad = Integer.parseInt(sc.nextLine());

            Vehiculo furgoneta = new Furgoneta(matricula, marca, modelo, true, esDeCarga, capacidad);
            flota.add(furgoneta);

            System.out.println("Furgoneta creada correctamente.");

        } else {
            System.out.println("Opción no válida.");
        }
    }

    public static Cliente buscarCliente(String dni) {
        for (Cliente c : clientes) {
            if (c.getDni().equalsIgnoreCase(dni)) {
                return c;
            }
        }
        return null;
    }

    public static Vehiculo buscarVehiculo(String matricula) {
        for (Vehiculo v : flota) {
            if (v.getMatricula().equalsIgnoreCase(matricula)) {
                return v;
            }
        }
        return null;
    }

    public static void listarVehiculosDisponibles() {
        boolean hayDisponibles = false;

        System.out.println("\n=== VEHÍCULOS DISPONIBLES ===");
        for (Vehiculo v : flota) {
            if (v.isDisponible()) {
                System.out.println(v);
                hayDisponibles = true;
            }
        }

        if (!hayDisponibles) {
            System.out.println("No hay vehículos disponibles.");
        }
    }

    public static void realizarReserva(Cliente c, Vehiculo v, LocalDate fInicio, LocalDate fFin) {
        v.setDisponible(false);

        Reserva reserva = new Reserva(c, v, fInicio, fFin);

        exportarTicket(reserva);

        System.out.println("Reserva realizada correctamente.");
    }

    // =========================
    // CONSOLA
    // =========================

    public static int mostrarMenu() {
        System.out.println("\n========= JAVADRIVE =========");
        System.out.println("1. Crear cliente");
        System.out.println("2. Crear vehículo");
        System.out.println("3. Listar vehículos disponibles");
        System.out.println("4. Realizar reserva");
        System.out.println("5. Guardar y salir");
        System.out.print("Selecciona una opción: ");

        return Integer.parseInt(sc.nextLine());
    }

    public static void pedirDatosReserva() {
        System.out.print("Introduce el DNI del cliente: ");
        String dni = sc.nextLine();

        Cliente cliente = buscarCliente(dni);
        if (cliente == null) {
            System.out.println("Error: el cliente no existe.");
            return;
        }

        System.out.print("Introduce la matrícula del vehículo: ");
        String matricula = sc.nextLine();

        Vehiculo vehiculo = buscarVehiculo(matricula);
        if (vehiculo == null) {
            System.out.println("Error: el vehículo no existe.");
            return;
        }

        if (!vehiculo.isDisponible()) {
            System.out.println("Error: el vehículo no está disponible.");
            return;
        }

        try {
            System.out.print("Introduce la fecha de inicio (yyyy-MM-dd): ");
            LocalDate fechaInicio = LocalDate.parse(sc.nextLine());

            System.out.print("Introduce la fecha de fin (yyyy-MM-dd): ");
            LocalDate fechaFin = LocalDate.parse(sc.nextLine());

            if (!fechaFin.isAfter(fechaInicio)) {
                System.out.println("Error: la fecha de fin debe ser posterior a la fecha de inicio.");
                return;
            }

            realizarReserva(cliente, vehiculo, fechaInicio, fechaFin);

        } catch (Exception e) {
            System.out.println("Error al introducir las fechas. Usa el formato yyyy-MM-dd.");
        }
    }
}

