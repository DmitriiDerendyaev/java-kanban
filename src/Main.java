    import models.Epic;
    import models.SubTask;
    import models.Task;
    import models.TaskStatus;
    import service.FileBackedTasksManager;
    import service.HistoryManager;
    import util.Manager;
    import service.TaskManager;

    import java.io.IOException;
    import java.time.Duration;
    import java.time.LocalDateTime;
    import java.time.ZoneId;
    import java.time.ZonedDateTime;

    public class Main {

        public static void main(String[] args) throws IOException {

            TaskManager fileBackedTaskManager = new FileBackedTasksManager("src/resources/memory.csv");

            fileBackedTaskManager.createTask(new Task("Просто отвертка",
                    "Купить отвертку",
                    TaskStatus.NEW,
                    Duration.ofMinutes(90),
                    ZonedDateTime.of(LocalDateTime.of(2023, 7, 25, 13, 00),
                            ZoneId.of("Europe/Izhevsk")))); // "Europe/Samara -> Correct"

            fileBackedTaskManager.createEpic(new Epic("Купить дом", "Купить пентхаус в Казани"));

            fileBackedTaskManager.createSubTask(new SubTask("Материал для пола",
                    "Покупка расходников для пола",
                    TaskStatus.NEW,
                    Duration.ofMinutes(180),
                    ZonedDateTime.of(LocalDateTime.of(2023, 8, 25, 14, 30),
                            ZoneId.of("Europe/Izhevsk")),
                    2));
            fileBackedTaskManager.createSubTask(new SubTask("Купить спойлер",
                    "Купить спойлер белого цвета",
                    TaskStatus.NEW,
                    Duration.ofMinutes(180),
                    ZonedDateTime.of(LocalDateTime.of(2023, 9, 30, 14, 30),
                            ZoneId.of("Europe/Izhevsk")),
                    2));

            System.out.println(fileBackedTaskManager.getHistory());


            fileBackedTaskManager.getTaskByID(1);
            fileBackedTaskManager.getEpicByID(2);
            fileBackedTaskManager.getSubTaskByID(3);
        }
    }
