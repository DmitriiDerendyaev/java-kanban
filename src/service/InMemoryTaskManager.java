package service;

import models.Epic;
import models.SubTask;
import models.Task;
import models.TaskStatus;
import util.Manager;

import java.time.ZonedDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, SubTask> subTasks = new HashMap<>();

    HistoryManager historyManager = Manager.getDefaultHistory();

    protected TreeSet<Task> prioritizedTask = new TreeSet<>();

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

    private void updateTaskIfPresent(Task newTask){
        int taskId = newTask.getTaskID();

        Task existingTask = prioritizedTask.stream()
                .filter(task -> task.getTaskID() == taskId)
                .findFirst()
                .orElse(null);

        if(existingTask != null){
            prioritizedTask.remove(existingTask);
        }

        prioritizedTask.add(newTask);
    }

    public TreeSet<Task> getPrioritizedTask() {
        return prioritizedTask;
    }

    private boolean hasTimeOverlap(Task newTask) {
        ZonedDateTime startTimeNewTask = newTask.getStartTime();
        ZonedDateTime endTimeNewTask = newTask.getEndTime();

        for (Task existingTask : prioritizedTask) {
            ZonedDateTime startTimeExistingTask = existingTask.getStartTime();
            ZonedDateTime endTimeExistingTask = existingTask.getEndTime();

            if (startTimeNewTask.isBefore(endTimeExistingTask) && startTimeExistingTask.isBefore(endTimeNewTask)) {
                return true; // Ќайдено пересечение
            }
        }
        return false; // ѕересечений нет
    }



    @Override
    public int createEpic(Epic newEpic) {
        newEpic.setTaskID(currentId++);

        if (hasTimeOverlap(newEpic)) {
            throw new IllegalArgumentException("Ќова€ задача пересекаетс€ по времени выполнени€ с существующими задачами");
        }

        epics.put(newEpic.getTaskID(), newEpic);

        // ѕри создании эпика без сабтасков он инициализируетс€ с текущим временем,
        // что приводит к некорректной сортировке при добавлении в TreeSet
        // ѕотенциальное решение заключаетс€ в том, чтобы каждый раз добавл€ть эпик, но уже после добавлени€ первого сабтаска
//        updateTaskIfPresent(newEpic);
        return newEpic.getTaskID();
    }

    @Override
    public int createTask(Task newTask) {
        newTask.setTaskID(currentId++);

        if (hasTimeOverlap(newTask)) {
            throw new IllegalArgumentException("Ќова€ задача пересекаетс€ по времени выполнени€ с существующими задачами");
        }

        updateTaskIfPresent(newTask);

        tasks.put(newTask.getTaskID(), newTask);
        return newTask.getTaskID();
    }

    @Override
    public int createSubTask(SubTask newSubTask) {
        newSubTask.setTaskID(currentId++);

        if (hasTimeOverlap(newSubTask)) {
            throw new IllegalArgumentException("Ќова€ подзадача пересекаетс€ по времени выполнени€ с существующими задачами");
        }

        updateTaskIfPresent(newSubTask);


        epics.get(newSubTask.getEpicID()).addSubTaskID(newSubTask.getTaskID());
        subTasks.put(newSubTask.getTaskID(), newSubTask);

        updateEpicStatus(epics.get(newSubTask.getEpicID()));

        updateTaskIfPresent(epics.get(newSubTask.getEpicID()));

//        updateTaskIfPresent(newSubTask);
//        updateTaskIfPresent(epics.get(newSubTask.getEpicID()));
        return newSubTask.getTaskID();
    }

    @Override
    public int updateTask(Task updatedTask) {
        tasks.put(updatedTask.getTaskID(), updatedTask);

        updateTaskIfPresent(updatedTask);
        return updatedTask.getTaskID();
    }

    @Override
    public int updateSubTask(SubTask updatedSubTask) {
        subTasks.put(updatedSubTask.getTaskID(), updatedSubTask);
        updateEpicStatus(epics.get(updatedSubTask.getEpicID()));

        updateTaskIfPresent(updatedSubTask);
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
        prioritizedTask.remove(tasks.remove(taskID));

        return taskID;
    }

    @Override
    public int removeSubTaskByID(Integer subTaskID) {
        SubTask subtask = subTasks.remove(subTaskID);
        epics.get(subtask.getEpicID()).getTaskCollection().remove(subTaskID);

        prioritizedTask.remove(subtask);
        updateEpicStatus(epics.get(subtask.getEpicID()));
        return subTaskID;
    }

    @Override
    public void removeEpicByID(Integer epicId) {
        Epic removedEpic = epics.remove(epicId);
        prioritizedTask.remove(removedEpic);

        Iterator<Integer> subTaskIterator = subTasks.keySet().iterator();
        while (subTaskIterator.hasNext()) {
            Integer currentSubTaskID = subTaskIterator.next();
            SubTask currentSubTask = subTasks.get(currentSubTaskID);
            if (currentSubTask.getEpicID() == epicId) {
                subTaskIterator.remove();
                prioritizedTask.remove(currentSubTask);
            }
        }
    }

    @Override
    public void clearTasks() {
        tasks.clear();

        prioritizedTask.removeIf(task -> task.getClass().equals(Task.class));
    }

    @Override
    public void clearSubTasks() {
        subTasks.clear();

        for (Integer currentEpic : epics.keySet()) {
            epics.get(currentEpic).getTaskCollection().clear();
        }

        prioritizedTask.removeIf(task -> task instanceof SubTask);
        updateEpicsStatus();
    }

    @Override
    public void clearEpics() {
        epics.clear();
        subTasks.clear();

        prioritizedTask.removeIf(task -> task instanceof Epic);
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
