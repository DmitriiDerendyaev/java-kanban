package service;

import models.Epic;
import models.SubTask;
import models.Task;
import models.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    protected HashMap<Integer, Epic> epic = new HashMap<>();
    protected HashMap<Integer, Task> task = new HashMap<>();
    protected HashMap<Integer, SubTask> subTask = new HashMap<>();

    int currentId = 1;

    public int createEpic(Epic newEpic){
        newEpic.setTaskID(currentId++);
        epic.put(newEpic.getTaskID(), newEpic);

        return newEpic.getTaskID();
    }

    public int createTask(Task newTask){
        newTask.setTaskID(currentId++);
        task.put(newTask.getTaskID(), newTask);

        return newTask.getTaskID();
    }

    public int createSubTask(SubTask newSubTask){
        newSubTask.setTaskID(currentId++);
        epic.get(newSubTask.getEpicID()).addSubTaskID(newSubTask.getTaskID());
        subTask.put(newSubTask.getEpicID(), newSubTask);

        return newSubTask.getTaskID();
    }


}
