import model.Propietario;
import model.Restaurante;
import repository.PropietarioRepository;
import repository.RestauranteRepository;
import service.PropietarioService;
import service.RestauranteService;
import model.Plato;
import repository.PlatoRepository;
import service.PlatoService;

import java.time.LocalDate;

// Demo HU-01 + HU-02 sin tocar Propietario
public class Main {
    public static void main(String[] args) {
        // --- HU-01: crear propietario ---
        PropietarioRepository propRepo = new PropietarioRepository();
        PropietarioService propService = new PropietarioService(propRepo);

        Propietario p1 = new Propietario(
                "Carlos",
                "Perez",
                "12345678",
                "+573005698325",
                LocalDate.of(1990, 5, 10),
                "carlos@mail.com",
                "abc123"
        );
        propService.registrarPropietario(p1);
        System.out.println(p1);
        System.out.println("Clave guardada: " + p1.getClave());

        // --- HU-02: crear restaurante (requiere propietario existente) ---
        RestauranteRepository restRepo = new RestauranteRepository();
        RestauranteService restService = new RestauranteService(restRepo, propRepo);

        Restaurante r1 = new Restaurante(
                "La Plazoleta",
                "900123456",           // NIT solo números
                "Cra 1 # 2-3",         // direccion
                "+573005698325",       // telefono max 13, + opcional
                "http://logo.com/logo.png",
                "12345678"             // idPropietario = documento de p1
        );

        restService.crearRestaurante(r1);
        System.out.println(r1);
        System.out.println("Restaurantes guardados: " + restRepo.obtenerTodos().size());

        // --- HU-03: crear plato (requiere restaurante existente y su propietario) ---
        PlatoRepository platoRepo = new PlatoRepository();
        PlatoService platoService = new PlatoService(platoRepo, restRepo);

        System.out.println("\n--- Pruebas HU-03 ---");

        Plato platoValido = new Plato(
                "Bandeja Paisa", 25000, "Plato típico antioqueño",
                "http://img.com/bandeja.png", "Almuerzo", "900123456"
        );
        try {
            platoService.crearPlato(platoValido, "12345678");
            System.out.println("Plato creado: " + platoValido);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        Plato platoPrecioInvalido = new Plato(
                "Sancocho", 0, "Sopa tradicional",
                "http://img.com/sancocho.png", "Sopa", "900123456"
        );
        try {
            platoService.crearPlato(platoPrecioInvalido, "12345678");
        } catch (IllegalArgumentException e) {
            System.out.println("Error esperado (precio): " + e.getMessage());
        }

        Plato platoOtroDueno = new Plato(
                "Mondongo", 18000, "Sopa de mondongo",
                "http://img.com/mondongo.png", "Sopa", "900123456"
        );
        try {
            platoService.crearPlato(platoOtroDueno, "99999999");
        } catch (IllegalArgumentException e) {
            System.out.println("Error esperado (no es dueño): " + e.getMessage());
        }

        Plato platoRestauranteInexistente = new Plato(
                "Ajiaco", 20000, "Sopa bogotana",
                "http://img.com/ajiaco.png", "Sopa", "000000000"
        );
        try {
            platoService.crearPlato(platoRestauranteInexistente, "12345678");
        } catch (IllegalArgumentException e) {
            System.out.println("Error esperado (restaurante no existe): " + e.getMessage());
        }

        System.out.println("Platos guardados: " + platoRepo.obtenerTodos().size());
    }
}