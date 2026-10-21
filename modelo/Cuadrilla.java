package modelo;
public class Cuadrilla {
    private final String codigo;
    private final double capacidadHoras;
    private final boolean certificadaRefrigeracion;
    public Cuadrilla(String codigo, double capacidadHoras, boolean certificada) {
        this.codigo = Validacion.texto(codigo, "Código de cuadrilla");
        this.capacidadHoras = Validacion.positivo(capacidadHoras, "Capacidad de la cuadrilla");
        certificadaRefrigeracion = certificada;
    }
    public String getCodigo() { return codigo; }
    public double getCapacidadHoras() { return capacidadHoras; }
    public boolean isCertificadaRefrigeracion() { return certificadaRefrigeracion; }
}
