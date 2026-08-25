# Console UI Test Plan

The `test-ui` skill runs each test case in a fresh Ruby process. Each `### Input` block contains one command, and the following `### Expected output` block contains its exact response. Keep the headings and fenced blocks unchanged so the test runner can parse them.

Optional persistence blocks: `### Data file` seeds `data/ruby.txt` before the process starts, and `### Saved data` asserts the exact contents of `data/ruby.txt` after the process exits.

## Greeting and exit

Aim: Verify that Ruby prints its banner and greeting on startup, then exits with the required farewell.

### Startup output

```text
____________________________________________________________
 /$$$$$$$            /$$
 | $$__  $$          | $$
 | $$  \ $$ /$$   /$$| $$$$$$$  /$$   /$$
 | $$$$$$$/| $$  | $$| $$__  $$| $$  | $$
 | $$__  $$| $$  | $$| $$  \ $$| $$  | $$
 | $$  \ $$| $$  | $$| $$  | $$| $$  | $$
 | $$  | $$|  $$$$$$/| $$$$$$$/|  $$$$$$$
 |__/  |__/ \______/ |_______/  \____  $$
                                /$$  | $$
                               |  $$$$$$/
                                \______/
 Hello! I'm Ruby.
 What can I do for you?
____________________________________________________________
```

### Input

```text
bye
```

### Expected output

```text
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## Level 4 task workflow

Aim: Verify creation, listing, marking, and unmarking for todos, deadlines, and events.

### Startup output

```text
____________________________________________________________
 /$$$$$$$            /$$
 | $$__  $$          | $$
 | $$  \ $$ /$$   /$$| $$$$$$$  /$$   /$$
 | $$$$$$$/| $$  | $$| $$__  $$| $$  | $$
 | $$__  $$| $$  | $$| $$  \ $$| $$  | $$
 | $$  \ $$| $$  | $$| $$  | $$| $$  | $$
 | $$  | $$|  $$$$$$/| $$$$$$$/|  $$$$$$$
 |__/  |__/ \______/ |_______/  \____  $$
                                /$$  | $$
                               |  $$$$$$/
                                \______/
 Hello! I'm Ruby.
 What can I do for you?
____________________________________________________________
```

### Input

```text
todo borrow book
```

### Expected output

```text
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
```

### Input

```text
deadline return book /by Sunday
```

### Expected output

```text
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 2 tasks in the list.
____________________________________________________________
```

### Input

```text
event project meeting /from Mon 2pm /to 4pm
```

### Expected output

```text
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
```

### Input

```text
list
```

### Expected output

```text
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] borrow book
 2.[D][ ] return book (by: Sunday)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
```

### Input

```text
mark 2
```

### Expected output

```text
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] return book (by: Sunday)
____________________________________________________________
```

### Input

```text
unmark 2
```

### Expected output

```text
____________________________________________________________
 OK, I've marked this task as not done yet:
   [D][ ] return book (by: Sunday)
____________________________________________________________
```

### Input

```text
bye
```

### Expected output

```text
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## Level 5 error recovery

Aim: Verify that malformed and unknown commands report specific errors without changing valid task state.

### Startup output

```text
____________________________________________________________
 /$$$$$$$            /$$
 | $$__  $$          | $$
 | $$  \ $$ /$$   /$$| $$$$$$$  /$$   /$$
 | $$$$$$$/| $$  | $$| $$__  $$| $$  | $$
 | $$__  $$| $$  | $$| $$  \ $$| $$  | $$
 | $$  \ $$| $$  | $$| $$  | $$| $$  | $$
 | $$  | $$|  $$$$$$/| $$$$$$$/|  $$$$$$$
 |__/  |__/ \______/ |_______/  \____  $$
                                /$$  | $$
                               |  $$$$$$/
                                \______/
 Hello! I'm Ruby.
 What can I do for you?
____________________________________________________________
```

### Input

```text

```

### Expected output

```text
____________________________________________________________
 Sorry, I couldn't process that: Please enter a command.
____________________________________________________________
```

### Input

```text
todo read book
```

### Expected output

```text
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
```

### Input

```text
todo
```

### Expected output

```text
____________________________________________________________
 Sorry, I couldn't process that: A todo needs a description.
____________________________________________________________
```

### Input

```text
blah
```

### Expected output

```text
____________________________________________________________
 Sorry, I couldn't process that: I don't recognise that command.
____________________________________________________________
```

### Input

```text
todoish read book
```

### Expected output

```text
____________________________________________________________
 Sorry, I couldn't process that: I don't recognise that command.
____________________________________________________________
```

### Input

```text
deadline /by Sunday
```

### Expected output

```text
____________________________________________________________
 Sorry, I couldn't process that: A deadline needs a description.
____________________________________________________________
```

### Input

```text
deadline submit report
```

### Expected output

```text
____________________________________________________________
 Sorry, I couldn't process that: Use: deadline DESCRIPTION /by DATE_OR_TIME.
____________________________________________________________
```

### Input

```text
deadline submit report /bypass Sunday
```

### Expected output

```text
____________________________________________________________
 Sorry, I couldn't process that: Use: deadline DESCRIPTION /by DATE_OR_TIME.
____________________________________________________________
```

### Input

```text
deadline submit report /by
```

### Expected output

```text
____________________________________________________________
 Sorry, I couldn't process that: A deadline needs a date or time after /by.
____________________________________________________________
```

### Input

```text
deadline submit report /by Sunday
```

### Expected output

```text
____________________________________________________________
 Got it. I've added this task:
   [D][ ] submit report (by: Sunday)
 Now you have 2 tasks in the list.
____________________________________________________________
```

### Input

```text
event /from Mon /to Tue
```

### Expected output

```text
____________________________________________________________
 Sorry, I couldn't process that: An event needs a description.
____________________________________________________________
```

### Input

```text
event meeting /to Tue
```

### Expected output

```text
____________________________________________________________
 Sorry, I couldn't process that: Use: event DESCRIPTION /from START /to END.
____________________________________________________________
```

### Input

```text
event meeting /fromage Mon /to Tue
```

### Expected output

```text
____________________________________________________________
 Sorry, I couldn't process that: Use: event DESCRIPTION /from START /to END.
____________________________________________________________
```

### Input

```text
event meeting /from Mon
```

### Expected output

```text
____________________________________________________________
 Sorry, I couldn't process that: An event needs an end after /to.
____________________________________________________________
```

### Input

```text
event meeting /from /to Tue
```

### Expected output

```text
____________________________________________________________
 Sorry, I couldn't process that: An event needs a start after /from.
____________________________________________________________
```

### Input

```text
event meeting /from Mon /to
```

### Expected output

```text
____________________________________________________________
 Sorry, I couldn't process that: An event needs an end after /to.
____________________________________________________________
```

### Input

```text
event meeting /from Mon /to Tue
```

### Expected output

```text
____________________________________________________________
 Got it. I've added this task:
   [E][ ] meeting (from: Mon to: Tue)
 Now you have 3 tasks in the list.
____________________________________________________________
```

### Input

```text
mark
```

### Expected output

```text
____________________________________________________________
 Sorry, I couldn't process that: Give me a task number after mark.
____________________________________________________________
```

### Input

```text
mark banana
```

### Expected output

```text
____________________________________________________________
 Sorry, I couldn't process that: The task number for mark must be a whole number.
____________________________________________________________
```

### Input

```text
mark 7
```

### Expected output

```text
____________________________________________________________
 Sorry, I couldn't process that: Task 7 does not exist; choose a number from 1 to 3.
____________________________________________________________
```

### Input

```text
mark 2
```

### Expected output

```text
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] submit report (by: Sunday)
____________________________________________________________
```

### Input

```text
unmark
```

### Expected output

```text
____________________________________________________________
 Sorry, I couldn't process that: Give me a task number after unmark.
____________________________________________________________
```

### Input

```text
list
```

### Expected output

```text
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[D][X] submit report (by: Sunday)
 3.[E][ ] meeting (from: Mon to: Tue)
____________________________________________________________
```

### Input

```text
bye
```

### Expected output

```text
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## Level 6 deletion and index shifting

Aim: Verify collection-backed deletion, invalid delete handling, task counts, and indexes after removals.

### Startup output

```text
____________________________________________________________
 /$$$$$$$            /$$
 | $$__  $$          | $$
 | $$  \ $$ /$$   /$$| $$$$$$$  /$$   /$$
 | $$$$$$$/| $$  | $$| $$__  $$| $$  | $$
 | $$__  $$| $$  | $$| $$  \ $$| $$  | $$
 | $$  \ $$| $$  | $$| $$  | $$| $$  | $$
 | $$  | $$|  $$$$$$/| $$$$$$$/|  $$$$$$$
 |__/  |__/ \______/ |_______/  \____  $$
                                /$$  | $$
                               |  $$$$$$/
                                \______/
 Hello! I'm Ruby.
 What can I do for you?
____________________________________________________________
```

### Input

```text
delete
```

### Expected output

```text
____________________________________________________________
 Sorry, I couldn't process that: Give me a task number after delete.
____________________________________________________________
```

### Input

```text
delete 1
```

### Expected output

```text
____________________________________________________________
 Sorry, I couldn't process that: There are no tasks to delete.
____________________________________________________________
```

### Input

```text
todo read book
```

### Expected output

```text
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
```

### Input

```text
deadline return book /by Sunday
```

### Expected output

```text
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 2 tasks in the list.
____________________________________________________________
```

### Input

```text
event project meeting /from Mon 2pm /to 4pm
```

### Expected output

```text
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
```

### Input

```text
delete banana
```

### Expected output

```text
____________________________________________________________
 Sorry, I couldn't process that: The task number for delete must be a whole number.
____________________________________________________________
```

### Input

```text
delete 0
```

### Expected output

```text
____________________________________________________________
 Sorry, I couldn't process that: Task numbers must be positive whole numbers.
____________________________________________________________
```

### Input

```text
delete 4
```

### Expected output

```text
____________________________________________________________
 Sorry, I couldn't process that: Task 4 does not exist; choose a number from 1 to 3.
____________________________________________________________
```

### Input

```text
delete 2
```

### Expected output

```text
____________________________________________________________
 Noted. I've removed this task:
   [D][ ] return book (by: Sunday)
 Now you have 2 tasks in the list.
____________________________________________________________
```

### Input

```text
list
```

### Expected output

```text
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
```

### Input

```text
mark 2
```

### Expected output

```text
____________________________________________________________
 Nice! I've marked this task as done:
   [E][X] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
```

### Input

```text
delete 2
```

### Expected output

```text
____________________________________________________________
 Noted. I've removed this task:
   [E][X] project meeting (from: Mon 2pm to: 4pm)
 Now you have 1 tasks in the list.
____________________________________________________________
```

### Input

```text
list
```

### Expected output

```text
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
____________________________________________________________
```

### Input

```text
bye
```

### Expected output

```text
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## Level 7 tasks are saved to disk

Aim: Verify that adding, marking, and deleting tasks writes the data file in the expected format.

### Startup output

```text
____________________________________________________________
 /$$$$$$$            /$$
 | $$__  $$          | $$
 | $$  \ $$ /$$   /$$| $$$$$$$  /$$   /$$
 | $$$$$$$/| $$  | $$| $$__  $$| $$  | $$
 | $$__  $$| $$  | $$| $$  \ $$| $$  | $$
 | $$  \ $$| $$  | $$| $$  | $$| $$  | $$
 | $$  | $$|  $$$$$$/| $$$$$$$/|  $$$$$$$
 |__/  |__/ \______/ |_______/  \____  $$
                                /$$  | $$
                               |  $$$$$$/
                                \______/
 Hello! I'm Ruby.
 What can I do for you?
____________________________________________________________
```

### Input

```text
todo read book
```

### Expected output

```text
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
```

### Input

```text
deadline submit report /by Sunday
```

### Expected output

```text
____________________________________________________________
 Got it. I've added this task:
   [D][ ] submit report (by: Sunday)
 Now you have 2 tasks in the list.
____________________________________________________________
```

### Input

```text
event project meeting /from Aug 6th 2pm /to 4pm
```

### Expected output

```text
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
```

### Input

```text
mark 2
```

### Expected output

```text
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] submit report (by: Sunday)
____________________________________________________________
```

### Input

```text
delete 1
```

### Expected output

```text
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] read book
 Now you have 2 tasks in the list.
____________________________________________________________
```

### Input

```text
bye
```

### Expected output

```text
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

### Saved data

```text
D | 1 | submit report | Sunday
E | 0 | project meeting | Aug 6th 2pm | 4pm
```

## Level 7 saved tasks are loaded at startup

Aim: Verify that tasks saved in the data file are restored on startup with their type and done status.

### Data file

```text
T | 1 | read book
D | 0 | return book | June 6th
E | 1 | project meeting | Aug 6th 2pm | 4pm
```

### Startup output

```text
____________________________________________________________
 /$$$$$$$            /$$
 | $$__  $$          | $$
 | $$  \ $$ /$$   /$$| $$$$$$$  /$$   /$$
 | $$$$$$$/| $$  | $$| $$__  $$| $$  | $$
 | $$__  $$| $$  | $$| $$  \ $$| $$  | $$
 | $$  \ $$| $$  | $$| $$  | $$| $$  | $$
 | $$  | $$|  $$$$$$/| $$$$$$$/|  $$$$$$$
 |__/  |__/ \______/ |_______/  \____  $$
                                /$$  | $$
                               |  $$$$$$/
                                \______/
 Hello! I'm Ruby.
 What can I do for you?
____________________________________________________________
```

### Input

```text
list
```

### Expected output

```text
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] read book
 2.[D][ ] return book (by: June 6th)
 3.[E][X] project meeting (from: Aug 6th 2pm to: 4pm)
____________________________________________________________
```

### Input

```text
bye
```

### Expected output

```text
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```
