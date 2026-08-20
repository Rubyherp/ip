import java.util.Scanner;

/**
 * Starts the Ruby chatbot application.
 */
public class Ruby {
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.printWelcome();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String userInput = scanner.nextLine();

            if ("bye".equals(userInput)) {
                ui.printFarewell();
                break;
            }

            ui.printMessage(userInput);
        }

        scanner.close();
    }
}
