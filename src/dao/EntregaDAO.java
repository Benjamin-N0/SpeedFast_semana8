package dao;

import model.Entrega;

import java.util.List;

public interface EntregaDAO {


    boolean guardar(Entrega entrega);

    List<Entrega> listarTodos();

    boolean actualizar(Entrega entrega);

    boolean eliminar(int id);
}