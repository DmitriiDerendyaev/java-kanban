package util;

import service.*;

import java.io.IOException;

public class Manager {
    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultFileTaskManager() throws IOException {
        return new FileBackedTasksManager("src/resources/memory.csv");
    }

}
