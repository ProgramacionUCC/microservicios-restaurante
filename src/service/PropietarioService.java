package service;

import model.Propietario;
import repository.PropietarioRepository;

import java.util.List;
import java.time.LocalDate;
import java.time.Period;
import org.mindrot.BCrypt;

// registra y lista propietarios.
public class PropietarioService {
    // Repository que usa para guardar.
    private PropietarioRepository propietarioRepository;

    // Al crearlo hay que pasarle el repository.
    public PropietarioService(PropietarioRepository propietarioRepository) {
        this.propietarioRepository = propietarioRepository;
    }

    // Revisa obligatorios, encripta la clave con bcrypt y lo guarda.
    public void registrarPropietario(Propietario propietario) {
        if (vacio(propietario.getNombre())) throw new IllegalArgumentException("Nombre obligatorio");
        if (vacio(propietario.getApellido())) throw new IllegalArgumentException("Apellido obligatorio");
        if (vacio(propietario.getDocumentoDeIdentidad())) throw new IllegalArgumentException("Documento obligatorio");
        if (vacio(propietario.getCelular())) throw new IllegalArgumentException("Celular obligatorio");
        if (propietario.getFechaNacimiento() == null) throw new IllegalArgumentException("Fecha nacimiento obligatoria");
        if (vacio(propietario.getCorreo())) throw new IllegalArgumentException("Correo obligatorio");
        if (vacio(propietario.getClave())) throw new IllegalArgumentException("Clave obligatoria");
        // Email con estructura valida.
        if (!propietario.getCorreo().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) throw new IllegalArgumentException("Correo no valido");
        // Celular maximo 13 caracteres, solo numeros y +.
        if (propietario.getCelular().length() > 13 || !propietario.getCelular().matches("^\\+?[0-9]+$")) throw new IllegalArgumentException("Celular no valido, max 13 y solo numeros y +");
        // Documento solo numerico.
        if (!propietario.getDocumentoDeIdentidad().matches("^[0-9]+$")) throw new IllegalArgumentException("Documento solo numerico");
        // Mayor de edad (18 o mas). Fecha ya es LocalDate.
        if (!esMayorDeEdad(propietario.getFechaNacimiento())) throw new IllegalArgumentException("Debe ser mayor de edad");
        String encriptada = BCrypt.hashpw(propietario.getClave(), BCrypt.gensalt());
        propietario.setClave(encriptada);
        propietarioRepository.guardarPropietario(propietario);
        System.out.println("Propietario registrado");
    }

    // Dice si un texto viene vacio o nulo.
    private boolean vacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    // Calcula si tiene 18 o mas.
    private boolean esMayorDeEdad(LocalDate nacimiento) {
        if (nacimiento == null) return false;
        int edad = Period.between(nacimiento, LocalDate.now()).getYears();
        return edad >= 18;
    }

    public List<Propietario> listarPropietarios() {
        return propietarioRepository.getPropietarios();
    }
}
