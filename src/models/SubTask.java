package models;

import java.time.Duration;
import java.time.ZonedDateTime;

public class SubTask extends Task{
    protected Integer epicId;

    public SubTask(String taskName,
                   String taskDescription,
                   TaskStatus taskStatus,
                   Duration duration,
                   ZonedDateTime startTime,
                   Integer epicId) {
        super(taskName, taskDescription, taskStatus, duration, startTime);
        this.epicId = epicId;
    }

    //TODO: удалить deprecated классы
//    @Deprecated
//    public SubTask(String taskName, String taskDescription, int taskID, TaskStatus taskStatus, Integer epicId) {
//        super(taskName, taskDescription, taskID, taskStatus);
//        this.epicId = epicId;
//    }


    public Integer getEpicID() {
        return epicId;
    }

    public void setEpicID(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
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
