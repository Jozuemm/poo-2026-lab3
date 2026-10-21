package vista;
import controlador.Controller;
import modelo.*;
import java.util.*;

/** Único componente que lee la consola y presenta información al usuario. */
public class Vista {
    private final Controller controller;
    private final Scanner lector;
    public Vista(Controller controller) { this(controller, new Scanner(System.in)); }
    public Vista(Controller controller, Scanner lector) {
        this.controller = Objects.requireNonNull(controller);
        this.lector = Objects.requireNonNull(lector);
    }
    public void ejecutar() {
        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            try {
                int opcion = leerEntero("Opción: ");
                switch (opcion) {
                    case 1: cotizarYRegistrar(); break;
                    case 2: registrarCuadrilla(); break;
                    case 3: System.out.println("Jornada creada: " + controller.crearJornada(leerTexto("Código de cuadrilla: ")).getId()); break;
                    case 4: controller.asignarOrden(leerEntero("ID de jornada: "), leerTexto("Código de orden: ")); exito(); break;
                    case 5: controller.retirarOrden(leerEntero("ID de jornada: "), leerTexto("Código de orden: ")); exito(); break;
                    case 6: controller.iniciarJornada(leerEntero("ID de jornada: ")); exito(); break;
                    case 7: registrarResultado(); break;
                    case 8: mostrarOrdenes(); break;
                    case 9: mostrarCuadrillas(); break;
                    case 10: mostrarReportes(); break;
                    case 0: continuar = false; break;
                    default: System.out.println("Opción inválida.");
                }
            } catch (IllegalArgumentException error) {
                System.out.println("Operación rechazada: " + error.getMessage());
            } catch (NoSuchElementException fin) {
                System.out.println("Fin de la entrada.");
                continuar = false;
            }
        }
        System.out.println("Programa finalizado.");
    }
    private void mostrarMenu() {
        System.out.println("\nTECNIHOGAR\n1. Cotizar y registrar orden\n2. Registrar cuadrilla"
                + "\n3. Crear jornada\n4. Asignar orden\n5. Retirar orden\n6. Iniciar jornada"
                + "\n7. Registrar resultado\n8. Consultar órdenes\n9. Consultar cuadrillas"
                + "\n10. Reportes y jornadas abiertas\n0. Salir");
    }
    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return lector.nextLine().trim();
    }
    private int leerEntero(String mensaje) {
        while (true) {
            try { return Integer.parseInt(leerTexto(mensaje)); }
            catch (NumberFormatException error) { System.out.println("Ingrese un número entero válido."); }
        }
    }
    private double leerDecimal(String mensaje) {
        while (true) {
            try {
                double valor = Double.parseDouble(leerTexto(mensaje).replace(',', '.'));
                if (!Double.isFinite(valor)) throw new NumberFormatException();
                return valor;
            } catch (NumberFormatException error) { System.out.println("Ingrese un número decimal finito."); }
        }
    }
    private boolean leerSiNo(String mensaje) {
        while (true) {
            String respuesta = leerTexto(mensaje).toLowerCase(Locale.ROOT);
            if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")) return true;
            if (respuesta.equals("n") || respuesta.equals("no")) return false;
            System.out.println("Responda s o n.");
        }
    }
    private void cotizarYRegistrar() {
        System.out.println("1. Diagnóstico | 2. Instalación | 3. Instalación certificada | 4. Mantenimiento de aire");
        int tipo = leerEntero("Categoría: ");
        if (tipo < 1 || tipo > 4) throw new IllegalArgumentException("Categoría inválida.");
        String codigo = leerTexto("Código propuesto: ");
        String cliente = leerTexto("Cliente: ");
        String direccion = leerTexto("Dirección: ");
        double horas = leerDecimal("Horas estimadas: ");
        int opcionZona = leerEntero("Zona (1 metropolitana / 2 periférica): ");
        if (opcionZona != 1 && opcionZona != 2) throw new IllegalArgumentException("Zona inválida.");
        Zona zona = opcionZona == 1 ? Zona.METROPOLITANA : Zona.PERIFERICA;
        int puntos = 0, btu = 0;
        double materiales = 0;
        if (tipo == 2 || tipo == 3) puntos = leerEntero("Cantidad de puntos: ");
        if (tipo == 3) materiales = leerDecimal("Valor de materiales: Q");
        if (tipo == 4) btu = leerEntero("Capacidad BTU: ");
        OrdenDeServicio propuesta = controller.crearPropuesta(tipo, codigo, cliente, direccion, horas, zona, puntos, materiales, btu);
        Cotizacion cotizacion = controller.cotizar(propuesta);
        System.out.printf(Locale.US, "Costo: Q%.2f | Plazo: %d días hábiles%n", cotizacion.getCosto(), cotizacion.getDias());
        if (leerSiNo("¿Acepta y desea registrar la orden? (s/n): ")) {
            controller.registrarOrden(propuesta);
            System.out.println("Orden registrada y cobrada. Estado: PENDIENTE.");
        } else System.out.println("Cotización finalizada sin registro ni cobro.");
    }
    private void registrarCuadrilla() {
        controller.registrarCuadrilla(leerTexto("Código: "), leerDecimal("Capacidad en horas: "),
                leerSiNo("¿Certificada en refrigeración? (s/n): "));
        exito();
    }
    private void registrarResultado() {
        int id = leerEntero("ID de jornada: ");
        String codigo = leerTexto("Código de orden: ");
        int opcion = leerEntero("Resultado (1 completada / 2 fallida): ");
        if (opcion != 1 && opcion != 2) throw new IllegalArgumentException("Resultado inválido.");
        controller.registrarResultado(id, codigo, opcion == 1 ? ResultadoVisita.COMPLETADA : ResultadoVisita.FALLIDA);
        exito();
    }
    private void mostrarOrdenes() {
        for (OrdenDeServicio orden : controller.consultarOrdenes())
            System.out.printf(Locale.US, "%s | %s | %s | %s | %s | %.2f h | %s | visitas: %d/%d%n",
                    orden.getCodigo(), orden.getCategoria(), orden.getNombreCliente(), orden.getDireccion(),
                    orden.getZona(), orden.getHorasEstimadas(), orden.getEstado(), orden.getVisitasRealizadas(), orden.getMaxVisitas());
    }
    private void mostrarCuadrillas() {
        for (Cuadrilla cuadrilla : controller.consultarCuadrillas())
            System.out.printf(Locale.US, "%s | %.2f horas | Refrigeración: %s%n", cuadrilla.getCodigo(),
                    cuadrilla.getCapacidadHoras(), cuadrilla.isCertificadaRefrigeracion() ? "sí" : "no");
    }
    private void mostrarReportes() {
        System.out.println("\nÓRDENES POR CATEGORÍA Y ESTADO");
        for (Map.Entry<String, Map<EstadoOrden, Integer>> fila : controller.contarOrdenes().entrySet())
            System.out.println(fila.getKey() + ": " + fila.getValue());
        double registro = controller.getIngresosRegistro(), visitas = controller.getIngresosVisitas();
        System.out.printf(Locale.US, "\nRegistro: Q%.2f | Visitas adicionales: Q%.2f | Total: Q%.2f%n", registro, visitas, registro + visitas);
        for (Map.Entry<String, Double> fila : controller.ingresosPorCategoria().entrySet())
            System.out.printf(Locale.US, "%s: Q%.2f%n", fila.getKey(), fila.getValue());
        System.out.println("\nJORNADAS ABIERTAS");
        if (controller.consultarJornadasAbiertas().isEmpty()) System.out.println("No hay jornadas abiertas.");
        for (Jornada jornada : controller.consultarJornadasAbiertas()) {
            System.out.printf(Locale.US, "Jornada %d | Cuadrilla %s | %s | Asignadas %.2f h | Disponibles %.2f h%n",
                    jornada.getId(), jornada.getCuadrilla().getCodigo(), jornada.getEstado(),
                    jornada.calcularHorasAsignadas(), jornada.calcularCapacidadDisponible());
            for (OrdenDeServicio orden : jornada.getOrdenes()) {
                ResultadoVisita resultado = jornada.getResultados().get(orden.getCodigo());
                System.out.println("  " + orden.getCodigo() + " | " + (resultado == null ? "Sin resultado" : resultado));
            }
        }
    }
    private void exito() { System.out.println("Operación realizada."); }
}
