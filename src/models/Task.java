package models;

public class Task   {
    protected String taskName;
    protected String taskDescription;
    protected int taskID;

    protected TaskStatus taskStatus;

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public Task(String taskName, String taskDescription, TaskStatus taskStatus) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
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

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskID=" + taskID +
                ", taskStatus=" + taskStatus +
                '}' + "\n";
    }
}
