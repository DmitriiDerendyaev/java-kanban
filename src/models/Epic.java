package models;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task{

    protected ArrayList<Integer> taskCollection = new ArrayList<>();

    public Epic(String taskName, String taskDescription, /*int taskID,*/ TaskStatus taskStatus) {
        super(taskName, taskDescription, /*taskID,*/ taskStatus);
    }

    public ArrayList<Integer> getTaskCollection() {
        return taskCollection;
    }

    public void addSubTaskID(Integer subTaskID){
        taskCollection.add(subTaskID);
    }
}
