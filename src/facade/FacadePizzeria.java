/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner; // Importar Scanner
import java.util.stream.Collectors; // Para filtrar pedidos

import sistema.Cliente;
import sistema.I_Cliente;
import sistema.I_Menu;
import sistema.I_Pedido;
import sistema.I_Pizza;
import sistema.Menu;
import sistema.Pedido;

/**
 *
 * @author javi
 */
public class FacadePizzeria implements I_FacadeRestaurante {
    private I_Menu menu;
    private List<I_Cliente> clientes;
    private List<I_Pedido> pedidos; // Lista con los pedidos que pueda barajar simultaneamente el local
    
    public FacadePizzeria(){
        // Inicializar los componentes del subsistema
        //menu = new Menu(); // menu sin pizzas
        menu = new Menu(1); // Usar constructor con pizzas predeterminadas
        clientes = new ArrayList<>();
        pedidos = new ArrayList<>();
                // Podríamos precargar clientes o leerlos de algún sitio si fuera necesario
    }

    @Override
    public I_Cliente registrarCliente(String nombre, boolean tarjetaFidelidad) {
        
        I_Cliente nuevoCliente = new Cliente(nombre, tarjetaFidelidad);
        clientes.add(nuevoCliente);
        System.out.println("Cliente registrado con ID: " + nuevoCliente.getId());
        return nuevoCliente;
    }

    @Override
    public I_Cliente iniciarSesion(int id) {
        System.out.println("Buscando cliente con ID: " + id);        
        for (I_Cliente cliente : clientes) {
            if (cliente.getId() == id) {
                                 return cliente;
            }
        }
        
        return null; // Devuelve null si no se encuentra
    }

    @Override
    public void verMenu() {
        System.out.println("--- Menú Disponible ---");
        // Mostrar el menú simple (solo nombres)
        menu.muestraMenu();

        // Preguntar si desea ver el menú detallado
        Scanner sc = new Scanner(System.in);
        System.out.print("¿Desea ver el menú detallado con ingredientes? (s/N): ");
        String respuesta = sc.nextLine();

        if (respuesta.equalsIgnoreCase("s")) {
            System.out.println("\n--- Menú Detallado ---");
            menu.muestraMenuDetallado();
            
        }
        // No cerramos el Scanner aquí si es System.in y se puede necesitar en otro lugar.
        // Si este es el único lugar donde se usa interactivamente, se podría cerrar.
        // sc.close(); // Considerar si cerrar o no el Scanner de System.in
        System.out.println("----------------------------------");
    }

    @Override
    public I_Pedido crearPedido(I_Cliente cliente) {
        if (cliente == null) {
            System.out.println("Error: No se puede crear un pedido sin un cliente válido.");
            return null;
        }
        // Crear un nuevo pedido asociado al cliente
        I_Pedido nuevoPedido = new Pedido(cliente);
        pedidos.add(nuevoPedido); // Añadir el nuevo pedido a la lista de pedidos activos
                System.out.println("Nuevo pedido creado para el cliente " + cliente.getNombre());
        return nuevoPedido;
    }

    
    public void anadirPizzaAPedido(I_Pedido pedido, I_Pizza pizza) {
        if (pedido == null || pizza == null) {
             System.out.println("Error: Pedido o pizza no válidos.");
             return;
        }
        // Delegar la acción al objeto pedido
        pedido.añadir_Apedido(pizza);
        System.out.println("Pizza '" + pizza.getNombre() + "' añadida al pedido.");
    }


    @Override
    public void verPedido(I_Pedido pedido) {
        if (pedido == null) {
             System.out.println("Error: Pedido no válido.");
             return;
        }
        // Delegar la visualización al objeto pedido
        System.out.println("--- Detalles del Pedido ---");
        pedido.mostrar_Pedido();
        System.out.println("---------------------------");
    }

    @Override
    public void confirmarPedido(I_Pedido pedido) {
         if (pedido == null) {
             System.out.println("Error: Pedido no válido.");
             return;
        }
        // Delegar la confirmación (tramitación) al objeto pedido
        System.out.println("Confirmando y tramitando pedido...");
        pedido.tramitar_Pedido();
        // Opcionalmente, se podría eliminar el pedido de la lista de activos aquí
        // pedidos.remove(pedido);
         System.out.println("Pedido confirmado y tramitado.");
    }

    @Override
    public void actualizarCatalogo() { // Nombre corregido según la interfaz
        // Delegar la actualización al objeto menu
        System.out.println("Iniciando actualización interactiva del catálogo...");
        menu.actualizaMenu();
        System.out.println("Catálogo actualizado.");
    }


    public boolean existePizza(String nombrePizza){
        return menu.existePizza(nombrePizza);
    }

    // Implementación del método interactivo seleccionarPizza
    
    public I_Pizza seleccionarPizza() {
        // Delega la selección interactiva al menú
        return menu.escogerPizza();
    }

    // Implementación del nuevo método seleccionarPizzaPorIndice
    
    public I_Pizza seleccionarPizzaPorIndice(int indice) {
        // Delega la obtención de la pizza por índice al menú
        I_Pizza pizza = menu.getPizza(indice);
        if (pizza != null) {
            System.out.println("Pizza seleccionada por índice " + indice + ": " + pizza.getNombre());
        }
        return pizza;
    }

    // Nuevo: Obtener pedidos confirmados
    public List<I_Pedido> getPedidosConfirmados() {
        return pedidos.stream()
                           .filter(p -> p.getEstado() == Pedido.EstadoPedido.CONFIRMADO)
                           .collect(Collectors.toList());
    }

    // Nuevo: Despachar un pedido por ID
    public boolean despacharPedido(int idPedido) {
        I_Pedido pedidoADespachar = null;
        for (I_Pedido pedido : pedidos) {
            if (pedido.getId() == idPedido) {
                pedidoADespachar = pedido;
                break;
            }
        }

        if (pedidoADespachar == null) {
            System.out.println("Error: No se encontró ningún pedido con ID " + idPedido + ".");
            return false;
        }

        if (pedidoADespachar.getEstado() == Pedido.EstadoPedido.CONFIRMADO) {
            pedidos.remove(pedidoADespachar);
            System.out.println("Pedido ID " + idPedido + " despachado correctamente.");
            return true;
        } else {
            System.out.println("Error: El pedido ID " + idPedido + " no está confirmado (Estado: " + pedidoADespachar.getEstado() + "). No se puede despachar.");
            return false;
        }
    }
}
