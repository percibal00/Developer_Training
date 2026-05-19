package Modelo;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import java.sql.Connection;
import java.sql.PreparedStatement;


public class PreguntaDAO {

    private Context context;

    public PreguntaDAO(Context context) {
        this.context = context;
    }

    public List<Pregunta> obtenerPreguntas() {
        List<Pregunta> lista = new ArrayList<>();
        Connection conn = ConexionMySQL.conectar();
        if (conn == null) return lista;

        String sql = "SELECT pregunta, respuesta, tipo FROM preguntas";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pregunta p = new Pregunta(
                        rs.getString("pregunta"),
                        rs.getString("respuesta"),
                        rs.getString("tipo")
                );
                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lista;
    }

    public void insertarPregunta(Pregunta p) {
        new Thread(() -> {
            try {
                Connection conn = ConexionMySQL.conectar();
                if (conn == null) return;

                String sql = "INSERT INTO preguntas (pregunta, respuesta, tipo) VALUES (?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, p.getPregunta());
                    ps.setString(2, p.getRespuesta());
                    ps.setString(3, p.getTipo());

                    ps.executeUpdate();
                    System.out.println("INSERTADO");
                }
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
