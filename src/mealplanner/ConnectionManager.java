package mealplanner;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager {
    private static final String DB_URL = "jdbc:postgresql:meals_db";
    private static final String USER = "postgres";
    private static final String PASS = "1111";
    private static Connection con;

    public static Connection getConnection() throws Exception {
        con = DriverManager.getConnection(DB_URL, USER, PASS);
        return con;
    }


}
