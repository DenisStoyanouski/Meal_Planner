package mealplanner;

import java.sql.*;
import java.time.DayOfWeek;
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
            statement.executeUpdate("CREATE SEQUENCE if not exists meal_sequence\n" +
                    "  start 1\n" +
                    "  increment 1;");
            statement.executeUpdate("create table if not exists meals (" +
                    "category varchar," +
                    "meal varchar," +
                    "meal_id integer" +
                    ")");
            //statement.executeUpdate("drop table if exists ingredients");
            statement.executeUpdate("CREATE SEQUENCE if not exists ingredient_sequence\n" +
                    "  start 1\n" +
                    "  increment 1;");
            statement.executeUpdate("create table if not exists ingredients (" +
                    "ingredient varchar," +
                    "ingredient_id integer," +
                    "meal_id integer NOT NULL" +
                    ")");
            //statement.executeUpdate("drop table if exists plan");
            statement.executeUpdate("create table if not exists plan (" +
                    "option varchar NOT NULL," +
                    "category varchar NOT NULL," +
                    "meal_id integer NOT NULL" +
                    ")");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addNewMeal(String category, String name) {
        String insert = "INSERT INTO meals (category, meal, meal_id) VALUES (?, ?, nextval('meal_sequence'))";
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
        String insert = "INSERT INTO ingredients (ingredient, ingredient_id, meal_id) VALUES (?, nextval('ingredient_sequence'), ?)";
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

    public void printMealsByCategory(String category) {
        int meal_id;
        List<String> ingredients;
        String meals = "SELECT meal, meal_id FROM meals WHERE category = ? ORDER BY meal_id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(meals)) {
            preparedStatement.setString(1, category);
            try (ResultSet categoryRows = preparedStatement.executeQuery()) {
                if (!categoryRows.isBeforeFirst()) {
                    throw new SQLException();
                }
                System.out.println("Category: " + category + "\n");
                while (categoryRows.next()) {
                    System.out.println("Name: " + categoryRows.getString("meal"));
                    meal_id = categoryRows.getInt("meal_id");
                    System.out.println("Ingredients: ");
                    ingredients = getIngredients(meal_id);
                    ingredients.forEach(System.out::println);
                    System.out.println();
                }
            } catch (SQLException e) {
                System.out.println("No meals found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Meal> getMealsByCategory(String category) {
        List<Meal> meals = new ArrayList<>();
        String query = "SELECT meal, meal_id FROM meals WHERE category = ? ORDER BY meal";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, category);
            try (ResultSet categoryRows = preparedStatement.executeQuery()) {
                if (!categoryRows.isBeforeFirst()) {
                    throw new SQLException();
                }
                while (categoryRows.next()) {
                    String meal = categoryRows.getString("meal");
                    int meal_id = categoryRows.getInt("meal_id");
                    meals.add(new Meal(category, meal, meal_id));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("No meals found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return meals;
    }

    private List<String> getIngredients(int meal_id) {
        List<String> ingredients = new ArrayList<>();
        String allIngredients = "SELECT ingredient, ingredient_id FROM ingredients where meal_id = ? ORDER BY ingredient_id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(allIngredients)) {
            preparedStatement.setInt(1, meal_id);
            try (ResultSet categoryRows = preparedStatement.executeQuery()) {
                while (categoryRows.next()) {
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

    public void addRowToPlan(String dayOfWeek, String typeOfMeal, int mealId) {
        if (!isRowExistInDb(dayOfWeek, typeOfMeal)) {
            addAsNewRow(dayOfWeek, typeOfMeal, mealId);
        } else {
            addAsUpdateColumn(dayOfWeek, typeOfMeal, mealId);
        }
    }

    private boolean isRowExistInDb(String dayOfWeek, String typeOfMeal) {
        String query = "SELECT COUNT(*) FROM plan WHERE option = ? AND category = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, dayOfWeek);
            preparedStatement.setString(2, typeOfMeal);
            try (ResultSet categoryRows = preparedStatement.executeQuery()) {
                if (categoryRows.isBeforeFirst()) {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void addAsUpdateColumn(String dayOfWeek, String typeOfMeal, int mealId) {
        String query = "UPDATE plan SET meal_id = ? WHERE option = ? AND category = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, mealId);
            preparedStatement.setString(2, dayOfWeek);
            preparedStatement.setString(3, typeOfMeal);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addAsNewRow(String dayOfWeek, String typeOfMeal, int mealId) {
        String insert = "INSERT INTO plan (option, category, meal_id) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insert)) {
            preparedStatement.setString(1, dayOfWeek);
            preparedStatement.setString(2, typeOfMeal);
            preparedStatement.setInt(3, mealId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<DailyPlan> getPlan() {
        List<DailyPlan> plans = new ArrayList<>();
        String query = "SELECT category, meal_id FROM plan WHERE option = ?";
        for (DayOfWeek day : DayOfWeek.values()) {
            DailyPlan dailyPlan = new DailyPlan(day.name());
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, day.name());
                try (ResultSet row = preparedStatement.executeQuery()) {
                    while (row.next()) {
                        String category = row.getString("category");
                        String meal = getMealById(row.getInt("meal_id"));
                        dailyPlan.setMeal(category, meal);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            plans.add(dailyPlan);
        }
        return plans;
    }

    private String getMealById(int meal_id) {
        String meal = " ";
        String query = "SELECT meal FROM meals WHERE meal_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, meal_id);
            try (ResultSet row = preparedStatement.executeQuery()) {
                while (row.next()) {
                    meal = row.getString("meal");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return meal;
    }
}
