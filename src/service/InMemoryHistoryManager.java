package service;

import models.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    protected List<Task> historyTask = new ArrayList<>();
    private static final int HISTORY_SIZE = 10;
    @Override
    public void add(Task task) {
        historyTask.add(task);
        if (historyTask.size() > HISTORY_SIZE) {
            historyTask.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyTask;
    }
}
