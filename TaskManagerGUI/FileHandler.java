import java.io.*;
import java.util.*;

public class FileHandler {
    private static final String FILE_NAME = "tasks.txt";

    public static void writeToFile(List<Task> tasks) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Task task : tasks) {
                writer.write(task.toDataString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    public static List<Task> readFromFile() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return tasks;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tasks.add(Task.fromDataString(line));
            }
        } catch (IOException e) {
            System.out.println("Error reading from file.");
        }

        return tasks;
    }
}
