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

            if (userInput.startsWith("todo ")) {
                String description = userInput.substring("todo ".length());
                String response = taskList.addItem(new Todo(description));
                ui.printMessage(response);
                continue;
            }

            if (userInput.startsWith("deadline ")) {
                String details = userInput.substring("deadline ".length());
                String[] parts = details.split(" /by ", 2);

                String description = parts[0];
                String deadline = parts[1];
                Task task = new Deadline(description, deadline);

                String response = taskList.addItem(task);
                ui.printMessage(response);
                continue;
            }

            if (userInput.startsWith("event ")) {
                String details = userInput.substring("event ".length());
                String[] descriptionAndDates = details.split(" /from ", 2);
                String description = descriptionAndDates[0];
                String[] dates = descriptionAndDates[1].split(" /to ", 2);

                String startDate = dates[0];
                String endDate = dates[1];
                Task task = new Event(description, startDate, endDate);

                String response = taskList.addItem(task);
                ui.printMessage(response);
                continue;
            }

            String response = taskList.addItem(new Task(userInput));
            ui.printMessage(response);
        }

        scanner.close();
    }
}
