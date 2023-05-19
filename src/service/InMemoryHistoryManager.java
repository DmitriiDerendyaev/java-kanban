package service;

import models.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    protected List<Task> historyTask = new ArrayList<>();
    private static final int HISTORY_SIZE = 10;
    @Override
    public void add(Task task) {
        if (historyTask.size() < HISTORY_SIZE) {
            historyTask.add(task);
        } else {
            historyTask.remove(0);
            historyTask.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyTask;
    }
}
