package repository;

import model.Propietario;

import java.util.ArrayList;
import java.util.List;

// Es el cajon donde se guardan los propietarios (una lista en memoria)
public class PropietarioRepository {
    // Lista donde se van agregando
    private List<Propietario> propietarios = new ArrayList<>();

    // Agrega uno a la lista
    public void guardarPropietario(Propietario propietario) {
        propietarios.add(propietario);
    }

    // Devuelve todos los que hay guardados
    public List<Propietario> getPropietarios() {
        return propietarios;
    }
}
