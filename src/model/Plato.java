package model;

// HU-03: datos del plato. Solo guarda datos, no valida nada (eso lo hace el service).
public class Plato {
    private String nombre;
    private int precio; // entero positivo, mayor a 0
    private String descripcion;
    private String urlImagen;
    private String categoria;
    private String idRestaurante; // usamos el NIT del restaurante como identificador
    private boolean activo; // por defecto true al crearse

    public Plato(String nombre, int precio, String descripcion, String urlImagen, String categoria, String idRestaurante) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.urlImagen = urlImagen;
        this.categoria = categoria;
        this.idRestaurante = idRestaurante;
        this.activo = true; // regla de negocio: todo plato nace activo
    }

    public String getNombre() {
        return nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getIdRestaurante() {
        return idRestaurante;
    }

    public boolean isActivo() {
        return activo;
    }

    @Override
    public String toString() {
        return "Plato{" +
                "nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", categoria='" + categoria + '\'' +
                ", idRestaurante='" + idRestaurante + '\'' +
                ", activo=" + activo +
                '}';
    }
}