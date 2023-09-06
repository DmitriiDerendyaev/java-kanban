package service;

import models.Epic;
import models.SubTask;
import models.Task;
import models.TaskStatus;
import org.junit.jupiter.api.*;
import servers.KVServer;
import util.Manager;

import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager>{

    @Override
    protected HttpTaskManager createTaskManager() {
        try {
            return new HttpTaskManager("http://localhost:8070");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("�� ������� ������� HttpTaskManager" + e);
        }
    }

    HttpTaskManager httpTaskManager;
    static KVServer kvServer;


    @BeforeAll
    public static void keyValueServerStart(){
        try {
            kvServer = new KVServer();
        } catch (IOException e) {
            throw new RuntimeException("������ �������� KVServer" + e);
        }
        kvServer.start();
    }
    @BeforeEach
    public void currentSetUp(){
        try {
            httpTaskManager = new HttpTaskManager("http://localhost:8070");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
//        kvServer.start();
    }

    @AfterEach
    public void tearDown(){
        httpTaskManager.clearTasks();
        httpTaskManager.clearSubTasks();
        httpTaskManager.clearEpics();
//        kvServer.stop();
    }

    @Override
    @Test
    public void testCreateEpic() {
        Epic epic = new Epic("�������� ����", "�������� ��������� �����");
        int epicId = httpTaskManager.createEpic(epic);
        Epic createdEpic = httpTaskManager.getEpicByID(epicId);
        assertNotNull(createdEpic);
        assertEquals(epic.getTaskName(), createdEpic.getTaskName());
        assertEquals(epic.getTaskDescription(), createdEpic.getTaskDescription());
    }

    @Override
    @Test
    public void testCreateTask() {
        Task task = new Task("�������� ������", "�������� �������� ������", TaskStatus.NEW, Duration.ofHours(2), ZonedDateTime.now());
        int taskId = httpTaskManager.createTask(task);
        Task createdTask = httpTaskManager.getTaskByID(taskId);
        assertNotNull(createdTask);
        assertEquals(task.getTaskName(), createdTask.getTaskName());
        assertEquals(task.getTaskDescription(), createdTask.getTaskDescription());
    }

    @Override
    @Test
    public void testCreateSubTask() {
        Epic epic = new Epic("�������� ����", "�������� ��������� �����");
        int epicId = httpTaskManager.createEpic(epic);
        SubTask subTask = new SubTask("�������� ���������", "�������� �������� ���������", TaskStatus.NEW, Duration.ofHours(1), ZonedDateTime.now().plusHours(10), epicId);
        int subTaskId = httpTaskManager.createSubTask(subTask);
        SubTask createdSubTask = httpTaskManager.getSubTaskByID(subTaskId);
        assertNotNull(createdSubTask);
        assertEquals(subTask.getTaskName(), createdSubTask.getTaskName());
        assertEquals(subTask.getTaskDescription(), createdSubTask.getTaskDescription());
        assertEquals(epicId, createdSubTask.getEpicID());
    }

//    @Test
//    public void testUpdateTask() {
//        Task task = new Task("�������� ������", "�������� �������� ������", TaskStatus.NEW, Duration.ofHours(2), ZonedDateTime.now());
//        int taskId = httpTaskManager.createTask(task);
//        Task taskToUpdate = new Task("����������� ������", "����������� �������� ������", TaskStatus.IN_PROGRESS, Duration.ofHours(3), ZonedDateTime.now());
//        taskToUpdate.setTaskID(taskId);
//        int updatedTaskId = httpTaskManager.updateTask(taskToUpdate);
//        Task updatedTask = httpTaskManager.getTaskByID(updatedTaskId);
//        assertNotNull(updatedTask);
//        assertEquals("����������� ������", updatedTask.getTaskName());
//        assertEquals("����������� �������� ������", updatedTask.getTaskDescription());
//        assertEquals(TaskStatus.IN_PROGRESS, updatedTask.getTaskStatus());
//    }
//
//    @Test
//    public void testUpdateSubTask() {
//        Epic epic = new Epic("�������� ����", "�������� ��������� �����");
//        int epicId = httpTaskManager.createEpic(epic);
//        SubTask subTask = new SubTask("�������� ���������", "�������� �������� ���������", TaskStatus.NEW, Duration.ofHours(1), ZonedDateTime.now().plusHours(10), epicId);
//        int subTaskId = httpTaskManager.createSubTask(subTask);
//        SubTask updatedSubTask = new SubTask("����������� ���������", "����������� �������� ���������", TaskStatus.IN_PROGRESS, Duration.ofHours(2), ZonedDateTime.now().plusHours(5), epicId);
//        updatedSubTask.setTaskID(subTaskId);
//        int updatedSubTaskId = httpTaskManager.updateSubTask(updatedSubTask);
//        SubTask updatedSubTaskGot = httpTaskManager.getSubTaskByID(updatedSubTaskId);
//        assertNotNull(updatedSubTaskGot);
//        assertEquals("����������� ���������", updatedSubTaskGot.getTaskName());
//        assertEquals("����������� �������� ���������", updatedSubTaskGot.getTaskDescription());
//        assertEquals(TaskStatus.IN_PROGRESS, updatedSubTaskGot.getTaskStatus());
//    }
//
//    @Test
//    public void testRemoveTaskByID() {
//        Task task = new Task("�������� ������", "�������� �������� ������", TaskStatus.NEW, Duration.ofHours(2), ZonedDateTime.now());
//        int taskId = httpTaskManager.createTask(task);
//        httpTaskManager.removeTaskByID(taskId);
//        assertNull(httpTaskManager.getTaskByID(taskId));
//    }
//
//    @Test
//    public void testRemoveSubTaskByID() {
//        Epic epic = new Epic("�������� ����", "�������� ��������� �����");
//        int epicId = httpTaskManager.createEpic(epic);
//        SubTask subTask = new SubTask("�������� ���������", "�������� �������� ���������", TaskStatus.NEW, Duration.ofHours(1), ZonedDateTime.now().plusHours(10), epicId);
//        int subTaskId = httpTaskManager.createSubTask(subTask);
//        httpTaskManager.removeSubTaskByID(subTaskId);
//        assertNull(httpTaskManager.getSubTaskByID(subTaskId));
//    }
//
//    @Test
//    public void testRemoveEpicByID() {
//        Epic epic = new Epic("�������� ����", "�������� ��������� �����");
//        int epicId = httpTaskManager.createEpic(epic);
//        httpTaskManager.removeEpicByID(epicId);
//        assertNull(httpTaskManager.getEpicByID(epicId));
//    }
//
//    @Test
//    public void testClearTasks() {
//        Task task1 = new Task("�������� ������ 1", "�������� �������� ������ 1", TaskStatus.NEW, Duration.ofHours(2), ZonedDateTime.now());
//        Task task2 = new Task("�������� ������ 2", "�������� �������� ������ 2", TaskStatus.NEW, Duration.ofHours(1), ZonedDateTime.now().plusHours(5));
//        httpTaskManager.createTask(task1);
//        httpTaskManager.createTask(task2);
//        httpTaskManager.clearTasks();
//        assertEquals(0, httpTaskManager.getTaskList().size());
//    }
//
//    @Test
//    public void testClearSubTasks() {
//        Epic epic = new Epic("�������� ����", "�������� ��������� �����");
//        int epicId = httpTaskManager.createEpic(epic);
//        SubTask subTask1 = new SubTask("�������� ��������� 1", "�������� �������� ��������� 1", TaskStatus.NEW, Duration.ofHours(1), ZonedDateTime.now(), epicId);
//        SubTask subTask2 = new SubTask("�������� ��������� 2", "�������� �������� ��������� 2", TaskStatus.NEW, Duration.ofHours(2), ZonedDateTime.now().plusDays(2), epicId);
//        httpTaskManager.createSubTask(subTask1);
//        httpTaskManager.createSubTask(subTask2);
//        httpTaskManager.clearSubTasks();
//        assertEquals(0, httpTaskManager.getSubTaskList().size());
//    }
//
//    @Test
//    public void testClearEpics() {
//        Epic epic1 = new Epic("�������� ���� 1", "�������� ��������� ����� 1");
//        Epic epic2 = new Epic("�������� ���� 2", "�������� ��������� ����� 2");
//        httpTaskManager.createEpic(epic1);
//        httpTaskManager.createEpic(epic2);
//        httpTaskManager.clearEpics();
//        assertEquals(0, httpTaskManager.getEpicList().size());
//    }
//
//    @Test
//    public void testCreateSubTaskWithInvalidEpicID() {
//        SubTask subTask = new SubTask("�������� ���������", "�������� �������� ���������", TaskStatus.NEW, Duration.ofHours(1), ZonedDateTime.now(), -1);
//        assertThrows(NullPointerException.class, () -> httpTaskManager.createSubTask(subTask));
//    }
//
//    @Test
//    public void testUpdateTaskWithInvalidID() {
//        Task taskToUpdate = new Task("����������� ������", "����������� �������� ������", 1, TaskStatus.NEW, Duration.ofHours(2), ZonedDateTime.now());
//        assertThrows(IllegalArgumentException.class, () -> httpTaskManager.updateTask(taskToUpdate));
//    }
//
//    @Test
//    public void testUpdateSubTaskWithInvalidID() {
//        SubTask updatedSubTask = new SubTask("����������� ���������", "����������� �������� ���������", 2, TaskStatus.NEW, 1, Duration.ofHours(1), ZonedDateTime.now());
//        assertThrows(NullPointerException.class, () -> httpTaskManager.updateSubTask(updatedSubTask));
//    }
//
//    @Test
//    public void testRemoveTaskWithInvalidID() {
//        assertThrows(NullPointerException.class, () -> httpTaskManager.removeTaskByID(-1));
//    }
//
//    @Test
//    public void testRemoveSubTaskWithInvalidID() {
//        assertThrows(NullPointerException.class, () -> httpTaskManager.removeSubTaskByID(-1));
//    }
//
//    @Test
//    public void testRemoveEpicWithInvalidID() {
//        assertThrows(NullPointerException.class, () -> httpTaskManager.removeEpicByID(-1));
//    }
//
//    @Test
//    public void testGetPrioritizedTask() {
//        // ����� ����� �������� ���� ��� ������ getPrioritizedTask, ���� �� ���������� � HttpTaskManager
//        List<Task> prioritizedTasks = httpTaskManager.getPrioritizedTask();
//        assertNotNull(prioritizedTasks);
//        // �������� �������� �� ������������ ���������� �����, ���� ����������
//    }



}
