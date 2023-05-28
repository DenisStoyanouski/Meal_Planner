package mealplanner;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Objects;

import static mealplanner.Menu.*;

public class Planner {

    static private List<Meal> meals;

    static private String mealName;

    static private int mealId;

    static void makePlan() {
        for (DayOfWeek day : DayOfWeek.values()) {
            System.out.println(day.toString());
            for (String typeOfMeal : TYPE_OF_MEALS) {
                meals = connectorDB.getMealsByCategory(typeOfMeal);
                printMeals();
                System.out.printf("Choose the %s for %s from the list above:", typeOfMeal, day);
                getName();
                getMealId();
                connectorDB.addRowToPlan(day.toString(), typeOfMeal, mealId);
            }
        }
    }

    private static void getName() {
        while (true) {
            String nameCandidate = Menu.getInput();
            if (isRightFormat(nameCandidate) && !nameCandidate.isEmpty() && !nameCandidate.isBlank()) {
                mealName = nameCandidate;
                if (isExistedName()) {
                    return;
                } else {
                    System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
                }
            } else {
                System.out.println("Wrong format. Use letters only!");
            }
        }
    }

    private static boolean isExistedName() {
        for(Meal meal : meals) {
            if (Objects.equals(meal.getName(),mealName)) {
                return true;
            }
        }
        return false;
    }

    private static void printMeals() {
        meals.forEach(meal-> System.out.println(meal.getName()));
    }

    private static void getMealId() {
        for (Meal meal : meals) {
            if (Objects.equals(meal.getName(), mealName)) {
                mealId = meal.getMeal_id();
            }
        }
    }
}
