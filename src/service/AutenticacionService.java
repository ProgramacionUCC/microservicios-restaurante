package service;

import model.Propietario;
import repository.PropietarioRepository;
import org.mindrot.BCrypt;

// HU-05: se encarga de dejar entrar solo a usuarios con correo y clave correctos
public class AutenticacionService {

    // donde estan guardados los usuarios
    private final PropietarioRepository propietarioRepository;

    public AutenticacionService(PropietarioRepository propietarioRepository) {
        this.propietarioRepository = propietarioRepository;
    }

    // Inicia sesion con correo y clave. Si todo esta bien devuelve el usuario, si no avisa con error.
    // Intentos ilimitados: no bloquea, solo avisa cada vez que falla.
    public Propietario iniciarSesion(String correo, String clave) {
        if (esVacio(correo) || esVacio(clave)) {
            throw new IllegalArgumentException("Correo y clave son obligatorios.");
        }

        // busca por correo
        Propietario usuario = null;
        for (Propietario p : propietarioRepository.getPropietarios()) {
            if (p.getCorreo().equalsIgnoreCase(correo.trim())) {
                usuario = p;
                break;
            }
        }

        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        // compara clave escrita con la guardada encriptada
        if (!BCrypt.checkpw(clave, usuario.getClave())) {
            throw new IllegalArgumentException("Contraseña incorrecta.");
        }

        return usuario;
    }

    // Revisa si el usuario tiene el rol que se necesita para hacer algo
    public boolean tienePermiso(Propietario usuario, String rolRequerido) {
        if (usuario == null || esVacio(rolRequerido)) return false;
        return rolRequerido.equalsIgnoreCase(usuario.getRol());
    }

    // Dice si un texto esta vacio
    private boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
