package service;

import model.Plato;
import model.Restaurante;
import repository.PlatoRepository;
import repository.RestauranteRepository;

public class PlatoService {

    private final PlatoRepository platoRepository;
    private final RestauranteRepository restauranteRepository;

    public PlatoService(PlatoRepository platoRepository, RestauranteRepository restauranteRepository) {
        this.platoRepository = platoRepository;
        this.restauranteRepository = restauranteRepository;
    }

    // idPropietarioAutenticado simula, por ahora, el usuario que hizo login (hasta que exista HU-05)
    public void crearPlato(Plato plato, String idPropietarioAutenticado) {
        if (plato == null) throw new IllegalArgumentException("Plato no puede ser nulo");

        if (esNuloOVacio(plato.getNombre()) ||
                esNuloOVacio(plato.getDescripcion()) ||
                esNuloOVacio(plato.getUrlImagen()) ||
                esNuloOVacio(plato.getCategoria()) ||
                esNuloOVacio(plato.getIdRestaurante())) {
            throw new IllegalArgumentException("Todos los campos (Nombre, Precio, Descripción, UrlImagen, Categoría e idRestaurante) son obligatorios.");
        }

        if (plato.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio del plato debe ser un número entero positivo mayor a 0.");
        }

        Restaurante restaurante = restauranteRepository.obtenerPorNit(plato.getIdRestaurante());
        if (restaurante == null) {
            throw new IllegalArgumentException("El restaurante indicado no existe.");
        }

        if (!restaurante.getIdPropietario().equals(idPropietarioAutenticado)) {
            throw new IllegalArgumentException("Solo el propietario del restaurante puede crear platos en él.");
        }

        platoRepository.guardar(plato);
    }

    // HU-04: solo precio y descripcion, y solo el dueño del restaurante puede
    public void modificarPlato(String nombrePlato, String idRestaurante, int nuevoPrecio, String nuevaDescripcion, String idPropietarioAutenticado) {
        // revisa que nada venga vacio
        if (esNuloOVacio(nombrePlato) || esNuloOVacio(idRestaurante) || esNuloOVacio(nuevaDescripcion) || esNuloOVacio(idPropietarioAutenticado)) {
            throw new IllegalArgumentException("Nombre del plato, idRestaurante, descripcion y propietario son obligatorios.");
        }
        // precio debe ser mayor a 0
        if (nuevoPrecio <= 0) {
            throw new IllegalArgumentException("El precio del plato debe ser un número entero positivo mayor a 0.");
        }
        // el restaurante debe existir
        Restaurante restaurante = restauranteRepository.obtenerPorNit(idRestaurante);
        if (restaurante == null) {
            throw new IllegalArgumentException("El restaurante indicado no existe.");
        }
        // solo el dueño puede modificar
        if (!restaurante.getIdPropietario().equals(idPropietarioAutenticado)) {
            throw new IllegalArgumentException("Solo el propietario del restaurante puede modificar sus platos.");
        }
        // buscar el plato por nombre y restaurante
        Plato plato = platoRepository.obtenerTodos().stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombrePlato) && p.getIdRestaurante().equalsIgnoreCase(idRestaurante))
                .findFirst()
                .orElse(null);
        if (plato == null) {
            throw new IllegalArgumentException("El plato indicado no existe en ese restaurante.");
        }
        // solo se cambia precio y descripcion
        plato.setPrecio(nuevoPrecio);
        plato.setDescripcion(nuevaDescripcion);
    }

    private boolean esNuloOVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}