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

            if (userInput.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(userInput.substring("mark ".length()));
                String response = taskList.markItem(taskNumber - 1);
                ui.printMessage(response);
                continue;
            }

            if (userInput.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(userInput.substring("unmark ".length()));
                String response = taskList.unmarkItem(taskNumber - 1);
                ui.printMessage(response);
                continue;
            }

            String response = taskList.addItem(new Task(userInput));
            ui.printMessage(response);
        }

        scanner.close();
    }
}
