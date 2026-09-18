package com.miapp.vista;

import com.miapp.controlador.EstudianteController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class EstudianteView extends JFrame {

    // ── Componentes UI ────────────────────────────────────────────────────────
    private JTextField txtNombre;
    private JButton btnBuscar;
    private JButton btnMostrarTodos;

    // Componentes para Agregar (Enunciado 1)
    private JTextField txtAggNombre;
    private JTextField txtAggCarrera;
    private JTextField txtAggPromedio;
    private JButton btnAgregar;

    // Componentes para Ordenar (Enunciado 2)
    private JComboBox<String> cmbCriterio;
    private JButton btnOrdenar;

    private JTable tblResultados;
    private DefaultTableModel modeloTabla;
    private JLabel lblEstado;

    // ── Controlador ───────────────────────────────────────────────────────────
    private EstudianteController controlador;

    public EstudianteView() {
        initComponentes();
        initEventos();
    }

    // ── Inicialización de componentes ─────────────────────────────────────────
    private void initComponentes() {
        setTitle("Búsqueda de Estudiantes — MVC NetBeans");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Contenedor Norte que agrupa los tres formularios
        JPanel panelNorte = new JPanel();
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.Y_AXIS));

        // 1. Panel Buscar estudiante
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("Buscar estudiante"));

        JLabel lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(59, 139, 212));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);

        btnMostrarTodos = new JButton("Mostrar todos");

        panelBusqueda.add(lblNombre);
        panelBusqueda.add(txtNombre);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnMostrarTodos);

        // 2. Panel Agregar estudiante (Enunciado 1)
        JPanel panelAgregar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelAgregar.setBorder(BorderFactory.createTitledBorder("Agregar estudiante"));

        txtAggNombre = new JTextField(12);
        txtAggCarrera = new JTextField(12);
        txtAggPromedio = new JTextField(5);
        btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(new Color(46, 139, 87));
        btnAgregar.setForeground(Color.WHITE);

        panelAgregar.add(new JLabel("Nombre:"));
        panelAgregar.add(txtAggNombre);
        panelAgregar.add(new JLabel("Carrera:"));
        panelAgregar.add(txtAggCarrera);
        panelAgregar.add(new JLabel("Promedio:"));
        panelAgregar.add(txtAggPromedio);
        panelAgregar.add(btnAgregar);

        // 3. Panel Ordenar resultados (Enunciado 2)
        JPanel panelOrdenar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelOrdenar.setBorder(BorderFactory.createTitledBorder("Ordenar resultados"));

        cmbCriterio = new JComboBox<>(new String[]{"Nombre", "Promedio"});
        btnOrdenar = new JButton("Ordenar");

        panelOrdenar.add(new JLabel("Criterio:"));
        panelOrdenar.add(cmbCriterio);
        panelOrdenar.add(btnOrdenar);

        // Apilamos los 3 subpaneles en la parte superior
        panelNorte.add(panelBusqueda);
        panelNorte.add(panelAgregar);
        panelNorte.add(panelOrdenar);

        // Panel Central — Tabla de resultados
        String[] columnas = {"ID", "Nombre", "Carrera", "Promedio"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblResultados = new JTable(modeloTabla);
        tblResultados.setRowHeight(24);
        tblResultados.getTableHeader().setReorderingAllowed(false);
        tblResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tblResultados);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultados"));

        // Panel Inferior — Barra de Estado
        lblEstado = new JLabel("Ingrese un nombre y presione Buscar o use 'Mostrar todos'.");
        lblEstado.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        lblEstado.setForeground(Color.GRAY);

        add(panelNorte, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(lblEstado, BorderLayout.SOUTH);
    }

    // ── Eventos ───────────────────────────────────────────────────────────────
    private void initEventos() {
        // Evento Buscar
        btnBuscar.addActionListener((ActionEvent e) -> {
            if (controlador != null) {
                controlador.buscarEstudiante(txtNombre.getText().trim());
            }
        });

        txtNombre.addActionListener((ActionEvent e) -> btnBuscar.doClick());

        // Evento Mostrar Todos (Enunciado 3)
        btnMostrarTodos.addActionListener((ActionEvent e) -> {
            if (controlador != null) {
                controlador.mostrarTodos();
            }
        });

        // Evento Agregar (Enunciado 1)
        btnAgregar.addActionListener((ActionEvent e) -> {
            if (controlador != null) {
                try {
                    String nombre = txtAggNombre.getText();
                    String carrera = txtAggCarrera.getText();
                    double promedio = Double.parseDouble(txtAggPromedio.getText().trim().replace(",", "."));

                    // Pasamos parámetros simples al Controlador (NO creamos Estudiante aquí)
                    controlador.agregarEstudiante(nombre, carrera, promedio);

                    // Limpiar campos del formulario
                    txtAggNombre.setText("");
                    txtAggCarrera.setText("");
                    txtAggPromedio.setText("");
                } catch (NumberFormatException ex) {
                    mostrarError("Por favor ingrese un promedio numérico válido.");
                }
            }
        });

        // Evento Ordenar (Enunciado 2)
        btnOrdenar.addActionListener((ActionEvent e) -> {
            if (controlador != null) {
                String criterio = (String) cmbCriterio.getSelectedItem();
                controlador.ordenarPor(criterio);
            }
        });
    }

    // ── Métodos para interactuar con la interfaz ──────────────────────────────
    public void mostrarEstudiante(Object[] fila) {
        limpiarTabla();
        agregarFila(fila);
        setEstado("Se encontró 1 estudiante.");
    }

    public void mostrarEstudiantes(List<Object[]> filas) {
        limpiarTabla();
        if (filas == null || filas.isEmpty()) {
            setEstado("No se encontraron estudiantes.");
            return;
        }
        for (Object[] fila : filas) {
            agregarFila(fila);
        }
        setEstado("Se encontraron " + filas.size() + " estudiante(s).");
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        setEstado("Error: " + mensaje);
    }

    public String getNombreBuscado() {
        return txtNombre.getText().trim();
    }

    public void setControlador(EstudianteController controlador) {
        this.controlador = controlador;
    }

    private void agregarFila(Object[] fila) {
        modeloTabla.addRow(fila);
    }

    private void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    private void setEstado(String texto) {
        lblEstado.setText(texto);
    }
}