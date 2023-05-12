import models.Epic;
import models.SubTask;
import models.Task;
import models.TaskStatus;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        //�������� ���� ����� �� �� #1
        ///////////////////////////////////////////////////////
        Task firstTask = new Task("������ ��������", "������ ��������", TaskStatus.NEW);            // ID = 1
        Task secondTask = new Task("������ �����", "������ �����", TaskStatus.NEW);         // ID = 2
        Task thirdTask = new Task("������ �����", "������ �����", TaskStatus.NEW);                  // ID = 3
        System.out.println("���������� ������: ID = " + taskManager.createTask(firstTask));
        System.out.println("���������� ������: ID = " + taskManager.createTask(secondTask));
        System.out.println("���������� ������: ID = " + taskManager.createTask(thirdTask));

        Epic firstEpic = new Epic("������ ���", "������ �������� � ������", TaskStatus.NEW);        // ID = 4
        System.out.println("���������� �����: ID = " + taskManager.createEpic(firstEpic));

        SubTask firstSubTask = new SubTask("�������� ��� ����", "������� ����������� ��� ����",
                TaskStatus.NEW, 4);                                                                                // ID = 5
        System.out.println("���������� ���������: ID = " + taskManager.createSubTask(firstSubTask));

        Epic secondEpic = new Epic("������ ������", "������ Porsche 911", TaskStatus.NEW);          // ID = 6
        System.out.println("���������� �����: ID = " + taskManager.createEpic(secondEpic));

        SubTask secondSubTask = new SubTask("������ �������� ��� �������",
                "������� ����������� ��� �������", TaskStatus.NEW, 4);                                // ID = 7
        System.out.println("���������� ���������: ID = " + taskManager.createSubTask(secondSubTask));

        SubTask thirdSubTask = new SubTask("������ �������", "������ ������� ������ �����",         // ID = 8
                TaskStatus.NEW, 6);
        System.out.println("���������� ���������: ID = " + taskManager.createSubTask(thirdSubTask));

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getSubTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println("\n");
        ///////////////////////////////////////////////////////////

        //���������� �����
        Task updSecondTask = new Task("������ �����", "������ �����", 2, TaskStatus.IN_PROGRESS);
        taskManager.updateTask(updSecondTask);

        SubTask updFirstSubTask = new SubTask("�������� ��� ����", "������� ����������� ��� ����",
                5, TaskStatus.DONE, 4);
        taskManager.updateSubTask(updFirstSubTask);

        SubTask updSecondSubTask = new SubTask("������ �������� ��� �������",
                "������� ����������� ��� �������", 7, TaskStatus.IN_PROGRESS, 4);
        taskManager.updateSubTask(updSecondSubTask);

        SubTask updThirdSubTask = new SubTask("������ �������", "������ ������� ������ �����",         // ID = 8
                8, TaskStatus.IN_PROGRESS, 6);
        taskManager.updateSubTask(updThirdSubTask);

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getSubTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println("\n");
        ///////////////////////////////////////////////////////

        //�������� �����
        taskManager.removeTaskByID(3);
        taskManager.removeSubTaskByID(7);
        taskManager.removeEpicByID(6);

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getSubTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println("\n");

        System.out.println("All test passed");
    }
}
