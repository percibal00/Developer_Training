package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NivelesDAO {

    public List<Niveles> obtenerTodosLosNiveles() {
        List<Niveles> lista = new ArrayList<>();
        Connection conn = ConexionMySQL.conectar();
        if (conn == null) return lista;

        String sql = "SELECT idNiveles, seccion, numeroNivel FROM Niveles ORDER BY numeroNivel ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Niveles n = new Niveles(
                        rs.getString("idNiveles"),
                        rs.getString("seccion"),
                        rs.getInt("numeroNivel")
                );
                lista.add(n);
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
        return lista;
    }

    public List<Niveles> obtenerNivelesPorSeccion(String seccion) {
        List<Niveles> lista = new ArrayList<>();
        Connection conn = ConexionMySQL.conectar();
        if (conn == null) return lista;

        String sql = "SELECT idNiveles, seccion, numeroNivel FROM Niveles WHERE seccion = ? ORDER BY numeroNivel ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, seccion);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Niveles n = new Niveles(
                            rs.getString("idNiveles"),
                            rs.getString("seccion"),
                            rs.getInt("numeroNivel")
                    );
                    lista.add(n);
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
        return lista;
    }
}
