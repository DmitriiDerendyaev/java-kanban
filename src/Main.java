import models.Epic;
import models.SubTask;
import models.Task;
import models.TaskStatus;
import service.FileBackedTasksManager;
import service.HistoryManager;
import util.Manager;
import service.TaskManager;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        TaskManager fileBackedTaskManager = new FileBackedTasksManager("src/resources/memory.csv");

        fileBackedTaskManager.createTask(new Task("Просто отвертка", "Купить отвертку", TaskStatus.NEW));
        fileBackedTaskManager.createEpic(new Epic("Купить дом", "Купить пентхаус в Казани"));
        fileBackedTaskManager.createSubTask(new SubTask("Материал для пола", "Покупка расходников для пола",
                TaskStatus.NEW, 2));
        fileBackedTaskManager.createSubTask(new SubTask("Купить спойлер", "Купить спойлер белого цвета",         // ID = 8
                TaskStatus.NEW, 2));

        System.out.println(fileBackedTaskManager.getHistory());


        fileBackedTaskManager.getTaskByID(1);
        fileBackedTaskManager.getEpicByID(2);
        fileBackedTaskManager.getSubTaskByID(3);
    }
}
