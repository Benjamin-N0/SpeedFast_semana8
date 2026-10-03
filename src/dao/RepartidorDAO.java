package dao;

import model.Repartidor;

import java.util.List;

public interface RepartidorDAO {
    public void guardar(Repartidor repartidor);
    public List<Repartidor> listarTodos();
    public void actualizar(Repartidor repartidor);
    boolean eliminar(int id);
}
