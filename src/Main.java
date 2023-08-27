    import com.google.gson.Gson;
    import com.google.gson.GsonBuilder;
    import deserializer.EpicAdapter;
    import deserializer.SubTaskAdapter;
    import deserializer.TaskAdapter;
    import models.Epic;
    import models.SubTask;
    import models.Task;
    import models.TaskStatus;
    import service.FileBackedTasksManager;
    import service.HistoryManager;
    import service.InMemoryTaskManager;
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

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Task.class, new TaskAdapter())
                    .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
                    .registerTypeAdapter(Epic.class, new EpicAdapter())
                    .create();

            //id=1
            Task task1 = new Task("Просто отвертка",
                    "Купить отвертку",
                    TaskStatus.NEW,
                    Duration.ofMinutes(90),
                    ZonedDateTime.of(LocalDateTime.of(2023, 7, 25, 13, 00),
                            ZoneId.of("Europe/Samara")));
            fileBackedTaskManager.createTask(task1); // "Europe/Samara -> Correct"

            System.out.println(gson.toJson(task1));


            Epic epic1 = new Epic("Купить дом", "Купить пентхаус в Казани");
            //id=2
            fileBackedTaskManager.createEpic(epic1);



            SubTask subTask1 = new SubTask("Материал для пола",
                    "Покупка расходников для пола",
                    TaskStatus.NEW,
                    Duration.ofMinutes(180),
                    ZonedDateTime.of(LocalDateTime.of(2023, 8, 25, 14, 30),
                            ZoneId.of("Europe/Samara")),
                    2);

            //id=3
            fileBackedTaskManager.createSubTask(subTask1);

            System.out.println(gson.toJson(epic1));
            System.out.println(gson.toJson(subTask1));

            //id=4
            fileBackedTaskManager.createSubTask(new SubTask("Купить спойлер",
                    "Купить спойлер белого цвета",
                    TaskStatus.NEW,
                    Duration.ofMinutes(200),
                    ZonedDateTime.of(LocalDateTime.of(2022, 9, 30, 14, 30),
                            ZoneId.of("Europe/Samara")),
                    2));

            //id=5 -> для тестирования ошибки по пересечению
//            fileBackedTaskManager.createTask(new Task("Специально для теста",
//                    "Протестировать",
//                    TaskStatus.NEW,
//                    Duration.ofMinutes(90),
//                    ZonedDateTime.of(LocalDateTime.of(2023, 7, 25, 13, 30),
//                            ZoneId.of("Europe/Samara")))); // "Europe/Samara -> Correct"

            System.out.println(fileBackedTaskManager.getHistory());


            fileBackedTaskManager.getTaskByID(1);
            fileBackedTaskManager.getEpicByID(2);
            fileBackedTaskManager.getSubTaskByID(3);

            System.out.println(fileBackedTaskManager.getPrioritizedTask());
        }
    }
