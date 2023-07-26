package service;

import models.Epic;
import models.SubTask;
import models.Task;
import models.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @Override
    protected FileBackedTasksManager createTaskManager() {
        try {
            return new FileBackedTasksManager("src/resources/memory.csv");
        } catch (IOException e) {
            throw new RuntimeException("�� ������� ������� FileBackedTasksManager.", e);
        }
    }

    FileBackedTasksManager fileBackedTasksManager;

    @BeforeEach
    public void currentSetUp() throws IOException {
        fileBackedTasksManager = new FileBackedTasksManager("src/resources/memory.csv");
    }

    @AfterEach
    public void tearDown() throws IOException {
        taskManager.clearEpics();
        taskManager.clearFileContents();
    }

    @Test
    public void testSaveAndLoadEmptyTaskList() throws IOException {
        Task task1 = new Task("������ 1", "�������� ������", TaskStatus.NEW, Duration.ofHours(2), ZonedDateTime.now());

        taskManager.createTask(task1);
        taskManager.removeTaskByID(task1.getTaskID());
        taskManager.clearFileContents();
        //����� ���� �������� ���� ����� ����

        // ��������� ��������� ����� �� ����� ����� �������� ������ ������� ���������, ��� ������������� ������ ��� ���� � ����
        FileBackedTasksManager newFileManager = new FileBackedTasksManager("src/resources/memory.csv");

        // ���������, ��� ������ ����� ������ ����� ��������
        List<Task> taskList = newFileManager.getTaskList();
        assertTrue(taskList.isEmpty());
    }

    @Test
    public void testSaveAndLoadEpicWithoutSubTasks() throws IOException {
        Epic epic = new Epic("���� 1", "�������� �����");
        fileBackedTasksManager.createEpic(epic);
        fileBackedTasksManager.getEpicByID(1);

        FileBackedTasksManager newFileManager = new FileBackedTasksManager("src/resources/memory.csv");


        // ���������, ��� ���� ��� �������� ��� �������� � ������������ ���������
        List<Epic> taskList = newFileManager.getEpicList();
        assertEquals(1, taskList.size());
        assertTrue(taskList.contains(epic));
    }

    @Test
    public void testSaveAndLoadEmptyHistory() throws IOException {
        Epic epic = new Epic("���� 1", "�������� �����");
        fileBackedTasksManager.createEpic(epic);

        FileBackedTasksManager newFileManager = new FileBackedTasksManager("src/resources/memory.csv");

        // ���������, ��� ������� ����� ����� ����� ��������
        List<Task> historyTasks = newFileManager.getHistory();
        assertTrue(historyTasks.isEmpty());
    }
}