package models;

public class SubTask extends Task{
    private String epicID;

    public SubTask(String taskName, String taskDescription, int taskID, TaskStatus taskStatus, String epicID) {
        super(taskName, taskDescription, taskID, taskStatus);
        this.epicID = epicID;
    }

    public SubTask(String epicID) {
        this.epicID = epicID;
    }

    public String getEpicID() {
        return epicID;
    }
}
