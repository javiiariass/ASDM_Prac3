/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package asdm_prac3;

import java.util.List;
import java.util.Scanner;

import facade.FacadePizzeria; // Necesario para List
import sistema.I_Cliente;
import sistema.I_Pedido;
import sistema.I_Pizza;
import sistema.Pedido;

/**
 *
 * @author javi
 */
public class ASDM_Prac3 {

    private static final String ADMIN_PASSWORD = "123"; // Contraseña de admin
    private static boolean isAdminLoggedIn = false; // Estado de login admin
    private static FacadePizzeria restaurante = new FacadePizzeria();
    private static Scanner scanner = new Scanner(System.in);
    private static I_Cliente clienteActual = null;
    private static I_Pedido pedidoActual = null; // Pedido en curso para el cliente logueado

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int opcion = -1;

        System.out.println("--- Bienvenido a la Pizzería ---");

        while (opcion != 0) {
            mostrarMenuPrincipal(); // Muestra menú según estado de login
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());

                if (isAdminLoggedIn) {
                    manejarMenuAdmin(opcion);
                } else if (clienteActual != null) {
                    manejarMenuCliente(opcion);
                } else {
                    manejarMenuLogin(opcion);
                }

            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                opcion = -1; // Resetear opción
            } catch (Exception e) { // Captura genérica
                 System.out.println("Ocurrió un error inesperado: " + e.getMessage());
                 // e.printStackTrace(); // Descomentar para depuración
                 opcion = -1; // Resetear opción
            }
        }

        scanner.close(); // Cerrar el scanner al salir
        System.out.println("\n--- Gracias por visitar la Pizzería ---");
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n--- Menú Principal ---");
        if (isAdminLoggedIn) {
            System.out.println("== MODO ADMINISTRADOR ==");
            System.out.println("1. Actualizar Catálogo (Añadir Pizza)");
            System.out.println("2. Ver Pedidos Confirmados");
            System.out.println("3. Despachar Pedido Confirmado");
            System.out.println("4. Ver Menú de Pizzas"); // Admin también puede ver el menú
            System.out.println("9. Cerrar Sesión (Admin)");
            System.out.println("0. Salir del Sistema");
        } else if (clienteActual != null) {
            System.out.println("== Cliente: " + clienteActual.getNombre() + " (ID: " + clienteActual.getId() + ") ==");
            System.out.println("Pedido actual: " + (pedidoActual != null ? "En curso (ID: " + pedidoActual.getId() + ")" : "Ninguno"));
            System.out.println("1. Ver Menú de Pizzas");
            System.out.println("2. Crear Nuevo Pedido");
            System.out.println("3. Añadir Pizza al Pedido Actual (por índice)");
            System.out.println("4. Ver Pedido Actual");
            System.out.println("5. Confirmar Pedido Actual");
            System.out.println("9. Cerrar Sesión");
            System.out.println("0. Salir del Sistema");
        } else {
            System.out.println("== Acceso ==");
            System.out.println("1. Iniciar Sesión (Cliente)");
            System.out.println("2. Registrar Nuevo Cliente");
            System.out.println("3. Iniciar Sesión (Administrador)");
            System.out.println("4. Ver Menú de Pizzas"); // Permitir ver menú sin loguearse
            System.out.println("0. Salir del Sistema");
        }
    }

    // Manejadores de menú según el estado
    private static void manejarMenuLogin(int opcion) {
        switch (opcion) {
            case 1:
                iniciarSesionCliente();
                break;
            case 2:
                registrarCliente();
                break;
            case 3:
                iniciarSesionAdmin();
                break;
            case 4:
                verMenu();
                break;
            case 0:
                // Salir (manejado en bucle principal)
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    private static void manejarMenuCliente(int opcion) {
        switch (opcion) {
            case 1:
                verMenu();
                break;
            case 2:
                crearPedido();
                break;
            case 3:
                anadirPizzaAlPedido();
                break;
            case 4:
                verPedidoActual();
                break;
            case 5:
                confirmarPedido();
                break;
            case 9:
                cerrarSesion();
                break;
            case 0:
                // Salir (manejado en bucle principal)
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    private static void manejarMenuAdmin(int opcion) {
        switch (opcion) {
            case 1:
                restaurante.actualizarCatalogo(); // Llama a la función específica de admin
                break;
            case 2:
                verPedidosConfirmados();
                break;
            case 3:
                despacharPedidoAdmin();
                break;
             case 4:
                verMenu(); // Admin también puede ver el menú
                break;
            case 9:
                cerrarSesion();
                break;
            case 0:
                // Salir (manejado en bucle principal)
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    // --- Funciones de Login/Logout/Registro ---
    private static void iniciarSesionCliente() {
        System.out.println("\n--- Inicio de Sesión (Cliente) ---");
        System.out.print("Ingrese ID de cliente: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            I_Cliente cliente = restaurante.iniciarSesion(id);
            if (cliente != null) {
                clienteActual = cliente;
                pedidoActual = null; // Resetear pedido al cambiar de cliente
                isAdminLoggedIn = false;
                System.out.println("Inicio de sesión exitoso para: " + clienteActual.getNombre());
            } else {
                System.out.println("ID de cliente no encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Debe ser un número.");
        }
    }

    private static void registrarCliente() {
        System.out.println("\n--- Registro de Cliente ---");
        System.out.print("Ingrese nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("¿Tiene tarjeta de fidelidad? (s/N): ");
        boolean tarjeta = scanner.nextLine().equalsIgnoreCase("s");
        I_Cliente nuevoCliente = restaurante.registrarCliente(nombre, tarjeta);
        if (nuevoCliente != null) {
            // Opcional: Iniciar sesión automáticamente tras registrarse
            clienteActual = nuevoCliente;
            pedidoActual = null;
            isAdminLoggedIn = false;
            System.out.println("Registro exitoso. Sesión iniciada para: " + clienteActual.getNombre());
            clienteActual.mostrarCliente();
        } else {
            System.out.println("Error al registrar cliente.");
        }
    }

    private static void iniciarSesionAdmin() {
        System.out.println("\n--- Inicio de Sesión (Administrador) ---");
        System.out.print("Ingrese la contraseña: ");
        String pass = scanner.nextLine();
        if (ADMIN_PASSWORD.equals(pass)) {
            isAdminLoggedIn = true;
            clienteActual = null; // Desconectar cliente si lo hubiera
            pedidoActual = null;
            System.out.println("Inicio de sesión de administrador exitoso.");
        } else {
            System.out.println("Contraseña incorrecta.");
        }
    }

    private static void cerrarSesion() {
        if (isAdminLoggedIn) {
            isAdminLoggedIn = false;
            System.out.println("Sesión de administrador cerrada.");
        } else if (clienteActual != null) {
            String nombreCliente = clienteActual.getNombre();
            clienteActual = null;
            pedidoActual = null; // También limpiar pedido actual al cerrar sesión
            System.out.println("Sesión de cliente " + nombreCliente + " cerrada.");
        } else {
            System.out.println("No hay ninguna sesión activa para cerrar.");
        }
    }

    // --- Funciones Comunes / Cliente ---
    private static void verMenu() {
        System.out.println("\n--- Ver Menú ---");
        restaurante.verMenu();
    }

    private static void crearPedido() {
        System.out.println("\n--- Crear Pedido ---");
        if (clienteActual == null) {
             System.out.println("Error: Debe iniciar sesión como cliente para crear un pedido.");
             return;
        }
        if (pedidoActual != null && pedidoActual.getEstado() == Pedido.EstadoPedido.NUEVO) {
            System.out.println("Ya tiene un pedido nuevo en curso (ID: " + pedidoActual.getId() + "). Confírmelo o añada más pizzas.");
        } else {
            pedidoActual = restaurante.crearPedido(clienteActual);
            if (pedidoActual == null) {
                System.out.println("Error al crear el pedido.");
            }
        }
    }

    private static void anadirPizzaAlPedido() {
        System.out.println("\n--- Añadir Pizza al Pedido Actual ---");
        if (pedidoActual == null) {
            System.out.println("Debe crear un pedido ('Opción 2') antes de añadir pizzas.");
            return;
        }
         if (pedidoActual.getEstado() != Pedido.EstadoPedido.NUEVO) {
            System.out.println("Solo se pueden añadir pizzas a pedidos en estado 'NUEVO'. Este pedido (ID: " + pedidoActual.getId() + ") está en estado: " + pedidoActual.getEstado());
            return;
        }

        restaurante.verMenu(); // Mostrar menú para facilitar selección
        System.out.print("Ingrese el número de la pizza a añadir: ");
        try {
            int indicePizza = Integer.parseInt(scanner.nextLine());
            I_Pizza pizzaSeleccionada = restaurante.seleccionarPizzaPorIndice(indicePizza);
            if (pizzaSeleccionada != null) {
                restaurante.anadirPizzaAPedido(pedidoActual, pizzaSeleccionada);
            } else {
                System.out.println("Número de pizza inválido o no encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Debe ser un número.");
        }
    }

    private static void verPedidoActual() {
        System.out.println("\n--- Ver Pedido Actual ---");
        if (pedidoActual != null) {
            restaurante.verPedido(pedidoActual);
        } else {
            System.out.println("No hay ningún pedido activo para el cliente actual.");
        }
    }

    private static void confirmarPedido() {
        System.out.println("\n--- Confirmar Pedido ---");
        if (pedidoActual != null) {
             if (pedidoActual.getEstado() == Pedido.EstadoPedido.NUEVO) {
                restaurante.confirmarPedido(pedidoActual);
                // Si la confirmación fue exitosa (estado cambió a CONFIRMADO), limpiar pedidoActual
                if (pedidoActual.getEstado() == Pedido.EstadoPedido.CONFIRMADO) {
                     pedidoActual = null; // El pedido ya no está "en curso" para el cliente
                }
             } else {
                 System.out.println("El pedido actual (ID: " + pedidoActual.getId() + ") no está en estado 'NUEVO' y no puede ser confirmado de nuevo. Estado: " + pedidoActual.getEstado());
             }
        } else {
            System.out.println("No hay ningún pedido activo para confirmar.");
        }
    }


    


     private static void verPedidosConfirmados() {
        System.out.println("\n--- Ver Pedidos Confirmados ---");
        List<I_Pedido> confirmados = restaurante.getPedidosConfirmados();
        if (confirmados.isEmpty()) {
            System.out.println("No hay pedidos confirmados pendientes de despachar.");
        } else {
            System.out.println("Pedidos en estado CONFIRMADO:");
            for (I_Pedido p : confirmados) {
                p.mostrar_Pedido(); // Mostrar detalles completos
                // O una versión más corta:
                // System.out.println(" - ID: " + p.getId() + ", Cliente: " + p.getCliente().getNombre() + ", Pizzas: " + p.getCantidadPizzas());
            }
        }
    }

    private static void despacharPedidoAdmin() {
        System.out.println("\n--- Despachar Pedido Confirmado ---");
        verPedidosConfirmados(); // Mostrar primero los disponibles
        List<I_Pedido> confirmados = restaurante.getPedidosConfirmados(); // Obtener la lista de nuevo

        if (confirmados.isEmpty()) {
            // El mensaje ya se muestra en verPedidosConfirmados
            return;
        }

        System.out.print("Ingrese el ID del pedido a despachar: ");
        try {
            int idPedido = Integer.parseInt(scanner.nextLine());
            boolean despachado = restaurante.despacharPedido(idPedido);
            // El método despacharPedido ya imprime mensajes de éxito/error
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Debe ser un número.");
        }
    }

// --- Añadir el método addPizzaToMenu a FacadePizzeria ---
    // Este método debería estar DENTRO de la clase FacadePizzeria.java
    // Lo pongo aquí como comentario para referencia de la refactorización necesaria.
    /*
    // Dentro de FacadePizzeria.java:
    public void addPizzaToMenu(String nombre, List<String> ingredientes) {
        // La comprobación principal se hace ANTES de llamar a este método
        // Se podría añadir un check adicional por seguridad si se desea
        // if (existePizza(nombre)) {
        //     System.out.println("Error interno: Intento de añadir pizza duplicada '" + nombre + "'");
        //     return;
        // }
        I_Pizza nuevaPizza = new Pizza(nombre, ingredientes);
        menu.add(nuevaPizza);
        System.out.println("Pizza '" + nombre + "' añadida al menú.");
    }
    */

}
