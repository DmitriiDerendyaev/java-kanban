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

        //Основной блок теста по ТЗ #1
        ///////////////////////////////////////////////////////
        Task firstTask = new Task("Просто отвертка", "Купить отвертку", TaskStatus.NEW);            // ID = 1
        Task secondTask = new Task("Просто шуруп", "Купить шуруп", TaskStatus.NEW);         // ID = 2
        Task thirdTask = new Task("Просто доска", "Купить доску", TaskStatus.NEW);                  // ID = 3
        System.out.println("Добавление задачи: ID = " + inMemoryTaskManager.createTask(firstTask));
        System.out.println("Добавление задачи: ID = " + inMemoryTaskManager.createTask(secondTask));
        System.out.println("Добавление задачи: ID = " + inMemoryTaskManager.createTask(thirdTask));

        Epic firstEpic = new Epic("Купить дом", "Купить пентхаус в Казани", TaskStatus.NEW);        // ID = 4
        System.out.println("Добавление эпика: ID = " + inMemoryTaskManager.createEpic(firstEpic));

        SubTask firstSubTask = new SubTask("Материал для пола", "Покупка расходников для пола",
                TaskStatus.NEW, 4);                                                                                // ID = 5
        System.out.println("Добавление подзадачи: ID = " + inMemoryTaskManager.createSubTask(firstSubTask));

        Epic secondEpic = new Epic("Купить машину", "Купить Porsche 911", TaskStatus.NEW);          // ID = 6
        System.out.println("Добавление эпика: ID = " + inMemoryTaskManager.createEpic(secondEpic));

        SubTask secondSubTask = new SubTask("Купить материал для потолка",
                "Покупка расходников для потолка", TaskStatus.NEW, 4);                                // ID = 7
        System.out.println("Добавление подзадачи: ID = " + inMemoryTaskManager.createSubTask(secondSubTask));

        SubTask thirdSubTask = new SubTask("Купить спойлер", "Купить спойлер белого цвета",         // ID = 8
                TaskStatus.NEW, 6);
        System.out.println("Добавление подзадачи: ID = " + inMemoryTaskManager.createSubTask(thirdSubTask));

        System.out.println(inMemoryTaskManager.getTaskList());
        System.out.println(inMemoryTaskManager.getSubTaskList());
        System.out.println(inMemoryTaskManager.getEpicList());
        System.out.println("\n");
        ///////////////////////////////////////////////////////////

        //Обновление задач
        Task updSecondTask = new Task("Просто шуруп", "Купить шуруп", 2, TaskStatus.IN_PROGRESS);
        inMemoryTaskManager.updateTask(updSecondTask);

        SubTask updFirstSubTask = new SubTask("Материал для пола", "Покупка расходников для пола",
                5, TaskStatus.DONE, 4);
        inMemoryTaskManager.updateSubTask(updFirstSubTask);

        SubTask updSecondSubTask = new SubTask("Купить материал для потолка",
                "Покупка расходников для потолка", 7, TaskStatus.IN_PROGRESS, 4);
        inMemoryTaskManager.updateSubTask(updSecondSubTask);

        SubTask updThirdSubTask = new SubTask("Купить спойлер", "Купить спойлер белого цвета",         // ID = 8
                8, TaskStatus.IN_PROGRESS, 6);
        inMemoryTaskManager.updateSubTask(updThirdSubTask);

        System.out.println(inMemoryTaskManager.getTaskList());
        System.out.println(inMemoryTaskManager.getSubTaskList());
        System.out.println(inMemoryTaskManager.getEpicList());
        System.out.println("\n");
        ///////////////////////////////////////////////////////

        //Удаление задач
        inMemoryTaskManager.removeTaskByID(3);
        inMemoryTaskManager.removeSubTaskByID(7);
        inMemoryTaskManager.removeEpicByID(6);

        System.out.println(inMemoryTaskManager.getTaskList());
        System.out.println(inMemoryTaskManager.getSubTaskList());
        System.out.println(inMemoryTaskManager.getEpicList());
        System.out.println("\n");

        System.out.println("Tests of the 3rd sprint passed successfully");

        ///////////////////////////////////////////////////////
        // Запись в историю

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
