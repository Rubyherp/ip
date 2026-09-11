package ruby.contact;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ruby.RubyException;

/**
 * Stores the contacts managed by Ruby.
 */
public class ContactList {
    private final List<Contact> contacts;

    /**
     * Creates an empty contact list.
     */
    public ContactList() {
        this.contacts = new ArrayList<>();
    }

    /**
     * Adds a contact to the end of this list.
     *
     * @param contact Contact to add.
     * @return Confirmation containing the contact and updated contact count.
     */
    public String addContact(Contact contact) {
        assert contact != null : "Contact cannot be null";
        contacts.add(contact);
        return "Got it. I've added this contact:\n  "
                + contact
                + "\nNow you have "
                + contacts.size()
                + " contacts in the list.";
    }

    /**
     * Removes and returns confirmation for the contact at a zero-based index.
     *
     * @param index Zero-based index of the contact.
     * @return Confirmation describing the removed contact and remaining count.
     * @throws RubyException If the index does not identify an existing contact.
     */
    public String deleteContact(int index) throws RubyException {
        assert index >= 0 : "Parser only produces non-negative contact indexes";
        Contact contact = getContact(index, "delete");
        contacts.remove(index);

        return "Noted. I've removed this contact:\n  "
                + contact
                + "\nNow you have "
                + contacts.size()
                + " contacts in the list.";
    }

    /**
     * Formats all stored contacts as a one-based numbered list.
     *
     * @return Numbered contacts, or a message when there are no contacts.
     */
    public String listContacts() {
        if (contacts.isEmpty()) {
            return "You have no contacts yet.";
        }
        return "Here are your contacts:"
                + IntStream.range(0, contacts.size())
                        .mapToObj(i -> "\n" + (i + 1) + ". " + contacts.get(i))
                        .collect(Collectors.joining());
    }

    /**
     * Formats every contact as one line of the saved data file.
     *
     * @return One contact per line, or an empty string when there are no
     *         contacts.
     */
    public String toDataString() {
        return contacts.stream()
                .map(Contact::toDataString)
                .collect(Collectors.joining("\n"));
    }

    /**
     * Retrieves a contact after checking that the requested index exists.
     *
     * @param index  Zero-based index of the contact.
     * @param action Action requested by the user, used in error messages.
     * @return Contact at the specified index.
     * @throws RubyException If the index does not identify an existing contact.
     */
    private Contact getContact(int index, String action) throws RubyException {
        if (contacts.isEmpty()) {
            throw new RubyException("There are no contacts to " + action + ".");
        }
        if (index < 0 || index >= contacts.size()) {
            throw new RubyException(
                    "Contact " + (index + 1)
                            + " does not exist; choose a number from 1 to "
                            + contacts.size()
                            + ".");
        }
        assert contacts.get(index) != null : "Contact at index " + index + " should not be null";
        return contacts.get(index);
    }
}
