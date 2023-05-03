package models;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task{

    private HashMap<String, SubTask> subTask;

    public Epic(String taskName, String taskDescription, int taskID, TaskStatus taskStatus, HashMap<String, SubTask> subTask) {
        super(taskName, taskDescription, taskID, taskStatus);
        this.subTask = subTask;
    }

    public void addSubtask(SubTask subtask) {
        subTask.put(subtask.getEpicID(), subtask);
    }

    public ArrayList<SubTask> getSubtasks() {
        return new ArrayList<>(subTask.values());
    }
}
