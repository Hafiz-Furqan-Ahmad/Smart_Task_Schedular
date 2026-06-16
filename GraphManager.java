package datastructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Manages task dependencies using an adjacency list graph (Task ID → tasks it unlocks)
public class GraphManager {

    private HashMap<String, ArrayList<String>> adjacencyList; // Key: Task ID, Value: tasks that depend on it
    public GraphManager() {
        adjacencyList = new HashMap<>();
    }

    // Adds a task node to the graph if it doesn't already exist
    public void addTaskNode(String taskId) {
        if (!adjacencyList.containsKey(taskId)) {
            adjacencyList.put(taskId, new ArrayList<>());
        }
    }

    // Creates dependency: prerequisiteId must be completed before dependentId
    public void addDependency(String prerequisiteId, String dependentId) {
        addTaskNode(prerequisiteId);
        ArrayList<String> dependents = adjacencyList.get(prerequisiteId);
        if (!dependents.contains(dependentId)) {
            dependents.add(dependentId);
        }
    }

    // Returns list of tasks unlocked when taskId is completed
    public ArrayList<String> getDependents(String taskId) {
        return adjacencyList.getOrDefault(taskId, new ArrayList<>());
    }

    // Removes task and cleans up references from other tasks' dependency lists
    public void removeTask(String taskId) {
        adjacencyList.remove(taskId);
        for (ArrayList<String> dependents : adjacencyList.values()) {
            dependents.remove(taskId);
        }
    }

    // Prints the dependency graph for display
    public void displayGraph() {
        System.out.println("\n📊 DEPENDENCY GRAPH (Task → Unlocks):");
        System.out.println("─────────────────────────────────────");

        if (adjacencyList.isEmpty()) {
            System.out.println("  No dependencies set.");
            return;
        }

        for (String taskId : adjacencyList.keySet()) {
            ArrayList<String> dependents = adjacencyList.get(taskId);
            if (dependents.isEmpty()) {
                System.out.println("  " + taskId + "  →  (no dependents)");
            } else {
                System.out.println("  " + taskId + "  →  " + dependents);
            }
        }
    }
}