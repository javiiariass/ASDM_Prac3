package sistema;

import java.time.LocalDateTime; // Para fecha/hora si se desea
import java.time.format.DateTimeFormatter;

public class Pedido implements I_Pedido {
    // Enum para los estados del pedido
    public enum EstadoPedido { NUEVO, CONFIRMADO, EN_PREPARACION, LISTO, DESPACHADO, CANCELADO }

    private static int contadorId = 0; // Contador estático para IDs únicos
    private int id;
    private int cantidad;
    private String fecha_pedido; // Mantener o usar LocalDateTime
    private I_Pizza[] pedido = new Pizza[10]; // Considerar usar ArrayList<I_Pizza>
    private I_Cliente cliente;
    private EstadoPedido estado; // Nuevo campo para el estado

    public Pedido(I_Cliente c) {
        this.id = ++contadorId; // Asignar ID único
        this.cantidad = 0;
        this.cliente = c;
        this.estado = EstadoPedido.NUEVO; // Estado inicial
        // Formatear fecha actual (opcional)
        this.fecha_pedido = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public EstadoPedido getEstado() {
        return estado;
    }

     @Override
    public void setEstado(EstadoPedido estado) {
        // Podría añadirse lógica para validar transiciones de estado si es necesario
        this.estado = estado;
    }

    @Override
    public I_Cliente getCliente() {
        return cliente;
    }

     @Override
    public int getCantidadPizzas() {
        return cantidad;
    }


    @Override
    public void añadir_Apedido(I_Pizza p) {
        if (estado == EstadoPedido.NUEVO) { // Solo añadir si el pedido es nuevo
            if (cantidad < pedido.length) {
                pedido[cantidad++] = p;
            } else {
                System.out.println("Se ha producido un error: Pedido lleno.");
            }
        } else {
            System.out.println("No se pueden añadir pizzas a un pedido que no está en estado NUEVO (estado actual: " + estado + ")");
        }
    }

    @Override
    public void tramitar_Pedido() {
        if (this.cantidad == 0) {
             System.out.println("Error: No se puede tramitar un pedido vacío.");
             return;
        }
        if (this.estado == EstadoPedido.NUEVO) {
            this.estado = EstadoPedido.CONFIRMADO;
            System.out.println("Pedido ID: " + id + " del cliente " + cliente.getNombre() + " confirmado y listo para preparar.");
            // Aquí podría iniciarse la lógica de preparación si existiera
        } else {
             System.out.println("El pedido ID: " + id + " no se puede tramitar (estado actual: " + estado + ").");
        }
    }

    @Override
    public void mostrar_Pedido() {
        System.out.println("--- Pedido ID: " + id + " ---");
        System.out.println("Cliente: " + cliente.getNombre() + " (ID: " + cliente.getId() + ")");
        System.out.println("Estado: " + estado);
        System.out.println("Fecha: " + fecha_pedido);
        System.out.println("Pizzas (" + cantidad + "):");
        if (cantidad == 0) {
            System.out.println("  (El pedido está vacío)");
        } else {
            for (int i = 0; i < cantidad; i++) {
                if (pedido[i] != null) {
                    System.out.println("- " + pedido[i].getNombre()); // Mostrar solo nombre para brevedad
                    // pedido[i].mostrar_Descripcion(); // O mostrar descripción completa
                }
            }
        }
        System.out.println("-------------------------");
    }

    // Método para despachar (llamado por la fachada)
    public void despachar() {
         if (this.estado == EstadoPedido.CONFIRMADO /* || estado == LISTO etc. */) {
            this.estado = EstadoPedido.DESPACHADO;
            System.out.println("Pedido ID: " + id + " despachado.");
        } else {
            System.out.println("Error: El pedido ID: " + id + " no se puede despachar (estado actual: " + estado + "). Debe estar CONFIRMADO.");
        }
    }
}
