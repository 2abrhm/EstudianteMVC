/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.miapp;

import com.miapp.vista.EstudianteView;
import com.miapp.controlador.EstudianteController;

/**
 *
 * @author taidy
 */
public class EstudianteMVC {
    public static void main(String[] args) {
        // Lanza la Vista en el hilo de Swing (buena práctica)
        javax.swing.SwingUtilities.invokeLater(() -> {
            EstudianteView vista = new EstudianteView();
            new EstudianteController(vista);   // el controlador conecta todo
            vista.setVisible(true);
        });
    }
}