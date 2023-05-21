package mealplanner;

import java.util.Scanner;

public class Menu {

    private static Scanner scanner = new Scanner(System.in);
    private static String category;
    private static String name;
    private static String[] ingredients;

    private static Meal meal;

    private static CookBook cookBook;

    static void addMeal() {
        askQuestions();
        createMeal();
        addMealToBook();
    }

    private static String getInput() {
        return scanner.nextLine();
    }

    private static void askQuestions() {
        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        category = getInput();
        System.out.println("Input the meal's name:");
        name = getInput();
        System.out.println("Input the ingredients:");
        ingredients = getInput().split(",");
    }

    private static void createMeal() {
        meal = new Meal(category, name, ingredients);
    }

    private static void addMealToBook() {
        cookBook = new CookBook();
        System.out.println(meal.toString());
        cookBook.addMeal(meal);
    }
}
