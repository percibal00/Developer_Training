package modelo;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionMySQL {

    //URL
    private static final String URL =
            "jdbc:mysql://10.0.2.2:3306/proyectoprogramacion?serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection conectar() {

        Connection conn = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("CONECTADO");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }
}