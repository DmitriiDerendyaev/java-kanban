        import models.Epic;
        import models.SubTask;
        import models.Task;
        import models.TaskStatus;
        import servers.KVServer;
        import util.Manager;
        import service.TaskManager;

        import java.io.IOException;
        import java.time.Duration;
        import java.time.LocalDateTime;
        import java.time.ZoneId;
        import java.time.ZonedDateTime;
        import java.util.Scanner;

        public class Main {

            public static void main(String[] args) throws IOException, InterruptedException {

                KVServer kvServer = new KVServer();
                kvServer.start();

                TaskManager taskManager = Manager.getDefault();

                //id=1
                Task task1 = new Task("������ ��������",
                        "������ ��������",
                        TaskStatus.NEW,
                        Duration.ofMinutes(90),
                        ZonedDateTime.of(LocalDateTime.of(2023, 7, 25, 13, 00),
                                ZoneId.of("Europe/Samara")));
                taskManager.createTask(task1); // "Europe/Samara -> Correct"



                Epic epic1 = new Epic("������ ���", "������ �������� � ������");
                //id=2
                taskManager.createEpic(epic1);



                SubTask subTask1 = new SubTask("�������� ��� ����",
                        "������� ����������� ��� ����",
                        TaskStatus.NEW,
                        Duration.ofMinutes(180),
                        ZonedDateTime.of(LocalDateTime.of(2023, 8, 25, 14, 30),
                                ZoneId.of("Europe/Samara")),
                        2);

                //id=3
                taskManager.createSubTask(subTask1);

                //id=4
                taskManager.createSubTask(new SubTask("������ �������",
                        "������ ������� ������ �����",
                        TaskStatus.NEW,
                        Duration.ofMinutes(200),
                        ZonedDateTime.of(LocalDateTime.of(2022, 9, 30, 14, 30),
                                ZoneId.of("Europe/Samara")),
                        2));

                taskManager.getTaskByID(1);
                taskManager.getEpicByID(2);
                taskManager.getSubTaskByID(3);


                //id=5
                Task task2 = new Task("������ ��������",
                        "������ ��������",
                        TaskStatus.NEW,
                        Duration.ofMinutes(180),
                        ZonedDateTime.of(LocalDateTime.of(2025, 7, 25, 13, 00),
                                ZoneId.of("Europe/Samara")));
                taskManager.createTask(task2);
            }
        }
