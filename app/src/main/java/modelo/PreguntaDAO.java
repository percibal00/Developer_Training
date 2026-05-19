package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;


public class PreguntaDAO {

    public void insertarPregunta(Pregunta p) {

        new Thread(() -> {

            try {

                Connection conn = ConexionMySQL.conectar();

                String sql =
                        "INSERT INTO preguntas (pregunta, respuesta, tipo) VALUES (?, ?, ?)";

                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setString(1, p.getPregunta());
                ps.setString(2, p.getRespuesta());
                ps.setString(3, p.getTipo());

                ps.executeUpdate();

                System.out.println("INSERTADO");

                conn.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }
}