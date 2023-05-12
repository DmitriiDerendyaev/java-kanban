package service;

import models.Epic;
import models.SubTask;
import models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class TaskManager {

    protected HashMap<Integer, Epic> epic = new HashMap<>();
    protected HashMap<Integer, Task> task = new HashMap<>();
    protected HashMap<Integer, SubTask> subTask = new HashMap<>();

    int currentId = 1;

    public int createEpic(Epic newEpic){
        newEpic.setTaskID(currentId++);
        epic.put(newEpic.getTaskID(), newEpic);

        return newEpic.getTaskID();
    }

    public int createTask(Task newTask){
        newTask.setTaskID(currentId++);
        task.put(newTask.getTaskID(), newTask);

        return newTask.getTaskID();
    }

    public int createSubTask(SubTask newSubTask){
        newSubTask.setTaskID(currentId++);
        epic.get(newSubTask.getEpicID()).addSubTaskID(newSubTask.getTaskID());
        subTask.put(newSubTask.getTaskID(), newSubTask);

        return newSubTask.getTaskID();
    }

    public int updateTask(Task updatedTask){
        task.put(updatedTask.getTaskID(), updatedTask);

        return updatedTask.getTaskID();
    }

    public int updateSubTask(SubTask updatedSubTask){
        subTask.put(updatedSubTask.getTaskID(), updatedSubTask);

        return updatedSubTask.getTaskID();
    }

    public Task getTaskByID(Integer taskID){
        return task.get(taskID);
    }

    public SubTask getSubTaskByID(Integer subTaskID){
        return subTask.get(subTaskID);
    }

    public Epic getEpicByID(Integer epicID){
        return epic.get(epicID);
    }

    public int removeTaskByID(Integer taskID){
        task.remove(taskID);

        return taskID;
    }

    public int removeSubTaskByID(Integer subTaskID){
        subTask.remove(subTaskID);

        for(Integer currentEpic: epic.keySet()){
            if(epic.get(currentEpic).getTaskCollection().remove(subTaskID))
                break;
        }
        return subTaskID;
    }

    public void removeEpicByID(Integer epicID){
        epic.remove(epicID);
        Iterator<Integer> subTaskIterator = subTask.keySet().iterator();
        while(subTaskIterator.hasNext()){
            Integer currentSubTask = subTaskIterator.next();
            if(subTask.get(currentSubTask).getEpicID() == epicID){
                subTaskIterator.remove();
            }
        }
    }


    public void clearTask(){
        task.clear();
    }

    public void clearSubTask(){
        subTask.clear();

        for(Integer currentEpic: epic.keySet()){
            epic.get(currentEpic).getTaskCollection().clear();
        }
    }

    public void clearEpic(){
        epic.clear();
        subTask.clear();
    }

    public ArrayList<Task> getTaskList() {
        return (ArrayList<Task>) task.values();
    }

    public ArrayList<SubTask> getSubTaskList(){
        return (ArrayList<SubTask>) subTask.values();
    }

    public ArrayList<Epic> getEpicList(){
        return (ArrayList<Epic>) epic.values();
    }

    public ArrayList<SubTask> getSubTaskListByEpicID(Integer epicID){
        ArrayList<SubTask> newSubTaskList = new ArrayList<>();

        for(Integer currentSubTask: epic.get(epicID).getTaskCollection()){
            newSubTaskList.add(subTask.get(currentSubTask));
        }
        return newSubTaskList;
    }

    private void updateEpicStatus(){

    }

}
