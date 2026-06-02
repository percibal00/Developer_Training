package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionMySQL {

    //URL
    private static final String URL =
            "jdbc:mysql://10.0.2.2:3306/developer_training_db?serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection conectar() {

        Connection conn = null;

        try {
            // Intentar con el driver más moderno primero
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                Class.forName("com.mysql.jdbc.Driver");
            }

            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión establecida correctamente.");

        } catch (Exception e) {
            System.err.println("Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }

        return conn;
    }
}