package mealplanner;

import java.util.List;

public class Meal {
    private final String category;
    private final String name;
    private final List<String> ingredients;

    public Meal(String category, String name, List<String> ingredients) {
        this.category = category;
        this.name = name;
        this.ingredients = ingredients;
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

    public List<String> getIngredients() {
        return ingredients;
    }

    private String getIngredientsToString() {
        StringBuilder sb = new StringBuilder();
        ingredients.stream().forEach(ingredient -> sb.append(ingredient).append("\n"));
        return sb.toString();
    }


}
