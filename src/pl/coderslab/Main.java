package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String tasks[][] = readFile("tasks.csv");
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
                    rightChoice = true;
                }
                case "remove" -> {
                    System.out.println(ConsoleColors.YELLOW + "You chose option REMOVE");
                    System.out.print(ConsoleColors.RESET);
                    tasks = removeTasks(tasks);
                    rightChoice = true;
                }
                case "list" -> {
                    System.out.println(ConsoleColors.YELLOW + "You chose option LIST");
                    System.out.print(ConsoleColors.RESET);
                    listTasks(tasks);
                    rightChoice = true;
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
        String tasks[][] = new String[0][0];
        int count = 0;

        if(!Files.exists(path1)) {
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
        System.out.println("Please select the task's number you would like to remove. From 0 to " + (tasks.length-1));

        while(!doubleCheck) {
            if(!scan.hasNextInt()) {
                scan.next();
                System.out.println("Incorrect value. Please select the task from 0 to " + (tasks.length-1));
            }
            input = scan.nextInt();
            if (input < 0 || input >= tasks.length) {
                System.out.println("Incorrect value. Please select the task from 0 to " + (tasks.length-1));
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

    public static String[][] copyMultiArray(String[][] tasks) {
        String [][] currentArr = new String[tasks.length+1][3];
        for(int i=0; i<tasks.length; i++) {
            System.arraycopy(tasks[i], 0, currentArr[i], 0, tasks[i].length);
        }
        return currentArr;
    }

    public static String[][] addTask(String[][] tasks) {
        Scanner scan = new Scanner(System.in);
        String[][] expandedArr = copyMultiArray(tasks);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        boolean validDate = false;
        dateFormat.setLenient(validDate);

        System.out.println("Please add task description:");
        String task = scan.nextLine();
        System.out.println("Please add task due date. Please use format yyyy-mm-dd");
        String deadline = "";

        while (!validDate) {
            deadline = scan.nextLine();
            try {
                Date javaDate = dateFormat.parse(deadline);
                validDate = true;
            } catch (ParseException e) {
                System.out.println("Invalid date. Please try again");
                validDate = false;
            }
        }

        System.out.println("Is your task important? true/false");
        while(!scan.hasNextBoolean()) {
            scan.next();
            System.out.println("Incorrect value. Is your task important? true/false");
        }
        boolean isImportant = scan.nextBoolean();

        expandedArr[expandedArr.length-1][0] = task;
        expandedArr[expandedArr.length-1][1] = deadline;
        expandedArr[expandedArr.length-1][2] = Boolean.toString(isImportant);

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

    public static class ConsoleColors {
        // Reset
        public static final String RESET = "\033[0m";  // Text Reset

        // Regular Colors
        public static final String BLACK = "\033[0;30m";   // BLACK
        public static final String RED = "\033[0;31m";     // RED
        public static final String GREEN = "\033[0;32m";   // GREEN
        public static final String YELLOW = "\033[0;33m";  // YELLOW
        public static final String BLUE = "\033[0;34m";    // BLUE
        public static final String PURPLE = "\033[0;35m";  // PURPLE
        public static final String CYAN = "\033[0;36m";    // CYAN
        public static final String WHITE = "\033[0;37m";   // WHITE

        // Bold
        public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
        public static final String RED_BOLD = "\033[1;31m";    // RED
        public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
        public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
        public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
        public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
        public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
        public static final String WHITE_BOLD = "\033[1;37m";  // WHITE

        // Underline
        public static final String BLACK_UNDERLINED = "\033[4;30m";  // BLACK
        public static final String RED_UNDERLINED = "\033[4;31m";    // RED
        public static final String GREEN_UNDERLINED = "\033[4;32m";  // GREEN
        public static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
        public static final String BLUE_UNDERLINED = "\033[4;34m";   // BLUE
        public static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
        public static final String CYAN_UNDERLINED = "\033[4;36m";   // CYAN
        public static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE

        // Background
        public static final String BLACK_BACKGROUND = "\033[40m";  // BLACK
        public static final String RED_BACKGROUND = "\033[41m";    // RED
        public static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
        public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
        public static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
        public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
        public static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
        public static final String WHITE_BACKGROUND = "\033[47m";  // WHITE

        // High Intensity
        public static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
        public static final String RED_BRIGHT = "\033[0;91m";    // RED
        public static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
        public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
        public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
        public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
        public static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
        public static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE

        // Bold High Intensity
        public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
        public static final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
        public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
        public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
        public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
        public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
        public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
        public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE

        // High Intensity backgrounds
        public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
        public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
        public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
        public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
        public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
        public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
        public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // CYAN
        public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE
    }


}




