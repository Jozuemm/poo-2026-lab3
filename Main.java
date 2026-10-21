import modelo.SistemaServicios;
import controlador.Controller;
import vista.Vista;

public class Main {
    public static void main(String[] args) {
        SistemaServicios modelo = new SistemaServicios();
        Controller controlador = new Controller(modelo);
        Vista vista = new Vista(controlador);
        vista.ejecutar();
    }
}
