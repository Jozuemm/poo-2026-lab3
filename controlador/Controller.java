package controlador;
import modelo.*;
import java.util.*;

/** Recibe acciones de la vista y delega en el modelo. No usa Scanner ni imprime. */
public class Controller {
    private final SistemaServicios sistema;
    public Controller(SistemaServicios sistema) { this.sistema = Objects.requireNonNull(sistema); }
    public OrdenDeServicio crearPropuesta(int tipo, String codigo, String cliente, String direccion,
            double horas, Zona zona, int puntos, double materiales, int btu) {
        // Seleccionar la clase al construir es válido. Las reglas posteriores son polimórficas.
        switch (tipo) {
            case 1: return new Diagnostico(codigo, cliente, direccion, horas, zona);
            case 2: return new Instalacion(codigo, cliente, direccion, horas, zona, puntos);
            case 3: return new InstalacionElectricaCertificada(codigo, cliente, direccion, horas, zona, puntos, materiales);
            case 4: return new MantenimientoAire(codigo, cliente, direccion, horas, zona, btu);
            default: throw new IllegalArgumentException("Categoría inválida.");
        }
    }
    public Cotizacion cotizar(OrdenDeServicio propuesta) { return sistema.cotizar(propuesta); }
    public void registrarOrden(OrdenDeServicio orden) { sistema.registrarOrden(orden); }
    public void registrarCuadrilla(String codigo, double capacidad, boolean certificada) { sistema.registrarCuadrilla(codigo, capacidad, certificada); }
    public Jornada crearJornada(String codigo) { return sistema.crearJornada(codigo); }
    public void asignarOrden(int id, String codigo) { sistema.asignarOrden(id, codigo); }
    public void retirarOrden(int id, String codigo) { sistema.retirarOrden(id, codigo); }
    public void iniciarJornada(int id) { sistema.iniciarJornada(id); }
    public void registrarResultado(int id, String codigo, ResultadoVisita resultado) { sistema.registrarResultado(id, codigo, resultado); }
    public List<OrdenDeServicio> consultarOrdenes() { return sistema.consultarOrdenes(); }
    public List<Cuadrilla> consultarCuadrillas() { return sistema.consultarCuadrillas(); }
    public List<Jornada> consultarJornadasAbiertas() { return sistema.consultarJornadasAbiertas(); }
    public Map<String, Map<EstadoOrden, Integer>> contarOrdenes() { return sistema.contarOrdenes(); }
    public double getIngresosRegistro() { return sistema.getIngresosRegistro(); }
    public double getIngresosVisitas() { return sistema.getIngresosVisitas(); }
    public Map<String, Double> ingresosPorCategoria() { return sistema.ingresosPorCategoria(); }
}
