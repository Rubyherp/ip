package ruby;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Loads tasks from and saves tasks to a data file on the hard disk.
 */
public class Storage {
    private final String FILE_PATH = "data/ruby.txt";

    /**
     * Creates a storage handler for the default data file.
     */
    public Storage() {
    }

    /**
     * Reads the tasks saved in the data file.
     *
     * @return The saved tasks, or an empty list when no data file exists yet.
     * @throws RubyException If the data file exists but cannot be read.
     */
    public TaskList load() throws RubyException {
        File file = new File(FILE_PATH);
        TaskList taskList = new TaskList();
        File parentDir = file.getParentFile();

        if (parentDir != null) {
            parentDir.mkdirs();
        }

        if (!file.exists()) {
            return taskList;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isBlank()) {
                    continue;
                }
                taskList.addItem(parseLine(line));
            }
        } catch (FileNotFoundException exception) {
            throw new RubyException("The data file could not be found.");
        }

        return taskList;
    }

    /**
     * Writes every task to the data file, overwriting any previous contents.
     *
     * @param taskList Tasks to save.
     * @throws RubyException If the data file cannot be written.
     */
    public void save(TaskList taskList) throws RubyException {
        File file = new File(FILE_PATH);
        File parentDir = file.getParentFile();

        if (parentDir != null) {
            parentDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(taskList.toDataString());
        } catch (IOException exception) {
            throw new RubyException("The tasks could not be saved to disk.");
        }
    }

    /**
     * Rebuilds a task from one line of the data file.
     *
     * @param line One line in the format
     *             {@code TYPE | DONE | DESCRIPTION [ | EXTRA ...]}.
     * @return The reconstructed task.
     * @throws RubyException If the line is missing required fields or has an
     *                       unknown type.
     */
    private static Task parseLine(String line) throws RubyException {
        String[] parts = line.split(" \\| ", -1);
        if (parts.length < 3) {
            throw new RubyException("The data file contains a malformed task line.");
        }

        String type = parts[0];
        boolean isDone = "1".equals(parts[1]);

        Task task;
        switch (type) {
            case "T":
                task = new Todo(joinParts(parts, 2, parts.length));
                break;
            case "D":
                if (parts.length < 4) {
                    throw new RubyException("The data file contains a malformed deadline.");
                }
                task = new Deadline(joinParts(parts, 2, parts.length - 1), parts[parts.length - 1]);
                break;
            case "E":
                if (parts.length < 5) {
                    throw new RubyException("The data file contains a malformed event.");
                }
                task = new Event(joinParts(parts, 2, parts.length - 2),
                        parts[parts.length - 2], parts[parts.length - 1]);
                break;
            default:
                throw new RubyException("The data file contains an unknown task type: " + type);
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Joins the parts between two indexes back together with the {@code " | "}
     * separator,
     * so that descriptions containing the separator are preserved.
     *
     * @param parts Parts of a split data line.
     * @param start First index to include.
     * @param end   Index after the last part to include.
     * @return The joined text, or an empty string when the range is empty.
     */
    private static String joinParts(String[] parts, int start, int end) {
        StringBuilder joined = new StringBuilder();
        for (int i = start; i < end; i++) {
            if (i > start) {
                joined.append(" | ");
            }
            joined.append(parts[i]);
        }
        return joined.toString();
    }
}
