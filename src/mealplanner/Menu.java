package mealplanner;

import java.sql.Connection;
import java.util.*;
import java.util.stream.Collectors;

public class Menu {

    private static final Scanner scanner = new Scanner(System.in);
    private static String category;
    private static String name;
    private static List<String> ingredients;

    private static Meal meal;

    private static final String[] TYPE_OF_MEALS = {"breakfast", "lunch", "dinner"};

    private static final CookBook cookBook = new CookBook();

    private static Connection connection;

    public static void startMenu() {
        connection = new ConnectorDB().getConnection();
        while (true) {
            System.out.println("What would you like to do (add, show, exit)?");
            switch (getInput()) {
                case "add":
                    addMeal();
                    break;
                case "show":
                    show();
                    break;
                case "exit":
                    exit();
                    break;
                default:
                    break;
            }
        }
    }

    private static void show() {
        cookBook.printAllMeals();
    }

    private static void exit() {
        System.out.println("Bye!");
        System.exit(0);
    }

    private static void addMeal() {
        askQuestions();
        createMeal();
        addMealToBook();
    }

    private static String getInput() {
        return scanner.nextLine();
    }

    private static void askQuestions() {
        getCategory();
        getName();
        getIngredients();
    }

    private static void getCategory() {
        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
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

    private static boolean isRightFormat(String str) {
        return !str.isBlank() && !str.isEmpty() && str.matches("[a-zA-Z\\s,-]*");
    }

    private static void createMeal() {
        meal = new Meal(category, name, ingredients);
    }

    private static void addMealToBook() {
        if (cookBook.addMeal(meal)) {
            System.out.println("The meal has been added!");
        }
    }
}
