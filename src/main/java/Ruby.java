/**
 * Starts the Ruby chatbot application.
 */
public class Ruby {
    public static void main(String[] args) {
        String horizontalLine = "____________________________________________________________";
        String chatbotName = "Ruby";
        String greeting = "Hello! I'm " + chatbotName + ".";
        String prompt = "What can I do for you?";
        String farewell = "Bye. Hope to see you again soon!";
        String banner = " /$$$$$$$            /$$\n"
                + "| $$__  $$          | $$\n"
                + "| $$  \\ $$ /$$   /$$| $$$$$$$  /$$   /$$\n"
                + "| $$$$$$$/| $$  | $$| $$__  $$| $$  | $$\n"
                + "| $$__  $$| $$  | $$| $$  \\ $$| $$  | $$\n"
                + "| $$  \\ $$| $$  | $$| $$  | $$| $$  | $$\n"
                + "| $$  | $$|  $$$$$$/| $$$$$$$/|  $$$$$$$\n"
                + "|__/  |__/ \\______/ |_______/  \\____  $$\n"
                + "                               /$$  | $$\n"
                + "                              |  $$$$$$/\n"
                + "                               \\______/";

        System.out.println(horizontalLine);
        System.out.println(banner);
        System.out.println(greeting);
        System.out.println(prompt);
        System.out.println(horizontalLine);
        System.out.println(farewell);
        System.out.println(horizontalLine);
    }
}
