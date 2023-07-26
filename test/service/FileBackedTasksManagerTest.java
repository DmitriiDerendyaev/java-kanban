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
            throw new RuntimeException("Не удалось создать FileBackedTasksManager.", e);
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
        Task task1 = new Task("Задача 1", "Описание задачи", TaskStatus.NEW, Duration.ofHours(2), ZonedDateTime.now());

        taskManager.createTask(task1);
        taskManager.removeTaskByID(task1.getTaskID());
        taskManager.clearFileContents();
        //после этих операций файл будет пуст

        // Загружаем состояние задач из файла через создание нового объекта менеджера, при инициализации объект сам идет в файл
        FileBackedTasksManager newFileManager = new FileBackedTasksManager("src/resources/memory.csv");

        // Проверяем, что список задач пустой после загрузки
        List<Task> taskList = newFileManager.getTaskList();
        assertTrue(taskList.isEmpty());
    }

    @Test
    public void testSaveAndLoadEpicWithoutSubTasks() throws IOException {
        Epic epic = new Epic("Эпик 1", "Описание эпика");
        fileBackedTasksManager.createEpic(epic);
        fileBackedTasksManager.getEpicByID(1);

        FileBackedTasksManager newFileManager = new FileBackedTasksManager("src/resources/memory.csv");


        // Проверяем, что эпик без подзадач был сохранен и восстановлен корректно
        List<Epic> taskList = newFileManager.getEpicList();
        assertEquals(1, taskList.size());
        assertTrue(taskList.contains(epic));
    }

    @Test
    public void testSaveAndLoadEmptyHistory() throws IOException {
        Epic epic = new Epic("Эпик 1", "Описание эпика");
        fileBackedTasksManager.createEpic(epic);

        FileBackedTasksManager newFileManager = new FileBackedTasksManager("src/resources/memory.csv");

        // Проверяем, что история задач пуста после загрузки
        List<Task> historyTasks = newFileManager.getHistory();
        assertTrue(historyTasks.isEmpty());
    }
}