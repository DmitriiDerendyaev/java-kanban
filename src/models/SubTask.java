package models;

public class SubTask extends Task{
    private Integer epicID;


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
}
