
package sistema;


public interface I_Pedido {
    
    public void añadir_Apedido(I_Pizza p);
    public void tramitar_Pedido();
    
    public void mostrar_Pedido();
    int getId(); // obtener ID del pedido
    Pedido.EstadoPedido getEstado(); // obtener estado del pedido
    void setEstado(Pedido.EstadoPedido estado); // establecer estado (para despachar)
    I_Cliente getCliente(); // obtener el cliente asociado
    int getCantidadPizzas();
}
