package mealplanner;

import java.util.ArrayList;
import java.util.List;

public class CookBook {
    private final List<Meal> meals = new ArrayList();

    public boolean addMeal(Meal meal) {
        meals.add(meal);
        return meals.contains(meal);
    }
}
