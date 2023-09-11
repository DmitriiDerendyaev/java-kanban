package service;

import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
import models.*;

import java.io.*;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager{

    private String pathFile;


    public FileBackedTasksManager(String pathFile) throws IOException {
        this.pathFile = pathFile;
        load();
    }

    public FileBackedTasksManager(){
    }


    @Override
    public int createEpic(Epic newEpic) {
        int epicId = super.createEpic(newEpic);
        save();

        return epicId;
    }

    @Override
    public int createTask(Task newTask) {
        int taskId = super.createTask(newTask);
        save();

        return taskId;
    }

    @Override
    public int createSubTask(SubTask newSubTask) {
        int subTaskId = super.createSubTask(newSubTask);
        save();

        return subTaskId;
    }

    @Override
    public int updateTask(Task updatedTask) {
        int taskId = super.updateTask(updatedTask);;
        save();

        return taskId;
    }

    @Override
    public int updateSubTask(SubTask updatedSubTask) {
        int subTaskId = super.updateSubTask(updatedSubTask);;
        save();

        return subTaskId;
    }

    @Override
    public int removeTaskByID(Integer taskID) {
        int taskId = super.removeTaskByID(taskID);
        save();

        return taskId;
    }

    @Override
    public int removeSubTaskByID(Integer subTaskID) {
        int subTaskId = super.removeSubTaskByID(subTaskID);
        save();

        return subTaskId;
    }

    @Override
    public void removeEpicByID(Integer epicId) {
        super.removeEpicByID(epicId);
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearSubTasks() {
        super.clearSubTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public Task getTaskByID(Integer taskID) {
        Task task = super.getTaskByID(taskID);
        save();

        return task;
    }

    @Override
    public SubTask getSubTaskByID(Integer subTaskID) {
        SubTask subTask = super.getSubTaskByID(subTaskID);
        save();

        return subTask;
    }

    @Override
    public Epic getEpicByID(Integer epicId) {
        Epic epic = super.getEpicByID(epicId);
        save();

        return epic;
    }

    private String taskToString(Task task){
        if (task instanceof SubTask) {
            return String.format("%d,%s,%s,%s,%s,%s,%s,%s",
                    task.getTaskID(),
                    TaskType.SUB_TASK.toString(),
                    task.getTaskName(),
                    task.getTaskStatus().toString(),
                    task.getTaskDescription(),
                    task.getDuration().toString(),
                    task.getStartTime(),
                    task.getEndTime());
        } else if (task instanceof Epic) {
            return String.format("%d,%s,%s,%s,%s,%s,%s,%s,",
                    task.getTaskID(),
                    TaskType.EPIC.toString(),
                    task.getTaskName(),
                    task.getTaskStatus().toString(),
                    task.getTaskDescription(),
                    task.getDuration().toString(),
                    task.getStartTime(),
                    task.getEndTime());
        } else {
            return String.format("%d,%s,%s,%s,%s,%s,%s,%s\n",
                    task.getTaskID(),
                    TaskType.TASK.toString(),
                    task.getTaskName(),
                    task.getTaskStatus().toString(),
                    task.getTaskDescription(),
                    task.getDuration().toString(),
                    task.getStartTime(),
                    task.getEndTime());
        }
    }

    private Task fromString(String value){
        String[] taskString = value.split(",");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmXXX'['VV']'");
        DateTimeFormatter durationFormatter = DateTimeFormatter.ofPattern("'PT'HH'H'mm'M'");
        if(taskString[1].equals("TASK")){
            int id = Integer.parseInt(taskString[0]);
            String name = taskString[2];
            String status = taskString[3];
            String description = taskString[4];
            String duration = taskString[5];
            String startTime = taskString[6];

            return new Task(name,
                    description,
                    id,
                    TaskStatus.valueOf(status),
                    Duration.parse(duration),
                    ZonedDateTime.parse(startTime, dateTimeFormatter));
        } else if (taskString[1].equals("SUB_TASK")) {
            int id = Integer.parseInt(taskString[0]);
            String name = taskString[2];
            String status = taskString[3];
            String description = taskString[4];
            String duration = taskString[5];
            String startTime = taskString[6];
            int epicId = Integer.parseInt(taskString[8]);

            return new SubTask(name,
                    description,
                    id,
                    TaskStatus.valueOf(status),
                    epicId,
                    Duration.parse(duration),
                    ZonedDateTime.parse(startTime, dateTimeFormatter));
        } else {
            int id = Integer.parseInt(taskString[0]);
            String name = taskString[2];
            String status = taskString[3];
            String description = taskString[4];
            String duration = taskString[5];
            String startTime = taskString[6];
            String input = taskString[9];
            input = input.replaceAll("[\\[\\]]", "");

            String[] numbers = input.split("\\s+");
            List<Integer> subTaskCollection = new ArrayList<>();

            //TODO: выяснить, нужно ли считывать массив subTask для epic,
            // т.к. выбрасывается NPE при обновлении Epic до инициализации SubTasks

            return new Epic(name,
                    description,
                    id,
                    TaskStatus.valueOf(status),
                    subTaskCollection,
                    Duration.parse(duration),
                    ZonedDateTime.parse(startTime, dateTimeFormatter));
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
    protected void save() {
        try (FileWriter writer = new FileWriter(pathFile)) {
            writer.write("id,type,name,status,description,duration,startTime,endTime,epic,subTasks\n");
            for (Task currentTask : getTasks().values()) {
                writer.write(taskToString(currentTask));
            }
            for (Epic currentEpic: getEpics().values()){
                writer.write(String.format("%s,%s,\n",taskToString(currentEpic),
                        currentEpic.getTaskCollection().toString().replace(',', ' ')));
            }
            for (SubTask currentSubTask : getSubTasks().values()) {
                writer.write(String.format("%s,%s,\n",taskToString(currentSubTask), currentSubTask.getEpicID()));
            }

            writer.write("\n");

            writer.write(historyToString(historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Failed to save tasks.", e);
        }
    }
    protected void load(){
        try (BufferedReader reader = new BufferedReader(new FileReader(pathFile))){
            String line;

            if(reader.readLine() == null){
                return;
            }

            while (!(line = reader.readLine()).equals("")) {
                String[] taskData = line.split(",");

                int id = Integer.parseInt(taskData[0]);

                currentId = Math.max(id, currentId);

                TaskType type = TaskType.valueOf(taskData[1].toUpperCase());

                switch (type) {
                    case TASK:
                        super.createTask(fromString(line));
                        break;
                    case SUB_TASK:
                        super.createSubTask((SubTask) fromString(line));
                        break;
                    case EPIC:
                        super.createEpic((Epic) fromString(line));
                        break;
                    default:
                        throw new ManagerLoadException("Unknown task type: " + type);
                }
            }

            line = reader.readLine();

            if(line == null){
                return;
            }
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

    @Override
    public List<Task> getPrioritizedTask() {
        return super.getPrioritizedTask();
    }

    public void clearFileContents() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathFile))) {

        }
    }
}
