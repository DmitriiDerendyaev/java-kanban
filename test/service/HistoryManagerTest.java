package service;

import models.Task;
import models.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {

    private HistoryManager historyManager;

    @BeforeEach
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void testAddHistory() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", 1, TaskStatus.NEW, Duration.ofHours(2), ZonedDateTime.now());
        Task task2 = new Task("Задача 2", "Описание задачи 2", 2, TaskStatus.IN_PROGRESS, Duration.ofHours(1), ZonedDateTime.now().plusHours(1));

        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> historyTasks = historyManager.getHistory();
        assertEquals(2, historyTasks.size());
        assertTrue(historyTasks.contains(task1));
        assertTrue(historyTasks.contains(task2));
    }

    @Test
    public void testAddDuplicate() {
        Task task = new Task("Задача 1", "Описание задачи 1", 1, TaskStatus.NEW, Duration.ofHours(2), ZonedDateTime.now());

        historyManager.add(task);
        historyManager.add(task);

        List<Task> historyTasks = historyManager.getHistory();
        assertEquals(1, historyTasks.size());
        assertTrue(historyTasks.contains(task));
    }

    @Test
    public void testRemoveFromHistoryAtBeginning() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", 1, TaskStatus.NEW, Duration.ofHours(2), ZonedDateTime.now());
        Task task2 = new Task("Задача 2", "Описание задачи 2", 2, TaskStatus.IN_PROGRESS, Duration.ofHours(1), ZonedDateTime.now().plusHours(1));

        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(task1.getTaskID());

        List<Task> historyTasks = historyManager.getHistory();
        assertEquals(1, historyTasks.size());
        assertFalse(historyTasks.contains(task1));
        assertTrue(historyTasks.contains(task2));
    }

    @Test
    public void testRemoveFromHistoryInMiddle() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", 1, TaskStatus.NEW, Duration.ofHours(2), ZonedDateTime.now());
        Task task2 = new Task("Задача 2", "Описание задачи 2", 2, TaskStatus.IN_PROGRESS, Duration.ofHours(1), ZonedDateTime.now().plusHours(1));
        Task task3 = new Task("Задача 3", "Описание задачи 3", 3, TaskStatus.DONE, Duration.ofHours(3), ZonedDateTime.now().plusHours(2));

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task2.getTaskID());

        List<Task> historyTasks = historyManager.getHistory();
        assertEquals(2, historyTasks.size());
        assertTrue(historyTasks.contains(task1));
        assertFalse(historyTasks.contains(task2));
        assertTrue(historyTasks.contains(task3));
    }
    @Test
    public void testRemoveFromHistoryAtEnd() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", 1, TaskStatus.NEW, Duration.ofHours(2), ZonedDateTime.now());
        Task task2 = new Task("Задача 2", "Описание задачи 2", 2, TaskStatus.IN_PROGRESS, Duration.ofHours(1), ZonedDateTime.now().plusHours(1));

        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(task2.getTaskID());

        List<Task> historyTasks = historyManager.getHistory();
        assertEquals(1, historyTasks.size());
        assertTrue(historyTasks.contains(task1));
        assertFalse(historyTasks.contains(task2));
    }

    @Test
    public void testRemoveNonExistentTaskFromHistory() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", 1, TaskStatus.NEW, Duration.ofHours(2), ZonedDateTime.now());
        Task task2 = new Task("Задача 2", "Описание задачи 2", 2, TaskStatus.IN_PROGRESS, Duration.ofHours(1), ZonedDateTime.now().plusHours(1));

        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(task2.getTaskID());

        assertThrows(IllegalStateException.class, () -> historyManager.remove(task2.getTaskID()));
    }

    @Test
    public void testGetEmptyHistory() {
        List<Task> historyTasks = historyManager.getHistory();
        assertTrue(historyTasks.isEmpty());
    }
}
