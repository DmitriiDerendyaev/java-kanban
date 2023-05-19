package service;

import models.Epic;
import models.SubTask;
import models.Task;
import models.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private static final int HISTORY_SIZE = 10;
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected List<Task> historyTask = new ArrayList<>();

    int currentId = 1;


    @Override
    public List<Task> getHistory() {
        return historyTask;
    }

    private void updateHistory(Task newTask) {
        if (historyTask.size() < HISTORY_SIZE) {
            historyTask.add(newTask);
        } else {
            historyTask.remove(0);
            historyTask.add(newTask);
        }
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
        updateHistory(tasks.get(taskID));
        return tasks.get(taskID);
    }

    public SubTask getSubTaskByID(Integer subTaskID) {
        updateHistory(subTasks.get(subTaskID));
        return subTasks.get(subTaskID);
    }

    @Override
    public Epic getEpicByID(Integer epicId) {
        updateHistory(epics.get(epicId));
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
    public ArrayList<Task> getTaskList() {
        ArrayList<Task> tasksList = new ArrayList<>(tasks.values());
        return tasksList;
    }

    @Override
    public ArrayList<SubTask> getSubTaskList() {
        ArrayList<SubTask> subTasksList = new ArrayList<>(subTasks.values());
        return subTasksList;
    }

    @Override
    public ArrayList<Epic> getEpicList() {
        ArrayList<Epic> epicsList = new ArrayList<>(epics.values());
        return epicsList;
    }

    @Override
    public ArrayList<SubTask> getSubTaskListByEpicID(Integer epicId) {
        ArrayList<SubTask> newSubTaskList = new ArrayList<>();

        for (Integer currentSubTask : epics.get(epicId).getTaskCollection()) {
            newSubTaskList.add(subTasks.get(currentSubTask));
        }
        return newSubTaskList;
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
    }

}
