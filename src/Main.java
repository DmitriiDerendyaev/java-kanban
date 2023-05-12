import models.Epic;
import models.SubTask;
import models.Task;
import models.TaskStatus;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        //Основной блок теста по ТЗ #1
        ///////////////////////////////////////////////////////
        Task firstTask = new Task("Просто отвертка", "Купить отвертку", TaskStatus.NEW);            // ID = 1
        Task secondTask = new Task("Просто шуруп", "Купить шуруп", TaskStatus.NEW);         // ID = 2
        Task thirdTask = new Task("Просто доска", "Купить доску", TaskStatus.NEW);                  // ID = 3
        System.out.println("Добавление задачи: ID = " + taskManager.createTask(firstTask));
        System.out.println("Добавление задачи: ID = " + taskManager.createTask(secondTask));
        System.out.println("Добавление задачи: ID = " + taskManager.createTask(thirdTask));

        Epic firstEpic = new Epic("Купить дом", "Купить пентхаус в Казани", TaskStatus.NEW);        // ID = 4
        System.out.println("Добавление эпика: ID = " + taskManager.createEpic(firstEpic));

        SubTask firstSubTask = new SubTask("Материал для пола", "Покупка расходников для пола",
                TaskStatus.NEW, 4);                                                                                // ID = 5
        System.out.println("Добавление подзадачи: ID = " + taskManager.createSubTask(firstSubTask));

        Epic secondEpic = new Epic("Купить машину", "Купить Porsche 911", TaskStatus.NEW);          // ID = 6
        System.out.println("Добавление эпика: ID = " + taskManager.createEpic(secondEpic));

        SubTask secondSubTask = new SubTask("Купить материал для потолка",
                "Покупка расходников для потолка", TaskStatus.NEW, 4);                                // ID = 7
        System.out.println("Добавление подзадачи: ID = " + taskManager.createSubTask(secondSubTask));

        SubTask thirdSubTask = new SubTask("Купить спойлер", "Купить спойлер белого цвета",         // ID = 8
                TaskStatus.NEW, 6);
        System.out.println("Добавление подзадачи: ID = " + taskManager.createSubTask(thirdSubTask));

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getSubTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println("\n");
        ///////////////////////////////////////////////////////////

        //Обновление задач
        Task updSecondTask = new Task("Просто шуруп", "Купить шуруп", 2, TaskStatus.IN_PROGRESS);
        taskManager.updateTask(updSecondTask);

        SubTask updFirstSubTask = new SubTask("Материал для пола", "Покупка расходников для пола",
                5, TaskStatus.DONE, 4);
        taskManager.updateSubTask(updFirstSubTask);

        SubTask updSecondSubTask = new SubTask("Купить материал для потолка",
                "Покупка расходников для потолка", 7, TaskStatus.IN_PROGRESS, 4);
        taskManager.updateSubTask(updSecondSubTask);

        SubTask updThirdSubTask = new SubTask("Купить спойлер", "Купить спойлер белого цвета",         // ID = 8
                8, TaskStatus.IN_PROGRESS, 6);
        taskManager.updateSubTask(updThirdSubTask);

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getSubTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println("\n");
        ///////////////////////////////////////////////////////

        //Удаление задач
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
