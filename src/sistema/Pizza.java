package sistema;

import java.util.ArrayList;
import java.util.List; // Importar ArrayList

public class Pizza implements I_Pizza {
    String nombre;
    List<String> ingredientes;
    // Se elimina FechaPedido

    // Constructor actualizado para aceptar nombre y lista de ingredientes
    public Pizza(String n, List<String> ingredientes) {
        this.nombre = n;
        // Crear una nueva lista para evitar modificaciones externas
        this.ingredientes = new ArrayList<>(ingredientes);
    }

    
    

    @Override
    public void mostrar_Descripcion(){
        System.out.println("Pizza " + nombre);
        System.out.println("Los ingredientes son:");
        for (String ingrediente : ingredientes) {
            System.out.println("- " + ingrediente);
        }
        
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public List<String> getIngredientes() {
        // Devuelve una copia para proteger la lista interna
        return new ArrayList<>(ingredientes);
    }
}
