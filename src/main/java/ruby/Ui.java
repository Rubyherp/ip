package ruby;

import java.util.Scanner;

/**
 * Handles all user interaction, formatting every message with a consistent
 * indentation and horizontal line.
 */
public class Ui {
    private static final String HORIZONTAL_LINE =
            "____________________________________________________________";
    private static final String CHATBOT_NAME = "Ruby";
    private static final String GREETING = "Hello! I'm " + CHATBOT_NAME + ".";
    private static final String PROMPT = "What can I do for you?";
    private static final String FAREWELL = "Bye. Hope to see you again soon!";
    private static final String BANNER = "/$$$$$$$            /$$\n"
            + "| $$__  $$          | $$\n"
            + "| $$  \\ $$ /$$   /$$| $$$$$$$  /$$   /$$\n"
            + "| $$$$$$$/| $$  | $$| $$__  $$| $$  | $$\n"
            + "| $$__  $$| $$  | $$| $$  \\ $$| $$  | $$\n"
            + "| $$  \\ $$| $$  | $$| $$  | $$| $$  | $$\n"
            + "| $$  | $$|  $$$$$$/| $$$$$$$/|  $$$$$$$\n"
            + "|__/  |__/ \\______/ |_______/  \\____  $$\n"
            + "                               /$$  | $$\n"
            + "                              |  $$$$$$/\n"
            + "                               \\______/";
    private static final String INDENT = " ";
    private final Scanner scanner;

    /**
     * Creates a user interface for the Ruby chatbot.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    private void printHorizontalLine() {
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Prints every line of a chatbot message with a single leading space.
     *
     * @param message Message to print.
     */
    private void printIndented(String message) {
        System.out.println(INDENT + message.replace("\n", "\n" + INDENT));
    }

    /**
     * Prints Ruby's banner, greeting, and command prompt.
     */
    public void printWelcome() {
        printHorizontalLine();
        printIndented(BANNER);
        printIndented(GREETING);
        printIndented(PROMPT);
        printHorizontalLine();
    }

    /**
     * Prints a chatbot message between horizontal divider lines.
     *
     * @param message Message to print.
     */
    public void printMessage(String message) {
        printHorizontalLine();
        printIndented(message);
        printHorizontalLine();
    }

    /**
     * Prints Ruby's farewell message.
     */
    public void printFarewell() {
        printMessage(FAREWELL);
    }

    /**
     * Returns whether another command can be read from the user.
     *
     * @return True when another input line is available.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads one complete command from the user.
     *
     * @return The user's raw command text.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

}
