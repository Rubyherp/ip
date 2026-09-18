# Ruby Personality Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Give Ruby a consistent "precious gem" personality across every user-facing phrase while keeping command behaviour, data format, and the exact-match console UI suite intact.

**Architecture:** Inline rewrite (approved approach B). Strings change where they live in `Ui`, `ExitCommand`, `MainWindow`, `TaskList`, `ContactList`, and `Parser`. A single private `errorMessage` helper in `Ruby` replaces the two different error prefixes so GUI and console match.

**Tech Stack:** Java 25, JavaFX 17, Gradle, JUnit 5, Python 3 exact-match UI test runner.

## Global Constraints

- Java 25 only; on macOS run `sdk use java 25.0.3.fx-zulu` before build/test commands.
- SE-EDU Java coding standard: 4-space indent, max line 120 chars, Javadoc for classes/public methods.
- The em dash `—` is part of the approved copy; keep files UTF-8.
- Data lines (task/contact text, list numbering) and the saved-data format must not change.
- Do not commit unless the user explicitly asks (AGENTS.md). If asked, use SE-EDU commit messages.
- After every code change: run `./gradlew check` and `python3 .codex/skills/test-ui/scripts/run_ui_tests.py`; update `test/ui-test-plan.md` so the suite stays green.

---

### Task 1: Restore a green baseline

The worktree has uncommitted WIP personality strings in `Ruby.java` and `Parser.java` that the approved inventory replaces. Restore the committed versions first so every later task can end green.

**Files:**
- Restore: `src/main/java/ruby/Ruby.java`, `src/main/java/ruby/command/Parser.java`

**Interfaces:**
- Consumes: nothing.
- Produces: a clean, passing baseline.

- [ ] **Step 1: Preserve the WIP if desired**

Run (optional, only if you want a copy of the WIP strings):

```bash
git stash push -m "wip-sassy-personality" -- src/main/java/ruby/Ruby.java src/main/java/ruby/command/Parser.java
```

- [ ] **Step 2: Restore the committed files**

Run:

```bash
git restore src/main/java/ruby/Ruby.java src/main/java/ruby/command/Parser.java
```

Expected: `git status --short` shows only `docs/superpowers/specs/2026-09-18-ruby-personality-design.md` as new.

- [ ] **Step 3: Verify the suite is green**

Run: `python3 .codex/skills/test-ui/scripts/run_ui_tests.py`
Expected: `All 8 UI test case(s) passed.`

---

### Task 2: Greeting, prompt, and farewell

**Files:**
- Modify: `src/main/java/ruby/ui/Ui.java`
- Modify: `src/main/java/ruby/command/ExitCommand.java`
- Modify: `src/main/java/ruby/MainWindow.java`
- Modify: `test/ui-test-plan.md`

**Interfaces:**
- Consumes: green baseline from Task 1.
- Produces: `Ui.GREETING`, `Ui.PROMPT`, `ExitCommand` farewell, `MainWindow.WELCOME_MESSAGE` in the new voice. No signatures change.

- [ ] **Step 1: Update the test plan first (failing expectations)**

In `test/ui-test-plan.md`, replace the greeting and prompt lines in **every** `### Startup output` block (all 8 cases):

```text
 Hello! I'm Ruby.
 What can I do for you?
```

becomes

```text
 Hi, I'm Ruby — the gem your task list has been missing.
 What are we polishing today?
```

Then replace the body of **every** expected output for a `bye` input:

```text
 Bye. Hope to see you again soon!
```

becomes

```text
 Bye. Your tasks are in precious hands.
```

- [ ] **Step 2: Run the runner to verify it fails**

Run: `python3 .codex/skills/test-ui/scripts/run_ui_tests.py`
Expected: FAIL in `Greeting and exit` at `<startup>` (actual still shows `Hello! I'm Ruby.`).

- [ ] **Step 3: Update `Ui.java`**

Replace the constants:

```java
    private static final String GREETING = "Hi, I'm Ruby — the gem your task list has been missing.";
    private static final String PROMPT = "What are we polishing today?";
```

Delete the unused `FAREWELL` constant (it is dead code; `ExitCommand` owns the farewell).

- [ ] **Step 4: Update `ExitCommand.java`**

Replace the returned string:

```java
        return "Bye. Your tasks are in precious hands.";
```

- [ ] **Step 5: Update `MainWindow.java`**

Replace `WELCOME_MESSAGE` with:

```java
    private static final String WELCOME_MESSAGE = "Hi, I'm Ruby — the gem your task list has been missing.\n"
            + "What are we polishing today?";
```

- [ ] **Step 6: Verify**

Run: `./gradlew check` and `python3 .codex/skills/test-ui/scripts/run_ui_tests.py`
Expected: both pass; runner prints `All 8 UI test case(s) passed.`

- [ ] **Step 7: Commit (only if the user asks)**

```bash
git add src/main/java/ruby/ui/Ui.java src/main/java/ruby/command/ExitCommand.java src/main/java/ruby/MainWindow.java test/ui-test-plan.md
git commit -m "Give Ruby's greeting and farewell a voice"
```

---

### Task 3: Task messages

**Files:**
- Modify: `src/main/java/ruby/task/TaskList.java`
- Modify: `test/ui-test-plan.md`

**Interfaces:**
- Consumes: Task 2 complete (suite green).
- Produces: `TaskList` confirmation and error strings in the new voice. Method signatures unchanged.

- [ ] **Step 1: Update the test plan (failing expectations)**

In `test/ui-test-plan.md`, replace the expected bodies for these commands (task lines and numbering stay byte-identical):

| Command (case) | New expected body |
|---|---|
| `todo borrow book` (Level 4) | `Added to the collection:` / `  [T][ ] borrow book` / `That's 1 task on your plate.` |
| `deadline return book /by 2019-06-06` (Level 4) | `Added to the collection:` / `  [D][ ] return book (by: Jun 06 2019)` / `That's 2 tasks on your plate.` |
| `event project meeting /from 2019-08-06 1400 /to 2019-08-06 1600` (Level 4) | `Added to the collection:` / `  [E][ ] project meeting (from: Aug 06 2019, 14:00 to: Aug 06 2019, 16:00)` / `That's 3 tasks on your plate.` |
| `list` (Level 4) | `Here's everything on your plate:` + the same numbered tasks |
| `mark 2` (Level 4) | `Done — consider it polished:` / `  [D][X] return book (by: Jun 06 2019)` |
| `unmark 2` (Level 4) | `Undone — brilliance takes time:` / `  [D][ ] return book (by: Jun 06 2019)` |
| `todo read book` (Level 5) | `Added to the collection:` / `  [T][ ] read book` / `That's 1 task on your plate.` |
| `deadline submit report /by 2019-06-06` (Level 5) | `Added to the collection:` / `  [D][ ] submit report (by: Jun 06 2019)` / `That's 2 tasks on your plate.` |
| `event meeting /from 2019-08-06 1400 /to 2019-08-06 1600` (Level 5) | `Added to the collection:` / `  [E][ ] meeting (from: Aug 06 2019, 14:00 to: Aug 06 2019, 16:00)` / `That's 3 tasks on your plate.` |
| `mark 2` (Level 5) | `Done — consider it polished:` / `  [D][X] submit report (by: Jun 06 2019)` |
| `mark 7` (Level 5) | `Sorry, I couldn't process that: Task 7? You only have 3. Pick a number from 1 to 3.` |
| `list` (Level 5) | `Here's everything on your plate:` + the same numbered tasks |
| `delete 1` (Level 6, no tasks) | `Sorry, I couldn't process that: You have no tasks to delete — add one first.` |
| `todo read book` (Level 6) | `Added to the collection:` / `  [T][ ] read book` / `That's 1 task on your plate.` |
| `deadline return book /by 2019-06-06` (Level 6) | `Added to the collection:` / `  [D][ ] return book (by: Jun 06 2019)` / `That's 2 tasks on your plate.` |
| `event project meeting /from 2019-08-06 1400 /to 2019-08-06 1600` (Level 6) | `Added to the collection:` / `  [E][ ] project meeting (from: Aug 06 2019, 14:00 to: Aug 06 2019, 16:00)` / `That's 3 tasks on your plate.` |
| `delete 4` (Level 6) | `Sorry, I couldn't process that: Task 4? You only have 3. Pick a number from 1 to 3.` |
| `delete 2` (Level 6, first) | `Removed — gone without a trace:` / `  [D][ ] return book (by: Jun 06 2019)` / `That leaves 2 tasks on your plate.` |
| `list` (Level 6, after delete) | `Here's everything on your plate:` + the same numbered tasks |
| `mark 2` (Level 6, second) | `Done — consider it polished:` / `  [E][X] project meeting (from: Aug 06 2019, 14:00 to: Aug 06 2019, 16:00)` |
| `delete 2` (Level 6, second) | `Removed — gone without a trace:` / `  [E][X] project meeting (from: Aug 06 2019, 14:00 to: Aug 06 2019, 16:00)` / `That leaves 1 task on your plate.` |
| `list` (Level 6, final) | `Here's everything on your plate:` / `1.[T][ ] read book` |
| `todo read book` (Level 7) | `Added to the collection:` / `  [T][ ] read book` / `That's 1 task on your plate.` |
| `deadline submit report /by 2019-06-06` (Level 7) | `Added to the collection:` / `  [D][ ] submit report (by: Jun 06 2019)` / `That's 2 tasks on your plate.` |
| `event project meeting /from 2019-08-06 1400 /to 2019-08-06 1600` (Level 7) | `Added to the collection:` / `  [E][ ] project meeting (from: Aug 06 2019, 14:00 to: Aug 06 2019, 16:00)` / `That's 3 tasks on your plate.` |
| `mark 2` (Level 7) | `Done — consider it polished:` / `  [D][X] submit report (by: Jun 06 2019)` |
| `delete 1` (Level 7) | `Removed — gone without a trace:` / `  [T][ ] read book` / `That leaves 2 tasks on your plate.` |
| `todo read book` (Level 9) | `Added to the collection:` / `  [T][ ] read book` / `That's 1 task on your plate.` |
| `deadline return book /by 2019-06-06` (Level 9) | `Added to the collection:` / `  [D][ ] return book (by: Jun 06 2019)` / `That's 2 tasks on your plate.` |
| `event project meeting /from 2019-08-06 1400 /to 2019-08-06 1600` (Level 9) | `Added to the collection:` / `  [E][ ] project meeting (from: Aug 06 2019, 14:00 to: Aug 06 2019, 16:00)` / `That's 3 tasks on your plate.` |
| `find book` (Level 9) | `Found them — I never miss:` / `1.[T][ ] read book` / `2.[D][ ] return book (by: Jun 06 2019)` |
| `find BOOK` (Level 9) | same as `find book` |
| `find dance` (Level 9) | `Nothing. Even I can't find what isn't there.` |

Leave `### Saved data` blocks untouched.

- [ ] **Step 2: Run the runner to verify it fails**

Run: `python3 .codex/skills/test-ui/scripts/run_ui_tests.py`
Expected: FAIL in `Level 4 task workflow` at `todo borrow book` (actual still `Got it. I've added this task:`).

- [ ] **Step 3: Update `TaskList.java`**

Replace `addItem`'s return with:

```java
        int count = tasks.size();
        String noun = count == 1 ? "task" : "tasks";
        return "Added to the collection:\n  "
                + task
                + "\nThat's " + count + " " + noun + " on your plate.";
```

Replace `markItem`'s return with:

```java
        return "Done — consider it polished:\n  " + task;
```

Replace `unmarkItem`'s return with:

```java
        return "Undone — brilliance takes time:\n  " + task;
```

Replace `deleteItem`'s return with:

```java
        int count = tasks.size();
        String noun = count == 1 ? "task" : "tasks";
        return "Removed — gone without a trace:\n  "
                + task
                + "\nThat leaves " + count + " " + noun + " on your plate.";
```

Replace `listItems`'s return with:

```java
        return "Here's everything on your plate:"
                + IntStream.range(0, tasks.size())
                        .mapToObj(i -> "\n" + (i + 1) + "." + tasks.get(i))
                        .collect(Collectors.joining());
```

Replace the two `find` returns with:

```java
        if (result.isEmpty()) {
            return "Nothing. Even I can't find what isn't there.";
        }
        return "Found them — I never miss:" + result;
```

Replace `getTask`'s throws with:

```java
        if (tasks.isEmpty()) {
            throw new RubyException("You have no tasks to " + action + " — add one first.");
        }
        if (index < 0 || index >= tasks.size()) {
            throw new RubyException(
                    "Task " + (index + 1)
                            + "? You only have " + tasks.size()
                            + ". Pick a number from 1 to " + tasks.size() + ".");
        }
```

- [ ] **Step 4: Verify**

Run: `./gradlew check` and `python3 .codex/skills/test-ui/scripts/run_ui_tests.py`
Expected: both pass; runner prints `All 8 UI test case(s) passed.`

- [ ] **Step 5: Commit (only if the user asks)**

```bash
git add src/main/java/ruby/task/TaskList.java test/ui-test-plan.md
git commit -m "Give Ruby's task replies a voice"
```

---

### Task 4: Contact messages

**Files:**
- Modify: `src/main/java/ruby/contact/ContactList.java`
- Modify: `test/ui-test-plan.md`

**Interfaces:**
- Consumes: Task 3 complete (suite green).
- Produces: `ContactList` confirmation and error strings in the new voice. Method signatures unchanged.

- [ ] **Step 1: Update the test plan (failing expectations)**

In the `Contacts` case, replace these expected bodies (contact lines and numbering stay byte-identical):

| Command | New expected body |
|---|---|
| `contact list` (empty) | `No contacts yet — a fresh, unpolished page.` |
| `contact add John Doe /phone 91234567 /email john@example.com /address 123 Street` | `Saved. I never forget a name:` / `  John Doe \| 91234567 \| john@example.com \| 123 Street` / `That's 1 contact in your circle.` |
| `contact add Jane` | `Saved. I never forget a name:` / `  Jane` / `That's 2 contacts in your circle.` |
| `contact list` | `Your circle, as requested:` / `1. John Doe \| 91234567 \| john@example.com \| 123 Street` / `2. Jane` |
| `contact delete 1` | `Removed from your circle:` / `  John Doe \| 91234567 \| john@example.com \| 123 Street` / `That leaves 1 contact in your circle.` |
| `contact delete 5` | `Sorry, I couldn't process that: Contact 5? You only have 1. Pick a number from 1 to 1.` |

- [ ] **Step 2: Run the runner to verify it fails**

Run: `python3 .codex/skills/test-ui/scripts/run_ui_tests.py`
Expected: FAIL in `Contacts` at `contact list` (actual still `You have no contacts yet.`).

- [ ] **Step 3: Update `ContactList.java`**

Replace `addContact`'s return with:

```java
        int count = contacts.size();
        String noun = count == 1 ? "contact" : "contacts";
        return "Saved. I never forget a name:\n  "
                + contact
                + "\nThat's " + count + " " + noun + " in your circle.";
```

Replace `deleteContact`'s return with:

```java
        int count = contacts.size();
        String noun = count == 1 ? "contact" : "contacts";
        return "Removed from your circle:\n  "
                + contact
                + "\nThat leaves " + count + " " + noun + " in your circle.";
```

Replace `listContacts` with:

```java
        if (contacts.isEmpty()) {
            return "No contacts yet — a fresh, unpolished page.";
        }
        return "Your circle, as requested:"
                + IntStream.range(0, contacts.size())
                        .mapToObj(i -> "\n" + (i + 1) + ". " + contacts.get(i))
                        .collect(Collectors.joining());
```

Replace `getContact`'s throws with:

```java
        if (contacts.isEmpty()) {
            throw new RubyException("You have no contacts to " + action + " — add one first.");
        }
        if (index < 0 || index >= contacts.size()) {
            throw new RubyException(
                    "Contact " + (index + 1)
                            + "? You only have " + contacts.size()
                            + ". Pick a number from 1 to " + contacts.size() + ".");
        }
```

- [ ] **Step 4: Verify**

Run: `./gradlew check` and `python3 .codex/skills/test-ui/scripts/run_ui_tests.py`
Expected: both pass; runner prints `All 8 UI test case(s) passed.`

- [ ] **Step 5: Commit (only if the user asks)**

```bash
git add src/main/java/ruby/contact/ContactList.java test/ui-test-plan.md
git commit -m "Give Ruby's contact replies a voice"
```

---

### Task 5: Error voice and shared wrapper

**Files:**
- Modify: `src/main/java/ruby/command/Parser.java`
- Modify: `src/main/java/ruby/Ruby.java`
- Modify: `test/ui-test-plan.md`

**Interfaces:**
- Consumes: Tasks 2-4 complete (suite green).
- Produces: `Ruby.errorMessage(RubyException)` used by `getResponse` and `run`; all `Parser` errors in the new voice. `Parser` public signatures unchanged.

- [ ] **Step 1: Update the test plan (failing expectations)**

Replace the error expected bodies. Every error body becomes ` Hold on — <message>` between the divider lines.

`Level 5 error recovery`:

| Command | New expected body |
|---|---|
| *(empty input)* | `Hold on — I can't work with silence. Type a command.` |
| `todo` | `Hold on — A todo with no description? Give me something to work with after todo.` |
| `blah` | `Hold on — That's not in my repertoire. Try a command I know.` |
| `todoish read book` | `Hold on — That's not in my repertoire. Try a command I know.` |
| `deadline /by Sunday` | `Hold on — A deadline with no description? Give me something to call it.` |
| `deadline submit report` | `Hold on — That's not how deadlines work. Use: deadline DESCRIPTION /by DATE_OR_TIME.` |
| `deadline submit report /bypass Sunday` | `Hold on — That's not how deadlines work. Use: deadline DESCRIPTION /by DATE_OR_TIME.` |
| `deadline submit report /by` | `Hold on — A deadline needs a date or time after /by — I can't read minds.` |
| `deadline submit report /by Sunday` | `Hold on — That date is a mystery even to me. Use yyyy-mm-dd (e.g. 2019-10-15) or yyyy-mm-dd HHmm (e.g. 2026-10-15 1800).` |
| `event /from Mon /to Tue` | `Hold on — An event with no description? Give me something to call it.` |
| `event meeting /to Tue` | `Hold on — That's not how events work. Use: event DESCRIPTION /from START /to END.` |
| `event meeting /fromage Mon /to Tue` | `Hold on — That's not how events work. Use: event DESCRIPTION /from START /to END.` |
| `event meeting /from Mon` | `Hold on — An event needs an end after /to — don't leave me guessing.` |
| `event meeting /from /to Tue` | `Hold on — An event needs a start after /from — don't leave me guessing.` |
| `event meeting /from Mon /to` | `Hold on — An event needs an end after /to — don't leave me guessing.` |
| `mark` | `Hold on — You forgot the number. Give me a task number after mark.` |
| `mark banana` | `Hold on — That's not a number. Give me a whole number after mark.` |
| `mark 7` | `Hold on — Task 7? You only have 3. Pick a number from 1 to 3.` |
| `unmark` | `Hold on — You forgot the number. Give me a task number after unmark.` |

`Level 6 deletion and index shifting`:

| Command | New expected body |
|---|---|
| `delete` | `Hold on — You forgot the number. Give me a task number after delete.` |
| `delete 1` | `Hold on — You have no tasks to delete — add one first.` |
| `delete banana` | `Hold on — That's not a number. Give me a whole number after delete.` |
| `delete 0` | `Hold on — Since when is 0 a valid task number? Give me a positive whole number after delete.` |
| `delete 4` | `Hold on — Task 4? You only have 3. Pick a number from 1 to 3.` |

`Contacts`:

| Command | New expected body |
|---|---|
| `contact add John /phone abc` | `Hold on — What's a phone number without digits? Use a plus sign and at least 3 digits, e.g. +123456789.` |
| `contact add John /phone 91234567 /phone 98765432` | `Hold on — Once is enough — use /phone only once.` |
| `contact` | `Hold on — Here's how contacts work: contact add NAME [/phone PHONE] [/email EMAIL] [/address ADDRESS], contact list, or contact delete INDEX.` |
| `contact frobnicate` | `Hold on — That's not a contact command. Use contact add, contact list, or contact delete.` |
| `contact delete 5` | `Hold on — Contact 5? You only have 1. Pick a number from 1 to 1.` |
| `contact delete 0` | `Hold on — Since when is 0 a valid contact number? Give me a positive whole number after contact delete.` |

- [ ] **Step 2: Run the runner to verify it fails**

Run: `python3 .codex/skills/test-ui/scripts/run_ui_tests.py`
Expected: FAIL in `Level 5 error recovery` at the empty input (actual still `Sorry, I couldn't process that: Please enter a command.`).

- [ ] **Step 3: Update `Ruby.java`**

Replace both catch bodies with a shared helper:

```java
        } catch (RubyException exception) {
            return errorMessage(exception);
        }
```

and

```java
            } catch (RubyException exception) {
                ui.printMessage(errorMessage(exception));
            }
```

Add the helper below `getResponse`:

```java
    /**
     * Formats a user-facing error with Ruby's voice.
     *
     * @param exception Error raised while processing a command.
     * @return The error text with Ruby's error prefix.
     */
    private static String errorMessage(RubyException exception) {
        return "Hold on — " + exception.getMessage();
    }
```

- [ ] **Step 4: Update `Parser.java` messages**

Apply these exact replacements:

| Current | New |
|---|---|
| `"Get your brain in gear and type something."` (line 64) | `"I can't work with silence. Type a command."` |
| `"Are you illiterate? I don't know that command."` (line 99) | `"That's not in my repertoire. Try a command I know."` |
| `"What's a Todo without a description? Give me something after todo."` (line 112) | `"A todo with no description? Give me something to work with after todo."` |
| `"Give me a " + noun + " number after " + commandWord + "."` (line 144) | `"You forgot the number. Give me a " + noun + " number after " + commandWord + "."` |
| `capitalize(noun) + " since when is " + oneBasedIndex + " a valid " + noun + " number? Give me a positive whole number after " + commandWord + "."` (lines 149-150) | `"Since when is " + oneBasedIndex + " a valid " + noun + " number? Give me a positive whole number after " + commandWord + "."` |
| `"The " + noun + " number for " + commandWord + " must be a whole number!!!!!!!"` (line 154) | `"That's not a number. Give me a whole number after " + commandWord + "."` |
| `"Use: deadline DESCRIPTION " + DEADLINE_DELIMITER + " DATE_OR_TIME."` (line 176) | `"That's not how deadlines work. Use: deadline DESCRIPTION " + DEADLINE_DELIMITER + " DATE_OR_TIME."` |
| `"A deadline needs a description."` (line 182) | `"A deadline with no description? Give me something to call it."` |
| `"A deadline needs a date or time after " + DEADLINE_DELIMITER + "."` (lines 185 and 444) | `"A deadline needs a date or time after " + DEADLINE_DELIMITER + " — I can't read minds."` |
| `"Use: event DESCRIPTION " + EVENT_START_DELIMITER + " START " + EVENT_END_DELIMITER + " END."` (lines 202-207) | `"That's not how events work. Use: event DESCRIPTION " + EVENT_START_DELIMITER + " START " + EVENT_END_DELIMITER + " END."` |
| `"An event needs a description."` (line 214) | `"An event with no description? Give me something to call it."` |
| `"An event needs an end after " + EVENT_END_DELIMITER + "."` (lines 217 and 226) | `"An event needs an end after " + EVENT_END_DELIMITER + " — don't leave me guessing."` |
| `"An event needs a start after " + EVENT_START_DELIMITER + "."` (line 223) | `"An event needs a start after " + EVENT_START_DELIMITER + " — don't leave me guessing."` |
| `"Give me a keyword to search for after find."` (line 241) | `"Find what? Give me a keyword after find."` |
| `CONTACT_USAGE` value (lines 48-49) | `"contact add NAME [/phone PHONE] [/email EMAIL] [/address ADDRESS], contact list, or contact delete INDEX."` |
| `throw new RubyException(CONTACT_USAGE);` (line 256) | `throw new RubyException("Here's how contacts work: " + CONTACT_USAGE);` |
| `"Contact list doesn't take any arguments. Just type contact list."` (line 268) | `"Contact list doesn't take arguments. Just type contact list."` |
| `"Type something I understand. Use contact add, contact list, or contact delete."` (lines 274-275) | `"That's not a contact command. Use contact add, contact list, or contact delete."` |
| `"I don't know the field " + token + ". Use " + PHONE_TAG + ", " + EMAIL_TAG + ", or " + ADDRESS_TAG + "."` (lines 297-298) | `"I don't know the field " + token + ". Try " + PHONE_TAG + ", " + EMAIL_TAG + ", or " + ADDRESS_TAG + "."` |
| `"Use " + token + " only once."` (line 301) | `"Once is enough — use " + token + " only once."` |
| `"Contacts need a name. Give me something after contact add."` (line 312) | `"Contacts need a name. I can't remember someone with no name."` |
| `"The phone number must be at least 3 digits and may start with a +."` (line 326) | `"What's a phone number without digits? Use a plus sign and at least 3 digits, e.g. +123456789."` |
| `"That email address looks invalid. Use name@example.com."` (line 329) | `"That email won't reach anyone. Use name@example.com."` |
| `"I don't understand that date. Use yyyy-mm-dd (e.g. 2019-10-15)" + " or yyyy-mm-dd HHmm (e.g. 2026-10-15 1800)."` (lines 456-458) | `"That date is a mystery even to me. Use yyyy-mm-dd (e.g. 2019-10-15)" + " or yyyy-mm-dd HHmm (e.g. 2026-10-15 1800)."` |

Then delete the now-unused `capitalize` method and its Javadoc (lines 158-163).

Do not change `"The field " + tag + " must have a value after it."` or `"The character " + FIELD_SEPARATOR + " is not allowed in contact details."`; they already match.

- [ ] **Step 5: Verify**

Run: `./gradlew check` and `python3 .codex/skills/test-ui/scripts/run_ui_tests.py`
Expected: both pass; runner prints `All 8 UI test case(s) passed.`

- [ ] **Step 6: Commit (only if the user asks)**

```bash
git add src/main/java/ruby/command/Parser.java src/main/java/ruby/Ruby.java test/ui-test-plan.md
git commit -m "Unify Ruby's error voice across GUI and console"
```

---

### Task 6: User guide and final verification

**Files:**
- Modify: `docs/README.md`

**Interfaces:**
- Consumes: Tasks 2-5 complete (suite green).
- Produces: documented personality in the user guide; final green build.

- [ ] **Step 1: Fill the intro and add the "Meet Ruby" section**

In `docs/README.md`, replace `// Product intro goes here` with:

```markdown
Ruby is a gem among task managers: brilliant, a little vain, and secretly
proud of you. She tracks your tasks and contacts with high standards, teases
your typos, and never forgets a name.
```

Add after the intro, before `## Adding deadlines`:

````markdown
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
````

Keep the `// Product screenshot goes here` placeholder and the other placeholder sections as they are.

- [ ] **Step 2: Final verification**

Run, in order:

```bash
./gradlew check
python3 .codex/skills/test-ui/scripts/run_ui_tests.py
```

Expected: `BUILD SUCCESSFUL` and `All 8 UI test case(s) passed.`

- [ ] **Step 3: Optional visual check**

Run: `./gradlew run`
Expected: GUI opens; send `nonsense` and `todo read book`; replies use the new voice and the avatars render as full circles.

- [ ] **Step 4: Commit (only if the user asks)**

```bash
git add docs/README.md docs/superpowers/
git commit -m "Document Ruby's personality in the user guide"
```

---

## Self-Review Notes

- Spec coverage: greeting/prompt/farewell (Task 2), tasks (Task 3), contacts (Task 4), errors + shared wrapper (Task 5), user guide (Task 6). GUI palette and data format intentionally untouched.
- Spec gaps found during planning, resolved above: the bare `contact` usage line now reads `Here's how contacts work: ...`; the unused `Ui.FAREWELL` constant and the unused `Parser.capitalize` method are removed as dead code.
- Placeholder scan: no TBD/TODO; every changed string appears verbatim.
- Type consistency: `errorMessage(RubyException)` is defined in Task 5 and used only there; `TaskList`/`ContactList` signatures are unchanged.
