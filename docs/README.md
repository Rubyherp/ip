# Ruby User Guide

// Product screenshot goes here

Ruby is a gem among task managers: brilliant, a little vain, and secretly
proud of you. She tracks your tasks and contacts with high standards, teases
your typos, and never forgets a name.

## Meet Ruby

* **Name:** Ruby.
* **Personality:** a precious gem — confident, playful, and a perfectionist
  about your schedule. She teases the mistake, never the person.
* **Look:** rose and maroon, a cat for a face, and a fondness for shine.

Example exchanges:

```
> todo read book
Added to the collection:
  [T][ ] read book
That's 1 task on your plate.
```

```
> nonsense
Hold on — That's not in my repertoire. Try a command I know.
```

```
> bye
Bye. Your tasks are in precious hands.
```

## Adding deadlines

// Describe the action and its outcome.

// Give examples of usage

Example: `keyword (optional arguments)`

// A description of the expected outcome goes here

```
expected output
```

## Managing contacts

Ruby can keep a list of contacts alongside your tasks. A contact has a required
name and optional phone number, email address, and address.

Add a contact with `contact add`, supplying optional fields with `/phone`,
`/email`, and `/address` in any order:

```
contact add John Doe /phone 91234567 /email john@example.com /address 123 Street
Got it. I've added this contact:
  John Doe | 91234567 | john@example.com | 123 Street
Now you have 1 contacts in the list.
```

List your contacts with `contact list`:

```
contact list
Here are your contacts:
1. John Doe | 91234567 | john@example.com | 123 Street
```

Delete a contact by its number in the list with `contact delete INDEX`:

```
contact delete 1
Noted. I've removed this contact:
  John Doe | 91234567 | john@example.com | 123 Street
Now you have 0 contacts in the list.
```

Rules:

* A contact needs a name.
* Phone numbers may contain digits only, with an optional leading `+`.
* Emails must look like `name@example.com`.
* The `|` character is not allowed in contact details.
* Unknown, repeated, or empty fields are rejected.

## Feature ABC

// Feature details


## Feature XYZ

// Feature details
