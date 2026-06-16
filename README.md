markdown
# Smart Task Scheduler

A Java-based task management application that helps you organize and prioritize tasks with dependency tracking.

## Features

- **Task Management**: Add, view, and complete tasks
- **Dependency Tracking**: Set prerequisites (Task A must be done before Task B)
- **Smart Scheduling**: Automatically suggests the most urgent ready task
- **Priority Queue**: Tasks ordered by deadline (urgent tasks first)
- **Undo Support**: Undo the last completed task
- **Persistent Storage**: Automatically saves tasks and dependencies to files
- **Dependency Graph**: Visualize task relationships

## Data Structures Used

- **HashMap** - O(1) task lookup by ID
- **Graph (Adjacency List)** - Models task dependencies
- **Queue (LinkedList)** - FIFO for ready tasks
- **PriorityQueue** - Min-heap for deadline-based priority
- **Stack** - Undo functionality (LIFO)
- **ArrayList** - Master list for display

## Project Structure
Smart_Task_Scheduler/
├── src/
│ ├── model/
│ │ └── Task.java # Task blueprint
│ ├── datastructures/
│ │ └── GraphManager.java # Dependency graph
│ ├── core/
│ │ ├── TaskManager.java # Core logic controller
│ │ └── FileHandler.java # Save/Load operations
│ ├── ui/
│ │ └── ConsoleUI.java # User interface
│ └── Main.java # Entry point
├── tasks.txt # Auto-generated save file
└── dependencies.txt # Auto-generated save file

text

## How to Run

1. Compile all Java files:
```bash
javac src/**/*.java src/Main.java
Run the application:

bash
java -cp src Main
Or use your favorite IDE (IntelliJ, Eclipse, VS Code).

Usage
Add Task: Create tasks with ID, name, description, and deadline

Add Dependency: Set prerequisites (e.g., T1 → T2 means T1 must finish before T2)

Complete Task: Mark a task as done (only if all dependencies are met)

Suggest Next: Get the most urgent ready task

Undo: Revert the last completed task

View: See all tasks, ready tasks, or dependency graph

Example Workflow
text
1. Add Task: T1, "Write Report", "Q4 Report", deadline 3
2. Add Task: T2, "Submit Report", "Submit to manager", deadline 5
3. Add Dependency: T1 → T2
4. Complete T1 → T2 becomes ready
5. Suggest Next → Shows T2
Technologies
Java - Core language

Collections Framework - HashMap, PriorityQueue, Stack, etc.

File I/O - Persistent storage with CSV format

Author
Hafiz Furqan Ahmad

License
This project is for educational purposes.
