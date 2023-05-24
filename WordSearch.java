import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class WordSearch {
    private WordList list;
    private Scanner input;
    private boolean isRunning;
    public char[][] grid;
    private char[][] solution;
    private int width;
    private int height;


    public WordSearch() {
        list = new WordList();
        input = new Scanner(System.in);
    }

    public void start() {
        isRunning = true;
        System.out.println("Welcome to the console word search generator!");
        System.out.println("You can call these commands by number or by name.");
        printHelp();
        while (isRunning) {
            getCommand();
        }
    }

    private void getCommand() {
        String buffer = input.nextLine();

        if (buffer.toLowerCase().contains("generate") || buffer.toLowerCase().contains("1")) {
            generateNewSearch();
        } else if (buffer.toLowerCase().contains("print") || buffer.toLowerCase().contains("2")) {
            print(grid);
        } else if (buffer.toLowerCase().contains("solution") || buffer.toLowerCase().contains("3")) {
            print(solution);
        } else if (buffer.toLowerCase().contains("export") || buffer.toLowerCase().contains("4")) {
            exportWordSearch();
        } else if (buffer.toLowerCase().contains("help") || buffer.toLowerCase().contains("5")) {
            printHelp();
        } else if (buffer.toLowerCase().contains("exit") || buffer.toLowerCase().contains("6")) {
            System.out.println("Thanks for using the console word search tool. Goodbye!");
            isRunning = false;
            return;
        } else {
            System.out.println("Invalid selection, please try again.");
        }
        System.out.println();
        System.out.println("What would you like to do next?");
    }


    public void generateNewSearch() {
        list.configure();
        configureSearch();
        fillSearch();
        populateSearch(grid, true);
        populateSearch(solution, false);
    }

    public void configureSearch() {
        // WIP
    }

    public void fillSearch() {
        // WIP
    }

    public void populateSearch(char[][] array, boolean random) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (array[i][j] == '\u0000') {
                    if(random) {
                        array[i][j] = (char) ThreadLocalRandom.current().nextInt(97, 123);
                    } else {
                        array[i][j] = 'x';
                    }
                }
            }
        }
    }

    public boolean isValid(String word, Direction direction, Point point) {
        // validate that the word is in-bounds
        Point endpoint = new Point(point.x, point.y);
        endpoint.translate((word.length() - 1) * direction.xOffset, (word.length() - 1) * direction.yOffset);
        if (endpoint.x >= width || endpoint.x < 0) return false;
        if (endpoint.y >= height || endpoint.y < 0) return false;

        // validate each letter of the word comparing it to the grid
        for (int i = 0; i < word.length(); i++) {
            if (grid[point.x][point.y] == '\u0000') {
                System.out.println(point + " is null, so we are allowed to add " + word.charAt(i));
            } else if (grid[point.x][point.y] != word.charAt(i)) {
                System.out.println(point + "is " + grid[point.x][point.y] + " which is different than " + word.charAt(i));
                return false;
            } else {
                System.out.println(point + "is " + grid[point.x][point.y] + " which is the same as " + word.charAt(i));
            }
            // Adjust the point accordingly as we iterate through the word
            point.x += direction.xOffset;
            point.y += direction.yOffset;
        }

        return true;
    }

    public void exportWordSearch() {
        System.out.println("Please enter a file name to create");
        String fileName = input.nextLine();
        if(!fileName.endsWith(".txt")) fileName += ".txt";

        System.out.println("Outputting your WordSearch to " + fileName);
        File savedWordList = new File(fileName);
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(savedWordList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PrintWriter printWriter = new PrintWriter(fileWriter);

        String output = "";
        printWriter.println("WordSearch:");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                output += (" " + grid[i][j]);
            }
            printWriter.println(output);
            output = "";
        }

        printWriter.println("Solution:");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                output += (" " + solution[i][j]);
            }
            printWriter.println(output);
            output = "";
        }

        printWriter.close();
    }

    public void print(char[][] array) {
        String output = "";
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                output += (" " + array[i][j]);
            }
            System.out.println(output);
            output = "";
        }
    }

    public boolean confirm(String warning, String confirmation) {
        System.out.println(warning + " (Yes/No)");
        String confirmDeletion = input.nextLine();
        if (confirmDeletion.toLowerCase().contains("y")) {
            if(!confirmation.isEmpty()) System.out.println(confirmation);
            return true;
        } else if (confirmDeletion.toLowerCase().contains("n")) {
            System.out.println("Returning to the main menu.");
            return false;
        } else {
            System.out.println("Invalid selection, returning to the main menu.");
            return false;
        }
    }

    private void printHelp() {
        System.out.println("Here are the commands available to you:");
        System.out.println("[1] Generate a new word search");
        System.out.println("[2] Print your word search to the console");
        System.out.println("[3] Print your word search's solution to the console");
        System.out.println("[4] Export your word search and its solution to a text file");
        System.out.println("[5] Help");
        System.out.println("[6] Exit");
    }


}