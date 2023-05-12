import models.Epic;
import models.SubTask;
import models.Task;
import models.TaskStatus;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        //�������� ���� ����� �� ��
        ///////////////////////////////////////////////////////
        Task firstTask = new Task("������ ��������", "������ ��������", TaskStatus.NEW);            // ID = 1
        Task secondTask = new Task("������ �����", "������ �����", TaskStatus.IN_PROGRESS);         // ID = 2
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
        ///////////////////////////////////////////////////////////

        //���������� �����
        Task updSecondTask = new Task("������ �����", "������ �����", 2, TaskStatus.DONE);
        taskManager.updateTask(updSecondTask);

        SubTask updFirstSubTask = new SubTask("�������� ��� ����", "������� ����������� ��� ����",
                5, TaskStatus.DONE, 4);
        taskManager.updateSubTask(updFirstSubTask);

        //�������� �����
//        taskManager.removeEpicByID(6);

        taskManager.removeSubTaskByID(5);

        taskManager.clearEpic();
        System.out.println("All test passed");
    }
}
