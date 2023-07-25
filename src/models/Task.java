package models;

import java.time.Duration;
import java.time.ZonedDateTime;

public class Task implements Comparable<Task>{
    protected String taskName;
    protected String taskDescription;
    protected int taskID;
    protected TaskStatus taskStatus;

    protected Duration duration;
    protected ZonedDateTime startTime;
    protected ZonedDateTime endTime;

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    /**
     * Конструктор для Main и автотестов
     * @param taskName
     * @param taskDescription
     * @param taskStatus
     * @param duration
     * @param startTime
     */
    public Task(String taskName,
                String taskDescription,
                TaskStatus taskStatus,
                Duration duration,
                ZonedDateTime startTime) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.duration = duration;
        this.startTime = startTime;
        endTime = startTime.plus(duration);
    }

    /**
     * Конструктор для десериализации
     * @param taskName
     * @param taskDescription
     * @param taskID
     * @param taskStatus
     * @param duration
     * @param startTime
     */
    public Task(String taskName,
                String taskDescription,
                int taskID,
                TaskStatus taskStatus,
                Duration duration,
                ZonedDateTime startTime) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskID = taskID;
        this.taskStatus = taskStatus;
        this.duration = duration;
        this.startTime = startTime;
        endTime = startTime.plus(duration);
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

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskID=" + taskID +
                ", taskStatus=" + taskStatus +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                "}\n";
    }

    @Override
    public int compareTo(Task otherTask) {
        int startTimeComparison = this.startTime.compareTo(otherTask.startTime);
        if (startTimeComparison != 0) {
            return startTimeComparison;
        }
        // If startTime is equal, then compare by taskId
        return Integer.compare(this.taskID, otherTask.taskID);
    }
}
