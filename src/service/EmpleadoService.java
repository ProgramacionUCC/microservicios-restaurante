package service;

import model.Empleado;
import model.Propietario;
import model.Restaurante;
import repository.EmpleadoRepository;
import repository.RestauranteRepository;
import org.mindrot.BCrypt;

import java.time.LocalDate;
import java.time.Period;

// HU-05: solo el propietario del restaurante puede crear empleados
public class EmpleadoService {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String CELULAR_REGEX = "^\\+?[0-9]+$";
    private static final String DOCUMENTO_REGEX = "^[0-9]+$";
    private static final int CELULAR_MAX = 13;
    private static final int EDAD_MINIMA = 18;

    private final EmpleadoRepository empleadoRepository;
    private final RestauranteRepository restauranteRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository, RestauranteRepository restauranteRepository) {
        this.empleadoRepository = empleadoRepository;
        this.restauranteRepository = restauranteRepository;
    }

    // Metodo con autenticacion: solo PROPIETARIO dueño del restaurante puede crear empleado
    public void crearEmpleado(Empleado empleado, Propietario usuarioAutenticado) {
        // 1. Validar autenticacion y rol
        if (usuarioAutenticado == null) {
            throw new IllegalArgumentException("Debe estar autenticado para crear empleado.");
        }
        if (!"PROPIETARIO".equalsIgnoreCase(usuarioAutenticado.getRol())) {
            throw new IllegalArgumentException("Solo el propietario puede crear empleados.");
        }

        // 2. Validaciones de campos obligatorios
        validarObligatorios(empleado);
        validarFormato(empleado);

        // 3. Validar restaurante existe
        if (empleado.getIdRestaurante() == null || empleado.getIdRestaurante().trim().isEmpty()) {
            throw new IllegalArgumentException("El restaurante es obligatorio.");
        }
        Restaurante restaurante = restauranteRepository.obtenerPorNit(empleado.getIdRestaurante());
        if (restaurante == null) {
            throw new IllegalArgumentException("El restaurante indicado no existe.");
        }

        // 4. Validar que el propietario autenticado sea dueño de ese restaurante
        if (!restaurante.getIdPropietario().equals(usuarioAutenticado.getDocumentoDeIdentidad())) {
            throw new IllegalArgumentException("Solo el propietario del restaurante puede crear empleados en él.");
        }

        // 5. Encriptar clave y guardar
        encriptarClave(empleado);
        empleadoRepository.guardarEmpleado(empleado);
    }

    private void validarObligatorios(Empleado e) {
        if (vacio(e.getNombre())) throw new IllegalArgumentException("Nombre obligatorio");
        if (vacio(e.getApellido())) throw new IllegalArgumentException("Apellido obligatorio");
        if (vacio(e.getDocumentoDeIdentidad())) throw new IllegalArgumentException("Documento obligatorio");
        if (vacio(e.getCelular())) throw new IllegalArgumentException("Celular obligatorio");
        if (e.getFechaNacimiento() == null) throw new IllegalArgumentException("Fecha nacimiento obligatoria");
        if (vacio(e.getCorreo())) throw new IllegalArgumentException("Correo obligatorio");
        if (vacio(e.getClave())) throw new IllegalArgumentException("Clave obligatoria");
        if (vacio(e.getIdRestaurante())) throw new IllegalArgumentException("Restaurante obligatorio");
    }

    private void validarFormato(Empleado e) {
        if (!e.getCorreo().matches(EMAIL_REGEX)) throw new IllegalArgumentException("Correo no valido");
        if (e.getCelular().length() > CELULAR_MAX || !e.getCelular().matches(CELULAR_REGEX)) {
            throw new IllegalArgumentException("Celular no valido, max 13 y solo numeros y +");
        }
        if (!e.getDocumentoDeIdentidad().matches(DOCUMENTO_REGEX)) throw new IllegalArgumentException("Documento solo numerico");
        if (!esMayorDeEdad(e.getFechaNacimiento())) throw new IllegalArgumentException("Debe ser mayor de edad");
    }

    private void encriptarClave(Empleado e) {
        String encriptada = BCrypt.hashpw(e.getClave(), BCrypt.gensalt());
        e.setClave(encriptada);
    }

    private boolean vacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    private boolean esMayorDeEdad(LocalDate nacimiento) {
        if (nacimiento == null) return false;
        int edad = Period.between(nacimiento, LocalDate.now()).getYears();
        return edad >= 18;
    }
}
