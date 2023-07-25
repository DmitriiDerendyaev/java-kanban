package service;

import models.Epic;
import models.SubTask;
import models.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    TreeSet<Task> getPrioritizedTask();
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
    List<Task> getTaskList();
    List<SubTask> getSubTaskList();
    List<Epic> getEpicList();
    List<SubTask> getSubTaskListByEpicID(Integer epicId);
    List<Task> getHistory();


}
