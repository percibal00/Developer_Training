package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {

    //Registro
    public void registrarUsuario(Usuario u) {

        new Thread(() -> {

            try {

                Connection conn = ConexionMySQL.conectar();

                String sql =
                        "INSERT INTO usuarios (nombre, password, puntuacion) VALUES (?, ?, ?)";

                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setString(1, u.getNombre());
                ps.setString(2, u.getPassword());
                ps.setInt(3, u.getPuntuacion());

                ps.executeUpdate();

                System.out.println("USUARIO REGISTRADO");

                conn.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }

    //Login
    public boolean login(String nombre, String password) {

        try {

            Connection conn = ConexionMySQL.conectar();

            String sql =
                    "SELECT * FROM usuarios WHERE nombre=? AND password=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, nombre);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}