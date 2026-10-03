package dao;

import model.Pedido;

import java.util.List;

public interface PedidoDAO {
    public void guardar(Pedido pedido);
    public List<Pedido> listarTodos();
    public void actualizarEstado(int idPedido, String nuevoEstado);
    void actualizar(Pedido pedido);
    boolean eliminar(int id);
}
