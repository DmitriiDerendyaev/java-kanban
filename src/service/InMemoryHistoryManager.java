package service;

import models.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private static final int HISTORY_SIZE = 10;
    protected List<Task> historyTask = new ArrayList<>();

    @Override
    public void add(Task newTask) {
        if (historyTask.size() < HISTORY_SIZE) {
            historyTask.add(newTask);
        } else {
            historyTask.remove(0);
            historyTask.add(newTask);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyTask;
    }
}
