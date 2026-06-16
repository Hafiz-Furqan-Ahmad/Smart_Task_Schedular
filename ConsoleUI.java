package ui;

import core.TaskManager;
import java.util.Scanner;

// Console user interface that handles menu display and user input
public class ConsoleUI {

    private TaskManager taskManager;
    private Scanner scanner;

    public ConsoleUI() {
        taskManager = new TaskManager();
        scanner = new Scanner(System.in);
    }

    // Main application loop - displays menu and processes user choices until exit
    public void start() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   Smart Task Scheduler v1.0          ║");
        System.out.println("║             Welcome                  ║");
        System.out.println("╚══════════════════════════════════════╝");

        boolean running = true;

        while (running) {
            printMenu();
            int choice = getIntInput("Choice");

            switch (choice) {
                case 1: handleAddTask();            break;
                case 2: handleAddDependency();      break;
                case 3: handleCompleteTask();       break;
                case 4: taskManager.undoLastCompletion(); break;
                case 5: taskManager.suggestNextTask();    break;
                case 6: taskManager.displayAllTasks();    break;
                case 7: taskManager.displayReadyTasks();  break;
                case 8: taskManager.displayGraph();       break;
                case 9:
                    System.out.println(" Goodbye! Tasks saved automatically.");
                    running = false;
                    break;
                default:
                    System.out.println(" Invalid choice. Enter 1–9.");
            }
        }

        scanner.close();
    }

    // Displays the main menu options
    private void printMenu() {
        System.out.println("\n┌─────────────────────────────────┐");
        System.out.println("│           MAIN MENU             │");
        System.out.println("├─────────────────────────────────┤");
        System.out.println("│  1. Add Task                    │");
        System.out.println("│  2. Add Dependency              │");
        System.out.println("│  3. Complete Task               │");
        System.out.println("│  4. Undo Last Completion        │");
        System.out.println("│  5. Suggest Next Task           │");
        System.out.println("│  6. View All Tasks              │");
        System.out.println("│  7. View Ready Tasks            │");
        System.out.println("│  8. View Dependency Graph       │");
        System.out.println("│  9. Exit                        │");
        System.out.println("└─────────────────────────────────┘");
    }

    private void handleAddTask() {
        System.out.println("\n── Add New Task ──");
        String id = getStringInput("Task ID (e.g. T1)");
        String name = getStringInput("Task Name");
        String desc = getStringInput("Description");
        int deadline = getIntInput("Deadline (days, lower = more urgent)");

        taskManager.addTask(id, name, desc, deadline);
    }

    private void handleAddDependency() {
        System.out.println("\n── Add Dependency ──");
        System.out.println("(Which task must be done BEFORE another?)");
        String prereq = getStringInput("Prerequisite Task ID (must finish first)");
        String dependent = getStringInput("Dependent Task ID (unlocked after)");

        taskManager.addDependency(prereq, dependent);
    }

    private void handleCompleteTask() {
        System.out.println("\n── Complete Task ──");
        String id = getStringInput("Task ID to complete");
        taskManager.completeTask(id);
    }

    // Helper methods for safe user input
    private String getStringInput(String prompt) {
        System.out.print("  → " + prompt + ": ");
        return scanner.nextLine().trim();
    }

    // Repeatedly prompts until a valid integer is entered
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print("  → " + prompt + ": ");
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Error !! Please enter a valid number.");
            }
        }
    }
}