package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuariosDAO {

    public UsuariosDAO() {
    }

    /**
     * Busca un usuario en la base de datos MySQL por email y contraseña.
     */
    public Usuarios getUsuarioByLogin(String email, String password) {
        Usuarios usuario = null;
        Connection conn = ConexionMySQL.conectar();
        if (conn == null) return null;

        String query = "SELECT * FROM Usuarios WHERE EMAIL = ? AND CONTRASENA = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuarios();
                    usuario.setIdUsuario(rs.getInt("ID_USUARIO"));
                    usuario.setNombre(rs.getString("NOMBRE"));
                    usuario.setEmail(rs.getString("EMAIL"));
                    usuario.setEdad(rs.getInt("EDAD"));
                    usuario.setFechaRegistro(rs.getString("FECHA_REGISTRO"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return usuario;
    }

    /**
     * Registra un nuevo usuario en la base de datos MySQL.
     */
    public boolean registrarUsuario(Usuarios u) {
        Connection conn = ConexionMySQL.conectar();
        if (conn == null) return false;

        String query = "INSERT INTO Usuarios (NOMBRE, EMAIL, CONTRASENA, EDAD) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getContrasena());
            ps.setInt(4, u.getEdad());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
