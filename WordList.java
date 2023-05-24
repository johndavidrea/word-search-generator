import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class WordList {
    private Scanner input;
    private boolean isRunning;
    private ArrayList<String> wordList;
    private int longest;

    public WordList() {
        input = new Scanner(System.in);
        wordList = new ArrayList<>();
        longest = 0;
    }

    public void configure() {
        isRunning = true;

        printHelp();

        while (isRunning) {
            getCommand();
        }
    }

    private void getCommand() {
        String buffer = input.nextLine();

        if (buffer.toLowerCase().contains("add") || buffer.toLowerCase().contains("1")) {
            typeWords();
        } else if (buffer.toLowerCase().contains("import") || buffer.toLowerCase().contains("2")) {
            importText();
        } else if (buffer.toLowerCase().contains("export") || buffer.toLowerCase().contains("3")) {
            exportText();
        } else if (buffer.toLowerCase().contains("view") || buffer.toLowerCase().contains("4")) {
            viewList();
        } else if (buffer.toLowerCase().contains("remove") || buffer.toLowerCase().contains("5")) {
            removeWord();
        } else if (buffer.toLowerCase().contains("clear") || buffer.toLowerCase().contains("6")) {
            clearWords();
        } else if (buffer.toLowerCase().contains("help") || buffer.toLowerCase().contains("7")) {
            printHelp();
        } else if (buffer.toLowerCase().contains("save") || buffer.toLowerCase().contains("8")) {
            System.out.println("Your word list has been saved.");
            isRunning = false;
            return;
        } else {
            System.out.println("Invalid selection, please try again.");
        }
        System.out.println();
        System.out.println("What would you like to do next?");
    }

    private void addWord(String word) {
        if (word.matches("[a-zA-Z]+")) {
            wordList.add(word.toLowerCase());
            System.out.println(word + " was added to the list.");
            if (word.length() > longest) longest = word.length();
        } else {
            System.out.println(word + " was NOT added to the list since it contains non-alphabetic characters.");
        }
    }

    private void typeWords() {
        System.out.println("Type a word to add to the list:");
        addWord(input.nextLine().toLowerCase());

        if (confirm("Would you like to add another word to the list?", "")) {
            typeWords();
        }
    }

    private void importText() {
        String filepath = null;
        int triesRemaining = 3;
        System.out.println("Your text file must contain each word you'd like to add to the list on a new line,");
        System.out.println("Lines that contain non-alphabetical characters will not be added as words to the list.");
        System.out.println("Please input the filepath of the .txt file you'd like to import:");
        System.out.println("(this can be a relative or absolute filepath)");

        while (true) {
            try {
                // get a filepath from the user
                filepath = input.nextLine();
                if (!filepath.endsWith(".txt")) {
                    System.out.println("The file must end with the .txt extension");
                }
                // verify that the file exists
                File f = new File(filepath);
                if (!f.exists() || !f.isFile()) {
                    throw new FileNotFoundException();
                }
                // read the file and add the words to the list
                System.out.println(filepath + " is a valid file. Adding to the word list:");
                Scanner readFile = new Scanner(new File(filepath)).useDelimiter(System.lineSeparator());
                while (readFile.hasNext()) {
                    addWord(readFile.next());
                }
                // close the scanner and break from the loop
                readFile.close();
                break;

            } catch (Exception e) {
                if (triesRemaining <= 0) {
                    System.out.println("0 tries remaining, returning to the word list management menu");
                    break;
                }
                System.out.println(filepath + " is not a valid file, please try again. Tries remaining: " + triesRemaining);
                triesRemaining--;
            }
        }
    }

    private void exportText() {

        if (!confirm("Exporting your word list will overwrite WordList.txt (if it exists), are you sure?",
                "")) {
            return;
        }

        System.out.println("Outputting your file to WordList.txt");
        File savedWordList = new File("WordList.txt");
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(savedWordList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PrintWriter printWriter = new PrintWriter(fileWriter);

        for (int i = 0; i < wordList.size(); i++) {
            printWriter.println(wordList.get(i));
        }

        printWriter.close();
    }

    private void viewList() {
        System.out.println("Here's your word list:");
        printList();
        System.out.println("Your longest word is " + longest + " characters long.");
    }

    // prints the current word list and its statistics
    private void printList() {
        // check if the word list is empty
        if (wordList.size() == 0) {
            System.out.println("Your word list is empty.");
            return;
        }

        // print the words in the word list
        for (int i = 0; i < wordList.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + wordList.get(i));
        }
    }

    private void removeWord() {
        System.out.println("Which word would you like to remove?");
        printList();
        int index = getIndex();
        if (index == -1) {
            System.out.println("Invalid selection, returning to the word list management menu.");
            return;
        }
        System.out.println(wordList.get(index) + " has been selected.");

        if (confirm("Do you want to delete this entry?",
                wordList.get(index) + " has been removed from the word list.")) {
            wordList.remove(index);
            longest = 0;
            for (String word : wordList) {
                if (word.length() > longest) longest = word.length();
            }

        }
    }

    // Gets the correct index number of an entry based on an identifier from the user
    private int getIndex() {
        String identifier = input.nextLine();
        for (int i = 1; i <= wordList.size(); i++) {
            if (identifier.contains(wordList.get(i - 1)) || identifier.equals(Integer.toString(i))) {
                return (i - 1);
            }
        }
        // If no matching entry is found, return -1 to indicate the failure to find a matching index
        return -1;
    }

    private void clearWords() {
        if (confirm("Are you SURE you'd like to clear the word list?",
                "The word list has been cleared")) {
            wordList.clear();
            longest = 0;
        }
    }

    private boolean confirm(String warning, String confirmation) {
        System.out.println(warning + " (Yes/No)");
        String confirmDeletion = input.nextLine();
        if (confirmDeletion.toLowerCase().contains("y")) {
            if (!confirmation.isEmpty()) System.out.println(confirmation);
            return true;
        } else if (confirmDeletion.toLowerCase().contains("n")) {
            System.out.println("Returning to the word list management menu.");
            return false;
        } else {
            System.out.println("Invalid selection, returning to the word list management menu.");
            return false;
        }
    }

    private void printHelp() {
        System.out.println("Here are the commands available to you:");
        System.out.println("[1] Add words to your word list using the console");
        System.out.println("[2] Import words to your list from a .txt file");
        System.out.println("[3] Export your word list to a .txt file");
        System.out.println("[4] View your current word list");
        System.out.println("[5] Remove a word from your word list");
        System.out.println("[6] Clear your word list");
        System.out.println("[7] Help");
        System.out.println("[8] Save your word list and continue");
    }

    public int getLongest() {
        return longest;
    }

    public ArrayList<String> getWordList() {
        return wordList;
    }
}
