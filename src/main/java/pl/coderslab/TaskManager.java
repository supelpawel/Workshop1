package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {

    static final String[] options = {"add", "remove", "list", "exit"};
    static final String fileName = "src/tasks.csv";
    static String[][] tasks = null;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        tasks = readFile(fileName);
        displayOptions(options);
        chosenOption(scanner);
    }

    public static void displayOptions(String[] options) {
        System.out.println(ConsoleColors.BLUE + "Please select an option:" + ConsoleColors.RESET);
        for (String option : options) {
            System.out.println(option);
        }
    }

    public static String[][] readFile(String fileDirectory) {
        File file = new File(fileDirectory);
        String[][] tasks = new String[0][0];
        String[] temp;
        String fileInput;
        try (Scanner scannerOfFile = new Scanner(file)) {
            while (scannerOfFile.hasNextLine()) {
                fileInput = scannerOfFile.nextLine();
                temp = fileInput.split(", ");
                tasks = Arrays.copyOf(tasks, tasks.length + 1);
                tasks[tasks.length - 1] = temp;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public static void chosenOption(Scanner scanner) {
        String userInput;
        while (scanner.hasNextLine()) {
            userInput = scanner.nextLine();
            switch (userInput) {
                case "add":
                    addTask(scanner);
                    break;
                case "remove":
                    removeTask(scanner);
                    break;
                case "list":
                    listTab();
                    break;
                case "exit":
                    exit(fileName);
                    break;
                default:
                    System.out.println("Please select a correct option.");
            }
        }
    }

    public static void addTask(Scanner scanner) {
        String userInput = "";
        String[] temp = new String[3];
        System.out.println("Please add task description");
        userInput = scanner.nextLine();
        temp[0] = userInput;
        System.out.println("Please add task due date");
        userInput = scanner.nextLine();
        temp[1] = userInput;
        System.out.println("Is your task important: true/false");
        while (!scanner.hasNextBoolean()) {
            System.out.println("Incorrect data. Is your task important: true/false");
            scanner.nextLine();
        }
        userInput = scanner.nextLine();
        temp[2] = userInput;
        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = temp;
        displayOptions(options);
    }

    public static void listTab() {
        for (int i = 0; i < tasks.length; i++) {
            System.out.println(i + " : " + tasks[i][0] + " " + tasks[i][1] + " " + tasks[i][2]);
        }
        displayOptions(options);
    }

    public static void removeTask(Scanner scanner) {
        System.out.println("Please select number to remove");
        String userInput = scanner.nextLine();
        int userChoice = 0;
        try {
            if (NumberUtils.isParsable(userInput) && Integer.parseInt(userInput) > 0) {
                userChoice = Integer.parseInt(userInput) - 1;
                tasks = ArrayUtils.remove(tasks, userChoice);
            } else {
                System.out.println("Incorrect data");
                removeTask(scanner);
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        displayOptions(options);
    }

    private static void exit(String fileName) {
        Path dir = Paths.get(fileName);
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < tasks.length; i++) {
            lines.add(String.join(", ", tasks[i]));
        }
        try {
            Files.write(dir, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(ConsoleColors.RED + "Bye, bye");
    }
}
