package sistema;


public class Cliente implements I_Cliente {
    static int contadorId = 0;
    int id;
    String nombre;
    boolean tarjetaFidelidad;
    
    public Cliente(String n, boolean tarjeta){
        nombre=n;
        tarjetaFidelidad=tarjeta;
        this.id = contadorId;
        contadorId++;
    }
       
    @Override
    public void mostrarCliente(){
        System.out.println("--- Detalles del Cliente ---");
        System.out.println("ID: " + id);
        System.out.println("Nombre: " + nombre);
        System.out.println("Tarjeta Fidelidad: " + (tarjetaFidelidad ? "Sí" : "No"));
        System.out.println("----------------------------");
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean tieneTarjetaFidelidad() {
        return tarjetaFidelidad;
    }

    

    
    
    
}
