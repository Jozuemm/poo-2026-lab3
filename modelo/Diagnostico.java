package modelo;
public class Diagnostico extends OrdenDeServicio {
    public Diagnostico(String codigo, String cliente, String direccion, double horas, Zona zona) {
        super(codigo, cliente, direccion, horas, zona);
        Validacion.exigir(horas <= 1.5, "Un diagnóstico no puede superar 1.5 horas.");
    }
    @Override public double calcularCosto() { return getZona() == Zona.METROPOLITANA ? 150 : 225; }
    @Override public int estimarDiasAtencion() { return getZona() == Zona.METROPOLITANA ? 1 : 2; }
    @Override public int getMaxVisitas() { return 3; }
    @Override public String getCategoria() { return "Diagnóstico"; }
}
