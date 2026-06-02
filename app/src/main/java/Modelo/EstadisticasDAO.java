package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstadisticasDAO {

    /**
     * Guarda una nueva entrada de estadísticas en MySQL.
     * Insertamos una nueva fila para cada partida para tener un historial real.
     */
    public void guardarEstadisticas(Estadisticas e) {
        new Thread(() -> {
            Connection conn = ConexionMySQL.conectar();
            if (conn == null) return;

            String sql = "INSERT INTO Estadisticas (idUsuario, nivelesCompletados, tiempoTotalJuego, rachas, ultimoNivelJugado) " +
                         "VALUES (?, ?, ?, ?, ?)";
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, e.getIdUsuario());
                ps.setString(2, e.getNivelesCompletados());
                ps.setInt(3, e.getTiempoTotalJuego());
                ps.setInt(4, e.getRachas());
                ps.setString(5, e.getUltimoNivelJugado());

                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                try { if (conn != null) conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }).start();
    }

    /**
     * Obtiene el historial completo de un usuario.
     */
    public List<Estadisticas> obtenerHistorialUsuario(String idUsuario) {
        List<Estadisticas> historial = new ArrayList<>();
        Connection conn = ConexionMySQL.conectar();
        if (conn == null) return historial;

        // Ordenamos por ID descendente para tener las últimas partidas primero
        String sql = "SELECT * FROM Estadisticas WHERE idUsuario = ? ORDER BY idEstadisticas DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Estadisticas e = new Estadisticas();
                    e.setIdEstadisticas(rs.getString("idEstadisticas"));
                    e.setIdUsuario(rs.getString("idUsuario"));
                    e.setNivelesCompletados(rs.getString("nivelesCompletados"));
                    e.setTiempoTotalJuego(rs.getInt("tiempoTotalJuego"));
                    e.setRachas(rs.getInt("rachas"));
                    e.setUltimoNivelJugado(rs.getString("ultimoNivelJugado"));
                    historial.add(e);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
        }
        return historial;
    }

    /**
     * Mantenemos este método para compatibilidad, devolviendo el registro más reciente.
     */
    public Estadisticas obtenerEstadisticas(String idUsuario) {
        List<Estadisticas> lista = obtenerHistorialUsuario(idUsuario);
        if (!lista.isEmpty()) {
            return lista.get(0);
        }
        return null;
    }
}
