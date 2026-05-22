package Controlador;

import android.content.Context;
import Modelo.Usuarios;
import Modelo.UsuariosDAO;

public class UsuarioControlador {

    private UsuariosDAO usuariosDAO;

    public UsuarioControlador(Context context) {
        this.usuariosDAO = new UsuariosDAO(context);
    }

    /**
     * Intenta iniciar sesión con el email y contraseña proporcionados.
     * @return El objeto Usuarios si es correcto, null si falla.
     */
    public Usuarios login(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }

        return usuariosDAO.getUsuarioByLogin(email, password);
    }

    public boolean validarEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    public boolean validarPassword(String password) {
        return password != null && password.length() >= 4;
    }
}
