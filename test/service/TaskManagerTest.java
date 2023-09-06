package service;

import models.Epic;
import models.SubTask;
import models.Task;
import models.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = createTaskManager();
    }

    protected abstract T createTaskManager();

    @Test
    public void testCreateEpic() {
        Epic epic = new Epic("Эпик 1", "Описание эпика");

        int epicId = taskManager.createEpic(epic);

        Epic savedEpic = taskManager.getEpicByID(epicId);
        assertEquals(epic.getTaskName(), savedEpic.getTaskName());
        assertEquals(epic.getTaskDescription(), savedEpic.getTaskDescription());
    }
    @Test
    public void testCreateTask() {
        Task task = new Task("Задача 1", "Описание задачи", TaskStatus.NEW, Duration.ofHours(2), ZonedDateTime.now());
        int taskId = taskManager.createTask(task);

        Task savedTask = taskManager.getTaskByID(taskId);
        assertEquals(task.getTaskName(), savedTask.getTaskName());
        assertEquals(task.getTaskDescription(), savedTask.getTaskDescription());
    }

    @Test
    public void testCreateSubTask() {
        Epic epic = new Epic("Эпик 1", "Описание эпика");
        int epicId = taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача 1", "Описание подзадачи", TaskStatus.NEW, Duration.ofHours(1), ZonedDateTime.now().plusHours(10), epicId);
        int subTaskId = taskManager.createSubTask(subTask);

        SubTask savedSubTask = taskManager.getSubTaskByID(subTaskId);
        assertEquals(subTask.getTaskName(), savedSubTask.getTaskName());
        assertEquals(subTask.getTaskDescription(), savedSubTask.getTaskDescription());
        assertEquals(epicId, savedSubTask.getEpicID());
    }

    @Test
    public void testUpdateTask() {
        Task task = new Task("Задача 1", "Описание задачи", TaskStatus.NEW, Duration.ofHours(2), ZonedDateTime.now());
        int taskId = taskManager.createTask(task);


        Task taskToUpdate = new Task("UPD", "Описание задачи", 1, TaskStatus.NEW, Duration.ofHours(2), ZonedDateTime.now());
        taskManager.updateTask(taskToUpdate);

        Task updatedTask = taskManager.getTaskByID(taskId);
        assertEquals("UPD", updatedTask.getTaskName());
        assertEquals(1, updatedTask.getTaskID());
    }

    @Test
    public void testUpdateSubTask() {
        Epic epic = new Epic("Эпик 1", "Описание эпика");
        int epicId = taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача 1",
                "Описание подзадачи",
                TaskStatus.NEW,
                Duration.ofHours(1),
                ZonedDateTime.now(),
                epicId);
        int subTaskId = taskManager.createSubTask(subTask);

        SubTask updatedSubTask = new SubTask("UPD",
                "Описание подзадачи",
                2,
                TaskStatus.NEW,
                1,
                Duration.ofHours(1),
                ZonedDateTime.now());
        taskManager.updateSubTask(updatedSubTask);

        SubTask updatedSubTaskGot = taskManager.getSubTaskByID(subTaskId);

        assertEquals("UPD", updatedSubTaskGot.getTaskName());
        assertEquals(2, updatedSubTaskGot.getTaskID());
    }

    @Test
    public void testRemoveTaskByID() {
        Task task = new Task("Задача 1", "Описание задачи", TaskStatus.NEW, Duration.ofHours(2), ZonedDateTime.now());
        int taskId = taskManager.createTask(task);

        taskManager.removeTaskByID(taskId);

        assertTrue(taskManager.getTaskList().isEmpty());
    }

    @Test
    public void testRemoveSubTaskByID() {
        Epic epic = new Epic("Эпик 1", "Описание эпика");
        int epicId = taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача 1", "Описание подзадачи 1", TaskStatus.NEW, Duration.ofHours(1), ZonedDateTime.now(), epicId);
        SubTask subTaskSecond = new SubTask("Подзадача 2", "Описание подзадачи 2", TaskStatus.NEW, Duration.ofHours(1), ZonedDateTime.now().plusHours(10), epicId);
        int subTaskIdFirst = taskManager.createSubTask(subTask);
        int subTaskIdSecond = taskManager.createSubTask(subTaskSecond);

        taskManager.removeSubTaskByID(subTaskIdFirst);

        assertEquals(1, taskManager.getSubTaskList().size());
    }

    @Test
    public void testRemoveEpicByID() {
        Epic epic = new Epic("Эпик 1", "Описание эпика");
        int epicId = taskManager.createEpic(epic);

        taskManager.removeEpicByID(epicId);

        assertTrue(taskManager.getEpicList().isEmpty());
    }

    @Test
    public void testClearTasks() {
        Task task1 = new Task("Задача 1", "Описание задачи", TaskStatus.NEW, Duration.ofHours(2), ZonedDateTime.now());
        Task task2 = new Task("Задача 2", "Описание задачи", TaskStatus.NEW, Duration.ofHours(1), ZonedDateTime.now().plusHours(5));

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.clearTasks();

        assertEquals(0, taskManager.getTaskList().size());
    }

    @Test
    public void testClearSubTasks() {
        Epic epic = new Epic("Эпик 1", "Описание эпика");
        int epicId = taskManager.createEpic(epic);

        SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи", TaskStatus.NEW, Duration.ofHours(1), ZonedDateTime.now(), epicId);
        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи", TaskStatus.NEW, Duration.ofHours(2), ZonedDateTime.now().plusDays(2), epicId);

        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        taskManager.clearSubTasks();

        assertEquals(0, taskManager.getSubTaskList().size());
        assertTrue(epic.getTaskCollection().isEmpty());
    }

    @Test
    public void testClearEpics() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика");

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        taskManager.clearEpics();

        assertEquals(0, taskManager.getEpicList().size());
    }

    @Test
    public void testCreateSubTaskWithInvalidEpicID() {
        SubTask subTask = new SubTask("Подзадача 1", "Описание подзадачи", TaskStatus.NEW, Duration.ofHours(1), ZonedDateTime.now(), -1);

        assertThrows(NullPointerException.class, () -> taskManager.createSubTask(subTask));
    }

    @Test
    public void testUpdateTaskWithInvalidID() {
        Task taskToUpdate = new Task("UPD", "Описание задачи", 1, TaskStatus.NEW, Duration.ofHours(2), ZonedDateTime.now());

        assertThrows(IllegalArgumentException.class, () -> taskManager.updateTask(taskToUpdate));
    }

    @Test
    public void testUpdateSubTaskWithInvalidID() {
        SubTask updatedSubTask = new SubTask("UPD", "Описание подзадачи", 2, TaskStatus.NEW, 1, Duration.ofHours(1), ZonedDateTime.now());

        assertThrows(NullPointerException.class, () -> taskManager.updateSubTask(updatedSubTask));
    }

    @Test
    public void testRemoveTaskWithInvalidID() {
        assertThrows(NullPointerException.class, () -> taskManager.removeTaskByID(-1));
    }

    @Test
    public void testRemoveSubTaskWithInvalidID() {
        assertThrows(NullPointerException.class, () -> taskManager.removeSubTaskByID(-1));
    }

    @Test
    public void testRemoveEpicWithInvalidID() {
        assertThrows(NullPointerException.class, () -> taskManager.removeEpicByID(-1));
    }

}