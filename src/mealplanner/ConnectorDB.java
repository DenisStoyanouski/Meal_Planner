package mealplanner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectorDB {
    private final Connection connection;

    public ConnectorDB() throws Exception {
        this.connection = ConnectionManager.getConnection();
        createDefaultTables();
    }

    private void createDefaultTables() {
        try (Statement statement = connection.createStatement()) {
            //statement.executeUpdate("drop table if exists meals");
            statement.executeUpdate("create table if not exists meals (" +
                    "category varchar," +
                    "meal varchar," +
                    "meal_id SERIAL" +
                    ")");
            //statement.executeUpdate("drop table if exists ingredients");
            statement.executeUpdate("create table if not exists ingredients (" +
                    "ingredient varchar," +
                    "ingredient_id SERIAL," +
                    "meal_id integer NOT NULL" +
                    ")");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addNewMeal(String category, String name) {
        String insert = "INSERT INTO meals (category, meal) VALUES (?, ?)";
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

    public void addIngredientsForMeal(int mealId, List<String> ingredients) {
        String insert = "INSERT INTO ingredients (ingredient, meal_id) VALUES (?, ?)";
        for (String ingredient : ingredients) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(insert)) {
                preparedStatement.setString(1, ingredient);
                preparedStatement.setInt(2, mealId);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void getMeals() {
        int meal_id;
        List<String> ingredients;
        String meals = "SELECT * FROM meals";
        try (PreparedStatement preparedStatement = connection.prepareStatement(meals)) {
            try (ResultSet categoryRows = preparedStatement.executeQuery()) {
                if (!categoryRows.isBeforeFirst() ) {
                    System.out.println("There is not data!");
                    return;
                }
                while (categoryRows.next()) {
                    // Retrieve column values
                    System.out.println("Category: " + categoryRows.getString("category"));
                    System.out.println("Name: " + categoryRows.getString("meal"));
                    meal_id = categoryRows.getInt("meal_id");
                    System.out.println("Ingredients: ");
                    ingredients = getIngredients(meal_id);
                    ingredients.forEach(System.out::println);
                    System.out.println();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<String> getIngredients(int meal_id) {
        List<String> ingredients = new ArrayList<>();
        String allIngredients = "SELECT ingredient FROM ingredients where meal_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(allIngredients)) {
            preparedStatement.setInt(1, meal_id);
            try (ResultSet categoryRows = preparedStatement.executeQuery()) {
                while (categoryRows.next()) {
                    // Retrieve column values
                    ingredients.add(categoryRows.getString("ingredient"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }
}
