package service;

import org.junit.jupiter.api.AfterEach;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @AfterEach
    public void tearDown() {
        taskManager.clearEpics();
    }

    // Добавьте дополнительные тесты, специфичные для InMemoryTaskManager
}