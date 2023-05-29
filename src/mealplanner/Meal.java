package mealplanner;

import java.util.List;

public class Meal {
    private final String category;
    private final String name;
    private final int meal_id;
    private List<String> ingredients;

    public int getMeal_id() {
        return meal_id;
    }

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

    public String getName() {
        return name;
    }

    private String getIngredientsToString() {
        StringBuilder sb = new StringBuilder();
        ingredients.forEach(ingredient -> sb.append(ingredient).append("\n"));
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }


}
