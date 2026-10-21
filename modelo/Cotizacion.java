package modelo;
/** Resultado inmutable de una consulta; no es un registro de cobro. */
public class Cotizacion {
    private final String categoria;
    private final double costo;
    private final int dias;
    public Cotizacion(OrdenDeServicio propuesta) {
        Validacion.exigir(propuesta != null, "Faltan los datos de la cotización.");
        categoria = propuesta.getCategoria();
        costo = Validacion.dinero(propuesta.calcularCosto());
        dias = propuesta.estimarDiasAtencion();
    }
    public String getCategoria() { return categoria; }
    public double getCosto() { return costo; }
    public int getDias() { return dias; }
}
