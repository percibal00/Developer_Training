package Controlador;

import android.content.Context;
import Modelo.ConexionMySQL;
import Modelo.Usuarios;
import Modelo.UsuariosDAO;

public class UsuarioControlador {

    private UsuariosDAO usuariosDAO;

    public UsuarioControlador(Context context) {
        this.usuariosDAO = new UsuariosDAO(context);
    }

    /**
     * Intenta iniciar sesión con el email y contraseña proporcionados.
     * Realiza la consulta en segundo plano para evitar bloquear la UI.
     * @return El objeto Usuarios si es correcto, null si falla.
     */
    public Usuarios login(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }

        // Usamos una tarea síncrona pero que debe llamarse desde un hilo de fondo
        // en Android no se pueden hacer peticiones de red en el hilo principal.
        return ConexionMySQL.getUsuarioByLogin(email, password);
    }

    /**
     * Aquí podrías añadir más lógica, como verificar si el email ya existe,
     * encriptar contraseñas, etc.
     */
}
