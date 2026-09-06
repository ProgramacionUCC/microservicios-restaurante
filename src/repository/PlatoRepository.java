package repository;

import model.Plato;
import java.util.ArrayList;
import java.util.List;

// El cajon donde se guardan los platos (una lista en memoria), igual que los otros repository
public class PlatoRepository {
    private final List<Plato> platos = new ArrayList<>();

    public void guardar(Plato plato) {
        platos.add(plato);
    }

    public List<Plato> obtenerTodos() {
        return new ArrayList<>(platos);
    }

    public List<Plato> obtenerPorRestaurante(String idRestaurante) {
        List<Plato> resultado = new ArrayList<>();
        for (Plato p : platos) {
            if (p.getIdRestaurante().equalsIgnoreCase(idRestaurante)) {
                resultado.add(p);
            }
        }
        return resultado;
    }
}