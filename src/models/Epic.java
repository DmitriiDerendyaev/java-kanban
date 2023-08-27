package models;

import service.TaskManager;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Epic extends Task{

    protected List<Integer> taskCollection = new ArrayList<>();


    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription, TaskStatus.NEW, Duration.ZERO, getFormattedZonedDateTime());
    }

    private static ZonedDateTime getFormattedZonedDateTime() {
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmXXX'['VV']'");
        String formattedDateTime = now.format(formatter);
        return ZonedDateTime.parse(formattedDateTime, formatter);
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

    @Deprecated
    public Epic(String taskName,
                String taskDescription,
                TaskStatus taskStatus,
                List<Integer> taskCollection,
                Duration duration,
                ZonedDateTime startTime) {
        super(taskName, taskDescription, taskStatus, duration, startTime);
        this.taskCollection = taskCollection;
    }

    public List<Integer> getTaskCollection() {
        return taskCollection;
    }

    public void setTaskCollection(List<Integer> taskCollection) {
        this.taskCollection = taskCollection;
    }

    public void addSubTaskID(Integer subTaskID){
        if(!taskCollection.contains(subTaskID)){
            taskCollection.add(subTaskID);
        }

    }

    public void setStatus(TaskStatus status){
        super.taskStatus = status;
    }

    @Override
    public void setStartTime(ZonedDateTime startTime) {
        super.setStartTime(startTime);
    }

    public void updateStartTime(Map<Integer, SubTask> subTasks) {
        ZonedDateTime earliestStartTime = subTasks.values()
                .stream()
                .filter(subTask -> taskCollection.contains(subTask.getTaskID()))
                .map(SubTask::getStartTime)
                .filter(startTime -> startTime != null)
                .min(ZonedDateTime::compareTo)
                .orElse(null);

        ZonedDateTime latestStartTime = subTasks.values()
                .stream()
                .filter(subTask -> taskCollection.contains(subTask.getTaskID()))
                .map(SubTask::getEndTime)
                .filter(endTime -> endTime != null)
                .max(ZonedDateTime::compareTo)
                .orElse(null);

        this.startTime = earliestStartTime;
        this.endTime = latestStartTime;
        
        this.duration = Duration.between(startTime, endTime);
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
                "}\n";
    }

    // Переопределяем статический компаратор из класса Task
    public static final Comparator<Epic> epicComparator = new Comparator<Epic>() {
        @Override
        public int compare(Epic epic1, Epic epic2) {
            int startTimeComparison = epic1.startTime.compareTo(epic2.startTime);
            if (startTimeComparison != 0) {
                return startTimeComparison;
            }
            // Если startTime равны, то сравниваем по taskId
            return Integer.compare(epic1.taskID, epic2.taskID);
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Epic epic = (Epic) o;

        if (!this.getTaskName().equals(epic.getTaskName())) return false;
        if (!this.getTaskDescription().equals(epic.getTaskDescription())) return false;
        if(this.getTaskID() != epic.getTaskID()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = this.getTaskName().hashCode();
        result = 31 * result + this.getTaskDescription().hashCode();
        // Добавьте другие поля, которые нужно учитывать при вычислении хэш-кода

        return result;
    }
}
