package service;

import models.Epic;
import models.SubTask;
import models.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    int createEpic(Epic newEpic);
    int createTask(Task newTask);
    int createSubTask(SubTask newSubTask);
    int updateTask(Task updatedTask);
    int updateSubTask(SubTask updatedSubTask);
    Task getTaskByID(Integer taskID);
    SubTask getSubTaskByID(Integer subTaskID);
    Epic getEpicByID(Integer epicId);
    int removeTaskByID(Integer taskID);
    int removeSubTaskByID(Integer subTaskID);
    void removeEpicByID(Integer epicId);
    void clearTasks();
    void clearSubTasks();
    void clearEpics();
    ArrayList<Task> getTaskList();
    ArrayList<SubTask> getSubTaskList();
    ArrayList<Epic> getEpicList();
    ArrayList<SubTask> getSubTaskListByEpicID(Integer epicId);


}
