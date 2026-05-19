package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConexionMySQL {

    //URL
private static final String URL =
            "jdbc:mysql://10.0.2.2:3306/developer_training_db?serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection conectar() {

        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }

    /**
     * Busca un usuario en la base de datos MySQL por email y contraseña.
     */
    public static Usuarios getUsuarioByLogin(String email, String password) {
        Usuarios usuario = null;
        Connection conn = conectar();
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
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return usuario;
    }
}