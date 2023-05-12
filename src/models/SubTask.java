package models;

public class SubTask extends Task{
    protected Integer epicID;


    public SubTask(String taskName, String taskDescription, /*int taskID,*/ TaskStatus taskStatus, Integer epicID) {
        super(taskName, taskDescription, /*taskID,*/ taskStatus);
        this.epicID = epicID;
    }

    @Deprecated
    public SubTask(String taskName, String taskDescription, int taskID, TaskStatus taskStatus, Integer epicID) {
        super(taskName, taskDescription, taskID, taskStatus);
        this.epicID = epicID;
    }

    public Integer getEpicID() {
        return epicID;
    }

    public void setEpicID(Integer epicID) {
        this.epicID = epicID;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicID=" + epicID +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskID=" + taskID +
                ", taskStatus=" + taskStatus +
                '}' + "\n";
    }
}
