package mealplanner;

import java.util.HashMap;
import java.util.Map;

public class DailyPlan {
    private final String dayOfWeek;
    private final Map<String, String> meals = new HashMap<>();

    public DailyPlan(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;

    }

    public void setMeal(String category, String meal) {
        meals.put(category, meal);
    }

    @Override
    public String toString() {
        return dayOfWeek + "\n" +
                "Breakfast: " + meals.get("breakfast") + "\n" +
                "Lunch: " + meals.get("lunch") + "\n" +
                "Dinner: " + meals.get("dinner") + "\n";
    }
}
