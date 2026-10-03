package vista;
import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import dao.impl.EntregaDAOImpl;
import dao.impl.PedidoDAOImpl;
import dao.impl.RepartidorDAOImpl;
import model.Entrega;
import model.Pedido;
import model.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class VentanaEntregas extends JFrame {
    private JPanel panel1;
    private JLabel lblRepartidor;
    private JLabel lblPedido;
    private JTextField txtId;
    private JButton btnEliminar;
    private JButton btnListar;
    private JTable table1;
    private JButton btnActualizar;
    private JLabel lblIdActualizar;
    private JComboBox<Pedido> cbIdPedidoActualizar;
    private JComboBox<Repartidor> cbIdRepartidorActualizar;
    private JComboBox<Repartidor> cbRepartidor;
    private JComboBox<Pedido> cbPedido;
    private JTextField txtFecha;
    private JTextField txtHora;
    private JLabel lblFecha;
    private JLabel lblHora;
    private JButton btnRegistrar;
    private JTextField txtFechaRegistro;
    private JTextField txtHoraRegistro;
    private JLabel lblId;

    private EntregaDAO entregaDAO;
    private PedidoDAO pedidoDAO;
    private RepartidorDAO repartidorDAO;
    private DefaultTableModel modeloTabla;

    public VentanaEntregas() {

        // Configuración de la ventana
        setTitle("Gestion de Entregas");
        setContentPane(panel1);
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        entregaDAO = new EntregaDAOImpl();
        pedidoDAO = new PedidoDAOImpl();
        repartidorDAO = new RepartidorDAOImpl();

        // Modelo Tabla
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("ID Pedido");
        modeloTabla.addColumn("ID Repartidor");
        modeloTabla.addColumn("Fecha");
        modeloTabla.addColumn("Hora");

        table1.setModel(modeloTabla);

        cargarPedidos();
        cargarRepartidores();
        cargarPedidosActualizar();
        cargarRepartidoresActualizar();

        // Eventos de los botones
        btnRegistrar.addActionListener(e -> registrarEntrega());
        btnListar.addActionListener(e -> listarEntregas());
        btnActualizar.addActionListener(e -> actualizarEntrega());
        btnEliminar.addActionListener(e -> eliminarEntrega());

        table1.getSelectionModel().addListSelectionListener(
                e -> {
                    if (!e.getValueIsAdjusting()) {
                        cargarDatosSeleccionados();
                    }
                }
        );

        listarEntregas();
    }

    // CARGAR PEDIDOS PARA REGISTRO

    private void cargarPedidos() {
        cbPedido.removeAllItems();
        List<Pedido> pedidos = pedidoDAO.listarTodos();

        for (Pedido pedido : pedidos) {
            if ("PENDIENTE".equalsIgnoreCase(pedido.getEstado())) {
                cbPedido.addItem(pedido);
            }
        }
    }

    // CARGAR PEDIDOS PARA ACTUALIZACION

    private void cargarPedidosActualizar() {
        cbIdPedidoActualizar.removeAllItems();
        List<Pedido> pedidos = pedidoDAO.listarTodos();

        for (Pedido pedido : pedidos) {
            cbIdPedidoActualizar.addItem(pedido);
        }
    }

    private void seleccionarPedido(int idPedido) {
        for (int i = 0; i < cbIdPedidoActualizar.getItemCount(); i++) {
            Pedido pedido = (Pedido) cbIdPedidoActualizar.getItemAt(i);
            if (pedido != null && pedido.getId() == idPedido) {
                cbIdPedidoActualizar.setSelectedIndex(i);
                return;
            }
        }
    }

    // CARGAR REPARTIDORES PARA REGISTRO

    private void cargarRepartidores() {
        cbRepartidor.removeAllItems();
        List<Repartidor> repartidores = repartidorDAO.listarTodos();

        for (Repartidor repartidor : repartidores) {
            cbRepartidor.addItem(repartidor);
        }
    }

    // CARGAR REPARTIDORES PARA ACTUALIZACION

    private void cargarRepartidoresActualizar() {
        cbIdRepartidorActualizar.removeAllItems();
        List<Repartidor> repartidores = repartidorDAO.listarTodos();

        for (Repartidor repartidor : repartidores) {
            cbIdRepartidorActualizar.addItem(repartidor);
        }
    }

    private void seleccionarRepartidor(int idRepartidor) {
        for (int i = 0; i < cbIdRepartidorActualizar.getItemCount(); i++) {
            Repartidor repartidor = (Repartidor) cbIdRepartidorActualizar.getItemAt(i);
            if (repartidor != null && repartidor.getId() == idRepartidor) {
                cbIdRepartidorActualizar.setSelectedIndex(i);
                return;
            }
        }
    }

    // VALIDA FECHA/HORA
    private boolean validarFechaHora(String fecha, String hora) {
        try {
            LocalDate.parse(fecha);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(
                    this, "La fecha debe tener el formato yyyy-MM-dd\nEjemplo: 2026-10-02"
            );
            return false;
        }

        try {
            LocalTime.parse(hora);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(
                    this, "La hora debe tener el formato HH:mm\nEjemplo: 12:20"
            );
            return false;
        }
        return true;
    }

    // REGISTRAR ENTREGA

    private void registrarEntrega() {

        Pedido pedidoSeleccionado = (Pedido) cbPedido.getSelectedItem();
        Repartidor repartidorSeleccionado = (Repartidor) cbRepartidor.getSelectedItem();
        String fecha = txtFechaRegistro.getText().trim();
        String hora = txtHoraRegistro.getText().trim();

        if (pedidoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido");
            return;
        }

        if (repartidorSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un repartidor");
            return;
        }

        if (fecha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la fecha");
            return;
        }

        if (hora.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la hora");
            return;
        }

        if (!validarFechaHora(fecha, hora)) {
            return;
        }

        Entrega entrega = new Entrega(
                0,
                pedidoSeleccionado.getId(),
                repartidorSeleccionado.getId(),
                fecha,
                hora
        );

        boolean guardado = entregaDAO.guardar(entrega);

        if (!guardado) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo registrar la entrega.\n" + "Revise los datos e intente nuevamente."
            );
            return;
        }

        pedidoDAO.actualizarEstado(
                pedidoSeleccionado.getId(),
                "EN_REPARTO"
        );

        JOptionPane.showMessageDialog(
                this, "Entrega registrada correctamente"
        );

        txtFechaRegistro.setText("");
        txtHoraRegistro.setText("");

        cargarPedidos();
        cargarPedidosActualizar();
        listarEntregas();
    }

    // LISTAR ENTREGAS

    private void listarEntregas() {
        modeloTabla.setRowCount(0);
        List<Entrega> entregas = entregaDAO.listarTodos();

        for (Entrega entrega : entregas) {
            Object[] fila = {
                    entrega.getId(),
                    entrega.getIdPedido(),
                    entrega.getIdRepartidor(),
                    entrega.getFecha(),
                    entrega.getHora()
            };
            modeloTabla.addRow(fila);
        }
        table1.clearSelection();
    }

    // CARGAR DATOS DE LA TABLA

    private void cargarDatosSeleccionados() {
        int filaSeleccionada = table1.getSelectedRow();

        if (filaSeleccionada != -1) {
            try {
                int id = Integer.parseInt(table1.getValueAt(filaSeleccionada, 0).toString());
                int idPedido = Integer.parseInt(table1.getValueAt(filaSeleccionada, 1).toString());
                int idRepartidor = Integer.parseInt(table1.getValueAt(filaSeleccionada, 2).toString());
                String fecha = table1.getValueAt(filaSeleccionada, 3).toString();
                String hora = table1.getValueAt(filaSeleccionada, 4).toString();

                lblIdActualizar.setText(String.valueOf(id));
                txtFecha.setText(fecha);
                txtHora.setText(hora);
                txtId.setText(String.valueOf(id));

                seleccionarPedido(idPedido);
                seleccionarRepartidor(idRepartidor);

            } catch (NumberFormatException e) {
                System.err.println("Error en los IDs de la tabla: " + e.getMessage());
            }
        } else {
            lblIdActualizar.setText("");
            txtFecha.setText("");
            txtHora.setText("");
            txtId.setText("");

            cbIdPedidoActualizar.setSelectedIndex(-1);
            cbIdRepartidorActualizar.setSelectedIndex(-1);
        }
    }

    // ACTUALIZAR ENTREGA

    private void actualizarEntrega() {

        String idTexto = lblIdActualizar.getText().trim();
        String fecha = txtFecha.getText().trim();
        String hora = txtHora.getText().trim();

        if (idTexto.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una entrega de la tabla"
            );
            return;
        }

        Pedido pedidoSeleccionado = (Pedido) cbIdPedidoActualizar.getSelectedItem();
        Repartidor repartidorSeleccionado = (Repartidor) cbIdRepartidorActualizar.getSelectedItem();

        if (pedidoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un pedido");
            return;
        }

        if (repartidorSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un repartidor");
            return;
        }

        if (fecha.isEmpty() || hora.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete la fecha y la hora");
            return;
        }

        if (!validarFechaHora(fecha, hora)) {
            return;
        }

        int id;

        try {
            id = Integer.parseInt(idTexto);
            if (id <= 0) {
                JOptionPane.showMessageDialog(this, "El ID de la entrega debe ser mayor que 0");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID de la entrega no es valido");
            return;
        }

        int idPedido = pedidoSeleccionado.getId();
        int idRepartidor = repartidorSeleccionado.getId();

        Entrega entrega = new Entrega(
                id,
                idPedido,
                idRepartidor,
                fecha,
                hora
        );

        boolean actualizado = entregaDAO.actualizar(entrega);

        if (actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Entrega actualizada correctamente"
            );

            lblIdActualizar.setText("");
            txtFecha.setText("");
            txtHora.setText("");
            txtId.setText("");

            cargarPedidos();
            cargarPedidosActualizar();
            listarEntregas();

        } else {
            JOptionPane.showMessageDialog(
                    this, "No se pudo actualizar la entrega.\nPuede que la entrega no exista."
            );
        }
    }

    // ELIMINAR ENTREGA

    private void eliminarEntrega() {

        String idTexto = txtId.getText().trim();

        if (idTexto.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Ingrese o seleccione el ID de la entrega"
            );
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idTexto);
            if (id <= 0) {
                JOptionPane.showMessageDialog(this, "El ID debe ser mayor que 0");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID no es valido");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this, "¿Esta seguro de eliminar la entrega #" + id + "?",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        boolean eliminado = entregaDAO.eliminar(id);

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this, "Entrega eliminada correctamente"
            );

            txtId.setText("");
            lblIdActualizar.setText("");
            txtFecha.setText("");
            txtHora.setText("");

            cargarPedidos();
            cargarPedidosActualizar();
            listarEntregas();

        } else {
            JOptionPane.showMessageDialog(
                    this, "No se pudo eliminar la entrega.\nPuede que la entrega no exista."
            );
        }
    }
}