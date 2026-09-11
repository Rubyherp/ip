# Ruby User Guide

// Product screenshot goes here

// Product intro goes here

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
