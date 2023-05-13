package models;

public class SubTask extends Task{
    protected Integer epicId;


    public SubTask(String taskName, String taskDescription, /*int taskID,*/ TaskStatus taskStatus, Integer epicId) {
        super(taskName, taskDescription, /*taskID,*/ taskStatus);
        this.epicId = epicId;
    }

    @Deprecated
    public SubTask(String taskName, String taskDescription, int taskID, TaskStatus taskStatus, Integer epicId) {
        super(taskName, taskDescription, taskID, taskStatus);
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
                '}' + "\n";
    }
}
