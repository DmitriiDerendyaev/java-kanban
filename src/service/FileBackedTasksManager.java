package service;

import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
import models.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager{

    private final String pathFile;


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
            return String.format("%d,%s,%s,%s,%s",
                    task.getTaskID(),
                    TaskType.SUB_TASK.toString(),
                    task.getTaskName(),
                    task.getTaskStatus().toString(),
                    task.getTaskDescription());
        } else if (task instanceof Epic) {
            System.out.println("Epic");
            return String.format("%d,%s,%s,%s,%s,",
                    task.getTaskID(),
                    TaskType.EPIC.toString(),
                    task.getTaskName(),
                    task.getTaskStatus().toString(),
                    task.getTaskDescription());
        } else {
            System.out.println("Task");
            return String.format("%d,%s,%s,%s,%s,\n",
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
            input = input.replaceAll("[\\[\\]]", "");

            String[] numbers = input.split("\\s+");
            List<Integer> subTaskCollection = new ArrayList<>();

            for (String number : numbers) {
                subTaskCollection.add(Integer.parseInt(number));
            }
            return new Epic(name, description, id, TaskStatus.valueOf(status), subTaskCollection);
        }
    }

    static String historyToString(HistoryManager manager){
        StringBuilder stringBuilder = new StringBuilder();

        for(Task currentTask: manager.getHistory()){
            stringBuilder.append(currentTask.getTaskID() + ",");
        }

        return stringBuilder.toString();
    }

    static List<Integer> historyFromString(String value){
        List<Integer> historyList = new ArrayList<>();
        String[] historyArray = value.split(",");

        for (String currentItem : historyArray) {
            historyList.add(Integer.valueOf(currentItem));
        }

        return historyList;
    }
    public void saveTaskToFile() {
        try (FileWriter writer = new FileWriter(pathFile)) {
            writer.write("id,type,name,status,description,epic,subTasks\n");
            for (Task currentTask : getTasks().values()) {
                writer.write(taskToString(currentTask));
            }
            for (SubTask currentSubTask : getSubTasks().values()) {
                writer.write(String.format("%s,%s,\n",taskToString(currentSubTask), currentSubTask.getEpicID()));
            }
            for (Epic currentEpic: getEpics().values()){
                writer.write(String.format("%s,%s,\n",taskToString(currentEpic),
                        currentEpic.getTaskCollection().toString().replace(',', ' ')));
            }

            writer.write("\n");

            writer.write(historyToString(historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Failed to save tasks.", e);
        }
    }
    private void loadTasksFromFile(){
        try (BufferedReader reader = new BufferedReader(new FileReader(pathFile))){
            String line;

            if(reader.readLine() == null){
                return;
            }

            while (!(line = reader.readLine()).equals("")) {
                String[] taskData = line.split(",");

                int id = Integer.parseInt(taskData[0]);
                TaskType type = TaskType.valueOf(taskData[1].toUpperCase());

                switch (type) {
                    case TASK:
                        super.tasks.put(id, fromString(line));
                        break;
                    case SUB_TASK:
                        super.subTasks.put(id, (SubTask) fromString(line));
                        break;
                    case EPIC:
                        super.epics.put(id, (Epic) fromString(line));
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown task type: " + type);
                }
            }

            line = reader.readLine();

            for(Integer currentId: historyFromString(line)){
                if(tasks.containsKey(currentId))
                    historyManager.add(tasks.get(currentId));
                if(subTasks.containsKey(currentId))
                    historyManager.add(subTasks.get(currentId));
                if(epics.containsKey(currentId))
                    historyManager.add(epics.get(currentId));
            }

        } catch (FileNotFoundException e) {
            throw new ManagerLoadException("Failed to load tasks. File not found.", e);
        } catch (IOException e) {
            throw new ManagerLoadException("Failed to load tasks.", e);
        }
    }
}
