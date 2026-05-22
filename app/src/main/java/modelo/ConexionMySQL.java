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
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }
}