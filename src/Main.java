import models.SubTask;
import models.Task;
import service.TaskManager;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        taskManager.addEpic();

        ArrayList<Task> gotSubTasks = taskManager.getTaskList();
        System.out.println("All test failed");
    }
}
