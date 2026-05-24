package Controlador;

import Modelo.Usuarios;
import Modelo.UsuariosDAO;

public class UsuarioControlador {

    private UsuariosDAO usuariosDAO;

    public UsuarioControlador() {
        this.usuariosDAO = new UsuariosDAO();
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

    /**
     * Registra un nuevo usuario.
     * @return true si el registro fue exitoso, false en caso contrario.
     */
    public boolean registrarUsuario(Usuarios u) {
        if (u == null || !validarEmail(u.getEmail()) || !validarPassword(u.getContrasena())) {
            return false;
        }
        return usuariosDAO.registrarUsuario(u);
    }

    public boolean validarEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    public boolean validarPassword(String password) {
        return password != null && password.length() >= 4;
    }
}
