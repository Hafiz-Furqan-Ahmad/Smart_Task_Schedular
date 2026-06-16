package core;

import datastructures.GraphManager;
import model.Task;

import java.util.*;

// Core controller class that manages all task operations using 6 data structures
public class TaskManager {

    private HashMap<String, Task> taskMap;          // Fast O(1) lookup by ID
    private GraphManager graph;                     // Dependency graph
    private Queue<Task> readyQueue;                 // FIFO queue of tasks with 0 blockers
    private PriorityQueue<Task> priorityQueue;      // Orders ready tasks by deadline (min-heap)
    private Stack<String> undoStack;                // LIFO stack for undo operations
    private ArrayList<Task> allTasksList;           // Master list for display/iteration

    public TaskManager() {
        taskMap = new HashMap<>();
        graph = new GraphManager();
        readyQueue = new LinkedList<>();
        priorityQueue = new PriorityQueue<>();
        undoStack = new Stack<>();
        allTasksList = new ArrayList<>();

        loadFromFile();
    }

    // Adds a new task with 0 dependencies (immediately ready)
    public void addTask(String id, String name, String description, int deadline) {
        if (taskMap.containsKey(id)) {
            System.out.println(" Task ID '" + id + "' already exists!");
            return;
        }

        Task task = new Task(id, name, description, deadline);

        taskMap.put(id, task);
        allTasksList.add(task);
        graph.addTaskNode(id);

        readyQueue.offer(task);
        priorityQueue.offer(task);

        System.out.println(" Task added: " + task);
        saveToFile();
    }

    // Creates dependency: prerequisiteId must be completed before dependentId
    public void addDependency(String prerequisiteId, String dependentId) {
        if (!taskMap.containsKey(prerequisiteId)) {
            System.out.println(" Task '" + prerequisiteId + "' not found!");
            return;
        }
        if (!taskMap.containsKey(dependentId)) {
            System.out.println(" Task '" + dependentId + "' not found!");
            return;
        }

        graph.addDependency(prerequisiteId, dependentId);

        Task dependentTask = taskMap.get(dependentId);
        dependentTask.incrementDependency();

        readyQueue.remove(dependentTask);
        priorityQueue.remove(dependentTask);

        System.out.println(" Dependency added: " + prerequisiteId + " → " + dependentId);
        saveToFile();
    }

    // Marks task as completed and unlocks its dependents
    public void completeTask(String taskId) {
        Task task = taskMap.get(taskId);

        if (task == null) {
            System.out.println(" Task not found: " + taskId);
            return;
        }
        if (task.isCompleted()) {
            System.out.println("  Task already completed!");
            return;
        }
        if (task.getDependencyCount() > 0) {
            System.out.println("Task Blocked !! Cannot complete! Task still has "
                    + task.getDependencyCount() + " blocker(s).");
            return;
        }

        task.setCompleted(true);
        undoStack.push(taskId);

        readyQueue.remove(task);
        priorityQueue.remove(task);

        ArrayList<String> dependents = graph.getDependents(taskId);

        for (String dependentId : dependents) {
            Task dependent = taskMap.get(dependentId);
            if (dependent != null && !dependent.isCompleted()) {
                dependent.decrementDependency();

                if (dependent.getDependencyCount() == 0) {
                    readyQueue.offer(dependent);
                    priorityQueue.offer(dependent);
                    System.out.println(" Task unlocked: " + dependent.getName());
                }
            }
        }

        System.out.println(" Completed: " + task.getName());
        saveToFile();
    }

    // Undoes the most recently completed task using LIFO stack
    public void undoLastCompletion() {
        if (undoStack.isEmpty()) {
            System.out.println(" Nothing to undo!");
            return;
        }

        String taskId = undoStack.pop();
        Task task = taskMap.get(taskId);

        if (task == null) return;

        task.setCompleted(false);

        readyQueue.offer(task);
        priorityQueue.offer(task);

        ArrayList<String> dependents = graph.getDependents(taskId);
        for (String dependentId : dependents) {
            Task dependent = taskMap.get(dependentId);
            if (dependent != null && !dependent.isCompleted()) {
                dependent.incrementDependency();
                readyQueue.remove(dependent);
                priorityQueue.remove(dependent);
            }
        }

        System.out.println(" Undone: " + task.getName());
        saveToFile();
    }

    // Suggests the most urgent ready task using priority queue
    public void suggestNextTask() {
        if (priorityQueue.isEmpty()) {
            System.out.println(" All tasks completed or no tasks added yet!");
            return;
        }

        Task next = priorityQueue.peek();
        System.out.println(" Suggested next task: " + next);
    }

    // Displays all tasks from the master list
    public void displayAllTasks() {
        if (allTasksList.isEmpty()) {
            System.out.println(" No tasks yet. Add some tasks first!");
            return;
        }

        System.out.println("\n ALL TASKS:");
        System.out.println("─────────────────────────────────────────────────");
        for (Task task : allTasksList) {
            System.out.println("  " + task);
        }
        System.out.println("─────────────────────────────────────────────────");
    }

    // Displays tasks with 0 blockers (ready to start)
    public void displayReadyTasks() {
        if (readyQueue.isEmpty()) {
            System.out.println(" No tasks are ready yet (all blocked or all done).");
            return;
        }

        System.out.println("\n READY TASKS (can start now):");
        for (Task task : readyQueue) {
            System.out.println("  " + task);
        }
    }

    public void displayGraph() {
        graph.displayGraph();
    }

    private void saveToFile() {
        FileHandler.saveTasks(taskMap);
        HashMap<String, ArrayList<String>> depMap = new HashMap<>();
        for (String id : taskMap.keySet()) {
            depMap.put(id, graph.getDependents(id));
        }
        FileHandler.saveDependencies(depMap);
    }

    private void loadFromFile() {
        ArrayList<Task> loaded = FileHandler.loadTasks();
        for (Task task : loaded) {
            taskMap.put(task.getId(), task);
            allTasksList.add(task);
            graph.addTaskNode(task.getId());

            if (!task.isCompleted() && task.getDependencyCount() == 0) {
                readyQueue.offer(task);
                priorityQueue.offer(task);
            }
        }

        HashMap<String, ArrayList<String>> depMap = FileHandler.loadDependencies();
        for (String prereq : depMap.keySet()) {
            for (String dependent : depMap.get(prereq)) {
                graph.addDependency(prereq, dependent);
            }
        }
    }
}