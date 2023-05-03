package service;

import models.Epic;
import models.SubTask;
import models.Task;

import java.util.HashMap;

public class TaskManager {

    private HashMap<String, Task> tasks = new HashMap<>();
    private HashMap<String, SubTask> subTasks = new HashMap<>();
    private HashMap<String, Epic> epics = new HashMap<>();

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public Task getById(String id) {
        return tasks.get(id);
    }

    public List<Task> getAll() {
        return new ArrayList<>(tasks.values());
    }

    public List<Subtask> getSubtasksByEpic(String epicId) {
        List<Subtask> result = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (epicId.equals(subtask.getEpicId())) {
                result.add(subtask);
            }
        }
        return result;
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

}
