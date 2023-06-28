package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Epic extends Task{

    protected List<Integer> taskCollection = new ArrayList<>();

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription, TaskStatus.NEW);
    }

    @Deprecated
    public Epic(String taskName, String taskDescription, int taskID) {
        super(taskName, taskDescription, taskID, TaskStatus.NEW);
    }

    @Deprecated
    public Epic(String taskName, String taskDescription, int taskID, TaskStatus taskStatus, List<Integer> taskCollection) {
        super(taskName, taskDescription, taskID, taskStatus);
        this.taskCollection = taskCollection;
    }

    public List<Integer> getTaskCollection() {
        return taskCollection;
    }

    public void addSubTaskID(Integer subTaskID){
        taskCollection.add(subTaskID);
    }

    public void setStatus(TaskStatus status){
        super.taskStatus = status;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "taskCollection=" + taskCollection +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskID=" + taskID +
                ", taskStatus=" + taskStatus +
                '}' + "\n";
    }
}
