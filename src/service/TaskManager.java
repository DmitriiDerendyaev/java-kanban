package service;

import models.Epic;
import models.SubTask;
import models.Task;
import models.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class TaskManager {

    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, SubTask> subTasks = new HashMap<>();

    int currentId = 1;

    public int createEpic(Epic newEpic){
        newEpic.setTaskID(currentId++);
        epics.put(newEpic.getTaskID(), newEpic);

        return newEpic.getTaskID();
    }

    public int createTask(Task newTask){
        newTask.setTaskID(currentId++);
        tasks.put(newTask.getTaskID(), newTask);

        return newTask.getTaskID();
    }

    public int createSubTask(SubTask newSubTask){
        newSubTask.setTaskID(currentId++);
        epics.get(newSubTask.getEpicID()).addSubTaskID(newSubTask.getTaskID());
        subTasks.put(newSubTask.getTaskID(), newSubTask);

        updateEpicStatus();

        return newSubTask.getTaskID();
    }

    public int updateTask(Task updatedTask){
        tasks.put(updatedTask.getTaskID(), updatedTask);

        return updatedTask.getTaskID();
    }

    public int updateSubTask(SubTask updatedSubTask){
        subTasks.put(updatedSubTask.getTaskID(), updatedSubTask);
        updateEpicStatus();

        return updatedSubTask.getTaskID();
    }

    public Task getTaskByID(Integer taskID){
        return tasks.get(taskID);
    }

    public SubTask getSubTaskByID(Integer subTaskID){
        return subTasks.get(subTaskID);
    }

    public Epic getEpicByID(Integer epicId){
        return epics.get(epicId);
    }

    public int removeTaskByID(Integer taskID){
        tasks.remove(taskID);

        return taskID;
    }

    public int removeSubTaskByID(Integer subTaskID){
        subTasks.remove(subTaskID);

        for(Integer currentEpic: epics.keySet()){
            if(epics.get(currentEpic).getTaskCollection().remove(subTaskID))
                break;
        }

        updateEpicStatus();
        return subTaskID;
    }

    public void removeEpicByID(Integer epicId){
        epics.remove(epicId);
        Iterator<Integer> subTaskIterator = subTasks.keySet().iterator();
        while(subTaskIterator.hasNext()){
            Integer currentSubTask = subTaskIterator.next();
            if(subTasks.get(currentSubTask).getEpicID() == epicId){
                subTaskIterator.remove();
            }
        }
    }


    public void clearTask(){
        tasks.clear();
    }

    public void clearSubTask(){
        subTasks.clear();

        for(Integer currentEpic: epics.keySet()){
            epics.get(currentEpic).getTaskCollection().clear();
        }

        updateEpicStatus();
    }

    public void clearEpic(){
        epics.clear();
        subTasks.clear();
    }

    public ArrayList<Task> getTaskList() {
        ArrayList<Task> newTaskList = new ArrayList<>();
        for(Integer currentTask: tasks.keySet()){
            newTaskList.add(tasks.get(currentTask));
        }
        return newTaskList;
    }

    public ArrayList<SubTask> getSubTaskList(){
        ArrayList<SubTask> newSubTaskList = new ArrayList<>();
        for(Integer currentSubTask: subTasks.keySet()){
            newSubTaskList.add(subTasks.get(currentSubTask));
        }
        return newSubTaskList;
    }

    public ArrayList<Epic> getEpicList(){
        ArrayList<Epic> newEpicList = new ArrayList<>();
        for(Integer currentEpic: epics.keySet()){
            newEpicList.add(epics.get(currentEpic));
        }
        return newEpicList;
    }

    public ArrayList<SubTask> getSubTaskListByEpicID(Integer epicId){
        ArrayList<SubTask> newSubTaskList = new ArrayList<>();

        for(Integer currentSubTask: epics.get(epicId).getTaskCollection()){
            newSubTaskList.add(subTasks.get(currentSubTask));
        }
        return newSubTaskList;
    }

    private void updateEpicStatus(){
        for(Integer currentEpic: epics.keySet()){
            boolean isEverythingNew = true;
            boolean isEverythingDone = true;
            if(epics.get(currentEpic).getTaskCollection().isEmpty()){
                epics.get(currentEpic).setStatus(TaskStatus.NEW);
                continue;
            }
            for (Integer currentSubTask : epics.get(currentEpic).getTaskCollection()) {
                if(!subTasks.get(currentSubTask).getTaskStatus().equals(TaskStatus.NEW)){
                    isEverythingNew = false;
                }
                if(!subTasks.get(currentSubTask).getTaskStatus().equals(TaskStatus.DONE)){
                    isEverythingDone = false;
                }
            }
            if(isEverythingNew){
                epics.get(currentEpic).setStatus(TaskStatus.NEW);
            } else if (isEverythingDone) {
                epics.get(currentEpic).setStatus(TaskStatus.DONE);
            } else {
                epics.get(currentEpic).setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

}
