package repository;

import model.Propietario;

import java.util.ArrayList;
import java.util.List;

// Guarda los propietarios en una lista en memoria.
public class PropietarioRepository {
    // Lista donde se guardan.
    private List<Propietario> propietarios = new ArrayList<>();

    // Agrega el propietario recibido a la lista.
    public void guardarPropietario(Propietario propietario) {
        propietarios.add(propietario);
    }

    public List<Propietario> getPropietarios() {
        return propietarios;
    }
}
