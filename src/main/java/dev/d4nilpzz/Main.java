package dev.d4nilpzz;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<Vehiculo> vehiculos = new ArrayList<>();
    private static List<Cliente> clientes = new ArrayList<>();
    private static int contadorReservas = 10000000;
    private static final Scanner sc = new Scanner(System.in);

    private static final String CLIENTES_TXT = "data/clientes.txt";
    private static final String VEHICULOS_TXT = "data/vehiculos.txt";

    public static void main(String[] args) {
        System.out.println("JavaDrive. El super gestor de reservas para tu alquiler de super vehiculos.\n");
        cargarDatos();
        int i;
        do {
            i = mostrarMenu();
            switch (i) {
                case 1 -> listarVehiculosDisponibles();
                case 2 -> crearCliente();
                case 3 -> crearVehiculo();
                case 4 -> pedirDatosReserva();
                case 5 -> generarInformeXML();
                case 0 -> {
                    guardarDatos();
                    System.out.println("datos guardados.");
                }
                default -> System.out.println("[ERROR] Opcion no valida.");
            }
        } while (i != 0);
    }

    static void cargarDatos() {
        cargarClientes();
        cargarVehiculos();
    }

    private static void cargarClientes() {
        File file = new File(CLIENTES_TXT);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String l;
            while ((l = br.readLine()) != null) {
                l = l.trim();
                if (l.isEmpty()) continue;
                String[] p = l.split(";");
                if (p.length == 3) {
                    clientes.add(new Cliente(p[0], p[1], p[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Cargando clientes: " + e.getMessage());
        }
    }

    private static void cargarVehiculos() {
        File file = new File(VEHICULOS_TXT);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String l;
            while ((l = br.readLine()) != null) {
                l = l.trim();
                if (l.isEmpty()) continue;
                String[] p = l.split(";");
                if (p[0].equalsIgnoreCase("COCHE") && p.length == 7) {
                    Tipo tipo = Tipo.valueOf(p[5].toUpperCase());
                    Coche c = new Coche(p[1], p[2], p[3], tipo, Integer.parseInt(p[6]));
                    c.setDisponible(Boolean.parseBoolean(p[4]));
                    vehiculos.add(c);
                } else if (p[0].equalsIgnoreCase("FURGONETA") && p.length == 7) {
                    Furgoneta furg = new Furgoneta(p[1], p[2], p[3], Boolean.parseBoolean(p[5]), Integer.parseInt(p[6]));
                    furg.setDisponible(Boolean.parseBoolean(p[4]));
                    vehiculos.add(furg);
                }
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Cargando vehiculos: " + e.getMessage());
        }
    }

    static void guardarDatos() {
        guardarClientes();
        guardarVehiculos();
    }

    private static void guardarClientes() {
        new File("data").mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(CLIENTES_TXT))) {
            for (Cliente c : clientes) {
                pw.println(c.getDni() + ";" + c.getNombre() + ";" + c.getTelefono());
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Guardando clientes: " + e.getMessage());
        }
    }

    private static void guardarVehiculos() {
        new File("data").mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(VEHICULOS_TXT))) {
            for (Vehiculo v : vehiculos) {
                if (v instanceof Coche c) {
                    pw.println("COCHE;" + c.getMatricula() + ";" + c.getMarca() + ";" + c.getModelo() + ";" + c.isDisponible() + ";" + c.getTipo() + ";" + c.getNumPlazas());
                } else if (v instanceof Furgoneta furg) {
                    pw.println("FURGONETA;" + furg.getMatricula() + ";" + furg.getMarca() + ";" + furg.getModelo() + ";" + furg.isDisponible() + ";" + furg.isEsDeCarga() + ";" + furg.getCapacidad());
                }
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Guardando vehiculos: " + e.getMessage());
        }
    }

    static void exportarTicket(Reserva r) {
        new File("data").mkdirs();
        String nombreFichero = "data/reserva_R" + r.getIdReserva() + ".txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreFichero))) {
            pw.print(r.generarLineaTicket());
            System.out.println("Ticket exportado a: " + nombreFichero);
        } catch (IOException e) {
            System.out.println("[ERROR] Exportando ticket: " + e.getMessage());
        }
    }

    static void crearCliente() {
        System.out.print("DNI: ");
        String dni = sc.nextLine().trim();
        if (buscarCliente(dni) != null) {
            System.out.println("[ERROR] Ya existe un cliente con ese DNI.");
            return;
        }
        System.out.print("Nombre completo: ");
        String nombre = sc.nextLine().trim();
        System.out.print("Telefono: ");
        String telefono = sc.nextLine().trim();
        clientes.add(new Cliente(dni, nombre, telefono));
        System.out.println("Cliente creado correctamente.");
    }

    static void crearVehiculo() {
        System.out.print("Tipo (COCHE/FURGONETA): ");
        String tipo = sc.nextLine().trim().toUpperCase();
        System.out.print("Matricula: ");
        String matricula = sc.nextLine().trim();
        System.out.print("Marca: ");
        String marca = sc.nextLine().trim();
        System.out.print("Modelo: ");
        String modelo = sc.nextLine().trim();

        if (tipo.equals("COCHE")) {
            System.out.print("Tipo de coche (Pequeño/Familiar/Deportivo): ");
            String tipoCoche = sc.nextLine().trim();
            Tipo tipos = Tipo.valueOf(tipoCoche.toUpperCase());
            System.out.print("Numero de plazas (2-7): ");
            int plazas = Integer.parseInt(sc.nextLine().trim());
            vehiculos.add(new Coche(matricula, marca, modelo, tipos, plazas));
            System.out.println("Coche añadido.");
        } else if (tipo.equals("FURGONETA")) {
            System.out.print("Es de carga? (true/false): ");
            boolean esCarga = Boolean.parseBoolean(sc.nextLine().trim());
            System.out.print(esCarga ? "Capacidad (kg): " : "Capacidad (personas): ");
            int capacidad = Integer.parseInt(sc.nextLine().trim());
            vehiculos.add(new Furgoneta(matricula, marca, modelo, esCarga, capacidad));
            System.out.println("Furgoneta añadida.");
        } else {
            System.out.println("[ERROR] Ese tipo de vehiculo no existe.");
        }
    }

    static Cliente buscarCliente(String dni) {
        for (Cliente c : clientes) {
            if (c.getDni().equalsIgnoreCase(dni)) return c;
        }
        return null;
    }

    static Vehiculo buscarVehiculo(String matricula) {
        for (Vehiculo v : vehiculos) {
            if (v.getMatricula().equalsIgnoreCase(matricula)) return v;
        }
        return null;
    }

    static void listarVehiculosDisponibles() {
        System.out.println("\n-> VEHICULOS DISPONIBLES <-");
        boolean d = false;
        for (Vehiculo v : vehiculos) {
            if (v.isDisponible()) {
                System.out.println(v + " | " + v.obtenerDetalles());
                d = true;
            }
        }
        if (!d) System.out.println("No hay vehiculos disponibles.");
        System.out.println();
    }

    static void realizarReserva(Cliente c, Vehiculo v, LocalDate fInicio, LocalDate fFin) {
        v.setDisponible(false);
        contadorReservas++;
        Reserva reserva = new Reserva(contadorReservas, c, v, fInicio, fFin);
        exportarTicket(reserva);
        System.out.println("Reserva R" + contadorReservas + " realizada.");
    }

    static int mostrarMenu() {

        System.out.println("""
                ╔═════════════════════════════════════════╗ ╔═════════════════════════════════════╗
                ║   JAVADRIVE                             ║ ║ Hecho por Jose Miguel y Jose Daniel ║
                ╠═════════════════════════════════════════╣ ╚═════════════════════════════════════╝
                ║   1. Listar vehiculos disponibles       ║ ╔═════════════╗
                ║   2. Crear cliente                      ║ ║  Creditos   ║
                ║   3. Añadir vehículo                    ║ ║  del menu   ║
                ║   4. Realizar reserva                   ║ ║  a Jose     ║
                ║   5. Generar informe (XML)              ║ ║  Luis F.L   ║
                ║   0. Salir                              ║ ║             ║
                ╚═════════════════════════════════════════╝ ╚═════════════╝""");
        System.out.print("-> ");
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static void pedirDatosReserva() {
        System.out.print("DNI del cliente: ");
        String dni = sc.nextLine().trim();
        Cliente cliente = buscarCliente(dni);
        if (cliente == null) {
            System.out.println("[ERROR] Cliente no encontrado.");
            return;
        }

        System.out.print("Matricula del vehiculo: ");
        String matricula = sc.nextLine().trim();
        Vehiculo vehiculo = buscarVehiculo(matricula);
        if (vehiculo == null) {
            System.out.println("[ERROR] Vehiculo no encontrado.");
            return;
        }
        if (!vehiculo.isDisponible()) {
            System.out.println("[ERROR] El vehiculo no esta disponible.");
            return;
        }

        try {
            System.out.print("Fecha de recogida (YYYY-MM-DD): ");
            LocalDate fI = LocalDate.parse(sc.nextLine().trim());
            System.out.print("Fecha de devolucion (YYYY-MM-DD): ");
            LocalDate fF = LocalDate.parse(sc.nextLine().trim());
            if (!fF.isAfter(fI)) {
                System.out.println("[ERROR] La fecha de devolucion debe ser mas tarde a la de recogida.");
                return;
            }
            realizarReserva(cliente, vehiculo, fI, fF);
        } catch (DateTimeParseException e) {
            System.out.println("[ERROR] Formato incorrecto.");
        }
    }

    static void generarInformeXML() {
        try {
            File dir = new File("data");
            if (!dir.exists()) dir.mkdirs();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            ProcessingInstruction pi = doc.createProcessingInstruction(
                    "xml-stylesheet",
                    "type=\"text/xsl\" href=\"estilo_javadrive.xsl\""
            );
            doc.appendChild(pi);

            Element root = doc.createElement("javadrive");
            doc.appendChild(root);

            Element empresa = doc.createElement("empresa");
            root.appendChild(empresa);

            Element nombre = doc.createElement("nombre");
            nombre.setTextContent("JavaDrive S.L.");
            empresa.appendChild(nombre);

            Element ubicacion = doc.createElement("ubicacion");
            ubicacion.setTextContent("Sede Central del PSOE");
            empresa.appendChild(ubicacion);

            Element flota = doc.createElement("flota");
            root.appendChild(flota);

            for (Vehiculo v : vehiculos) {
                Element vehiculo = doc.createElement("vehiculo");
                vehiculo.setAttribute("tipo", (v instanceof Coche) ? "Coche" : "Furgoneta");

                Element matricula = doc.createElement("matricula");
                matricula.setTextContent(v.getMatricula());
                vehiculo.appendChild(matricula);

                Element marca = doc.createElement("marca");
                marca.setTextContent(v.getMarca());
                vehiculo.appendChild(marca);

                Element modelo = doc.createElement("modelo");
                modelo.setTextContent(v.getModelo());
                vehiculo.appendChild(modelo);

                Element disponible = doc.createElement("disponible");
                disponible.setTextContent(String.valueOf(v.isDisponible()));
                vehiculo.appendChild(disponible);

                Element especifico = doc.createElement("especifico");
                especifico.setTextContent(v.obtenerDetalles());
                vehiculo.appendChild(especifico);

                flota.appendChild(vehiculo);
            }

            Element clientesNode = doc.createElement("clientes");
            root.appendChild(clientesNode);

            for (Cliente c : clientes) {
                Element cliente = doc.createElement("cliente");

                Element dni = doc.createElement("dni");
                dni.setTextContent(c.getDni());
                cliente.appendChild(dni);

                Element nombreC = doc.createElement("nombre");
                nombreC.setTextContent(c.getNombre());
                cliente.appendChild(nombreC);

                Element telefono = doc.createElement("telefono");
                telefono.setTextContent(c.getTelefono());
                cliente.appendChild(telefono);

                clientesNode.appendChild(cliente);
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "2"
            );

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("data/reporte_completo.xml"));

            transformer.transform(source, result);

            System.out.println("Sea generado el XML en: data/reporte_completo.xml");

        } catch (Exception e) {
            System.out.println("[ERROR] Generando XML: " + e.getMessage());
        }
    }
}
