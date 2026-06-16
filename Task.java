package model;

// Task blueprint for the scheduler, implements Comparable for PriorityQueue sorting by deadline
public class Task implements Comparable<Task> {

    private String id;
    private String name;
    private String description;
    private int deadline;        // Lower = more urgent
    private boolean completed;
    private int dependencyCount; // Tasks blocking this one

    public Task(String id, String name, String description, int deadline) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.completed = false;
        this.dependencyCount = 0;
    }

    // Compares by deadline - smaller number = higher priority
    @Override
    public int compareTo(Task other) {
        return this.deadline - other.deadline;
    }

    public String getId() { return id; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public int getDeadline() { return deadline; }

    public boolean isCompleted() { return completed; }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getDependencyCount() { return dependencyCount; }

    public void setDependencyCount(int count) {
        this.dependencyCount = count;
    }

    public void incrementDependency() {
        this.dependencyCount++;
    }

    // Decreases blocker count, prevents negative values
    public void decrementDependency() {
        if (this.dependencyCount > 0) {
            this.dependencyCount--;
        }
    }

    @Override
    public String toString() {
        return "[" + id + "] " + name +
                " | Deadline: " + deadline + " days" +
                " | Blockers: " + dependencyCount +
                " | Done: " + (completed ? "✓" : "✗");
    }
}