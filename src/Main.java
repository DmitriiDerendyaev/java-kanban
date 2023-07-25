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
    import java.time.ZonedDateTime;

    public class Main {

        public static void main(String[] args) throws IOException {

            TaskManager fileBackedTaskManager = new FileBackedTasksManager("src/resources/memory.csv");

            Duration duration;
            ZonedDateTime zonedDateTime;

            fileBackedTaskManager.createTask(new Task("������ ��������", "������ ��������", TaskStatus.NEW));
            fileBackedTaskManager.createEpic(new Epic("������ ���", "������ �������� � ������"));
            fileBackedTaskManager.createSubTask(new SubTask("�������� ��� ����", "������� ����������� ��� ����",
                    TaskStatus.NEW, 2));
            fileBackedTaskManager.createSubTask(new SubTask("������ �������", "������ ������� ������ �����",
                    TaskStatus.NEW, 2));

            System.out.println(fileBackedTaskManager.getHistory());


            fileBackedTaskManager.getTaskByID(1);
            fileBackedTaskManager.getEpicByID(2);
            fileBackedTaskManager.getSubTaskByID(3);
        }
    }
