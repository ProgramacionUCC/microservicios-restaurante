import model.Propietario;
import model.Restaurante;
import repository.PropietarioRepository;
import repository.RestauranteRepository;
import service.PropietarioService;
import service.RestauranteService;
import model.Plato;
import model.Empleado;
import repository.PlatoRepository;
import repository.EmpleadoRepository;
import service.PlatoService;
import service.EmpleadoService;
import service.AutenticacionService;

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

        // --- HU-04: modificar plato (solo precio y descripcion, solo dueño) ---
        System.out.println("\n--- Pruebas HU-04 ---");

        // caso valido: cambia precio y descripcion
        try {
            platoService.modificarPlato("Bandeja Paisa", "900123456", 28000, "Bandeja actualizada con aguacate", "12345678");
            System.out.println("Plato modificado: " + platoValido);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // error: intenta modificar plato de otro restaurante (no es dueño)
        try {
            platoService.modificarPlato("Bandeja Paisa", "900123456", 30000, "Intento otro dueño", "99999999");
        } catch (IllegalArgumentException e) {
            System.out.println("Error esperado (no es dueño HU-04): " + e.getMessage());
        }

        // error: precio invalido
        try {
            platoService.modificarPlato("Bandeja Paisa", "900123456", 0, "Precio malo", "12345678");
        } catch (IllegalArgumentException e) {
            System.out.println("Error esperado (precio HU-04): " + e.getMessage());
        }

        System.out.println("Plato final: " + platoValido);

        // --- HU-05: autenticacion (correo y clave, intentos ilimitados, permisos por rol) ---
        System.out.println("\n--- Pruebas HU-05 ---");
        AutenticacionService authService = new AutenticacionService(propRepo);

        // login correcto
        try {
            Propietario logueado = authService.iniciarSesion("carlos@mail.com", "abc123");
            System.out.println("Login OK: " + logueado.getCorreo() + " rol=" + logueado.getRol());
            System.out.println("Tiene permiso PROPIETARIO? " + authService.tienePermiso(logueado, "PROPIETARIO"));
            System.out.println("Tiene permiso CLIENTE? " + authService.tienePermiso(logueado, "CLIENTE"));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // clave incorrecta
        try {
            authService.iniciarSesion("carlos@mail.com", "claveMala");
        } catch (IllegalArgumentException e) {
            System.out.println("Error esperado (clave): " + e.getMessage());
        }

        // correo no existe
        try {
            authService.iniciarSesion("noexiste@mail.com", "abc123");
        } catch (IllegalArgumentException e) {
            System.out.println("Error esperado (usuario): " + e.getMessage());
        }

        // intentos ilimitados: puede volver a intentar sin bloqueo
        try {
            authService.iniciarSesion("carlos@mail.com", "otraMala");
        } catch (IllegalArgumentException e) {
            System.out.println("Error esperado (intento ilimitado): " + e.getMessage());
        }

        // --- HU-05: validacion de autenticacion por endpoint ---
        System.out.println("\n--- Pruebas HU-05: Validacion por endpoint ---");

        // Crear admin para probar creacion de propietario/restaurante solo por admin
        Propietario admin = new Propietario("Admin", "Root", "99999999", "+573000000001", LocalDate.of(1985, 1, 1), "admin@mail.com", "admin123", "ADMINISTRADOR");
        propService.registrarPropietario(admin);
        Propietario adminLogueado = authService.iniciarSesion("admin@mail.com", "admin123");
        Propietario propietarioLogueado = authService.iniciarSesion("carlos@mail.com", "abc123");
        System.out.println("Admin logueado: " + adminLogueado);
        System.out.println("Propietario logueado: " + propietarioLogueado);

        // 1. Creacion de propietario solo por ADMINISTRADOR
        System.out.println("\n[1] Crear propietario solo admin:");
        Propietario nuevoProp = new Propietario("Ana", "Lopez", "11111111", "+573001112233", LocalDate.of(1995, 6, 15), "ana@mail.com", "clave123");
        try {
            propService.registrarPropietario(nuevoProp, adminLogueado);
            System.out.println("OK: admin creo propietario: " + nuevoProp);
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
        Propietario otroProp = new Propietario("Luis", "Gomez", "22222222", "+573002223344", LocalDate.of(1992, 3, 10), "luis@mail.com", "clave123");
        try {
            propService.registrarPropietario(otroProp, propietarioLogueado);
        } catch (Exception e) { System.out.println("Error esperado (solo admin crea propietario): " + e.getMessage()); }

        // 2. Creacion de restaurante solo por ADMINISTRADOR
        System.out.println("\n[2] Crear restaurante solo admin:");
        Restaurante r2 = new Restaurante("El Buen Sabor", "900999888", "Cra 5 # 10-20", "+573005698326", "http://logo.com/2.png", "12345678");
        try {
            restService.crearRestaurante(r2, adminLogueado);
            System.out.println("OK: admin creo restaurante: " + r2);
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
        Restaurante r3 = new Restaurante("Fallo Restaurante", "900999889", "Cra 5 # 10-21", "+573005698327", "http://logo.com/3.png", "12345678");
        try {
            restService.crearRestaurante(r3, propietarioLogueado);
        } catch (Exception e) { System.out.println("Error esperado (solo admin crea restaurante): " + e.getMessage()); }

        // 3. Creacion de empleado solo por PROPIETARIO dueño
        System.out.println("\n[3] Crear empleado solo propietario dueño:");
        EmpleadoRepository empRepo = new EmpleadoRepository();
        EmpleadoService empService = new EmpleadoService(empRepo, restRepo);
        Empleado emp1 = new Empleado("Pedro", "Empleado", "33333333", "+573003334455", LocalDate.of(1998, 7, 20), "pedro@mail.com", "emp123", "900123456");
        try {
            empService.crearEmpleado(emp1, propietarioLogueado);
            System.out.println("OK: propietario creo empleado: " + emp1);
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
        Empleado emp2 = new Empleado("Fallo", "Empleado", "44444444", "+573004445566", LocalDate.of(1999, 8, 21), "fallo@mail.com", "emp123", "900123456");
        try {
            empService.crearEmpleado(emp2, adminLogueado);
        } catch (Exception e) { System.out.println("Error esperado (solo propietario crea empleado): " + e.getMessage()); }
        System.out.println("Empleados guardados: " + empRepo.obtenerTodos().size());

        // 4. Creacion/modificacion de plato solo por propietario del restaurante (con objeto autenticado)
        System.out.println("\n[4] Crear/modificar plato solo propietario dueño (con Propietario autenticado):");
        Plato platoConAuth = new Plato("Arroz con Pollo", 18000, "Arroz casero", "http://img.com/arroz.png", "Almuerzo", "900123456");
        try {
            platoService.crearPlato(platoConAuth, propietarioLogueado);
            System.out.println("OK: propietario dueño creo plato con auth: " + platoConAuth);
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
        Plato platoFalloAuth = new Plato("Fallo Plato", 15000, "No debe crear", "http://img.com/fallo.png", "Almuerzo", "900123456");
        try {
            platoService.crearPlato(platoFalloAuth, adminLogueado);
        } catch (Exception e) { System.out.println("Error esperado (admin no es dueño, no crea plato): " + e.getMessage()); }
        try {
            platoService.modificarPlato("Bandeja Paisa", "900123456", 30000, "Modificado con auth propietario", propietarioLogueado);
            System.out.println("OK: propietario modifico plato con auth: " + platoValido);
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
        try {
            platoService.modificarPlato("Bandeja Paisa", "900123456", 31000, "Intento admin", adminLogueado);
        } catch (Exception e) { System.out.println("Error esperado (admin no modifica plato de otro): " + e.getMessage()); }

        System.out.println("\n--- Fin pruebas HU-05 validacion endpoints ---");
    }
}