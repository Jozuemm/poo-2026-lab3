package modelo;
import java.util.*;

public class Jornada {
    private final int id;
    private final Cuadrilla cuadrilla;
    private EstadoJornada estado = EstadoJornada.EN_PREPARACION;
    private final List<OrdenDeServicio> ordenes = new ArrayList<>();
    private final Map<String, ResultadoVisita> resultados = new LinkedHashMap<>();

    Jornada(int id, Cuadrilla cuadrilla) {
        Validacion.exigir(id > 0 && cuadrilla != null, "Datos de jornada inválidos.");
        this.id = id;
        this.cuadrilla = cuadrilla;
    }
    void asignarOrden(OrdenDeServicio orden) {
        validarAsignacion(orden);
        orden.marcarAsignada();
        ordenes.add(orden);
    }
    private void validarAsignacion(OrdenDeServicio orden) {
        Validacion.exigir(estado == EstadoJornada.EN_PREPARACION, "La jornada no está en preparación.");
        Validacion.exigir(orden != null && orden.getEstado() == EstadoOrden.PENDIENTE,
                "Solo puede asignar órdenes pendientes.");
        Validacion.exigir(!ordenes.contains(orden), "La orden ya pertenece a esta jornada.");
        Validacion.exigir(orden.esCompatible(cuadrilla), "Cuadrilla incompatible con el servicio.");
        Validacion.exigir(calcularHorasAsignadas() + orden.getHorasEstimadas()
                <= cuadrilla.getCapacidadHoras() + 1e-9, "La asignación excede la capacidad.");
    }
    void retirarOrden(OrdenDeServicio orden) {
        Validacion.exigir(estado == EstadoJornada.EN_PREPARACION, "Solo puede retirar durante preparación.");
        Validacion.exigir(ordenes.contains(orden), "La orden no pertenece a esta jornada.");
        orden.retirarAsignacion();
        ordenes.remove(orden);
    }
    void iniciar() {
        Validacion.exigir(estado == EstadoJornada.EN_PREPARACION, "La jornada no está en preparación.");
        Validacion.exigir(!ordenes.isEmpty(), "No puede iniciar una jornada vacía.");
        // Primero se validan TODAS las órdenes. Después se aplica la operación.
        for (OrdenDeServicio orden : ordenes) orden.validarInicio();
        for (OrdenDeServicio orden : ordenes) orden.iniciarVisita();
        estado = EstadoJornada.EN_CURSO;
    }
    void registrarResultado(OrdenDeServicio orden, ResultadoVisita resultado) {
        Validacion.exigir(estado == EstadoJornada.EN_CURSO, "La jornada no está en curso.");
        Validacion.exigir(ordenes.contains(orden), "La orden no pertenece a esta jornada.");
        Validacion.exigir(!resultados.containsKey(orden.getCodigo()), "El resultado ya fue registrado.");
        Validacion.exigir(resultado != null, "Seleccione un resultado.");
        orden.registrarResultado(resultado);
        resultados.put(orden.getCodigo(), resultado);
        if (resultados.size() == ordenes.size()) estado = EstadoJornada.CERRADA;
    }
    public double calcularHorasAsignadas() {
        double total = 0;
        for (OrdenDeServicio orden : ordenes) total += orden.getHorasEstimadas();
        return total;
    }
    public double calcularCapacidadDisponible() {
        return Math.max(0, cuadrilla.getCapacidadHoras() - calcularHorasAsignadas());
    }
    public int getId() { return id; }
    public Cuadrilla getCuadrilla() { return cuadrilla; }
    public EstadoJornada getEstado() { return estado; }
    public List<OrdenDeServicio> getOrdenes() { return Collections.unmodifiableList(new ArrayList<>(ordenes)); }
    public Map<String, ResultadoVisita> getResultados() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(resultados));
    }
}
