package util;

import service.*;

import java.io.IOException;

public class Manager {

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

    public static HttpTaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager("http://localhost:8070");
    }
}
