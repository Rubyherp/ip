# Ruby Personality (A-Personality) Design

- Date: 2026-09-18
- Status: Approved (pending written-spec review)
- Feature: CS2103 iP optional A-Personality

## Goal

Make Ruby's personality unmistakable across the product: name, phrases, GUI, and
user guide. Only user-facing text changes; command behaviour, data format, and
GUI layout stay the same.

## Persona

Ruby is a precious gem: brilliant, high standards, a little vain, secretly proud
of the user. The existing GUI tagline "Ruby — still more reliable than your
brain." stays.

## Voice rules

1. Tease the mistake, never the person. No "Are you illiterate?" or "You don't
   deserve to use me".
2. At most one gem/shine metaphor per message; do not repeat one across
   consecutive replies.
3. Keep user data lines exact: task/contact rendering, numbering, and
   indentation do not change.
4. Structured replies keep their shape: headline, indented item, count line.
5. Plain text only (no emoji) so console and GUI match and exact-match UI tests
   stay stable.
6. One shared error wrapper in `Ruby.java` for both GUI and console:
   `"Hold on — " + message`. This also fixes the current
   `meGet your brain...` concatenation and the GUI/console mismatch.

## Phrase inventory

Data lines (task/contact text and list numbering) stay byte-identical; the
surrounding prose, including the count sentences, changes.

### Greeting and farewell

| Location | New text |
|---|---|
| Greeting (`Ui.GREETING`, `MainWindow.WELCOME_MESSAGE` line 1) | `Hi, I'm Ruby — the gem your task list has been missing.` |
| Prompt (`Ui.PROMPT`, `MainWindow.WELCOME_MESSAGE` line 2) | `What are we polishing today?` |
| Farewell (`Ui.FAREWELL`, `ExitCommand`) | `Bye. Your tasks are in precious hands.` |

The ASCII banner is unchanged.

### Tasks (`TaskList`)

| Operation | New text |
|---|---|
| add | `Added to the collection:` + `\n  ` + task + `\nThat's N task(s) on your plate.` |
| mark | `Done — consider it polished:` + `\n  ` + task |
| unmark | `Undone — brilliance takes time:` + `\n  ` + task |
| delete | `Removed — gone without a trace:` + `\n  ` + task + `\nThat leaves N task(s) on your plate.` |
| list | `Here's everything on your plate:` + numbered tasks (an empty task list still prints only this heading; no special empty message is added) |
| find (matches) | `Found them — I never miss:` + numbered matches |
| find (no matches) | `Nothing. Even I can't find what isn't there.` |
| no tasks to act on | `You have no tasks to <action> — add one first.` |
| index out of range | `Task N? You only have M. Pick a number from 1 to M.` |

`N task(s)` uses the singular form when the count is 1 (`1 task`, `2 tasks`).

### Contacts (`ContactList`)

| Operation | New text |
|---|---|
| add | `Saved. I never forget a name:` + `\n  ` + contact + `\nThat's N contact(s) in your circle.` |
| list | `Your circle, as requested:` + numbered contacts |
| list (empty) | `No contacts yet — a fresh, unpolished page.` |
| delete | `Removed from your circle:` + `\n  ` + contact + `\nThat leaves N contact(s) in your circle.` |
| no contacts to act on | `You have no contacts to <action> — add one first.` |
| index out of range | `Contact N? You only have M. Pick a number from 1 to M.` |

### Errors (`Parser`)

All are wrapped by the shared wrapper, e.g. empty input renders as
`Hold on — I can't work with silence. Type a command.`

- `I can't work with silence. Type a command.`
- `That's not in my repertoire. Try a command I know.`
- `A todo with no description? Give me something to work with after todo.`
- `You forgot the number. Give me a task number after mark.`
- `Since when is 0 a valid task number? Give me a positive whole number after mark.`
- `That's not a number. Give me a whole number after mark.`
- `That's not how deadlines work. Use: deadline DESCRIPTION /by DATE_OR_TIME.`
- `A deadline with no description? Give me something to call it.`
- `A deadline needs a date or time after /by — I can't read minds.`
- `That's not how events work. Use: event DESCRIPTION /from START /to END.`
- `An event with no description? Give me something to call it.`
- `An event needs a start after /from — don't leave me guessing.`
- `An event needs an end after /to — don't leave me guessing.`
- `Find what? Give me a keyword after find.`
- `Contact list doesn't take arguments. Just type contact list.`
- `That's not a contact command. Use contact add, contact list, or contact delete.`
- `I don't know the field /phone. Try /phone, /email, or /address.`
- `Once is enough — use /phone only once.`
- `Contacts need a name. I can't remember someone with no name.`
- `What's a phone number without digits? Use a plus sign and at least 3 digits, e.g. +123456789.`
- `That email won't reach anyone. Use name@example.com.`
- `The field /phone must have a value after it.`
- `The character | is not allowed in contact details.`
- `That date is a mystery even to me. Use yyyy-mm-dd (e.g. 2019-10-15) or yyyy-mm-dd HHmm (e.g. 2026-10-15 1800).`

The task/contact `noun`, `action`, `token`, `tag`, and `commandWord` pieces are
interpolated exactly where they already are today; only the surrounding prose
changes.

## Implementation

- Approach B (inline rewrite): change strings where they live in `TaskList`,
  `ContactList`, `Parser`, `Ui`, `ExitCommand`, and `MainWindow`.
- Add one private helper in `Ruby.java`, e.g.
  `private static String errorMessage(RubyException exception)`, and use it from
  both `getResponse` and `run`, removing the two different prefixes.
- Task/contact count lines gain a singular/plural ternary.
- No changes to the data-file format, command semantics, or GUI layout/colors.

## Documentation

- `docs/README.md`: fill the product intro placeholder and add a "Meet Ruby"
  section with the backstory, voice description, and 2-3 example exchanges.
- Other user-guide placeholders (`Feature ABC`, `Feature XYZ`, `Adding
  deadlines`) stay untouched.

## Testing

- Update every expected output in `test/ui-test-plan.md` (all 8 cases; the
  startup greeting changes in each).
- Run `python3 .codex/skills/test-ui/scripts/run_ui_tests.py` until all cases
  pass.
- Run `./gradlew check` (JUnit + checkstyle) before completion.
- No new JUnit tests: no new logic beyond a one-line plural ternary.

## Out of scope

- GUI palette, fonts, avatars (already themed).
- Data-file format and saved-data expectations.
- New features, emoji, or non-text output.
- Filling the remaining user-guide placeholder sections.

## Risks

- Exact-match UI tests: every changed string must be mirrored in
  `test/ui-test-plan.md`, including the em dash and other Unicode characters
  (files must stay UTF-8).
- Console and GUI share the greeting; `Ui` and `MainWindow` must change together.
