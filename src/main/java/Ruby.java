import java.util.Scanner;

/**
 * Starts the Ruby chatbot application.
 */
public class Ruby {
    public static void main(String[] args) {
        Ui ui = new Ui();
        TaskList taskList = new TaskList();

        ui.printWelcome();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String userInput = scanner.nextLine();

            if ("bye".equals(userInput)) {
                ui.printFarewell();
                break;
            }

            if ("list".equals(userInput)) {
                String response = taskList.listItems();
                ui.printMessage(response);
                continue;
            }

            String response = taskList.addItem(userInput);
            ui.printMessage(response);
        }

        scanner.close();
    }
}
