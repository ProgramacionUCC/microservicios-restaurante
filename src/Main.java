import model.Propietario;
import repository.PropietarioRepository;
import service.PropietarioService;

import java.time.LocalDate;

// Prueba simple para ver si todo funciona
public class Main {
    public static void main(String[] args) {
        // 1. Donde se van a guardar
        PropietarioRepository repo = new PropietarioRepository();

        // 2. El que revisa y guarda (usa el repo)
        PropietarioService service = new PropietarioService(repo);

        // 3. Creamos un propietario con datos de ejemplo
        Propietario p1 = new Propietario(
                "Carlos",
                "Perez",
                "12345678",
                "+573005698325",
                LocalDate.of(1990, 5, 10), // fecha de nacimiento
                "carlos@mail.com",
                "abc123" // clave normal, luego se encripta sola
        );

        // 4. Lo revisa y lo guarda (si algo esta mal, avisa con error)
        service.registrarPropietario(p1);

        // 5. Lo mostramos
        System.out.println(p1);
        System.out.println("Clave guardada: " + p1.getClave()); // ya sale encriptada tipo $2a$10$...
    }
}
