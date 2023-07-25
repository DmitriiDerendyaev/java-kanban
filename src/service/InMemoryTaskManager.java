package service;

import models.Epic;
import models.SubTask;
import models.Task;
import models.TaskStatus;
import util.Manager;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, SubTask> subTasks = new HashMap<>();

    HistoryManager historyManager = Manager.getDefaultHistory();



    protected int currentId = 1;


    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public Map<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public int createEpic(Epic newEpic) {
        newEpic.setTaskID(currentId++);
        epics.put(newEpic.getTaskID(), newEpic);

        return newEpic.getTaskID();
    }

    @Override
    public int createTask(Task newTask) {
        newTask.setTaskID(currentId++);
        tasks.put(newTask.getTaskID(), newTask);

        return newTask.getTaskID();
    }

    @Override
    public int createSubTask(SubTask newSubTask) {
        newSubTask.setTaskID(currentId++);
        epics.get(newSubTask.getEpicID()).addSubTaskID(newSubTask.getTaskID());
        subTasks.put(newSubTask.getTaskID(), newSubTask);

        updateEpicStatus(epics.get(newSubTask.getEpicID()));

        return newSubTask.getTaskID();
    }

    @Override
    public int updateTask(Task updatedTask) {
        tasks.put(updatedTask.getTaskID(), updatedTask);

        return updatedTask.getTaskID();
    }

    @Override
    public int updateSubTask(SubTask updatedSubTask) {
        subTasks.put(updatedSubTask.getTaskID(), updatedSubTask);
        updateEpicStatus(epics.get(updatedSubTask.getEpicID()));

        return updatedSubTask.getTaskID();
    }

    @Override
    public Task getTaskByID(Integer taskID) {
        historyManager.add(tasks.get(taskID));
        return tasks.get(taskID);
    }

    public SubTask getSubTaskByID(Integer subTaskID) {
        historyManager.add(subTasks.get(subTaskID));
        return subTasks.get(subTaskID);
    }

    @Override
    public Epic getEpicByID(Integer epicId) {
        historyManager.add(epics.get(epicId));
        return epics.get(epicId);
    }

    @Override
    public int removeTaskByID(Integer taskID) {
        tasks.remove(taskID);

        return taskID;
    }

    @Override
    public int removeSubTaskByID(Integer subTaskID) {
        SubTask subtask = subTasks.remove(subTaskID);
        epics.get(subtask.getEpicID()).getTaskCollection().remove(subTaskID);

        updateEpicStatus(epics.get(subtask.getEpicID()));
        return subTaskID;
    }

    @Override
    public void removeEpicByID(Integer epicId) {
        epics.remove(epicId);
        Iterator<Integer> subTaskIterator = subTasks.keySet().iterator();
        while (subTaskIterator.hasNext()) {
            Integer currentSubTask = subTaskIterator.next();
            if (subTasks.get(currentSubTask).getEpicID() == epicId) {
                subTaskIterator.remove();
            }
        }
    }

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearSubTasks() {
        subTasks.clear();

        for (Integer currentEpic : epics.keySet()) {
            epics.get(currentEpic).getTaskCollection().clear();
        }

        updateEpicsStatus();
    }

    @Override
    public void clearEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public List<Task> getTaskList() {
        List<Task> tasksList = new ArrayList<>(tasks.values());
        return tasksList;
    }

    @Override
    public List<SubTask> getSubTaskList() {
        List<SubTask> subTasksList = new ArrayList<>(subTasks.values());
        return subTasksList;
    }

    @Override
    public List<Epic> getEpicList() {
        List<Epic> epicsList = new ArrayList<>(epics.values());
        return epicsList;
    }

    @Override
    public List<SubTask> getSubTaskListByEpicID(Integer epicId) {
        List<SubTask> newSubTaskList = new ArrayList<>();

        for (Integer currentSubTask : epics.get(epicId).getTaskCollection()) {
            newSubTaskList.add(subTasks.get(currentSubTask));
        }
        return newSubTaskList;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateEpicsStatus() {
        for (Integer currentEpic : epics.keySet()) {
            boolean isEverythingNew = true;
            boolean isEverythingDone = true;
            if (epics.get(currentEpic).getTaskCollection().isEmpty()) {
                epics.get(currentEpic).setStatus(TaskStatus.NEW);
                continue;
            }
            for (Integer currentSubTask : epics.get(currentEpic).getTaskCollection()) {
                if (!subTasks.get(currentSubTask).getTaskStatus().equals(TaskStatus.NEW)) {
                    isEverythingNew = false;
                }
                if (!subTasks.get(currentSubTask).getTaskStatus().equals(TaskStatus.DONE)) {
                    isEverythingDone = false;
                }
            }
            if (isEverythingNew) {
                epics.get(currentEpic).setStatus(TaskStatus.NEW);
            } else if (isEverythingDone) {
                epics.get(currentEpic).setStatus(TaskStatus.DONE);
            } else {
                epics.get(currentEpic).setStatus(TaskStatus.IN_PROGRESS);
            }

            epics.get(currentEpic).updateStartTime(subTasks);
        }
    }

    private void updateEpicStatus(Epic currentEpic) {
        boolean isEverythingNew = true;
        boolean isEverythingDone = true;
        if (currentEpic.getTaskCollection().isEmpty()) {
            currentEpic.setStatus(TaskStatus.NEW);
        }
        for (Integer currentSubTask : currentEpic.getTaskCollection()) {
            if (!subTasks.get(currentSubTask).getTaskStatus().equals(TaskStatus.NEW)) {
                isEverythingNew = false;
            }
            if (!subTasks.get(currentSubTask).getTaskStatus().equals(TaskStatus.DONE)) {
                isEverythingDone = false;
            }
        }
        if (isEverythingNew) {
            currentEpic.setStatus(TaskStatus.NEW);
        } else if (isEverythingDone) {
            currentEpic.setStatus(TaskStatus.DONE);
        } else {
            currentEpic.setStatus(TaskStatus.IN_PROGRESS);
        }

        currentEpic.updateStartTime(subTasks);
    }

}
