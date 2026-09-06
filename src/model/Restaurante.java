package model;

// HU-02: datos del restaurante.
public class Restaurante {
    private String nombre;
    private String nit;
    private String direccion;
    private String telefono;
    private String urlLogo;
    private String idPropietario;

    public Restaurante(String nombre, String nit, String direccion, String telefono, String urlLogo, String idPropietario) {
        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
        this.urlLogo = urlLogo;
        this.idPropietario = idPropietario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNit() {
        return nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public String getIdPropietario() {
        return idPropietario;
    }

    @Override
    public String toString() {
        return "Restaurante{" +
                "nombre='" + nombre + '\'' +
                ", nit='" + nit + '\'' +
                ", idPropietario='" + idPropietario + '\'' +
                '}';
    }
}
