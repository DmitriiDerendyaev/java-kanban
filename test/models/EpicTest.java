package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTasksManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    FileBackedTasksManager fileBackedTasksManager;
    Epic epic;
    SubTask subTask;
    Task task;

    @BeforeEach
    public void setUp() throws IOException {
        fileBackedTasksManager = new FileBackedTasksManager("src/resources/memory.csv");
        task = null;
        epic = null;
        subTask = null;
    }

    @Test
    public void shouldMakeEmptyEpic() throws IOException {
        fileBackedTasksManager.createEpic(new Epic("Купить дом", "Купить пентхаус в Казани"));

        assertEquals(TaskStatus.NEW, fileBackedTasksManager.getEpics().get(1).taskStatus, "При пустом эпике статус эпика не NEW");

        fileBackedTasksManager.clearFileContents();
    }

    @Test
    public void shouldMakeEpicWithNewStatus() throws IOException {
        //id=1
        fileBackedTasksManager.createEpic(new Epic("Купить дом", "Купить пентхаус в Казани"));

        //id=2
        fileBackedTasksManager.createSubTask(new SubTask("Материал для пола",
                "Покупка расходников для пола",
                TaskStatus.NEW,
                Duration.ofMinutes(180),
                ZonedDateTime.of(LocalDateTime.of(2023, 8, 25, 14, 30),
                        ZoneId.of("Europe/Samara")),
                1));

        //id=3
        fileBackedTasksManager.createSubTask(new SubTask("Купить спойлер",
                "Купить спойлер белого цвета",
                TaskStatus.NEW,
                Duration.ofMinutes(200),
                ZonedDateTime.of(LocalDateTime.of(2022, 9, 30, 14, 30),
                        ZoneId.of("Europe/Samara")),
                1));

        assertEquals(TaskStatus.NEW, fileBackedTasksManager.getEpics().get(1).taskStatus, "При заполненном эпике статус эпика не NEW");

        fileBackedTasksManager.clearFileContents();
    }

    @Test
    public void shouldMakeEpicWithDoneStatus() throws IOException {
        //id=1
        fileBackedTasksManager.createEpic(new Epic("Купить дом", "Купить пентхаус в Казани"));

        //id=2
        fileBackedTasksManager.createSubTask(new SubTask("Материал для пола",
                "Покупка расходников для пола",
                TaskStatus.DONE,
                Duration.ofMinutes(180),
                ZonedDateTime.of(LocalDateTime.of(2023, 8, 25, 14, 30),
                        ZoneId.of("Europe/Samara")),
                1));

        //id=3
        fileBackedTasksManager.createSubTask(new SubTask("Купить спойлер",
                "Купить спойлер белого цвета",
                TaskStatus.DONE,
                Duration.ofMinutes(200),
                ZonedDateTime.of(LocalDateTime.of(2022, 9, 30, 14, 30),
                        ZoneId.of("Europe/Samara")),
                1));

        assertEquals(TaskStatus.DONE, fileBackedTasksManager.getEpics().get(1).taskStatus, "При заполненном эпике все задачи DONE, а статус эпика не DONE");

        fileBackedTasksManager.clearFileContents();
    }

    @Test
    public void shouldMakeEpicWithInProgressStatusContainsNewDone() throws IOException {
        //id=1
        fileBackedTasksManager.createEpic(new Epic("Купить дом", "Купить пентхаус в Казани"));

        //id=2
        fileBackedTasksManager.createSubTask(new SubTask("Материал для пола",
                "Покупка расходников для пола",
                TaskStatus.NEW,
                Duration.ofMinutes(180),
                ZonedDateTime.of(LocalDateTime.of(2023, 8, 25, 14, 30),
                        ZoneId.of("Europe/Samara")),
                1));

        //id=3
        fileBackedTasksManager.createSubTask(new SubTask("Купить спойлер",
                "Купить спойлер белого цвета",
                TaskStatus.DONE,
                Duration.ofMinutes(200),
                ZonedDateTime.of(LocalDateTime.of(2022, 9, 30, 14, 30),
                        ZoneId.of("Europe/Samara")),
                1));

        assertEquals(TaskStatus.IN_PROGRESS, fileBackedTasksManager.getEpics().get(1).taskStatus, "При заполненном эпике все задачи DONE, а статус эпика не DONE");

        fileBackedTasksManager.clearFileContents();
    }


    @Test
    public void shouldMakeEpicWithInProgressStatusContainsNewProgress() throws IOException {
        //id=1
        fileBackedTasksManager.createEpic(new Epic("Купить дом", "Купить пентхаус в Казани"));

        //id=2
        fileBackedTasksManager.createSubTask(new SubTask("Материал для пола",
                "Покупка расходников для пола",
                TaskStatus.NEW,
                Duration.ofMinutes(180),
                ZonedDateTime.of(LocalDateTime.of(2023, 8, 25, 14, 30),
                        ZoneId.of("Europe/Samara")),
                1));

        //id=3
        fileBackedTasksManager.createSubTask(new SubTask("Купить спойлер",
                "Купить спойлер белого цвета",
                TaskStatus.IN_PROGRESS,
                Duration.ofMinutes(200),
                ZonedDateTime.of(LocalDateTime.of(2022, 9, 30, 14, 30),
                        ZoneId.of("Europe/Samara")),
                1));

        assertEquals(TaskStatus.IN_PROGRESS, fileBackedTasksManager.getEpics().get(1).taskStatus, "При заполненном эпике все задачи DONE, а статус эпика не DONE");

        fileBackedTasksManager.clearFileContents();
    }


}