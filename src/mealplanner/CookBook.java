package mealplanner;

import java.util.ArrayList;
import java.util.List;

public class CookBook {
    private final List<Meal> meals = new ArrayList();

    public boolean addMeal(Meal meal) {
        meals.add(meal);
        return meals.contains(meal);
    }

    public void printAllMeals() {
        if (!meals.isEmpty()) {
            meals.forEach(meal -> System.out.println(meal.toString()));
        } else {
            System.out.println("No meals saved. Add a meal first.");
        }

    }
}
