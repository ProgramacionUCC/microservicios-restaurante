import model.Propietario;
import repository.PropietarioRepository;
import service.PropietarioService;

import java.time.LocalDate;

public class Main {
    // Aqui empieza el programa.
    public static void main(String[] args) {
        // Donde se guardan.
        PropietarioRepository repo = new PropietarioRepository();
        // El que registra usando el repo.
        PropietarioService service = new PropietarioService(repo);

        // Un propietario con sus 7 datos.
        Propietario p1 = new Propietario("Carlos", "Perez", "12345678", "+573005698325", LocalDate.of(1990, 5, 10), "carlos@mail.com", "abc123");

        // Lo guardamos (ahi se encripta la clave) y lo mostramos.
        service.registrarPropietario(p1);
        System.out.println(p1);
        // Mostramos la clave ya encriptada para comprobar.
        System.out.println("Clave guardada: " + p1.getClave());
    }
}
