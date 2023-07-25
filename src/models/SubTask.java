package models;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Comparator;

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
    @Deprecated
    public SubTask(String taskName,
                   String taskDescription,
                   int taskID,
                   TaskStatus taskStatus,
                   Integer epicId,
                   Duration duration,
                   ZonedDateTime startTime) {
        super(taskName, taskDescription, taskID, taskStatus, duration, startTime);
        this.epicId = epicId;
    }


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
                "}\n";
    }

    // Переопределяем статический компаратор из класса Task
    public static final Comparator<SubTask> subTaskComparator = new Comparator<SubTask>() {
        @Override
        public int compare(SubTask subTask1, SubTask subTask2) {
            int startTimeComparison = subTask1.startTime.compareTo(subTask2.startTime);
            if (startTimeComparison != 0) {
                return startTimeComparison;
            }
            // Если startTime равны, то сравниваем по taskId
            return Integer.compare(subTask1.taskID, subTask2.taskID);
        }
    };
}
