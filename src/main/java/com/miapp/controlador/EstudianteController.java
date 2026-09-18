package com.miapp.controlador;

import com.miapp.modelo.Estudiante;
import com.miapp.vista.EstudianteView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EstudianteController {

    private EstudianteView vista;
    // Cambiamos el arreglo fijo por ArrayList para permitir agregar nuevos elementos
    private List<Estudiante> estudiantes;
    // Guarda los últimos resultados mostrados en la tabla para poder ordenarlos (Enunciado 2)
    private List<Estudiante> ultimosResultados;
    // Alterna el orden entre ascendente y descendente
    private boolean ordenAscendente = true;

    public EstudianteController(EstudianteView vista) {
        this.vista = vista;
        this.vista.setControlador(this);
        cargarDatos();
        this.ultimosResultados = new ArrayList<>();
    }

    private void cargarDatos() {
        estudiantes = new ArrayList<>();
        estudiantes.add(new Estudiante(1,  "Ana García",        "Ingeniería de Sistemas",  4.5));
        estudiantes.add(new Estudiante(2,  "Carlos López",      "Ingeniería Civil",        3.8));
        estudiantes.add(new Estudiante(3,  "María Rodríguez",   "Medicina",                4.9));
        estudiantes.add(new Estudiante(4,  "José Martínez",     "Derecho",                 3.5));
        estudiantes.add(new Estudiante(5,  "Laura Sánchez",     "Administración",          4.1));
        estudiantes.add(new Estudiante(6,  "Andrés Torres",     "Ingeniería de Sistemas",  3.9));
        estudiantes.add(new Estudiante(7,  "Valentina Gómez",   "Psicología",              4.3));
        estudiantes.add(new Estudiante(8,  "Luis Herrera",      "Economía",                3.7));
        estudiantes.add(new Estudiante(9,  "Sofía Díaz",        "Ingeniería Civil",        4.6));
        estudiantes.add(new Estudiante(10, "Juliana Morales",   "Medicina",                4.8));
        estudiantes.add(new Estudiante(11, "Ana Milena Ruiz",   "Derecho",                 4.0));
        estudiantes.add(new Estudiante(12, "Carlos Andrés Paz", "Administración",          3.6));
    }

    // ── Lógica de búsqueda ────────────────────────────────────────────────────
    public void buscarEstudiante(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            vista.mostrarError("Por favor ingrese un nombre para buscar.");
            return;
        }

        List<Estudiante> resultados = new ArrayList<>();
        String criterioBajo = criterio.toLowerCase().trim();

        for (Estudiante e : estudiantes) {
            if (e.getNombre().toLowerCase().contains(criterioBajo)) {
                resultados.add(e);
            }
        }

        // Guardamos los resultados para que se puedan ordenar luego
        this.ultimosResultados = new ArrayList<>(resultados);
        this.ordenAscendente = true;

        if (resultados.isEmpty()) {
            vista.mostrarEstudiantes(new ArrayList<>());
        } else if (resultados.size() == 1) {
            vista.mostrarEstudiante(convertirAFila(resultados.get(0)));
        } else {
            vista.mostrarEstudiantes(convertirAFilas(resultados));
        }
    }

    // ── Enunciado 3: Mostrar Todos ────────────────────────────────────────────
    public void mostrarTodos() {
        this.ultimosResultados = new ArrayList<>(this.estudiantes);
        this.ordenAscendente = true;
        vista.mostrarEstudiantes(convertirAFilas(this.ultimosResultados));
    }

    // ── Enunciado 1: Registrar un nuevo estudiante ────────────────────────────
    public void agregarEstudiante(String nombre, String carrera, double promedio) {
        // Validaciones en el Controlador
        if (nombre == null || nombre.trim().isEmpty()) {
            vista.mostrarError("El nombre no puede estar vacío.");
            return;
        }

        if (promedio < 0.0 || promedio > 5.0) {
            vista.mostrarError("El promedio debe estar entre 0.0 y 5.0.");
            return;
        }

        // El Controlador construye el objeto Estudiante (cumpliendo MVC)
        int nuevoId = estudiantes.size() + 1;
        Estudiante nuevo = new Estudiante(nuevoId, nombre.trim(), carrera.trim(), promedio);
        estudiantes.add(nuevo);

        // Refrescar mostrando todos
        mostrarTodos();
    }

    // ── Enunciado 2: Ordenar resultados ───────────────────────────────────────
    public void ordenarPor(String criterio) {
        if (ultimosResultados == null || ultimosResultados.isEmpty()) {
            vista.mostrarError("No hay resultados en la tabla para ordenar.");
            return;
        }

        Comparator<Estudiante> comparador = null;

        if ("Nombre".equalsIgnoreCase(criterio)) {
            comparador = Comparator.comparing(Estudiante::getNombre, String.CASE_INSENSITIVE_ORDER);
        } else if ("Promedio".equalsIgnoreCase(criterio)) {
            comparador = Comparator.comparingDouble(Estudiante::getPromedio);
        }

        if (comparador != null) {
            // Alternar ascendente/descendente
            if (!ordenAscendente) {
                comparador = comparador.reversed();
            }

            ultimosResultados.sort(comparador);
            ordenAscendente = !ordenAscendente; // Cambiar para el próximo clic

            vista.mostrarEstudiantes(convertirAFilas(ultimosResultados));
        }
    }

    // ── Traducción a datos genéricos (Modelo -> Vista) ────────────────────────
    private Object[] convertirAFila(Estudiante e) {
        return new Object[]{
            e.getId(),
            e.getNombre(),
            e.getCarrera(),
            String.format("%.2f", e.getPromedio())
        };
    }

    private List<Object[]> convertirAFilas(List<Estudiante> lista) {
        List<Object[]> filas = new ArrayList<>();
        for (Estudiante e : lista) {
            filas.add(convertirAFila(e));
        }
        return filas;
    }
}