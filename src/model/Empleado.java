package model;

import java.time.LocalDate;

// HU-05: datos del empleado. Solo guarda datos, no valida (lo hace el service).
public class Empleado {
    private String nombre;
    private String apellido;
    private String documentoDeIdentidad;
    private String celular;
    private LocalDate fechaNacimiento;
    private String correo;
    private String clave;
    private String rol; // siempre EMPLEADO
    private String idRestaurante; // NIT del restaurante donde trabaja

    public Empleado(String nombre, String apellido, String documentoDeIdentidad, String celular, LocalDate fechaNacimiento, String correo, String clave, String idRestaurante) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.documentoDeIdentidad = documentoDeIdentidad;
        this.celular = celular;
        this.fechaNacimiento = fechaNacimiento;
        this.correo = correo;
        this.clave = clave;
        this.rol = "EMPLEADO";
        this.idRestaurante = idRestaurante;
    }

    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getDocumentoDeIdentidad() { return documentoDeIdentidad; }
    public String getCelular() { return celular; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getCorreo() { return correo; }
    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
    public String getRol() { return rol; }
    public String getIdRestaurante() { return idRestaurante; }

    @Override
    public String toString() {
        return "Empleado{nombre='" + nombre + "', correo='" + correo + "', rol='" + rol + "', idRestaurante='" + idRestaurante + "'}";
    }
}
