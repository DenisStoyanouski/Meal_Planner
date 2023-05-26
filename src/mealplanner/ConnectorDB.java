package mealplanner;

import java.sql.*;
import java.util.List;

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
                    "meal_id integer SERIAL" +
                    ")");
            statement.executeUpdate("drop table if exists ingredients");
            statement.executeUpdate("create table ingredients (" +
                    "ingredient varchar," +
                    "ingredient_id integer SERIAL," +
                    "meal_id integer NOT NULL" +
                    ")");
            System.out.println("Tables are created");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addNewMeal(String category, String name) {
        String insert = "INSERT INTO meals (category, name) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insert)) {
            preparedStatement.setString(1, category);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getMealID(String meal) {
        String getter = "SELECT meal_id FROM meals WHERE meal = ?";
        int mealId = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(getter)) {
            preparedStatement.setString(1, meal);
            try (ResultSet categoryRows = preparedStatement.executeQuery()) {
                while (categoryRows.next()) {
                    // Retrieve column values
                    mealId = categoryRows.getInt("meal_id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mealId;
    }

    public void addIngredientsForMeal(String name, List<String> ingredients) {
        int meal_id = getMealID(name);
        String insert = "INSERT INTO ingredients (ingredient, meal_id) VALUES (?, ?)";
        for (String ingredient : ingredients) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(insert)) {
                preparedStatement.setString(1, ingredient);
                preparedStatement.setInt(2, meal_id);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void showAllMeals() {
        
    }
}
