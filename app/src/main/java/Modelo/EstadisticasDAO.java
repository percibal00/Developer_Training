package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EstadisticasDAO {

    public void guardarEstadisticas(Estadisticas e) {
        new Thread(() -> {
            Connection conn = ConexionMySQL.conectar();
            if (conn == null) return;

            String sql = "INSERT INTO Estadisticas (idUsuario, nivelesCompletados, tiempoTotalJuego, rachas, ultimoNivelJugado) " +
                         "VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE " +
                         "nivelesCompletados = ?, tiempoTotalJuego = ?, rachas = ?, ultimoNivelJugado = ?";
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, e.getIdUsuario());
                ps.setString(2, e.getNivelesCompletados());
                ps.setInt(3, e.getTiempoTotalJuego());
                ps.setInt(4, e.getRachas());
                ps.setString(5, e.getUltimoNivelJugado());
                
                ps.setString(6, e.getNivelesCompletados());
                ps.setInt(7, e.getTiempoTotalJuego());
                ps.setInt(8, e.getRachas());
                ps.setString(9, e.getUltimoNivelJugado());

                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                try { if (conn != null) conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }).start();
    }

    public Estadisticas obtenerEstadisticas(String idUsuario) {
        Connection conn = ConexionMySQL.conectar();
        if (conn == null) return null;

        String sql = "SELECT * FROM Estadisticas WHERE idUsuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Estadisticas e = new Estadisticas();
                    e.setIdEstadisticas(rs.getString("idEstadisticas"));
                    e.setIdUsuario(rs.getString("idUsuario"));
                    e.setNivelesCompletados(rs.getString("nivelesCompletados"));
                    e.setTiempoTotalJuego(rs.getInt("tiempoTotalJuego"));
                    e.setRachas(rs.getInt("rachas"));
                    e.setUltimoNivelJugado(rs.getString("ultimoNivelJugado"));
                    return e;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
        }
        return null;
    }
}
