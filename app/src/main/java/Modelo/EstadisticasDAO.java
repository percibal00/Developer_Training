package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EstadisticasDAO {

    /**
     * Guarda una nueva partida en MySQL.
     */
    public void guardarEstadisticas(Estadisticas e) {
        new Thread(() -> {
            Connection conn = ConexionMySQL.conectar();
            if (conn == null) return;

            // Eliminamos la columna FECHA_REGISTRO si no existe en tu tabla SQL
            String sql = "INSERT INTO Estadisticas (ID_ESTADISTICAS, ID_USUARIO, NIVELES_COMPLETADOS, TIEMPO_TOTAL_JUEGO, RACHAS, ULTIMO_NIVEL_JUGADO) " +
                         "VALUES (?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                // Generar un ID único para la clave primaria VARCHAR(50)
                String uniqueID = UUID.randomUUID().toString().substring(0, 45);
                
                ps.setString(1, uniqueID);
                ps.setInt(2, Integer.parseInt(e.getIdUsuario()));
                ps.setString(3, e.getNivelesCompletados());
                ps.setInt(4, e.getTiempoTotalJuego());
                ps.setInt(5, e.getRachas());
                ps.setString(6, e.getUltimoNivelJugado());

                int result = ps.executeUpdate();
                System.out.println("Partida guardada en DB: " + result);
                
            } catch (Exception ex) {
                System.err.println("ERROR AL GUARDAR PARTIDA: " + ex.getMessage());
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

        // Quitamos el ORDER BY FECHA_REGISTRO porque esa columna no existe en tu tabla Estadisticas
        String sql = "SELECT * FROM Estadisticas WHERE ID_USUARIO = ?";
        
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
            // Devolvemos la última (que en la lista es la última añadida)
            return lista.get(lista.size() - 1);
        }
        return null;
    }
}
