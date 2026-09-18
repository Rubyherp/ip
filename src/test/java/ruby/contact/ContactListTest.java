package ruby.contact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ruby.RubyException;

class ContactListTest {

    @Test
    void addContact_newList_reportsContactAndCount() {
        ContactList contacts = new ContactList();

        String response = contacts.addContact(new Contact("John Doe", "91234567", "john@example.com", ""));

        assertEquals("Saved. I never forget a name:\n"
                + "  John Doe | 91234567 | john@example.com\n"
                + "That's 1 contact in your circle.", response);
    }

    @Test
    void deleteContact_existingIndex_removesContactAndReportsCount() throws RubyException {
        ContactList contacts = new ContactList();
        contacts.addContact(new Contact("John", "", "", ""));
        contacts.addContact(new Contact("Jane", "91234567", "", ""));

        String response = contacts.deleteContact(0);

        assertEquals("Removed from your circle:\n"
                + "  John\n"
                + "That leaves 1 contact in your circle.", response);
        assertEquals("Your circle, as requested:\n"
                + "1. Jane | 91234567", contacts.listContacts());
    }

    @Test
    void deleteContact_emptyList_throws() {
        ContactList contacts = new ContactList();

        assertThrows(RubyException.class, () -> contacts.deleteContact(0));
    }

    @Test
    void deleteContact_outOfRangeIndex_throws() {
        ContactList contacts = new ContactList();
        contacts.addContact(new Contact("John", "", "", ""));

        assertThrows(RubyException.class, () -> contacts.deleteContact(1));
    }

    @Test
    void listContacts_emptyList_returnsNoContactsMessage() {
        ContactList contacts = new ContactList();

        assertEquals("No contacts yet — a fresh, unpolished page.", contacts.listContacts());
    }

    @Test
    void listContacts_multipleContacts_returnsNumberedList() {
        ContactList contacts = new ContactList();
        contacts.addContact(new Contact("John", "", "", ""));
        contacts.addContact(new Contact("Jane", "91234567", "jane@example.com", ""));

        assertEquals("Your circle, as requested:\n"
                + "1. John\n"
                + "2. Jane | 91234567 | jane@example.com", contacts.listContacts());
    }
}
