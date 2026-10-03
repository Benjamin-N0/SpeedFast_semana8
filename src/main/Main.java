package main;

import controlador.ControladorPedidos;
import vista.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {

        ControladorPedidos controlador = new ControladorPedidos();
        VentanaPrincipal ventana = new VentanaPrincipal(controlador);
        ventana.setVisible(true);
    }
}
