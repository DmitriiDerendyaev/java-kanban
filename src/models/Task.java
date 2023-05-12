package models;

public class Task   {
    private String taskName;
    private String taskDescription;
    private int taskID;

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    private TaskStatus taskStatus;

    public Task(String taskName, String taskDescription, /*int taskID,*/ TaskStatus taskStatus) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
//        this.taskID = taskID;
        this.taskStatus = taskStatus;
    }

    @Deprecated
    public Task(String taskName, String taskDescription, int taskID, TaskStatus taskStatus) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskID = taskID;
        this.taskStatus = taskStatus;
    }

    public Task() {
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public int getTaskID() {
        return taskID;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }
}
