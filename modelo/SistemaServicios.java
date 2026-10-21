package modelo;
import java.util.*;

/** Modelo de la aplicación: registros, reglas entre objetos y consultas. */
public class SistemaServicios {
    private final Map<String, OrdenDeServicio> ordenes = new LinkedHashMap<>();
    private final Map<String, Cuadrilla> cuadrillas = new LinkedHashMap<>();
    private final Map<Integer, Jornada> jornadas = new LinkedHashMap<>();
    private int siguienteIdJornada = 1;

    public SistemaServicios() { this(true); }
    public SistemaServicios(boolean cargarEjemplos) { if (cargarEjemplos) cargarDatosIniciales(); }
    public Cotizacion cotizar(OrdenDeServicio propuesta) { return new Cotizacion(propuesta); }
    public void registrarOrden(OrdenDeServicio orden) { agregarOrden(orden, true); }
    private void agregarOrden(OrdenDeServicio orden, boolean cobrar) {
        Validacion.exigir(orden != null, "Falta la orden.");
        Validacion.exigir(!ordenes.containsKey(orden.getCodigo()), "Código de orden repetido.");
        Validacion.exigir(orden.getEstado() == EstadoOrden.PENDIENTE
                && orden.getVisitasRealizadas() == 0 && orden.getIngresoRegistro() == 0,
                "La orden debe ser nueva.");
        Validacion.dinero(orden.calcularCosto());
        if (cobrar) orden.cobrarRegistro();
        ordenes.put(orden.getCodigo(), orden);
    }
    public void registrarCuadrilla(String codigo, double capacidad, boolean certificada) {
        Cuadrilla cuadrilla = new Cuadrilla(codigo, capacidad, certificada);
        Validacion.exigir(!cuadrillas.containsKey(cuadrilla.getCodigo()), "Código de cuadrilla repetido.");
        cuadrillas.put(cuadrilla.getCodigo(), cuadrilla);
    }
    public Jornada crearJornada(String codigoCuadrilla) {
        Cuadrilla cuadrilla = buscarCuadrilla(codigoCuadrilla);
        for (Jornada jornada : jornadas.values())
            Validacion.exigir(jornada.getCuadrilla() != cuadrilla || jornada.getEstado() == EstadoJornada.CERRADA,
                    "La cuadrilla ya tiene una jornada sin cerrar.");
        Jornada jornada = new Jornada(siguienteIdJornada, cuadrilla);
        jornadas.put(siguienteIdJornada++, jornada);
        return jornada;
    }
    public void asignarOrden(int id, String codigo) { buscarJornada(id).asignarOrden(buscarOrden(codigo)); }
    public void retirarOrden(int id, String codigo) { buscarJornada(id).retirarOrden(buscarOrden(codigo)); }
    public void iniciarJornada(int id) { buscarJornada(id).iniciar(); }
    public void registrarResultado(int id, String codigo, ResultadoVisita resultado) {
        buscarJornada(id).registrarResultado(buscarOrden(codigo), resultado);
    }
    public OrdenDeServicio buscarOrden(String codigo) {
        OrdenDeServicio orden = ordenes.get(Validacion.texto(codigo, "Código"));
        Validacion.exigir(orden != null, "No existe esa orden.");
        return orden;
    }
    public Cuadrilla buscarCuadrilla(String codigo) {
        Cuadrilla cuadrilla = cuadrillas.get(Validacion.texto(codigo, "Código"));
        Validacion.exigir(cuadrilla != null, "No existe esa cuadrilla.");
        return cuadrilla;
    }
    public Jornada buscarJornada(int id) {
        Jornada jornada = jornadas.get(id);
        Validacion.exigir(jornada != null, "No existe esa jornada.");
        return jornada;
    }
    public List<OrdenDeServicio> consultarOrdenes() { return Collections.unmodifiableList(new ArrayList<>(ordenes.values())); }
    public List<Cuadrilla> consultarCuadrillas() { return Collections.unmodifiableList(new ArrayList<>(cuadrillas.values())); }
    public List<Jornada> consultarJornadasAbiertas() {
        List<Jornada> abiertas = new ArrayList<>();
        for (Jornada jornada : jornadas.values())
            if (jornada.getEstado() != EstadoJornada.CERRADA) abiertas.add(jornada);
        return Collections.unmodifiableList(abiertas);
    }
    public Map<String, Map<EstadoOrden, Integer>> contarOrdenes() {
        Map<String, Map<EstadoOrden, Integer>> conteo = new LinkedHashMap<>();
        for (OrdenDeServicio orden : ordenes.values()) {
            Map<EstadoOrden, Integer> estados = conteo.computeIfAbsent(orden.getCategoria(), clave -> {
                Map<EstadoOrden, Integer> fila = new EnumMap<>(EstadoOrden.class);
                for (EstadoOrden estado : EstadoOrden.values()) fila.put(estado, 0);
                return fila;
            });
            estados.put(orden.getEstado(), estados.get(orden.getEstado()) + 1);
        }
        return conteo; // Copia calculada: modificarla no modifica las órdenes.
    }
    public double getIngresosRegistro() {
        double total = 0;
        for (OrdenDeServicio orden : ordenes.values()) total += orden.getIngresoRegistro();
        return Validacion.dinero(total);
    }
    public double getIngresosVisitas() {
        double total = 0;
        for (OrdenDeServicio orden : ordenes.values()) total += orden.getIngresoVisitasAdicionales();
        return Validacion.dinero(total);
    }
    public Map<String, Double> ingresosPorCategoria() {
        Map<String, Double> ingresos = new LinkedHashMap<>();
        for (OrdenDeServicio orden : ordenes.values()) {
            String categoria = orden.getCategoria();
            double monto = orden.getIngresoRegistro() + orden.getIngresoVisitasAdicionales();
            ingresos.put(categoria, Validacion.dinero(ingresos.getOrDefault(categoria, 0.0) + monto));
        }
        return ingresos;
    }
    private void cargarDatosIniciales() {
        agregarOrden(new Diagnostico("D01", "Ana", "Zona 1", 1, Zona.METROPOLITANA), false);
        agregarOrden(new Diagnostico("D02", "Luis", "Villa Nueva", 1.5, Zona.PERIFERICA), false);
        agregarOrden(new Instalacion("I01", "Marta", "Zona 10", 2.5, Zona.METROPOLITANA, 5), false);
        agregarOrden(new Instalacion("I02", "Carlos", "Mixco", 2.2, Zona.PERIFERICA, 1), false);
        agregarOrden(new InstalacionElectricaCertificada("E01", "Sofía", "Zona 4", 2.5, Zona.METROPOLITANA, 5, 2000), false);
        agregarOrden(new InstalacionElectricaCertificada("E02", "Pedro", "San Miguel", 1.2, Zona.PERIFERICA, 2, 1500), false);
        agregarOrden(new MantenimientoAire("A01", "Julia", "Zona 9", 1.5, Zona.METROPOLITANA, 9000), false);
        agregarOrden(new MantenimientoAire("A02", "Mario", "Zona 14", 2, Zona.METROPOLITANA, 60000), false);
        registrarCuadrilla("C01", 8, true);
        registrarCuadrilla("C02", 6, false);
        registrarCuadrilla("C03", 4, true);
    }
}
