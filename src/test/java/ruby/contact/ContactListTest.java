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

        assertEquals("Got it. I've added this contact:\n"
                + "  John Doe | 91234567 | john@example.com\n"
                + "Now you have 1 contacts in the list.", response);
    }

    @Test
    void deleteContact_existingIndex_removesContactAndReportsCount() throws RubyException {
        ContactList contacts = new ContactList();
        contacts.addContact(new Contact("John", "", "", ""));
        contacts.addContact(new Contact("Jane", "91234567", "", ""));

        String response = contacts.deleteContact(0);

        assertEquals("Noted. I've removed this contact:\n"
                + "  John\n"
                + "Now you have 1 contacts in the list.", response);
        assertEquals("Here are your contacts:\n"
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

        assertEquals("You have no contacts yet.", contacts.listContacts());
    }

    @Test
    void listContacts_multipleContacts_returnsNumberedList() {
        ContactList contacts = new ContactList();
        contacts.addContact(new Contact("John", "", "", ""));
        contacts.addContact(new Contact("Jane", "91234567", "jane@example.com", ""));

        assertEquals("Here are your contacts:\n"
                + "1. John\n"
                + "2. Jane | 91234567 | jane@example.com", contacts.listContacts());
    }
}
