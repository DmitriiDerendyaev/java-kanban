package service;

import models.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
            return String.format("%d, %s, %s, %s, %s,",
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

    private Task fromString(String value){
        String[] taskString = value.split(",");
        if(taskString[1].equals("TASK")){
            int id = Integer.parseInt(taskString[0]);
            String name = taskString[2];
            String status = taskString[3];
            String description = taskString[4];
            return new Task(name, description, id, TaskStatus.valueOf(status));
        } else if (taskString[1].equals("SUB_TASK")) {
            int id = Integer.parseInt(taskString[0]);
            String name = taskString[2];
            String status = taskString[3];
            String description = taskString[4];
            int epicId = Integer.parseInt(taskString[5]);
            return new SubTask(name, description, id, TaskStatus.valueOf(status), epicId);
        } else {
            int id = Integer.parseInt(taskString[0]);
            String name = taskString[2];
            String status = taskString[3];
            String description = taskString[4];
            String input = taskString[6];
            input = input.replaceAll("[\\[\\]]", ""); // ”даление квадратных скобок

            String[] numbers = input.split("\\s+"); // –азделение строки по пробелам
            List<Integer> subTaskCollection = new ArrayList<>();

            for (String number : numbers) {
                subTaskCollection.add(Integer.parseInt(number)); // ѕреобразование строки в число и добавление в список
            }
            return new Epic(name, description, id, TaskStatus.valueOf(status), subTaskCollection);
        }
    }

    public void saveTaskToFile() {
        try (FileWriter writer = new FileWriter(pathFile)) {
            writer.write("id,type,name,status,description,epic,subTasks\n");
            for (Task currentTask : getTasks().values()) {
                writer.write(taskToString(currentTask));
            }
            for (SubTask currentSubTask : getSubTasks().values()) {
                writer.write(String.format("%s, %s,\n",taskToString(currentSubTask), currentSubTask.getEpicID()));
            }
            for (Epic currentEpic: getEpics().values()){
                writer.write(String.format("%s, %s,\n",taskToString(currentEpic),
                        currentEpic.getTaskCollection().toString().replace(',', ' ')));
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
