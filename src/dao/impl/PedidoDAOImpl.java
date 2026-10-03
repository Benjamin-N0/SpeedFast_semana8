package dao.impl;
import dao.PedidoDAO;
import model.Pedido;
import util.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {
    @Override
    public void guardar(Pedido pedido) {

        String sql = "INSERT INTO pedido (direccion, tipo, estado) VALUES (?, ?, ?)";

        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet resultado = null;

        try{
            conexion = ConexionDB.conectar();

            ps = conexion.prepareStatement(
                    sql,
                    PreparedStatement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, pedido.getDireccion());
            ps.setString(2, pedido.getTipo());
            ps.setString(3, pedido.getEstado());

            ps.executeUpdate();

            resultado = ps.getGeneratedKeys();

            if(resultado.next()) {

                int idGenerado = resultado.getInt(1);

                pedido.setId(idGenerado);

                System.out.println(
                        "Pedido guardado correctamente. ID: " + idGenerado
                );
            }
        }catch (SQLException e) {

            System.out.println("Error al guardar el pedido");
            System.out.println(e.getMessage());

        }finally {

            try{
                if (resultado != null) {
                    resultado.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar el resultado");
            }

            try{
                if (ps != null) {
                    ps.close();
                }
            }catch (SQLException e) {
                System.out.println("Error al cerrar el PreparedStatement");
            }

            try{
                if (conexion != null) {
                    conexion.close();
                }
            }catch (SQLException e) {
                System.out.println("Error al cerrar la conexión");
            }
        }
    }
    @Override
    public List<Pedido> listarTodos() {

        List<Pedido> pedidos = new ArrayList<>();

        String sql = "SELECT id, direccion, tipo, estado FROM pedido";

        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet resultado = null;

        try {

            conexion = ConexionDB.conectar();
            ps = conexion.prepareStatement(sql);
            resultado = ps.executeQuery();

            while (resultado.next()) {

                int id = resultado.getInt("id");
                String direccion = resultado.getString("direccion");
                String tipo = resultado.getString("tipo");
                String estado = resultado.getString("estado");

                Pedido pedido = new Pedido(
                        id,
                        direccion,
                        tipo,
                        estado
                );
                pedidos.add(pedido);
            }
        }catch (SQLException e) {

            System.out.println("Error al listar los pedidos");
            System.out.println(e.getMessage());

        }finally {

            try {
                if (resultado != null) {
                    resultado.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar el resultado");
            }

            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar la PreparedStatement");
            }

            try {
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexion");
            }
        }
        return pedidos;
    }
    @Override
    public void actualizarEstado(int idPedido, String nuevoEstado) {

        String sql = "UPDATE pedido SET estado = ? WHERE id = ?";

        Connection conexion = null;
        PreparedStatement ps = null;

        try {

            conexion = ConexionDB.conectar();

            ps = conexion.prepareStatement(sql);

            ps.setString(1, nuevoEstado);

            ps.setInt(2, idPedido);

            ps.executeUpdate();

            System.out.println("Estado del pedido actualizado");

        } catch (SQLException e) {

            System.out.println("Error al actualizar el estado del pedido");
            System.out.println(e.getMessage());

        }finally {

            try{
                if (ps != null) {
                    ps.close();
                }
            }catch (SQLException e) {
                System.out.println("Error al cerrar la PreparedStatement");
            }
            try{
                if (conexion != null) {
                    conexion.close();
                }
            }catch (SQLException e) {
                System.out.println("Error al cerrar la conexión");
            }
        }
    }
    @Override
    public void actualizar(Pedido pedido) {

        String sql = "UPDATE pedido SET direccion = ?, tipo = ?, estado = ? WHERE id = ?";
        Connection conexion = null;
        PreparedStatement ps = null;

        try {
            conexion = ConexionDB.conectar();
            ps = conexion.prepareStatement(sql);

            ps.setString(1, pedido.getDireccion());
            ps.setString(2, pedido.getTipo());
            ps.setString(3, pedido.getEstado());
            ps.setInt(4, pedido.getId());
            ps.executeUpdate();

            System.out.println("Pedido actualizado correctamente");

        }catch (SQLException e){

            System.out.println("Error al actualizar el pedido");
            System.out.println(e.getMessage());

        }finally {
            try {
                if (ps != null){
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar el PreparedStatement");
            }
            try {
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexion");
            }
        }
    }

@Override
public boolean eliminar(int id) {
    String sql = "DELETE FROM pedido WHERE id = ?";
    Connection conexion = null;
    PreparedStatement ps = null;
    try {
        conexion = ConexionDB.conectar();
        ps = conexion.prepareStatement(sql);
        ps.setInt(1, id);
        int filasAfectadas = ps.executeUpdate();

        if (filasAfectadas > 0) {
            System.out.println("Pedido eliminado correctamente"
            );
            return true;
        }

        System.out.println("No se encontró el pedido");
        return false;

    }catch (SQLException e){

        System.out.println("Error al eliminar el pedido");
        System.out.println(e.getMessage());
        return false;

    }finally {
        try {
            if (ps != null){
                ps.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar el PreparedStatement");
        }
        try {
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexion");
            }
        }
    }
}