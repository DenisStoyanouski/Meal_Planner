package mealplanner;

import java.util.*;
import java.util.stream.Collectors;

public class Menu {

    private static final Scanner scanner = new Scanner(System.in);
    private static String category;
    private static String name;
    private static List<String> ingredients;
    private static Meal meal;
    static final String[] TYPE_OF_MEALS = {"breakfast", "lunch", "dinner"};
    private static CookBook cookBook;
    static ConnectorDB connectorDB;

    public static void startMenu() throws Exception {
        connectorDB = new ConnectorDB();
        while (true) {
            System.out.println("What would you like to do (add, show, plan, exit)?");
            switch (getInput()) {
                case "add":
                    addMeal();
                    break;
                case "show":
                    show();
                    break;
                case "plan":
                    Planner.makePlan();
                    break;
                case "exit":
                    exit();
                    break;
                default:
                    break;
            }
        }
    }

    private static void addMeal() {
        askQuestions();
        connectorDB.addNewMeal(category, name);
        int mealId = connectorDB.getMealID(name);
        connectorDB.addIngredientsForMeal(mealId, ingredients);
        System.out.println("The meal has been added!");
    }

    private static void show() {
        getCategory("print");
        connectorDB.printMealsByCategory(category);
    }

    private static void exit() {
        System.out.println("Bye!");
        System.exit(0);
    }

    static String getInput() {
        return scanner.nextLine();
    }

    private static void askQuestions() {
        getCategory("add");
        getName();
        getIngredients();
    }

    private static void getCategory(String goal) {
        switch (goal) {
            case "add":
                System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
                break;
            case "print":
                System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
                break;
            default:
                break;
        }
        while (true) {
            String categoryName = getInput();
            if (isCategoryName(categoryName)) {
                category = categoryName;
                break;
            } else {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            }
        }
    }

    private static void getName() {
        System.out.println("Input the meal's name:");
        while (true) {
            String nameCandidate = getInput();
            if (isRightFormat(nameCandidate) && !nameCandidate.isEmpty() && !nameCandidate.isBlank()) {
                name = nameCandidate;
                break;
            } else {
                System.out.println("Wrong format. Use letters only!");
            }
        }
    }

    private static void getIngredients() {
        System.out.println("Input the ingredients:");
        while (true) {
            ingredients = Arrays.stream(getInput().split(","))
                    .map(String::strip)
                    .collect(Collectors.toList());
            try {
                for (String ingredient : ingredients) {
                    if (!isRightFormat(ingredient)) {
                        throw new IllegalArgumentException();
                    }
                }
                break;
            } catch (IllegalArgumentException | NullPointerException e) {
                System.out.println("Wrong format. Use letters only!");
            }
        }
    }

    private static boolean isCategoryName(String category) {
        return Arrays.asList(TYPE_OF_MEALS).contains(category);
    }

    static boolean isRightFormat(String str) {
        return !str.isBlank() && !str.isEmpty() && str.matches("[a-zA-Z\\s,-]*");
    }
}
