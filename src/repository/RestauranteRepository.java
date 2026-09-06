package repository;

import model.Restaurante;
import java.util.ArrayList;
import java.util.List;

public class RestauranteRepository {
    private final List<Restaurante> restaurantes = new ArrayList<>();

    public void guardar(Restaurante restaurante) {
        restaurantes.add(restaurante);
    }

    public List<Restaurante> obtenerTodos() {
        return new ArrayList<>(restaurantes);
    }

    public boolean existePorNit(String nit) {
        return restaurantes.stream()
                .anyMatch(r -> r.getNit().equalsIgnoreCase(nit));
    }
    public Restaurante obtenerPorNit(String nit) {
        return restaurantes.stream()
                .filter(r -> r.getNit().equalsIgnoreCase(nit))
                .findFirst()
                .orElse(null);
    }
}
