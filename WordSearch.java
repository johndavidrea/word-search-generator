import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class WordSearch {
    private WordList list;
    private Scanner input;
    private boolean isRunning;
    public char[][] grid;
    private char[][] solutionGrid;
    private ArrayList<WordLocation> solutions;
    private int width;
    private int height;
    private int overlapChance;


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
            printGrid(grid);
        } else if (buffer.toLowerCase().contains("solution") || buffer.toLowerCase().contains("3")) {
            printSolution();
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
        populateSearch(solutionGrid, false);
    }

    public void configureSearch() {
        solutions = new ArrayList<>();
        // WIP

        width = 8;
        height = 8;

        grid = new char[width][height];
        solutionGrid = new char[width][height];

        overlapChance = 100;
    }

    public void fillSearch() {
        for (String word : list.getWordList()) {
            placeWord(word);
        }
    }

    public void placeWord(String word) {
        ArrayList<WordLocation> nonOverlappingWords = new ArrayList<>();
        ArrayList<WordLocation> overlappingWords = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                for (Direction direction : Direction.values()) {
                    WordLocation buffer = new WordLocation(new Point(i, j), direction, word);
                    if(validate(buffer) != null) {
                        if(buffer.getOverlapCount() <= 0) {
                            nonOverlappingWords.add(buffer);
                        } else {
                            overlappingWords.add(buffer);
                        }
                    }
                }
            }
        }

        if (nonOverlappingWords.size() == 0 && overlappingWords.size() == 0) {
            System.out.println(word + " has no valid positions available! Cannot add it to the WordSearch.");
        } else if(overlappingWords.isEmpty()) {
            writeWord(nonOverlappingWords.get(ThreadLocalRandom.current().nextInt(0, nonOverlappingWords.size())));
        } else if(ThreadLocalRandom.current().nextInt(1, 101) <= overlapChance) {
            WordLocation buffer = new WordLocation();
            for (WordLocation wordLocation : overlappingWords) {
                if (wordLocation.getOverlapCount() > buffer.getOverlapCount()) buffer = wordLocation;
            }
            writeWord(buffer);
        } else {
            writeWord(nonOverlappingWords.get(ThreadLocalRandom.current().nextInt(0, nonOverlappingWords.size())));
        }

    }

    public void writeWord(WordLocation wordLocation) {
        // output each letter of the word to the grid
        Point bufferPoint = new Point(wordLocation.getPoint().x, wordLocation.getPoint().y);
        for (int i = 0; i < wordLocation.getWord().length(); i++) {
            grid[bufferPoint.x][bufferPoint.y] = wordLocation.getWord().charAt(i);
            // Adjust bufferPoint accordingly as we iterate through the word
            bufferPoint.x += wordLocation.getDirection().xOffset;
            bufferPoint.y += wordLocation.getDirection().yOffset;
        }
        solutions.add(wordLocation);
    }

    public void populateSearch(char[][] array, boolean random) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (array[i][j] == '\u0000') {
                    if (random) {
                        array[i][j] = (char) ThreadLocalRandom.current().nextInt(97, 123);
                    } else {
                        array[i][j] = 'x';
                    }
                }
            }
        }
    }

    public WordLocation validate(WordLocation wordLocation) {
        // validate that the word is in-bounds
        Point endpoint = new Point(wordLocation.getPoint().x, wordLocation.getPoint().y);
        endpoint.translate((wordLocation.getWord().length() - 1) * wordLocation.getDirection().xOffset,
                (wordLocation.getWord().length() - 1) * wordLocation.getDirection().yOffset);
        if (endpoint.x >= width || endpoint.x < 0) return null;
        if (endpoint.y >= height || endpoint.y < 0) return null;

        Point bufferPoint = new Point(wordLocation.getPoint().x, wordLocation.getPoint().y);

        // validate each letter of the word comparing it to the grid
        for (int i = 0; i < wordLocation.getWord().length(); i++) {
            if (grid[bufferPoint.x][bufferPoint.y] == '\u0000') {

            } else if (grid[bufferPoint.x][bufferPoint.y] != wordLocation.getWord().charAt(i)) {
                return null;
            } else {
                wordLocation.incrementOverlapCount();
            }
            // Adjust bufferPoint accordingly as we iterate through the word
            bufferPoint.x += wordLocation.getDirection().xOffset;
            bufferPoint.y += wordLocation.getDirection().yOffset;
        }

        return wordLocation;
    }

    public void exportWordSearch() {
        System.out.println("Please enter a file name to create");
        String fileName = input.nextLine();
        if (!fileName.endsWith(".txt")) fileName += ".txt";

        System.out.println("Outputting your WordSearch to " + fileName);
        File savedWordList = new File(fileName);
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(savedWordList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PrintWriter printWriter = new PrintWriter(fileWriter);

        StringBuilder output = new StringBuilder();
        printWriter.println("WordSearch:");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                output.append(" ").append(grid[i][j]);
            }
            printWriter.println(output);
            output = new StringBuilder();
        }

        printWriter.println("Solution grid:");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                output.append(" ").append(solutionGrid[i][j]);
            }
            printWriter.println(output);
            output = new StringBuilder();
        }

        printWriter.println("Solution list:");
        for (WordLocation wordLocation : solutions) printWriter.println(wordLocation);

        printWriter.close();
    }

    public void printGrid(char[][] array) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                output.append(" ").append(array[i][j]);
            }
            System.out.println(output);
            output = new StringBuilder();
        }
    }

    public void printSolution() {
        System.out.println("Here is the grid with only letters that are part of a full word:");
        printGrid(solutionGrid);
        System.out.println("Here's the list of words and where they're located in the WordSearch:");
        for (WordLocation wordLocation : solutions) System.out.println(wordLocation);
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