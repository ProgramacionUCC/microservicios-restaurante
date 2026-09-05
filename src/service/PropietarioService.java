package service;

import model.Propietario;
import repository.PropietarioRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import org.mindrot.BCrypt;

// Se encarga de revisar los datos del propietario y guardarlos.
// Valida, encripta la clave y lo manda al repository.
public class PropietarioService {

    // Plantillas para revisar formato (regex)
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"; // debe tener @ y punto, ej: a@mail.com
    private static final String CELULAR_REGEX = "^\\+?[0-9]+$"; // solo numeros, puede empezar con +
    private static final String DOCUMENTO_REGEX = "^[0-9]+$"; // solo numeros
    private static final int CELULAR_MAX = 13; // celular no puede tener mas de 13 caracteres
    private static final int EDAD_MINIMA = 18; // debe tener 18 o mas

    // Donde se guardan los propietarios
    private final PropietarioRepository repository;

    // Recibe el repository para poder guardar
    public PropietarioService(PropietarioRepository repository) {
        this.repository = repository;
    }

    // Metodo principal: hace todo paso a paso
    public void registrarPropietario(Propietario propietario) {
        validarObligatorios(propietario);           // 1. revisa que nada venga vacio
        validarFormato(propietario);                // 2. revisa que email, celular, documento y edad esten bien
        encriptarClave(propietario);                // 3. cambia la clave por una encriptada
        repository.guardarPropietario(propietario); // 4. lo guarda en la lista
    }

    // Devuelve todos los propietarios guardados
    public List<Propietario> listarPropietarios() {
        return repository.getPropietarios();
    }

    // Revisa que los 7 campos no esten vacios
    private void validarObligatorios(Propietario p) {
        if (vacio(p.getNombre())) throw new IllegalArgumentException("Nombre obligatorio");
        if (vacio(p.getApellido())) throw new IllegalArgumentException("Apellido obligatorio");
        if (vacio(p.getDocumentoDeIdentidad())) throw new IllegalArgumentException("Documento obligatorio");
        if (vacio(p.getCelular())) throw new IllegalArgumentException("Celular obligatorio");
        if (p.getFechaNacimiento() == null) throw new IllegalArgumentException("Fecha nacimiento obligatoria");
        if (vacio(p.getCorreo())) throw new IllegalArgumentException("Correo obligatorio");
        if (vacio(p.getClave())) throw new IllegalArgumentException("Clave obligatoria");
    }

    // Revisa los formatos uno por uno
    private void validarFormato(Propietario p) {
        validarEmail(p.getCorreo());
        validarCelular(p.getCelular());
        validarDocumento(p.getDocumentoDeIdentidad());
        validarEdad(p.getFechaNacimiento());
    }

    // El correo debe tener @ y punto
    private void validarEmail(String correo) {
        if (!correo.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Correo no valido");
        }
    }

    // El celular max 13 y solo numeros con + al inicio si quiere
    private void validarCelular(String celular) {
        if (celular.length() > CELULAR_MAX || !celular.matches(CELULAR_REGEX)) {
            throw new IllegalArgumentException("Celular no valido, max 13 y solo numeros y +");
        }
    }

    // El documento solo puede tener numeros
    private void validarDocumento(String documento) {
        if (!documento.matches(DOCUMENTO_REGEX)) {
            throw new IllegalArgumentException("Documento solo numerico");
        }
    }

    // Debe ser mayor de edad
    private void validarEdad(LocalDate nacimiento) {
        if (!esMayorDeEdad(nacimiento)) {
            throw new IllegalArgumentException("Debe ser mayor de edad");
        }
    }

    // Cambia la clave normal por una encriptada (ya no se puede leer)
    private void encriptarClave(Propietario p) {
        String encriptada = BCrypt.hashpw(p.getClave(), BCrypt.gensalt());
        p.setClave(encriptada);
    }

    // Dice si un texto esta vacio o es null
    private boolean vacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    // Calcula si tiene 18 o mas comparando su fecha de nacimiento con hoy
    private boolean esMayorDeEdad(LocalDate nacimiento) {
        if (nacimiento == null) return false;
        int edad = Period.between(nacimiento, LocalDate.now()).getYears();
        return edad >= EDAD_MINIMA;
    }
}
