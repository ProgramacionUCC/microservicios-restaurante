package service;

import model.Restaurante;
import repository.PropietarioRepository;
import repository.RestauranteRepository;

public class RestauranteService {

    private final RestauranteRepository restauranteRepository;
    private final PropietarioRepository propietarioRepository;

    public RestauranteService(RestauranteRepository restauranteRepository, PropietarioRepository propietarioRepository) {
        this.restauranteRepository = restauranteRepository;
        this.propietarioRepository = propietarioRepository;
    }

    public void crearRestaurante(Restaurante restaurante) {
        if (restaurante == null) throw new IllegalArgumentException("Restaurante no puede ser nulo");
        //  Validar que no vengan campos nulos o vacíos (Campos obligatorios)
        if (esNuloOVacio(restaurante.getNombre()) ||
                esNuloOVacio(restaurante.getNit()) ||
                esNuloOVacio(restaurante.getDireccion()) ||
                esNuloOVacio(restaurante.getTelefono()) ||
                esNuloOVacio(restaurante.getUrlLogo()) ||
                esNuloOVacio(restaurante.getIdPropietario())) {
            throw new IllegalArgumentException("Todos los campos (Nombre, NIT, Dirección, Teléfono, UrlLogo e idPropietario) son obligatorios.");
        }

        //  Validar que el nombre no contenga únicamente números
        if (restaurante.getNombre().trim().matches("^\\d+$")) {
            throw new IllegalArgumentException("El nombre del restaurante no puede contener únicamente números.");
        }

        //  Validar que el NIT sea únicamente numérico
        if (!restaurante.getNit().trim().matches("^\\d+$")) {
            throw new IllegalArgumentException("El NIT debe ser únicamente numérico.");
        }

        //  Validar formato de Teléfono: únicamente numérico, opcional '+' al inicio y máximo 13 caracteres (ej: +573005698325)
        String telefono = restaurante.getTelefono().trim();
        if (telefono.length() > 13 || !telefono.matches("^\\+?\\d+$")) {
            throw new IllegalArgumentException("El teléfono debe contener únicamente números, máximo 13 caracteres en total y puede incluir '+' al inicio.");
        }

        //  Validar que el idPropietario corresponda a un usuario existente con rol PROPIETARIO
        boolean existePropietario = propietarioRepository.getPropietarios().stream()
                .anyMatch(p -> p.getDocumentoDeIdentidad().equals(restaurante.getIdPropietario()) && "PROPIETARIO".equals(p.getRol()));
        if (!existePropietario) {
            throw new IllegalArgumentException("El ID del propietario no corresponde a un usuario registrado con dicho rol.");
        }

        // Guardar restaurante en memoria tras pasar todas las validaciones
        restauranteRepository.guardar(restaurante);
    }

    private boolean esNuloOVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
