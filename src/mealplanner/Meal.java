package mealplanner;

import java.util.Arrays;
import java.util.List;

public class Meal {
    private final String category;
    private final String name;
    private final String[] ingredients;

    public Meal(String category, String name, String[] ingredients) {
        this.category = category;
        this.name = name;
        this.ingredients = ingredients.clone();
    }

    @Override
    public String toString() {
        return "Category:" + category + "\n" +
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

    public String[] getIngredients() {
        return ingredients;
    }

    private String getIngredientsToString() {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(ingredients).forEach(ingredient -> sb.append(ingredient).append("\n"));
        return sb.toString();
    }


}
