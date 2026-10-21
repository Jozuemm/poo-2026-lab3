package modelo;
public class MantenimientoAire extends OrdenDeServicio {
    private final int capacidadBTU;
    public MantenimientoAire(String codigo, String cliente, String direccion,
                             double horas, Zona zona, int btu) {
        super(codigo, cliente, direccion, horas, zona);
        Validacion.exigir(zona == Zona.METROPOLITANA, "El mantenimiento solo admite zona metropolitana.");
        Validacion.exigir(btu >= 9000 && btu <= 60000, "Los BTU deben estar entre 9000 y 60000.");
        capacidadBTU = btu;
    }
    @Override public double calcularCosto() {
        return Validacion.dinero(calcularTarifaBase() + 110 * getHorasEstimadas() + 80);
    }
    @Override public int estimarDiasAtencion() { return 2; }
    @Override public int getMaxVisitas() { return 1; }
    @Override public String getCategoria() { return "Mantenimiento de aire acondicionado"; }
    @Override public boolean esCompatible(Cuadrilla cuadrilla) {
        return cuadrilla != null && cuadrilla.isCertificadaRefrigeracion();
    }
    public int getCapacidadBTU() { return capacidadBTU; }
}
