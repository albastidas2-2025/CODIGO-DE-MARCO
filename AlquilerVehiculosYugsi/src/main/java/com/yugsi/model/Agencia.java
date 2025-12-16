package com.yugsi.model;
import java.util.ArrayList;
import java.util.List;

public class Agencia {
    private String nombre;
    private List<Vehiculo> inventario;
    private List<Alquiler> alquileres;

    public Agencia(String nombre) {
        this.nombre = nombre;
        this.inventario = new ArrayList<>();
        this.alquileres = new ArrayList<>();
        inicializarDatosDemo();
    }

    private void inicializarDatosDemo() {
        // Vehículos de ejemplo
        inventario.add(new Automovil("ABC123", "Toyota", "Corolla", 50.0, 4, "Gasolina"));
        inventario.add(new Automovil("DEF456", "Honda", "Civic", 55.0, 4, "Híbrido"));
        inventario.add(new Motocicleta("GHI789", "Yamaha", "MT-07", 35.0, 689, true));
        inventario.add(new Motocicleta("JKL012", "Kawasaki", "Ninja 400", 40.0, 399, false));

        // Clientes de ejemplo
        // (Para uso en pruebas)
    }

    // Métodos para gestionar vehículos
    public void agregarVehiculo(Vehiculo vehiculo) {
        inventario.add(vehiculo);
    }

    public List<Vehiculo> getVehiculosDisponibles() {
        // Expresión lambda para filtrar vehículos disponibles
        return inventario.stream()
                .filter(v -> v.isDisponible())
                .toList();
    }

    // Métodos para gestionar alquileres
    public Alquiler registrarAlquiler(Cliente cliente, Vehiculo vehiculo, int dias) {
        Alquiler nuevoAlquiler = new Alquiler(cliente, vehiculo, dias);
        alquileres.add(nuevoAlquiler);
        return nuevoAlquiler;
    }

    public List<Alquiler> getAlquileresActivos() {
        // Expresión lambda para filtrar alquileres activos
        return alquileres.stream()
                .filter(a -> "ACTIVO".equals(a.getEstado()))
                .toList();
    }

    // Getters
    public List<Vehiculo> getInventario() { return inventario; }
    public List<Alquiler> getAlquileres() { return alquileres; }
    public String getNombre() { return nombre; }
}