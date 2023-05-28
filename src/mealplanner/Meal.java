package mealplanner;

import java.util.Arrays;
import java.util.List;

public class Meal {
    private final String category;
    private final String name;

    private final int meal_id;
    private List<String> ingredients;

    public Meal(String category, String name, int meal_id) {
        this.category = category;
        this.name = name;
        this.meal_id = meal_id;
    }

    @Override
    public String toString() {
        return "Category: " + category + "\n" +
                "Name: " + name + "\n" +
                "Ingredients: " + "\n" +
                getIngredientsToString();
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    private String getIngredientsToString() {
        StringBuilder sb = new StringBuilder();
        ingredients.forEach(ingredient -> sb.append(ingredient).append("\n"));
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }


}
