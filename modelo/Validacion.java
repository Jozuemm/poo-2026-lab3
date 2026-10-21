package modelo;

/** Utilidad interna: todas las validaciones ocurren antes de modificar datos. */
final class Validacion {
    private Validacion() { }
    static String texto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty())
            throw new IllegalArgumentException(campo + " no puede estar vacío.");
        return valor.trim();
    }
    static double positivo(double valor, String campo) {
        if (!Double.isFinite(valor) || valor <= 0)
            throw new IllegalArgumentException(campo + " debe ser positivo y finito.");
        return valor;
    }
    static void exigir(boolean condicion, String mensaje) {
        if (!condicion) throw new IllegalArgumentException(mensaje);
    }
    static double dinero(double valor) {
        exigir(Double.isFinite(valor) && valor >= 0 && valor <= Long.MAX_VALUE / 100.0,
                "El monto calculado está fuera del rango admitido.");
        return Math.round(valor * 100.0) / 100.0;
    }
}
