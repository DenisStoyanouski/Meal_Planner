package mealplanner;

public class DailyPlan {
    private final String dayOfWeek;
    private final String breakfast;
    private final String lunch;
    private final String dinner;

    public DailyPlan(String dayOfWeek, String breakfast, String lunch, String dinner) {
        this.dayOfWeek = dayOfWeek;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
    }

    @Override
    public String toString() {
        return dayOfWeek + "\n" +
                "Breakfast: " + breakfast + "\n" +
                "Lunch: " + lunch + "\n" +
                "Dinner: " + dinner + "\n\n";
    }
}
