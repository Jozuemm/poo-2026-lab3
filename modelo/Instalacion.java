package modelo;
public class Instalacion extends OrdenDeServicio {
    private final int cantidadPuntos;
    public Instalacion(String codigo, String cliente, String direccion,
                       double horas, Zona zona, int puntos) {
        super(codigo, cliente, direccion, horas, zona);
        Validacion.exigir(puntos > 0, "La cantidad de puntos debe ser un entero positivo.");
        cantidadPuntos = puntos;
    }
    public int getCantidadPuntos() { return cantidadPuntos; }
    public double calcularHorasFacturables() {
        return Math.ceil(Math.max(getHorasEstimadas(), cantidadPuntos * 0.75));
    }
    @Override public double calcularCosto() {
        return Validacion.dinero(calcularTarifaBase() + 90 * calcularHorasFacturables());
    }
    @Override public int estimarDiasAtencion() { return getZona() == Zona.METROPOLITANA ? 3 : 5; }
    @Override public int getMaxVisitas() { return 3; }
    @Override public String getCategoria() { return "Instalación"; }
}
