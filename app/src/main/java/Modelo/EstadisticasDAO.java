package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstadisticasDAO {

    /**
     * Guarda una nueva partida en MySQL.
     */
    public void guardarEstadisticas(Estadisticas e) {
        new Thread(() -> {
            Connection conn = ConexionMySQL.conectar();
            if (conn == null) {
                System.err.println("ERROR: No se pudo conectar a la base de datos.");
                return;
            }

            // Usamos AUTO_INCREMENT: no enviamos el campo ID_ESTADISTICAS
            String sql = "INSERT INTO Estadisticas (ID_USUARIO, NIVELES_COMPLETADOS, TIEMPO_TOTAL_JUEGO, RACHAS, ULTIMO_NIVEL_JUGADO) " +
                         "VALUES (?, ?, ?, ?, ?)";
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, Integer.parseInt(e.getIdUsuario()));
                ps.setString(2, e.getNivelesCompletados());
                ps.setInt(3, e.getTiempoTotalJuego());
                ps.setInt(4, e.getRachas());
                ps.setString(5, e.getUltimoNivelJugado());

                int result = ps.executeUpdate();
                System.out.println("PARTIDA GUARDADA CORRECTAMENTE. Filas: " + result);
                
            } catch (Exception ex) {
                System.err.println("ERROR SQL AL GUARDAR: " + ex.getMessage());
                ex.printStackTrace();
            } finally {
                try { if (conn != null) conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }).start();
    }

    /**
     * Obtiene todas las partidas de un usuario.
     */
    public List<Estadisticas> obtenerHistorialUsuario(String idUsuario) {
        List<Estadisticas> historial = new ArrayList<>();
        Connection conn = ConexionMySQL.conectar();
        if (conn == null) return historial;

        String sql = "SELECT * FROM Estadisticas WHERE ID_USUARIO = ? ORDER BY ID_ESTADISTICAS DESC";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(idUsuario));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Estadisticas e = new Estadisticas();
                    e.setIdEstadisticas(rs.getString("ID_ESTADISTICAS"));
                    e.setIdUsuario(String.valueOf(rs.getInt("ID_USUARIO")));
                    e.setNivelesCompletados(rs.getString("NIVELES_COMPLETADOS"));
                    e.setTiempoTotalJuego(rs.getInt("TIEMPO_TOTAL_JUEGO"));
                    e.setRachas(rs.getInt("RACHAS"));
                    e.setUltimoNivelJugado(rs.getString("ULTIMO_NIVEL_JUGADO"));
                    historial.add(e);
                }
            }
        } catch (Exception ex) {
            System.err.println("ERROR AL LEER HISTORIAL: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
        }
        return historial;
    }

    public Estadisticas obtenerEstadisticas(String idUsuario) {
        List<Estadisticas> lista = obtenerHistorialUsuario(idUsuario);
        if (!lista.isEmpty()) {
            return lista.get(0); // Al estar ordenado por ID DESC, la 0 es la más reciente
        }
        return null;
    }
}
