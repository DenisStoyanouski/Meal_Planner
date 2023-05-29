package mealplanner;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static mealplanner.Menu.*;

public class Planner {
    static private List<Meal> meals;
    static private List<DailyPlan> weeklyPlan;
    static private String mealName;
    static private int mealId;

    static void makePlan() {
        for (DayOfWeek day : DayOfWeek.values()) {
            System.out.println(day);
            for (String typeOfMeal : TYPE_OF_MEALS) {
                meals = connectorDB.getMealsByCategory(typeOfMeal);
                printMeals();
                System.out.printf("Choose the %s for %s from the list above:%n", typeOfMeal, day);
                getName();
                getMealId();
                connectorDB.addRowToPlan(day.toString(), typeOfMeal, mealId);
            }
            System.out.printf("Yeah! We planned the meals for %s.%n%n", day);
        }
        printPlan();
    }

    static void getPlanFromDB() {
        weeklyPlan = connectorDB.getPlan();
    }

    static List<DailyPlan> getPlan() {
        return weeklyPlan;
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
        for (Meal meal : meals) {
            if (Objects.equals(meal.getName(), mealName)) {
                return true;
            }
        }
        return false;
    }

    private static void printMeals() {
        meals.forEach(meal -> System.out.println(meal.getName()));
    }

    private static void getMealId() {
        for (Meal meal : meals) {
            if (Objects.equals(meal.getName(), mealName)) {
                mealId = meal.getMeal_id();
            }
        }
    }

    private static void printPlan() {
        weeklyPlan = connectorDB.getPlan();
        weeklyPlan.forEach(plan -> System.out.println(plan.toString()));
    }

    static Map<String, Integer> getAllIngredientsFromPlan() {
        Map<String, Integer> allIngredientsFromPlan = new HashMap<>();
        for (DailyPlan dailyPlan : weeklyPlan) {
            List<String> mealNames = dailyPlan.getMeals().values().stream().toList();
            for (String mealName : mealNames) {
                int mealId = connectorDB.getMealID(mealName);
                List<String> ingredients = connectorDB.getIngredients(mealId);
                for (String ingredient : ingredients) {
                    if (!allIngredientsFromPlan.containsKey(ingredient)) {
                        allIngredientsFromPlan.put(ingredient, 1);
                    } else {
                        allIngredientsFromPlan.put(ingredient, allIngredientsFromPlan.get(ingredient) + 1);
                    }
                }
            }
        }
        return allIngredientsFromPlan;
    }
}
