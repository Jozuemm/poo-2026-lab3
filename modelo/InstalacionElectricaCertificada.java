package modelo;
public class InstalacionElectricaCertificada extends Instalacion {
    private final double valorMateriales;
    public InstalacionElectricaCertificada(String codigo, String cliente, String direccion,
            double horas, Zona zona, int puntos, double materiales) {
        super(codigo, cliente, direccion, horas, zona, puntos);
        valorMateriales = Validacion.positivo(materiales, "Valor de materiales");
    }
    public double getValorMateriales() { return valorMateriales; }
    @Override public double calcularCosto() {
        return Validacion.dinero(super.calcularCosto() + 120 + valorMateriales * 0.05);
    }
    @Override public int estimarDiasAtencion() { return super.estimarDiasAtencion() + 2; }
    @Override public int getMaxVisitas() { return 2; }
    @Override public String getCategoria() { return "Instalación eléctrica certificada"; }
}
