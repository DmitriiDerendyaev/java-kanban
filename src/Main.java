import models.Epic;
import models.SubTask;
import models.Task;
import models.TaskStatus;
import service.HistoryManager;
import service.InMemoryTaskManager;
import service.Manager;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {

//        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        TaskManager inMemoryTaskManager = Manager.getDefault();

        //�������� ���� ����� �� �� #1
        ///////////////////////////////////////////////////////
        Task firstTask = new Task("������ ��������", "������ ��������", TaskStatus.NEW);            // ID = 1
        Task secondTask = new Task("������ �����", "������ �����", TaskStatus.NEW);         // ID = 2
        Task thirdTask = new Task("������ �����", "������ �����", TaskStatus.NEW);                  // ID = 3
        System.out.println("���������� ������: ID = " + inMemoryTaskManager.createTask(firstTask));
        System.out.println("���������� ������: ID = " + inMemoryTaskManager.createTask(secondTask));
        System.out.println("���������� ������: ID = " + inMemoryTaskManager.createTask(thirdTask));

        Epic firstEpic = new Epic("������ ���", "������ �������� � ������", TaskStatus.NEW);        // ID = 4
        System.out.println("���������� �����: ID = " + inMemoryTaskManager.createEpic(firstEpic));

        SubTask firstSubTask = new SubTask("�������� ��� ����", "������� ����������� ��� ����",
                TaskStatus.NEW, 4);                                                                                // ID = 5
        System.out.println("���������� ���������: ID = " + inMemoryTaskManager.createSubTask(firstSubTask));

        Epic secondEpic = new Epic("������ ������", "������ Porsche 911", TaskStatus.NEW);          // ID = 6
        System.out.println("���������� �����: ID = " + inMemoryTaskManager.createEpic(secondEpic));

        SubTask secondSubTask = new SubTask("������ �������� ��� �������",
                "������� ����������� ��� �������", TaskStatus.NEW, 4);                                // ID = 7
        System.out.println("���������� ���������: ID = " + inMemoryTaskManager.createSubTask(secondSubTask));

        SubTask thirdSubTask = new SubTask("������ �������", "������ ������� ������ �����",         // ID = 8
                TaskStatus.NEW, 6);
        System.out.println("���������� ���������: ID = " + inMemoryTaskManager.createSubTask(thirdSubTask));

        System.out.println(inMemoryTaskManager.getTaskList());
        System.out.println(inMemoryTaskManager.getSubTaskList());
        System.out.println(inMemoryTaskManager.getEpicList());
        System.out.println("\n");
        ///////////////////////////////////////////////////////////

        //���������� �����
        Task updSecondTask = new Task("������ �����", "������ �����", 2, TaskStatus.IN_PROGRESS);
        inMemoryTaskManager.updateTask(updSecondTask);

        SubTask updFirstSubTask = new SubTask("�������� ��� ����", "������� ����������� ��� ����",
                5, TaskStatus.DONE, 4);
        inMemoryTaskManager.updateSubTask(updFirstSubTask);

        SubTask updSecondSubTask = new SubTask("������ �������� ��� �������",
                "������� ����������� ��� �������", 7, TaskStatus.IN_PROGRESS, 4);
        inMemoryTaskManager.updateSubTask(updSecondSubTask);

        SubTask updThirdSubTask = new SubTask("������ �������", "������ ������� ������ �����",         // ID = 8
                8, TaskStatus.IN_PROGRESS, 6);
        inMemoryTaskManager.updateSubTask(updThirdSubTask);

        System.out.println(inMemoryTaskManager.getTaskList());
        System.out.println(inMemoryTaskManager.getSubTaskList());
        System.out.println(inMemoryTaskManager.getEpicList());
        System.out.println("\n");
        ///////////////////////////////////////////////////////

        //�������� �����
        inMemoryTaskManager.removeTaskByID(3);
        inMemoryTaskManager.removeSubTaskByID(7);
        inMemoryTaskManager.removeEpicByID(6);

        System.out.println(inMemoryTaskManager.getTaskList());
        System.out.println(inMemoryTaskManager.getSubTaskList());
        System.out.println(inMemoryTaskManager.getEpicList());
        System.out.println("\n");

        System.out.println("Tests of the 3rd sprint passed successfully");

        ///////////////////////////////////////////////////////
        // ������ � �������

        System.out.println(inMemoryTaskManager.getTaskByID(2));
        System.out.println(inMemoryTaskManager.getEpicByID(4));
        System.out.println(inMemoryTaskManager.getTaskByID(2));
        System.out.println(inMemoryTaskManager.getTaskByID(1));
        System.out.println(inMemoryTaskManager.getTaskByID(2));
        System.out.println(inMemoryTaskManager.getSubTaskByID(5));
        System.out.println(inMemoryTaskManager.getTaskByID(1));
        System.out.println(inMemoryTaskManager.getTaskByID(2));
        System.out.println(inMemoryTaskManager.getTaskByID(1));
        System.out.println(inMemoryTaskManager.getTaskByID(2));
        System.out.println(inMemoryTaskManager.getTaskByID(1));
        System.out.println(inMemoryTaskManager.getTaskByID(2));

        System.out.println(inMemoryTaskManager.getHistory());

        System.out.println("Tests of the 4th sprint passed successfully");
    }
}
