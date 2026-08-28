package ruby.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

import ruby.RubyException;

class TaskListTest {

    private TaskList taskListWith(Task... tasks) {
        TaskList list = new TaskList();
        for (Task task : tasks) {
            list.addItem(task);
        }
        return list;
    }

    @Test
    void listItems_newList_returnsHeadingOnly() {
        TaskList taskList = new TaskList();
        assertEquals("Here are the tasks in your list:", taskList.listItems());
    }

    @Test
    void addItem_addsTaskAndReportsCount() {
        TaskList taskList = taskListWith(new Todo("read book"));
        String response = taskList.addItem(new Todo("return book"));
        assertEquals("Got it. I've added this task:\n  [T][ ] return book"
                + "\nNow you have 2 tasks in the list.", response);
    }

    @Test
    void listItems_withTasks_returnsNumberedList() {
        TaskList taskList = taskListWith(new Todo("read book"), new Todo("return book"));
        assertEquals("Here are the tasks in your list:\n1.[T][ ] read book\n2.[T][ ] return book",
                taskList.listItems());
    }

    @Test
    void markItem_marksTaskAtIndexAsDone() throws RubyException {
        TaskList taskList = taskListWith(new Todo("read book"), new Todo("return book"));
        String response = taskList.markItem(0);
        assertEquals("Nice! I've marked this task as done:\n  [T][X] read book", response);
        assertEquals("Here are the tasks in your list:\n1.[T][X] read book\n2.[T][ ] return book",
                taskList.listItems());
    }

    @Test
    void unmarkItem_unmarksTaskAtIndex() throws RubyException {
        TaskList taskList = taskListWith(new Todo("read book"), new Todo("return book"));
        taskList.markItem(0);
        String response = taskList.unmarkItem(0);
        assertEquals("OK, I've marked this task as not done yet:\n  [T][ ] read book", response);
    }

    @Test
    void deleteItem_removesTaskAtIndex() throws RubyException {
        TaskList taskList = taskListWith(new Todo("read book"), new Todo("return book"));
        String response = taskList.deleteItem(0);
        assertEquals("Noted. I've removed this task:\n  [T][ ] read book"
                + "\nNow you have 1 tasks in the list.", response);
        assertEquals("Here are the tasks in your list:\n1.[T][ ] return book",
                taskList.listItems());
    }

    @Test
    void toDataString_returnsOneLinePerTask() {
        TaskList taskList = taskListWith(new Todo("read book"), new Todo("return book"));
        assertEquals("T | 0 | read book\nT | 0 | return book", taskList.toDataString());
    }

    @Test
    void markItem_emptyList_throws() {
        TaskList taskList = new TaskList();
        RubyException exception = assertThrows(RubyException.class, () -> taskList.markItem(0));
        assertEquals("There are no tasks to mark.", exception.getMessage());
    }

    @Test
    void unmarkItem_outOfRangeIndex_throws() {
        TaskList taskList = taskListWith(new Todo("read book"), new Todo("return book"));
        RubyException exception = assertThrows(RubyException.class, () -> taskList.unmarkItem(5));
        assertEquals("Task 6 does not exist; choose a number from 1 to 2.", exception.getMessage());
    }

    @Test
    void deleteItem_emptyList_throws() {
        TaskList taskList = new TaskList();
        RubyException exception = assertThrows(RubyException.class, () -> taskList.deleteItem(0));
        assertEquals("There are no tasks to delete.", exception.getMessage());
    }

    @Test
    void find_matchingTasks_returnsNumberedMatches() {
        TaskList taskList = taskListWith(new Todo("read book"), new Todo("return book"),
                new Todo("buy milk"));
        assertEquals("Here are the matching tasks in your list:\n1.[T][ ] read book"
                        + "\n2.[T][ ] return book",
                taskList.find("book"));
    }

    @Test
    void find_keepsOriginalListNumbering() {
        TaskList taskList = taskListWith(new Todo("read book"), new Todo("buy milk"),
                new Todo("return book"));
        assertEquals("Here are the matching tasks in your list:\n1.[T][ ] read book"
                        + "\n3.[T][ ] return book",
                taskList.find("book"));
    }

    @Test
    void find_caseInsensitive_matchesRegardlessOfCase() {
        TaskList taskList = taskListWith(new Todo("Read Book"));
        assertEquals("Here are the matching tasks in your list:\n1.[T][ ] Read Book",
                taskList.find("BOOK"));
    }

    @Test
    void find_noMatches_returnsMessage() {
        TaskList taskList = taskListWith(new Todo("read book"));
        assertEquals("No matching tasks found.", taskList.find("dance"));
    }

    @Test
    void find_matchesOnFullTaskText_includesDeadlineDate() {
        TaskList taskList = taskListWith(new Deadline("return book",
                LocalDateTime.of(2019, 6, 6, 0, 0)));
        assertEquals("Here are the matching tasks in your list:\n1.[D][ ] return book (by: Jun 06 2019)",
                taskList.find("Jun 06"));
    }
}
