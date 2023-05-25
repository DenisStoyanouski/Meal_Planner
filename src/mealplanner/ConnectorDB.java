package mealplanner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
                connection.setAutoCommit(true);
                createDefaultTables();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private void createDefaultTables() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("drop table if exists meals");
            statement.executeUpdate("create table meals (" +
                    "category varchar," +
                    "meal varchar," +
                    "meal_id integer NOT NULL" +
                    ")");
            statement.executeUpdate("drop table if exists ingredients");
            statement.executeUpdate("create table ingredients (" +
                    "ingredient varchar," +
                    "ingredient_id integer NOT NULL," +
                    "meal_id integer NOT NULL" +
                    ")");
            System.out.println("Tables are created");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
