package sistema;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays; // Importar List
import java.util.Date;
import java.util.List;
import java.util.Scanner; // Para crear lista de ingredientes fácilmente

public class Menu implements I_Menu {

    String fecha_actualizacion;
    private List<I_Pizza> menu;

    public Menu() {
        menu = new ArrayList<>();
        fecha_actualizacion = Date.from(Instant.now()).toString();
    }

    /**
     * Genera un menú de pizzas predeterminado con 4 pizzas de ejemplo.
     *
     * @param notUsed
     */
    public Menu(int notUsed) {
        menu = new ArrayList<>();
        menu.add(new Pizza("Carbonara", Arrays.asList("Bacon", "Nata", "Mozzarella", "Huevo")));
        menu.add(new Pizza("Barbacoa", Arrays.asList("Carne picada", "Salsa Barbacoa", "Mozzarella", "Cebolla")));
        menu.add(new Pizza("Margarita", Arrays.asList("Tomate", "Mozzarella", "Albahaca")));
        menu.add(new Pizza("Hawaiana", Arrays.asList("Jamon", "Piña", "Mozzarella")));
        fecha_actualizacion = Date.from(Instant.now()).toString();
    }

    

    // Método actualizaMenu original adaptado para List y nuevo constructor de Pizza
    @Override
    public void actualizaMenu() {
        Scanner sc = new Scanner(System.in);
        String nombre;
        Pizza p;

        // Pide al usuario el numero de ingredientes y luego en un bucle solicita ingrediente a ingrediente
        // y añadelo al arrayList para luego pasarlo al constructor
        System.out.println("Introduzca nombre de la nueva pizza: ");
        nombre = sc.nextLine();
        if(existePizza(nombre)) {
            System.out.println("Error: Ya existe una pizza con el nombre '" + nombre + "'.");
            return;
        }
        

        System.out.println("¿Cuántos ingredientes desea añadir? ");
        int numIngredientes = sc.nextInt();
        sc.nextLine(); // Consumir newline
        // Crear lista de ingredientes para el nuevo constructor
        List<String> ingredientes = new ArrayList<>();
        for (int i = 1; i <= numIngredientes; i++) {
            System.out.println("Introduzca ingrediente " + i + ": ");
            String ingrediente = sc.nextLine();
            ingredientes.add(ingrediente);
        }
        p = new Pizza(nombre, ingredientes);

        menu.add(p);
        System.out.println("Pizza '" + nombre + "' añadida al menú.");
        fecha_actualizacion = Date.from(Instant.now()).toString();

    }

    @Override
    public void muestraMenu() {
        if (menu.isEmpty()) {
            System.out.println("El menú está vacío.");
            return;
        }
        for (int i = 0; i < menu.size(); i++) {
            System.out.println((i + 1) + ": " + menu.get(i).getNombre());
        }
        System.out.println("----------------------");
    }

    @Override
    public void muestraMenuDetallado() {

        if (menu.isEmpty()) {
            System.out.println("El menú está vacío.");
            return;
        }
        for (int i = 0; i < menu.size(); i++) {
            System.out.println("----------------------");
            System.out.println((i + 1) + ": " + menu.get(i).getNombre()); // Más descriptivo
            System.out.println("Ingredientes:"); // Más descriptivo

            for (String ingrediente : menu.get(i).getIngredientes()) {
                System.out.println("- " + ingrediente);
            }

            System.out.println("----------------------");
        }
    }

    @Override
    public I_Pizza escogerPizza() {
        if (menu.isEmpty()) {
            System.out.println("No hay pizzas en el menú para escoger.");
            return null;
        }
        Scanner sc = new Scanner(System.in);
        int tipo = -1; // Inicializar a un valor inválido
        int intentos = 0;
        final int MAX_INTENTOS = 3; // Limitar intentos

        do {
            System.out.println("¿Qué modelo de pizza quiere?");
            muestraMenu(); // Muestra el menú con números
            System.out.print("Elija un número de pizza (1-" + menu.size() + "): ");

            if (sc.hasNextInt()) {
                tipo = sc.nextInt();
                sc.nextLine(); // Consumir newline
                // Validar que el número esté en el rango correcto (1 a menu.size())
                if (tipo < 1 || tipo > menu.size()) {
                    System.out.println("Número inválido. Por favor, elija un número entre 1 y " + menu.size() + ".");
                    tipo = -1; // Resetear para continuar el bucle
                }
            } else {
                System.out.println("Entrada inválida. Por favor, introduzca un número.");
                sc.nextLine(); // Consumir la entrada inválida completa
                tipo = -1; // Asegurar que el bucle continúa
            }
            intentos++;
            if (tipo == -1 && intentos >= MAX_INTENTOS) {
                System.out.println("Demasiados intentos inválidos. Abortando selección.");
                // No cerrar sc si es System.in
                return null; // Salir si hay muchos intentos fallidos
            }

        } while (tipo == -1);

        // sc.close(); // No cerrar Scanner de System.in aquí
        // Devolver la pizza correspondiente al índice (tipo - 1)
        System.out.println("Ha seleccionado: " + menu.get(tipo - 1).getNombre());
        return menu.get(tipo - 1);
    }

    @Override
    public int getNumeroProductos() {
        return menu.size(); // Devolver el tamaño de la lista
    }

    // Implementación del método getPizza por índice (basado en 1)
    @Override
    public I_Pizza getPizza(int index) {
        // Validar índice (1-based index)
        if (index >= 1 && index <= menu.size()) {
            return menu.get(index - 1); // Convertir a 0-based index para la lista
        }
        System.out.println("Error: Índice de pizza inválido (" + index + ").");
        return null; // Devolver null si el índice no es válido
    }


    @Override
    public boolean existePizza(String nombrePizza){
        for (int i = 0; i < menu.size(); i++) {
            if (menu.get(i).getNombre().equalsIgnoreCase(nombrePizza)) {
                return true;
            }
        }
        return false;
    }

}
