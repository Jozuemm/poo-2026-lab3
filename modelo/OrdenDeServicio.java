package modelo;

/** Datos y ciclo de vida comunes. Las subclases resuelven sus propias políticas. */
public abstract class OrdenDeServicio {
    private final String codigo, nombreCliente, direccion;
    private final double horasEstimadas;
    private final Zona zona;
    private EstadoOrden estado = EstadoOrden.PENDIENTE;
    private int visitasRealizadas;
    private double ingresoRegistro, ingresoVisitasAdicionales;

    protected OrdenDeServicio(String codigo, String cliente, String direccion,
                              double horas, Zona zona) {
        this.codigo = Validacion.texto(codigo, "Código");
        this.nombreCliente = Validacion.texto(cliente, "Cliente");
        this.direccion = Validacion.texto(direccion, "Dirección");
        this.horasEstimadas = Validacion.positivo(horas, "Horas");
        Validacion.exigir(zona != null, "Seleccione una zona.");
        this.zona = zona;
    }
    public abstract double calcularCosto();
    public abstract int estimarDiasAtencion();
    public abstract int getMaxVisitas();
    public abstract String getCategoria();
    public boolean esCompatible(Cuadrilla cuadrilla) { return cuadrilla != null; }
    protected double calcularTarifaBase() { return zona == Zona.METROPOLITANA ? 100 : 175; }

    // Acceso de paquete: la vista y el controlador no cambian el ciclo de vida.
    void cobrarRegistro() { ingresoRegistro = Validacion.dinero(calcularCosto()); }
    void marcarAsignada() {
        Validacion.exigir(estado == EstadoOrden.PENDIENTE, "La orden no está pendiente.");
        estado = EstadoOrden.ASIGNADA;
    }
    void retirarAsignacion() {
        Validacion.exigir(estado == EstadoOrden.ASIGNADA, "La orden no está asignada.");
        estado = EstadoOrden.PENDIENTE;
    }
    void validarInicio() {
        Validacion.exigir(estado == EstadoOrden.ASIGNADA, "La orden no está asignada.");
        Validacion.exigir(visitasRealizadas < getMaxVisitas(), "La orden agotó sus visitas.");
    }
    void iniciarVisita() {
        validarInicio();
        if (visitasRealizadas >= 1) ingresoVisitasAdicionales += 40;
        visitasRealizadas++;
        estado = EstadoOrden.EN_ATENCION;
    }
    void registrarResultado(ResultadoVisita resultado) {
        Validacion.exigir(estado == EstadoOrden.EN_ATENCION, "La orden no está en atención.");
        Validacion.exigir(resultado != null, "Seleccione un resultado.");
        if (resultado == ResultadoVisita.COMPLETADA) estado = EstadoOrden.COMPLETADA;
        else estado = visitasRealizadas >= getMaxVisitas()
                ? EstadoOrden.CANCELADA : EstadoOrden.PENDIENTE;
    }
    public String getCodigo() { return codigo; }
    public String getNombreCliente() { return nombreCliente; }
    public String getDireccion() { return direccion; }
    public double getHorasEstimadas() { return horasEstimadas; }
    public Zona getZona() { return zona; }
    public EstadoOrden getEstado() { return estado; }
    public int getVisitasRealizadas() { return visitasRealizadas; }
    public double getIngresoRegistro() { return ingresoRegistro; }
    public double getIngresoVisitasAdicionales() { return ingresoVisitasAdicionales; }
}
