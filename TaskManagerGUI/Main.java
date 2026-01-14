import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class Main {
    private static DefaultListModel<Task> taskListModel = new DefaultListModel<>();
    private static JList<Task> taskJList;
    private static TaskManager taskManager = new TaskManager();

    public static void main(String[] args) {
        // Load tasks
        taskManager.loadTasks();
        for (Task task : taskManager.getTasks()) {
            taskListModel.addElement(task);
        }

        // Main Frame
        JFrame frame = new JFrame("Task Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        // Top panel
        JPanel panel = new JPanel();
        JTextField taskField = new JTextField(20);
        JButton addButton = new JButton("Add Task");

        panel.add(taskField);
        panel.add(addButton);

        // Task list
        taskJList = new JList<>(taskListModel);
        JScrollPane scrollPane = new JScrollPane(taskJList);

        // Bottom buttons
        JButton completeButton = new JButton("Mark Complete");
        JButton deleteButton = new JButton("Delete Task");
        JButton newListButton = new JButton("New List");
        JButton viewListButton = new JButton("View Current List");

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(completeButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(newListButton);
        bottomPanel.add(viewListButton);

        // Add functionality
        addButton.addActionListener(e -> {
            String desc = taskField.getText().trim();
            if (!desc.isEmpty()) {
                taskManager.addTask(desc);
                // Get the task that was just added with proper ID
                List<Task> tasks = taskManager.getTasks();
                Task newTask = tasks.get(tasks.size() - 1);
                taskListModel.addElement(newTask);
                taskField.setText("");
            }
        });

        completeButton.addActionListener(e -> {
            Task selected = taskJList.getSelectedValue();
            if (selected != null) {
                taskManager.completeTask(selected.getId());
                taskJList.repaint();
            }
        });

        deleteButton.addActionListener(e -> {
            Task selected = taskJList.getSelectedValue();
            if (selected != null) {
                taskManager.deleteTask(selected.getId());
                taskListModel.removeElement(selected);
            }
        });

        newListButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                frame,
                "This will clear the current list and create a new one. Are you sure?",
                "New List",
                JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION) {
                taskManager.clearTasks();
                taskListModel.clear();
                JOptionPane.showMessageDialog(frame, "New list created successfully!");
            }
        });

        viewListButton.addActionListener(e -> {
            StringBuilder listInfo = new StringBuilder();
            listInfo.append("Current Task List:\n\n");
            
            List<Task> tasks = taskManager.getTasks();
            if (tasks.isEmpty()) {
                listInfo.append("No tasks found.");
            } else {
                for (Task task : tasks) {
                    listInfo.append(task.toString()).append("\n");
                }
                listInfo.append("\nTotal tasks: ").append(tasks.size());
                long completedTasks = tasks.stream().filter(Task::isCompleted).count();
                listInfo.append("\nCompleted tasks: ").append(completedTasks);
                listInfo.append("\nRemaining tasks: ").append(tasks.size() - completedTasks);
            }
            
            JOptionPane.showMessageDialog(frame, listInfo.toString(), "Current List", JOptionPane.INFORMATION_MESSAGE);
        });

        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Save tasks on close
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                taskManager.saveTasks();
            }
        });

        frame.setVisible(true);
    }
}
