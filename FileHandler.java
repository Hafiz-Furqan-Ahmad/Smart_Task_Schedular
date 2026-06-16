package core;

import model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

// Handles saving/loading tasks and dependencies to/from text files for persistent storage
public class FileHandler {

    private static final String FILE_NAME = "tasks.txt";

    // Saves all tasks to tasks.txt in CSV format: id,name,description,deadline,completed,dependencyCount
    public static void saveTasks(HashMap<String, Task> taskMap) {
        try {
            FileWriter writer = new FileWriter(FILE_NAME, false);

            for (Task task : taskMap.values()) {
                String line = task.getId() + "," +
                        task.getName() + "," +
                        task.getDescription() + "," +
                        task.getDeadline() + "," +
                        task.isCompleted() + "," +
                        task.getDependencyCount();

                writer.write(line + "\n");
            }

            writer.close();
            System.out.println(" Tasks saved to " + FILE_NAME);

        } catch (IOException e) {
            System.out.println(" Error saving tasks: " + e.getMessage());
        }
    }

    // Loads tasks from tasks.txt and returns them as an ArrayList
    public static ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("!! No save file found. Starting fresh.");
            return tasks;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 6) continue;

                String id = parts[0];
                String name = parts[1];
                String description = parts[2];
                int deadline = Integer.parseInt(parts[3]);
                boolean completed = Boolean.parseBoolean(parts[4]);
                int depCount = Integer.parseInt(parts[5]);

                Task task = new Task(id, name, description, deadline);
                task.setCompleted(completed);
                task.setDependencyCount(depCount);

                tasks.add(task);
            }

            reader.close();
            System.out.println("Done !! " + tasks.size() + " tasks loaded from file.");

        } catch (IOException e) {
            System.out.println(" Error loading tasks: " + e.getMessage());
        }

        return tasks;
    }

    // Saves dependencies to dependencies.txt in format: prerequisiteId,dependentId
    public static void saveDependencies(HashMap<String, ArrayList<String>> depMap) {
        try {
            FileWriter writer = new FileWriter("dependencies.txt", false);

            for (String prereq : depMap.keySet()) {
                for (String dependent : depMap.get(prereq)) {
                    writer.write(prereq + "," + dependent + "\n");
                }
            }

            writer.close();
        } catch (IOException e) {
            System.out.println(" Error saving dependencies: " + e.getMessage());
        }
    }

    // Loads dependencies from dependencies.txt and returns them as a HashMap
    public static HashMap<String, ArrayList<String>> loadDependencies() {
        HashMap<String, ArrayList<String>> depMap = new HashMap<>();

        File file = new File("dependencies.txt");
        if (!file.exists()) return depMap;

        try {
            BufferedReader reader = new BufferedReader(new FileReader("dependencies.txt"));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 2) continue;

                String prereq = parts[0];
                String dependent = parts[1];

                depMap.computeIfAbsent(prereq, k -> new ArrayList<>()).add(dependent);
            }

            reader.close();

        } catch (IOException e) {
            System.out.println(" Error loading dependencies: " + e.getMessage());
        }

        return depMap;
    }
}