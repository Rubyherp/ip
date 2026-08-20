# Console UI Test Plan

The `test-ui` skill runs each test case in a fresh Ruby process. Each `### Input` block contains one command, and the following `### Expected output` block contains its exact response. Keep the headings and fenced blocks unchanged so the test runner can parse them.

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
