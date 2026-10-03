package dao.impl;

import dao.EntregaDAO;
import model.Entrega;
import util.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAOImpl implements EntregaDAO {

    // =========================================================
    // GUARDAR UNA ENTREGA
    // =========================================================
    @Override
    public boolean guardar(Entrega entrega) {

        String sql = "INSERT INTO entrega " +
                "(id_pedido, id_repartidor, fecha, hora) " +
                "VALUES (?, ?, ?, ?)";

        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet clavesGeneradas = null;

        try {

            conexion = ConexionDB.conectar();
            ps = conexion.prepareStatement(
                    sql,
                    java.sql.Statement.RETURN_GENERATED_KEYS
            );

            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
            ps.setString(3, entrega.getFecha());
            ps.setString(4, entrega.getHora());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                return false;
            }

            clavesGeneradas = ps.getGeneratedKeys();

            if (clavesGeneradas.next()) {
                entrega.setId(clavesGeneradas.getInt(1));
            }

            System.out.println("Entrega guardada correctamente");

            return true;

        } catch (SQLException e) {

            System.out.println("Error al guardar la entrega");
            System.out.println(e.getMessage());

            return false;

        } finally {

            try {
                if (clavesGeneradas != null) {
                    clavesGeneradas.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar ResultSet");
            }

            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar PreparedStatement");
            }

            try {
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión");
            }
        }
    }

    // LISTAR TODAS LAS ENTREGAS

    @Override
    public List<Entrega> listarTodos() {

        List<Entrega> entregas = new ArrayList<>();

        String sql = "SELECT id, id_pedido, id_repartidor, fecha, hora " +
                "FROM entrega";

        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            conexion = ConexionDB.conectar();

            ps = conexion.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("id");
                int idPedido = rs.getInt("id_pedido");
                int idRepartidor = rs.getInt("id_repartidor");
                String fecha = rs.getString("fecha");
                String hora = rs.getString("hora");

                Entrega entrega = new Entrega(
                        id,
                        idPedido,
                        idRepartidor,
                        fecha,
                        hora
                );

                entregas.add(entrega);
            }

        } catch (SQLException e) {

            System.out.println("Error al listar las entregas");
            System.out.println(e.getMessage());

        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar ResultSet");
            }

            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar PreparedStatement");
            }

            try {
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión");
            }
        }

        return entregas;
    }

    // ACTUALIZAR UNA ENTREGA

    @Override
    public boolean actualizar(Entrega entrega) {

        String sql = "UPDATE entrega SET " +
                "id_pedido = ?, " +
                "id_repartidor = ?, " +
                "fecha = ?, " +
                "hora = ? " +
                "WHERE id = ?";

        Connection conexion = null;
        PreparedStatement ps = null;

        try {

            conexion = ConexionDB.conectar();
            ps = conexion.prepareStatement(sql);

            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
            ps.setString(3, entrega.getFecha());
            ps.setString(4, entrega.getHora());
            ps.setInt(5, entrega.getId());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {

                System.out.println("Entrega actualizada correctamente");
                return true;

            } else {

                System.out.println("No existe la entrega");
                return false;
            }

        } catch (SQLException e) {

            System.out.println("Error al actualizar la entrega");
            System.out.println(e.getMessage());
            return false;

        } finally {

            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar PreparedStatement");
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

    // ELIMINAR UNA ENTREGA

    @Override
    public boolean eliminar(int id) {

        String sql = "DELETE FROM entrega WHERE id = ?";

        Connection conexion = null;
        PreparedStatement ps = null;

        try {

            conexion = ConexionDB.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {

                System.out.println("Entrega eliminada correctamente");

                return true;

            } else {

                System.out.println("No existe la entrega");

                return false;
            }

        } catch (SQLException e) {

            System.out.println("Error al eliminar la entrega");
            System.out.println(e.getMessage());

            return false;

        } finally {

            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar PreparedStatement");
            }

            try {
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión");
            }
        }
    }
}