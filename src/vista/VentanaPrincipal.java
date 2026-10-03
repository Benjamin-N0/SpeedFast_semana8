package vista;

import controlador.ControladorPedidos;

import javax.swing.*;

public class VentanaPrincipal extends JFrame{
    private JPanel panel1;
    private JButton btnPedidos;
    private JButton btnRepartidores;
    private JButton btnEntregas;

    private ControladorPedidos controlador;

    public VentanaPrincipal(ControladorPedidos controlador) {
        this.controlador = controlador;

        //configuracion de ventana principal
        setTitle("SPEEDFAST v0.8.3");
        setContentPane(panel1);
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //boton para abrir ventana de Pedidos
        btnPedidos.addActionListener(e -> {VentanaPedidos ventana = new VentanaPedidos(controlador);
            ventana.setVisible(true);
        });

        //boton para abrir ventana de Repartidores
        btnRepartidores.addActionListener(e -> {VentanaRepartidores ventana = new VentanaRepartidores();
            ventana.setVisible(true);
        });

        //boton para abrir ventana de entregas
        btnEntregas.addActionListener(e -> {VentanaEntregas ventana = new VentanaEntregas();
            ventana.setVisible(true);
        });
    }
}
