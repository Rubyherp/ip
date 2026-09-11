package ruby.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import ruby.RubyException;
import ruby.contact.Contact;
import ruby.contact.ContactList;
import ruby.task.Deadline;
import ruby.task.Event;
import ruby.task.TaskList;
import ruby.task.Todo;

class StorageTest {
    @TempDir
    Path tempDir;

    private String dataFilePath() {
        return tempDir.resolve("ruby.txt").toString();
    }

    @Test
    void load_noFile_returnsEmptyLists() throws RubyException {
        Storage storage = new Storage(dataFilePath());
        Storage.Data data = storage.load();
        assertEquals("Here are the tasks in your list:", data.tasks().listItems());
        assertEquals("You have no contacts yet.", data.contacts().listContacts());
    }

    @Test
    void saveThenLoad_roundTripsTasks() throws RubyException {
        TaskList taskList = new TaskList();
        taskList.addItem(new Todo("read book"));
        taskList.addItem(new Deadline("return book", LocalDateTime.of(2019, 6, 6, 18, 0)));
        taskList.addItem(new Event("project meeting",
                LocalDateTime.of(2019, 8, 6, 14, 0), LocalDateTime.of(2019, 8, 6, 16, 0)));

        Storage storage = new Storage(dataFilePath());
        storage.save(taskList, new ContactList());

        TaskList loaded = storage.load().tasks();
        assertEquals("T | 0 | read book\nD | 0 | return book | 2019-06-06T18:00"
                + "\nE | 0 | project meeting | 2019-08-06T14:00 | 2019-08-06T16:00",
                loaded.toDataString());
    }

    @Test
    void saveThenLoad_preservesDoneStatus() throws RubyException {
        TaskList taskList = new TaskList();
        Todo todo = new Todo("read book");
        todo.markAsDone();
        taskList.addItem(todo);

        Storage storage = new Storage(dataFilePath());
        storage.save(taskList, new ContactList());

        assertEquals("T | 1 | read book", storage.load().tasks().toDataString());
    }

    @Test
    void saveThenLoad_roundTripsContacts() throws RubyException {
        ContactList contactList = new ContactList();
        contactList.addContact(new Contact("John Doe", "91234567", "john@example.com", "123 Street"));
        contactList.addContact(new Contact("Jane", "", "", ""));

        Storage storage = new Storage(dataFilePath());
        storage.save(new TaskList(), contactList);

        assertEquals("C | John Doe | 91234567 | john@example.com | 123 Street"
                + "\nC | Jane |  |  | ", storage.load().contacts().toDataString());
    }

    @Test
    void saveThenLoad_mixedData_keepsTasksAndContacts() throws RubyException {
        TaskList taskList = new TaskList();
        taskList.addItem(new Todo("read book"));
        ContactList contactList = new ContactList();
        contactList.addContact(new Contact("John", "91234567", "", ""));

        Storage storage = new Storage(dataFilePath());
        storage.save(taskList, contactList);

        Storage.Data loaded = storage.load();
        assertEquals("T | 0 | read book", loaded.tasks().toDataString());
        assertEquals("C | John | 91234567 |  | ", loaded.contacts().toDataString());
    }

    @Test
    void load_taskOnlyFile_loadsTasksAndNoContacts() throws IOException, RubyException {
        Files.writeString(Path.of(dataFilePath()), "T | 1 | read book\n");
        Storage storage = new Storage(dataFilePath());

        Storage.Data loaded = storage.load();

        assertEquals("T | 1 | read book", loaded.tasks().toDataString());
        assertEquals("", loaded.contacts().toDataString());
    }

    @Test
    void load_malformedLine_throws() throws IOException {
        Files.writeString(Path.of(dataFilePath()), "T | 0\n");
        Storage storage = new Storage(dataFilePath());
        assertThrows(RubyException.class, storage::load);
    }

    @Test
    void load_unknownType_throws() throws IOException {
        Files.writeString(Path.of(dataFilePath()), "X | 0 | mystery\n");
        Storage storage = new Storage(dataFilePath());
        assertThrows(RubyException.class, storage::load);
    }

    @Test
    void load_malformedContactLine_throws() throws IOException {
        Files.writeString(Path.of(dataFilePath()), "C | John | 91234567\n");
        Storage storage = new Storage(dataFilePath());
        assertThrows(RubyException.class, storage::load);
    }

    @Test
    void load_contactWithoutName_throws() throws IOException {
        Files.writeString(Path.of(dataFilePath()), "C |  | 91234567 |  | \n");
        Storage storage = new Storage(dataFilePath());
        assertThrows(RubyException.class, storage::load);
    }
}
