package ruby;

/**
 * Starts the Ruby chatbot application.
 */
public class Ruby {
    private final Storage storage;
    private final TaskList taskList;
    private final Ui ui;

    /**
     * Creates Ruby and restores any tasks saved at the given file path.
     *
     * @param filePath Location of Ruby's saved task data.
     */
    public Ruby(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        TaskList loadedTaskList;
        try {
            loadedTaskList = storage.load();
        } catch (RubyException exception) {
            ui.printMessage("Sorry, I couldn't load your saved tasks: "
                    + exception.getMessage());
            loadedTaskList = new TaskList();
        }
        taskList = loadedTaskList;
    }

    /**
     * Runs Ruby until the user exits or the input stream ends.
     */
    public void run() {
        ui.printWelcome();
        boolean isExit = false;

        while (!isExit && ui.hasNextCommand()) {
            try {
                Command command = Parser.parse(ui.readCommand());
                command.execute(taskList, ui, storage);
                isExit = command.isExit();
            } catch (RubyException exception) {
                ui.printMessage("Sorry, I couldn't process that: " + exception.getMessage());
            }
        }
    }

    /**
     * Starts Ruby and processes commands until the input ends or the user exits.
     *
     * @param args Command-line arguments; not used by Ruby.
     */
    public static void main(String[] args) {
        new Ruby("data/ruby.txt").run();
    }
}
