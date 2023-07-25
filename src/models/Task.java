package models;

import java.time.Duration;

public class Task   {
    protected String taskName;
    protected String taskDescription;
    protected int taskID;
    protected TaskStatus taskStatus;

    Duration duration;

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public Task(String taskName,
                String taskDescription,
                TaskStatus taskStatus,
                Duration duration) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.duration = duration;
    }

    @Deprecated
    public Task(String taskName,
                String taskDescription,
                int taskID,
                TaskStatus taskStatus,
                Duration duration) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskID = taskID;
        this.taskStatus = taskStatus;
        this.duration = duration;
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
