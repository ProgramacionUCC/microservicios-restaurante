package model;

import java.time.LocalDate;

// Guarda los datos del propietario (solo datos, no valida nada)
public class Propietario {
    private String nombre;
    private String apellido;
    private String documentoDeIdentidad; // es String para que no se pierdan ceros como 00123
    private String celular; // tambien String por el + y los ceros
    private LocalDate fechaNacimiento; // fecha real, no texto, para calcular edad facil
    private String correo;
    private String clave; // al inicio es normal, despues se vuelve encriptada
    private String rol; // por defecto PROPIETARIO, pero puede ser ADMINISTRADOR para HU-05

    // Crea un propietario con sus 7 datos. El rol se pone solo.
    public Propietario(String nombre, String apellido, String documentoDeIdentidad, String celular, LocalDate fechaNacimiento, String correo, String clave) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.documentoDeIdentidad = documentoDeIdentidad;
        this.celular = celular;
        this.fechaNacimiento = fechaNacimiento;
        this.correo = correo;
        this.clave = clave;
        this.rol = "PROPIETARIO"; // todos los que se crean aqui son propietarios por defecto
    }

    // Constructor para crear usuarios con rol especifico (ADMINISTRADOR, etc.) - HU-05
    public Propietario(String nombre, String apellido, String documentoDeIdentidad, String celular, LocalDate fechaNacimiento, String correo, String clave, String rol) {
        this(nombre, apellido, documentoDeIdentidad, celular, fechaNacimiento, correo, clave);
        if (rol != null && !rol.trim().isEmpty()) {
            this.rol = rol.toUpperCase();
        }
    }

    // Getters para que el service pueda revisarlos
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

    // Solo se usa para cambiar la clave normal por la encriptada
    public void setClave(String clave) {
        this.clave = clave;
    }

    // Devuelve el rol (PROPIETARIO, ADMINISTRADOR, etc.)
    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        if (rol != null && !rol.trim().isEmpty()) {
            this.rol = rol.toUpperCase();
        }
    }

    @Override
    public String toString() {
        return "Propietario{nombre='" + nombre + "', correo='" + correo + "', rol='" + rol + "'}";
    }
}
