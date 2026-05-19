package Controlador;

import android.content.Context;

import Modelo.ConexionMySQL;
import Modelo.Usuarios;
import Modelo.UsuariosDAO;

public class RegistroControlador {

    private UsuariosDAO usuariosDAO;

    public RegistroControlador(Context context) {
        this.usuariosDAO = new UsuariosDAO(context);
    }

    public Usuarios Register(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }

        // Usamos una tarea síncrona pero que debe llamarse desde un hilo de fondo
        // en Android no se pueden hacer peticiones de red en el hilo principal.
        return ConexionMySQL.getUsuarioByLogin(email, password);
    }
    public boolean validarEmail(String email) {

        return email.contains("@") && email.contains(".");
    }
}
