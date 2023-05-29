package mealplanner;

import java.io.*;

public class Saver {

    static void doSave() {
        if (Planner.getPlan() == null) {
            Planner.getPlanFromDB();
        }
        if (Planner.getPlan() == null || Planner.getPlan().isEmpty()) {
            System.out.println("Unable to save. Plan your meals first.");
        } else {
            System.out.println("Input a filename:");
            String fileName = Menu.getInput();
            saveToFile(fileName, getShopList());
            System.out.println("Saved!");
        }
    }

    private static String getShopList() {
        StringBuilder sb = new StringBuilder();
        for (var entry : Planner.getAllIngredientsFromPlan().entrySet()) {
            sb.append(entry.getKey());
            sb.append(entry.getValue() == 1 ? "\n" : " x" + entry.getValue() + "\n");
        }
        return sb.toString();
    }

    private static void saveToFile(String fileName, String shopList) {
        String filePath = "." + File.separator + fileName;
        File file = new File(filePath);
        try (FileWriter fr = new FileWriter(file)) {
            fr.write(shopList);
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }
}
