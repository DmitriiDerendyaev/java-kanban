import models.Epic;
import models.SubTask;
import models.Task;
import models.TaskStatus;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        Task firstTask = new Task("������ ��������", "������ ��������", TaskStatus.NEW);            // ID = 1
        Task secondTask = new Task("������ �����", "������ �����", TaskStatus.IN_PROGRESS);         // ID = 2
        Task thirdTask = new Task("������ �����", "������ �����", TaskStatus.NEW);                  // ID = 3
        taskManager.createTask(firstTask);
        taskManager.createTask(secondTask);
        taskManager.createTask(thirdTask);

        Epic firstEpic = new Epic("������ ���", "������ �������� � ������", TaskStatus.NEW);        // ID = 4
        taskManager.createEpic(firstEpic);

        SubTask firstSubTask = new SubTask("�������� ��� ����", "������� ����������� ��� ����",
                TaskStatus.NEW, 4);                                                                                // ID = 5
        taskManager.createSubTask(firstSubTask);

        Epic secondEpic = new Epic("������ ������", "������ Porsche 911", TaskStatus.NEW);          // ID = 6
        taskManager.createEpic(secondEpic);

        SubTask secondSubTask = new SubTask("������ �������� ��� �������",
                "������� ����������� ��� �������", TaskStatus.NEW, 4);                                // ID = 7
        taskManager.createSubTask(secondSubTask);
        System.out.println("All test passed");
    }
}
