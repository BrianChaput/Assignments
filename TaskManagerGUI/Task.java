public class Task {
    private int id;
    private String description;
    private boolean isCompleted;

    public Task(String description) {
        this.description = description;
        this.isCompleted = false;
    }

    public Task(int id, String description) {
        this.id = id;
        this.description = description;
        this.isCompleted = false;
    }


    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void markCompleted() {
        this.isCompleted = true;
    }

    @Override
    public String toString() {
        return id + ". [" + (isCompleted ? "x" : " ") + "] " + description;
    }

    public String toDataString() {
        return id + "," + isCompleted + "," + description;
    }

    public static Task fromDataString(String line) {
        String[] parts = line.split(",", 3);
        Task task = new Task(Integer.parseInt(parts[0]), parts[2]);
        if (Boolean.parseBoolean(parts[1])) {
            task.markCompleted();
        }
        return task;
    }
}
