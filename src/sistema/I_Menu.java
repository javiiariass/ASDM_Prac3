package sistema;



public interface I_Menu {
    void actualizaMenu();
    void muestraMenu(); // Muestra solo nombres
    void muestraMenuDetallado();
    I_Pizza escogerPizza();
    int getNumeroProductos(); 
    boolean existePizza(String nombrePizza);
    I_Pizza getPizza(int index);
}
