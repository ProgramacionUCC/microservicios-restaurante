import model.Propietario;
import model.Restaurante;
import repository.PropietarioRepository;
import repository.RestauranteRepository;
import service.PropietarioService;
import service.RestauranteService;

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
    }
}