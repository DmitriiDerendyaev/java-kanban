package service;

import models.Epic;
import models.SubTask;
import models.Task;
import models.TaskType;

import java.io.*;

public class FileBackedTasksManager extends InMemoryTaskManager{

    private String pathFile;


    public FileBackedTasksManager(String pathFile) throws IOException {
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

    @Override
    public Task getTaskByID(Integer taskID) {
        Task task = super.getTaskByID(taskID);
        saveTaskToFile();

        return task;
    }

    @Override
    public SubTask getSubTaskByID(Integer subTaskID) {
        SubTask subTask = super.getSubTaskByID(subTaskID);
        saveTaskToFile();

        return subTask;
    }

    @Override
    public Epic getEpicByID(Integer epicId) {
        Epic epic = super.getEpicByID(epicId);
        saveTaskToFile();

        return epic;
    }

    private String taskToString(Task task){
        if (task instanceof SubTask) {
            System.out.println("SubTask");
            return String.format("%d, %s, %s, %s, %s",
                    task.getTaskID(),
                    TaskType.SUB_TASK.toString(),
                    task.getTaskName(),
                    task.getTaskStatus().toString(),
                    task.getTaskDescription());
        } else if (task instanceof Epic) {
            System.out.println("Epic");
            return String.format("%d, %s, %s, %s, %s,\n",
                    task.getTaskID(),
                    TaskType.EPIC.toString(),
                    task.getTaskName(),
                    task.getTaskStatus().toString(),
                    task.getTaskDescription());
        } else {
            System.out.println("Task");
            return String.format("%d, %s, %s, %s, %s,\n",
                    task.getTaskID(),
                    TaskType.TASK.toString(),
                    task.getTaskName(),
                    task.getTaskStatus().toString(),
                    task.getTaskDescription());
        }
    }

    public void saveTaskToFile() {
        try (FileWriter writer = new FileWriter(pathFile)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task currentTask : getTasks().values()) {
                writer.write(taskToString(currentTask));
            }
            for (SubTask currentSubTask : getSubTasks().values()) {
                writer.write(String.format("%s, %s,\n",taskToString(currentSubTask), currentSubTask.getEpicID()));
            }
            for (Epic currentEpic: getEpics().values()){
                writer.write(taskToString(currentEpic));
            }

            writer.write("\n");

            for (Task task : getHistory()) {
                writer.write(String.format("%d, ", task.getTaskID()));
            }
        } catch (IOException e) {
            e.getMessage();
            throw new RuntimeException(e);
        }
    }
    private void loadTasksFromFile(){

    }
}
