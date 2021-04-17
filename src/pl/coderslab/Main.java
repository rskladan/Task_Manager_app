package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String[][] tasks = readFile("tasks.csv");
        Scanner scanner = new Scanner(System.in);

        String input;
        boolean rightChoice = true;

        while (rightChoice) {

            System.out.println("*******************************************************");
            System.out.println(ConsoleColors.BLUE_BOLD + "Please select the option:");
            System.out.println(ConsoleColors.RESET + "add");
            System.out.println("remove");
            System.out.println("list");
            System.out.println("exit");
            System.out.println("*******************************************************");

            input = scanner.nextLine().toLowerCase();

            switch (input) {
                case "add" -> {
                    System.out.println(ConsoleColors.YELLOW + "You chose option ADD");
                    System.out.print(ConsoleColors.RESET);
                    tasks = addTask(tasks);
                }
                case "remove" -> {
                    System.out.println(ConsoleColors.YELLOW + "You chose option REMOVE");
                    System.out.print(ConsoleColors.RESET);
                    tasks = removeTasks(tasks);
                }
                case "list" -> {
                    System.out.println(ConsoleColors.YELLOW + "You chose option LIST");
                    System.out.print(ConsoleColors.RESET);
                    listTasks(tasks);
                }
                case "exit" -> {
                    System.out.println(ConsoleColors.YELLOW + "You chose option EXIT");
                    System.out.print(ConsoleColors.RESET);
                    exitTasks("tasks.csv", tasks);
                    rightChoice = false;
                }
                default -> {
                    System.out.println(ConsoleColors.RED_UNDERLINED + "Please select the correct option");
                    System.out.print(ConsoleColors.RESET);
                }
            }
        }
        System.out.println("Bye, bye.");
    }

    public static String[][] readFile(String fileName) {
        Path path1 = Paths.get(fileName);
        String[][] tasks = new String[0][0];
        int count = 0;

        if (!Files.exists(path1)) {
            System.out.println("File does't exist");
            System.exit(0);
        }

        try {
            for (String line : Files.readAllLines(path1)) {
                count++;
            }

            int i = 0;
            tasks = new String[count][3];
            String[] tempStr;

            for (String line : Files.readAllLines(path1)) {
                tempStr = line.split(", ");
                System.arraycopy(tempStr, 0, tasks[i], 0, tempStr.length);
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public static String[][] removeTasks(String[][] tasks) {
        Scanner scan = new Scanner(System.in);
        boolean doubleCheck = false;
        int input;
        System.out.println("Please select the task's number you would like to remove. From 0 to " + (tasks.length - 1));

        while (!doubleCheck) {
            if (!scan.hasNextInt()) {
                scan.next();
                System.out.println("Incorrect value. Please select the task from 0 to " + (tasks.length - 1));
            }
            input = scan.nextInt();
            if (input < 0 || input >= tasks.length) {
                System.out.println("Incorrect value. Please select the task from 0 to " + (tasks.length - 1));
            } else {
                System.out.println("Removed task number: " + input);
                tasks = ArrayUtils.remove(tasks, input);
                doubleCheck = true;
            }
        }
        return tasks;
    }

    public static void listTasks(String[][] tasks) {
        StringBuilder sb;
        for (int j = 0; j < tasks.length; j++) {
            sb = new StringBuilder();
            for (int k = 0; k < tasks[j].length; k++) {
                sb.append(tasks[j][k]).append(" ");
            }
            System.out.println(j + ": " + sb);
        }
    }

    public static String[][] addTask(String[][] tasks) {
        Scanner scan = new Scanner(System.in);
        String[][] expandedArr = Arrays.copyOf(tasks, tasks.length + 1);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
////        mozna wykorzystac tu localDate
//        boolean validDate = false;
//        dateFormat.setLenient(validDate);


        System.out.println(expandedArr.length);
        expandedArr[expandedArr.length - 1] = new String[3];

        System.out.println("Please add task description:");
        String task = scan.nextLine();
        System.out.println("Please add task due date. Please use format yyyy-mm-dd");
        String deadline = "";

        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        boolean validDate = false;

        while (!validDate) {
            deadline = scan.nextLine();
            try {
                validDate = LocalDate.from(customFormatter.parse(deadline)).isAfter(LocalDate.now());
                System.out.println(validDate);
                if (!validDate) {
                    System.out.println("Invalid date. Please try again");
                    System.out.println("Today is " + LocalDate.now());
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Please try again");
                validDate = false;
            }
        }

        System.out.println("Is your task important? true/false");
        while (!scan.hasNextBoolean()) {
            scan.next();
            System.out.println("Incorrect value. Is your task important? true/false");
        }
        boolean isImportant = scan.nextBoolean();

        expandedArr[expandedArr.length - 1][0] = task;
        expandedArr[expandedArr.length - 1][1] = deadline;
        expandedArr[expandedArr.length - 1][2] = Boolean.toString(isImportant);

        System.out.println(ConsoleColors.GREEN_BOLD + "Success! Task added" + ConsoleColors.RESET);

        return expandedArr;
    }

    static public void exitTasks(String fileName, String[][] tasks) {
        StringBuilder sb = new StringBuilder();
        try (PrintWriter writer = new PrintWriter(fileName)) {

            for (int j = 0; j < tasks.length; j++) {
                for (int k = 0; k < tasks[j].length; k++) {
                    if (k == 2) {
                        sb.append(tasks[j][k]).append('\n');
                    } else {
                        sb.append(tasks[j][k]).append(", ");
                    }
                }
            }
            writer.write(sb.toString());
            System.out.println(ConsoleColors.RED + "Saved!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}




