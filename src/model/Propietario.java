package model;

import java.time.LocalDate;

// HU-01: datos del propietario.
public class Propietario {
    private String nombre;
    private String apellido;
    private String documentoDeIdentidad;
    private String celular;
    private LocalDate fechaNacimiento;
    private String correo;
    private String clave;
    private String rol;

    public Propietario(String nombre, String apellido, String documentoDeIdentidad, String celular, LocalDate fechaNacimiento, String correo, String clave) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.documentoDeIdentidad = documentoDeIdentidad;
        this.celular = celular;
        this.fechaNacimiento = fechaNacimiento;
        this.correo = correo;
        this.clave = clave;
        // todo propietario tiene el rol de PROPIETARIO
        this.rol = "PROPIETARIO";
    }

    // Se necesitan para revisar que no vengan vacios y para encriptar.
    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDocumentoDeIdentidad() {
        return documentoDeIdentidad;
    }

    public String getCelular() {
        return celular;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getCorreo() {
        return correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    // Siempre es PROPIETARIO.
    public String getRol() {
        return rol;
    }

    @Override
    public String toString() {
        return "Propietario{nombre='" + nombre + "', correo='" + correo + "', rol='" + rol + "'}";
    }
}
