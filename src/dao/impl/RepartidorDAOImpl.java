package dao.impl;
import dao.RepartidorDAO;
import model.Repartidor;
import util.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAOImpl implements RepartidorDAO {
    @Override
    public void guardar(Repartidor repartidor) {
        String sql = "INSERT INTO repartidor (nombre) VALUES (?)";

        Connection conexion = null;
        PreparedStatement ps = null;

        try {
            conexion = ConexionDB.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, repartidor.getNombre());
            ps.executeUpdate();
            System.out.println("Repartidor guardado correctamente");

        }catch (SQLException e){

            System.out.println("Error al guardar el repartidor");
            System.out.println(e.getMessage());

        }finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar el prepareStatement");
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
    public List<Repartidor> listarTodos() {

        List<Repartidor> repartidores = new ArrayList<>();

        String sql = "SELECT id, nombre FROM repartidor";

        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet resultado = null;

        try {
            conexion = ConexionDB.conectar();

            ps = conexion.prepareStatement(sql);

            resultado = ps.executeQuery();


            while (resultado.next()) {

                int id = resultado.getInt("id");
                String nombre = resultado.getString("nombre");

                Repartidor repartidor = new Repartidor(id, nombre);

                repartidores.add(repartidor);
            }
        }catch (SQLException e){

            System.out.println("Error al listar los repartidores");
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
                System.out.println("Error al cerrar el prepareStatement");
            }

            try {
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión");
            }
        }
        return repartidores;
    }

    @Override
    public void actualizar(Repartidor repartidor) {

        String sql = "UPDATE repartidor SET nombre = ? WHERE id = ?";

        Connection conexion = null;
        PreparedStatement ps = null;

        try {
            conexion = ConexionDB.conectar();
            ps = conexion.prepareStatement(sql);

            ps.setString(1, repartidor.getNombre());
            ps.setInt(2, repartidor.getId());
            ps.executeUpdate();

            System.out.println("Repartidor actualizado correctamente");

        } catch (SQLException e) {

            System.out.println("Error al actualizar el repartidor");
            System.out.println(e.getMessage());

        } finally {

            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al cerrar el prepareStatement");
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

    @Override
    public boolean eliminar(int id) {

        String sql = "DELETE FROM repartidor WHERE id = ?";

        try {
            Connection conexion = ConexionDB.conectar();

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, id);

            int filasAfectadas = ps.executeUpdate();

            ps.close();
            conexion.close();

            if (filasAfectadas > 0) {
                System.out.println("Repartidor eliminado correctamente");
                return true;
            }

            System.out.println("No existe el repartidor");
            return false;

        } catch (SQLException e) {

            System.out.println("Error al eliminar el repartidor");
            System.out.println(e.getMessage());

            return false;
        }
    }
}
