package models;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Epic extends Task{

    protected List<Integer> taskCollection = new ArrayList<>();


    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription, TaskStatus.NEW, Duration.ZERO, ZonedDateTime.now());
    }


    @Deprecated
    public Epic(String taskName,
                String taskDescription,
                int taskID,
                TaskStatus taskStatus,
                List<Integer> taskCollection,
                Duration duration,
                ZonedDateTime startTime) {
        super(taskName, taskDescription, taskID, taskStatus, duration, startTime);
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
    public void setStartTime(ZonedDateTime startTime) {
        super.setStartTime(startTime);



    }

    @Override
    public String toString() {
        return "Epic{" +
                "taskCollection=" + taskCollection +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskID=" + taskID +
                ", taskStatus=" + taskStatus +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
