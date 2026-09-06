package repository;

import model.Empleado;
import java.util.ArrayList;
import java.util.List;

// Cajon donde se guardan los empleados (lista en memoria)
public class EmpleadoRepository {
    private final List<Empleado> empleados = new ArrayList<>();

    public void guardarEmpleado(Empleado empleado) {
        empleados.add(empleado);
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public List<Empleado> obtenerTodos() {
        return new ArrayList<>(empleados);
    }
}
