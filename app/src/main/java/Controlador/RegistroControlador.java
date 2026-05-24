package Controlador;

import android.content.Context;

import Modelo.Usuarios;
import Modelo.UsuariosDAO;

public class RegistroControlador {

    private UsuariosDAO usuariosDAO;

    public RegistroControlador(Context context) {
        this.usuariosDAO = new UsuariosDAO(context);
    }

    public boolean registrar(String nombre, String email, String password, int edad) {
        if (email == null || !validarEmail(email) || password == null || password.length() < 4) {
            return false;
        }

        Usuarios nuevoUsuario = new Usuarios();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setContrasena(password);
        nuevoUsuario.setEdad(edad);

        return usuariosDAO.registrarUsuario(nuevoUsuario);
    }
    public boolean validarEmail(String email) {

        return email.contains("@") && email.contains(".");
    }
}
