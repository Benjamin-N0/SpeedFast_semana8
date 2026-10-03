package vista;

import dao.RepartidorDAO;
import dao.impl.RepartidorDAOImpl;
import model.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class VentanaRepartidores extends JFrame {

    private JPanel panel1;
    private JTextField txtNombreRepartidor;
    private JLabel txtID;
    private JTextField txtNombre;
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnListar;
    private JTable table1;

    private RepartidorDAO repartidorDAO;
    private DefaultTableModel modeloTabla;

    public VentanaRepartidores() {

        setTitle("Gestión de Repartidores");
        setContentPane(panel1);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        repartidorDAO = new RepartidorDAOImpl();

        // CONFIGURAR TABLA

        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        table1.setModel(modeloTabla);

        // EVENTOS DE LOS BOTONES

        btnAgregar.addActionListener(e -> agregarRepartidor());
        btnEditar.addActionListener(e -> actualizarRepartidor());
        btnEliminar.addActionListener(e -> eliminarRepartidor());
        btnListar.addActionListener(e -> listarRepartidores());

        table1.getSelectionModel().addListSelectionListener(
                e -> cargarDatosSeleccionados()
        );
        listarRepartidores();
    }

    // AGREGAR REPARTIDOR

    private void agregarRepartidor() {

        String nombre = txtNombreRepartidor.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Ingrese el nombre del repartidor"
            );
            return;
        }

        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {

            JOptionPane.showMessageDialog(
                    this, "El nombre solo puede contener letras y espacios"
            );
            return;
        }

        Repartidor repartidor = new Repartidor(0, nombre);

        repartidorDAO.guardar(repartidor);
        JOptionPane.showMessageDialog(
                this, "Repartidor registrado correctamente"
        );
        txtNombreRepartidor.setText("");
        listarRepartidores();
    }

    // LISTAR REPARTIDORES

    private void listarRepartidores() {

        modeloTabla.setRowCount(0);

        List<Repartidor> repartidores = repartidorDAO.listarTodos();

        for (Repartidor repartidor : repartidores) {

            Object[] fila = {
                    repartidor.getId(),
                    repartidor.getNombre()
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
        String nombre = table1.getValueAt(filaSeleccionada, 1).toString();

        txtID.setText(id);
        txtNombre.setText(nombre);
    }

    // ACTUALIZAR REPARTIDOR

    private void actualizarRepartidor() {

        String idTexto = txtID.getText().trim();

        String nombre = txtNombre.getText().trim();

        if (idTexto.isEmpty()) {

            JOptionPane.showMessageDialog(this, "Seleccione un repartidor de la tabla");
            return;
        }

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre del repartidor"
            );
            return;
        }

        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {

            JOptionPane.showMessageDialog(this, "El nombre solo puede contener letras y espacios"
            );
            return;
        }

        int id = Integer.parseInt(idTexto);
        Repartidor repartidor = new Repartidor(id, nombre);

        repartidorDAO.actualizar(repartidor);

        JOptionPane.showMessageDialog(this, "Repartidor actualizado correctamente"
        );

        txtNombre.setText("");
        listarRepartidores();
    }

    // ELIMINAR REPARTIDOR

    private void eliminarRepartidor() {

        String idTexto = txtID.getText().trim();

        if (idTexto.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un repartidor de la tabla"
            );

            return;
        }

        int id;

        try {

            id = Integer.parseInt(idTexto);

            if (id <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "El ID debe ser mayor que 0"
                );

                return;
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "El ID no es valido"
            );

            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Esta seguro de eliminar el repartidor #" + id + "?",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        boolean eliminado = repartidorDAO.eliminar(id);

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this, "Repartidor eliminado correctamente"
            );

            txtID.setText("");
            txtNombre.setText("");

            listarRepartidores();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar el repartidor.\n" + "Puede que no exista o que tenga una entrega asociada."
            );
        }
    }
}
