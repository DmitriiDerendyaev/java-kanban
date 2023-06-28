package service;

import models.Epic;
import models.SubTask;
import models.Task;

public class FileBackedTasksManager extends InMemoryTaskManager{

    private String pathFile;

    public FileBackedTasksManager(String pathFile){
        this.pathFile = pathFile;
        loadTasksFromFile();
    }

    @Override
    public int createEpic(Epic newEpic) {
        int epicId = super.createEpic(newEpic);
        saveTaskToFile();

        return epicId;
    }

    @Override
    public int createTask(Task newTask) {
        int taskId = super.createTask(newTask);
        saveTaskToFile();

        return taskId;
    }

    @Override
    public int createSubTask(SubTask newSubTask) {
        int subTaskId = super.createSubTask(newSubTask);
        saveTaskToFile();

        return subTaskId;
    }

    @Override
    public int updateTask(Task updatedTask) {
        int taskId = super.updateTask(updatedTask);;
        saveTaskToFile();

        return taskId;
    }

    @Override
    public int updateSubTask(SubTask updatedSubTask) {
        int subTaskId = super.updateSubTask(updatedSubTask);;
        saveTaskToFile();

        return subTaskId;
    }

    @Override
    public int removeTaskByID(Integer taskID) {
        int taskId = super.removeTaskByID(taskID);
        saveTaskToFile();

        return taskId;
    }

    @Override
    public int removeSubTaskByID(Integer subTaskID) {
        int subTaskId = super.removeSubTaskByID(subTaskID);
        saveTaskToFile();

        return subTaskId;
    }

    @Override
    public void removeEpicByID(Integer epicId) {
        super.removeEpicByID(epicId);
        saveTaskToFile();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        saveTaskToFile();
    }

    @Override
    public void clearSubTasks() {
        super.clearSubTasks();
        saveTaskToFile();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        saveTaskToFile();
    }

    private void saveTaskToFile(){

    }
    private void loadTasksFromFile(){

    }
}
