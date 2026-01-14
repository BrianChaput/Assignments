import java.util.*;
//import java.io.*;

public class TaskManager {
    private List<Task> tasks;
    private int nextId;

    public TaskManager() {
        tasks = new ArrayList<>();
        nextId = 1;
    }

    public void addTask(String description) {
        Task task = new Task(nextId++, description);
        tasks.add(task);
        System.out.println("Added task: " + task);
    }

    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    public void completeTask(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                task.markCompleted();
                System.out.println("Task marked as completed.");
                return;
            }
        }
        System.out.println("Task not found.");
    }

    public void deleteTask(int id) {
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId() == id) {
                iterator.remove();
                System.out.println("Task deleted.");
                return;
            }
        }
        System.out.println("Task not found.");
    }

    public void saveTasks() {
        FileHandler.writeToFile(tasks);
    }

    public void loadTasks() {
        tasks = FileHandler.readFromFile();
        nextId = tasks.stream().mapToInt(Task::getId).max().orElse(0) + 1;
    }

    public void clearTasks() {
        tasks.clear();
        nextId = 1;
        System.out.println("All tasks cleared.");
    }

    public List<Task> getTasks() {
    return tasks;
}
}
