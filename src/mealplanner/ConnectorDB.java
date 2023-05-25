package mealplanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectorDB {

    private Connection connection;
    private final String DB_URL = "jdbc:postgresql:meal_db";
    private final String USER = "postgres";
    private final String PASS = "1111";

    public ConnectorDB() {
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS)) {
            if (con.isValid(5)) {
                System.out.println("Connection is valid.");
                connection = con;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
