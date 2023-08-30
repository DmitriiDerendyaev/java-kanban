package service;

import clients.KVTaskClient;
import com.google.gson.Gson;
import deserializer.EpicAdapter;
import deserializer.SubTaskAdapter;
import deserializer.TaskAdapter;
import models.Epic;
import models.SubTask;
import models.Task;

import java.io.IOException;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager{
    private final Gson gson;
    private final String URL;
    private final KVTaskClient taskClient;
    public HttpTaskManager(String URL) throws IOException, InterruptedException {
        super();
        this.URL = URL;
        taskClient = new KVTaskClient(URL);
        load();
        gson = new Gson()
                .newBuilder()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .create();
    }

    private Task deserialize(String json){
        return null;
    }

    private void serialize(){
    }

    @Override
    protected void save() {
        String tasksJSON = gson.toJson(tasks.values());
        String subTasksJSON = gson.toJson(subTasks.values());
        String epicsJSON = gson.toJson(epics.values());

        try {
            taskClient.put("tasks", tasksJSON);
            taskClient.put("subTasks", subTasksJSON);
            taskClient.put("epics", epicsJSON);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка добавления задач в KVServer: " + e.getMessage());
        }


        System.out.println(tasksJSON);
        System.out.println(subTasksJSON);
        System.out.println(epicsJSON);
    }

    @Override
    protected void load() {
        //TODO:.............
    }


    @Override
    public int createEpic(Epic newEpic) {
        return super.createEpic(newEpic);
    }

    @Override
    public int createTask(Task newTask) {
        return super.createTask(newTask);
    }

    @Override
    public int createSubTask(SubTask newSubTask) {
        return super.createSubTask(newSubTask);
    }

    @Override
    public int updateTask(Task updatedTask) {
        return super.updateTask(updatedTask);
    }

    @Override
    public int updateSubTask(SubTask updatedSubTask) {
        return super.updateSubTask(updatedSubTask);
    }

    @Override
    public int removeTaskByID(Integer taskID) {
        return super.removeTaskByID(taskID);
    }

    @Override
    public int removeSubTaskByID(Integer subTaskID) {
        return super.removeSubTaskByID(subTaskID);
    }

    @Override
    public void removeEpicByID(Integer epicId) {
        super.removeEpicByID(epicId);
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
    }

    @Override
    public void clearSubTasks() {
        super.clearSubTasks();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
    }

    @Override
    public Task getTaskByID(Integer taskID) {
        return super.getTaskByID(taskID);
    }

    @Override
    public SubTask getSubTaskByID(Integer subTaskID) {
        return super.getSubTaskByID(subTaskID);
    }

    @Override
    public Epic getEpicByID(Integer epicId) {
        return super.getEpicByID(epicId);
    }

    @Override
    public List<Task> getPrioritizedTask() {
        return super.getPrioritizedTask();
    }
}
