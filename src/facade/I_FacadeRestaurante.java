/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package facade;

import sistema.I_Cliente;
import sistema.I_Pedido;



/**
 *
 * @author javi
 */
public interface I_FacadeRestaurante {
    
    I_Cliente registrarCliente(String nombre, boolean tarjetaFidelidad);
    I_Cliente iniciarSesion(int id);
    void verMenu();
    I_Pedido crearPedido(I_Cliente cliente);
    void verPedido(I_Pedido pedido);
    void confirmarPedido(I_Pedido pedido);
    void actualizarCatalogo();
    }
