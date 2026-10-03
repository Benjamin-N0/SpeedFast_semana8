package vista;

import controlador.ControladorPedidos;
import dao.PedidoDAO;
import dao.impl.PedidoDAOImpl;
import model.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class VentanaPedidos extends JFrame {

    private JPanel panel1;
    private JTextField txtDireccionAgregar;
    private JComboBox cbTipoAgregar;
    private JButton agregarButton;
    private JTextField txtIDeliminar;
    private JButton btnEliminar;
    private JButton listadoButton;
    private JTable table1;
    private JLabel txtID;
    private JTextField txtDireccion;
    private JComboBox cbTipo;
    private JComboBox cbEstado;
    private JButton editarButton;
    private JLabel lblTipo;

    private ControladorPedidos controlador;
    private PedidoDAO pedidoDAO;
    private DefaultTableModel modeloTabla;


    public VentanaPedidos(ControladorPedidos controlador) {

        this.controlador = controlador;

        setTitle("Gestión de Pedidos");
        setContentPane(panel1);
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        pedidoDAO = new PedidoDAOImpl();

        // CARGAMOS LOS TIPOS DE PEDIDO

        cbTipoAgregar.addItem("COMIDA");
        cbTipoAgregar.addItem("ENCOMIENDA");
        cbTipoAgregar.addItem("EXPRESS");

        cbTipo.addItem("COMIDA");
        cbTipo.addItem("ENCOMIENDA");
        cbTipo.addItem("EXPRESS");

        // CARGAMOS LOS ESTADOS PERMITIDOS

        cbEstado.addItem("PENDIENTE");
        cbEstado.addItem("EN_REPARTO");
        cbEstado.addItem("ENTREGADO");

        // CONFIGURAMOS LA TABLA

        modeloTabla = new DefaultTableModel();

        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Direccion");
        modeloTabla.addColumn("Tipo");
        modeloTabla.addColumn("Estado");

        table1.setModel(modeloTabla);

        // EVENTOS DE LOS BOTONES

        agregarButton.addActionListener(e -> agregarPedido());
        listadoButton.addActionListener(e -> listarPedidos());
        editarButton.addActionListener(e -> actualizarPedido());
        btnEliminar.addActionListener(e -> eliminarPedido());

        table1.getSelectionModel().addListSelectionListener(
                e -> cargarDatosSeleccionados()
        );
        listarPedidos();
    }

    // AGREGAR PEDIDO

    private void agregarPedido() {

        String direccion = txtDireccionAgregar.getText().trim();

        if (direccion.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese la direccion del pedido"
            );
            return;
        }

        String tipo = cbTipoAgregar.getSelectedItem().toString();

        Pedido pedido = new Pedido(
                0,
                direccion,
                tipo,
                "PENDIENTE"
        );

        pedidoDAO.guardar(pedido);
        controlador.agregarPedido(pedido);

        JOptionPane.showMessageDialog(
                this,
                "Pedido registrado correctamente\n"
                        + "ID registrado: "
                        + pedido.getId()
        );

        txtDireccionAgregar.setText("");
        listarPedidos();
    }

    // LISTAR PEDIDOS

    private void listarPedidos() {

        modeloTabla.setRowCount(0);

        List<Pedido> pedidos = pedidoDAO.listarTodos();

        for (Pedido pedido : pedidos) {

            Object[] fila = {
                    pedido.getId(),
                    pedido.getDireccion(),
                    pedido.getTipo(),
                    pedido.getEstado()
            };
            modeloTabla.addRow(fila);
        }
    }

    // CARGAR DATOS SELECCIONADOS

    private void cargarDatosSeleccionados() {

        int filaSeleccionada = table1.getSelectedRow();

        if (filaSeleccionada == -1) {
            return;
        }

        String id = table1.getValueAt(filaSeleccionada, 0).toString();
        String direccion = table1.getValueAt(filaSeleccionada, 1).toString();
        String tipo = table1.getValueAt(filaSeleccionada, 2).toString();
        String estado = table1.getValueAt(filaSeleccionada, 3).toString();

        txtID.setText(id);
        txtDireccion.setText(direccion);
        cbTipo.setSelectedItem(tipo);

        cbEstado.setSelectedItem(estado);
        txtIDeliminar.setText(id);
    }

    // ACTUALIZAR PEDIDO

    private void actualizarPedido() {

        String idTexto = txtID.getText().trim();

        String direccion = txtDireccion.getText().trim();

        if (idTexto.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un pedido de la tabla"
            );
            return;
        }

        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese la dirección del pedido"
            );
            return;
        }

        int id = Integer.parseInt(idTexto);

        String tipo = cbTipo.getSelectedItem().toString();

        String estado = cbEstado.getSelectedItem().toString();

        Pedido pedido = new Pedido(
                id,
                direccion,
                tipo,
                estado
        );

        pedidoDAO.actualizar(pedido);
        JOptionPane.showMessageDialog(
                this,
                "Pedido actualizado correctamente"
        );

        txtID.setText("");
        txtDireccion.setText("");
        cbTipo.setSelectedIndex(0);
        cbEstado.setSelectedIndex(0);

        listarPedidos();
    }

    // ELIMINAR PEDIDO

    private void eliminarPedido() {

        String idTexto = txtIDeliminar.getText().trim();

        if (idTexto.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese o seleccione el ID del pedido"
            );
            return;
        }

        int id;

        try { id = Integer.parseInt(idTexto);

            if (id <= 0) { JOptionPane.showMessageDialog( this, "El ID debe ser mayor que 0" );
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog( this, "El ID no es valido" );
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Esta seguro de eliminar el pedido #"
                        + id
                        + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        boolean eliminado = pedidoDAO.eliminar(id);
        if (eliminado) {
            JOptionPane.showMessageDialog(this,
                    "Pedido eliminado correctamente"
            );
            txtIDeliminar.setText("");
            listarPedidos();
        }else {

            JOptionPane.showMessageDialog( this,
                    "No se pudo eliminar el pedido.\n" + "Puede que no exista o tenga una entrega asociada." );
        }
    }
}
