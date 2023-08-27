# java-kanban
Repository for homework project.

## Функции
1. Возможность хранить задачи всех типов. Для этого вам нужно выбрать подходящую коллекцию.
2. Методы для каждого из типа задач(Задача/Эпик/Подзадача):
~~- Получение списка всех задач.~~
~~- Удаление всех задач.~~
~~- Получение по идентификатору.~~
~~- Создание. Сам объект должен передаваться в качестве параметра.~~
~~- Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.~~
~~- Удаление по идентификатору.~~
3. Дополнительные методы:

- ~~Получение списка всех подзадач определённого эпика.~~
4. Управление статусами осуществляется по следующему правилу:
- Менеджер сам не выбирает статус для задачи. Информация о нём приходит менеджеру вместе с информацией о самой задаче. По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.
- Для эпиков:
  - если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
  - если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
  - во всех остальных случаях статус должен быть IN_PROGRESS.

## Вывод программы
### Введенные данные
```java

[Task{taskName='Просто отвертка', taskDescription='Купить отвертку', taskID=1, taskStatus=NEW}
, Task{taskName='Просто шуруп', taskDescription='Купить шуруп', taskID=2, taskStatus=NEW}
, Task{taskName='Просто доска', taskDescription='Купить доску', taskID=3, taskStatus=NEW}
]
[SubTask{epicID=4, taskName='Материал для пола', taskDescription='Покупка расходников для пола', taskID=5, taskStatus=NEW}
, SubTask{epicID=4, taskName='Купить материал для потолка', taskDescription='Покупка расходников для потолка', taskID=7, taskStatus=NEW}
, SubTask{epicID=6, taskName='Купить спойлер', taskDescription='Купить спойлер белого цвета', taskID=8, taskStatus=NEW}
]
[Epic{taskCollection=[5, 7], taskName='Купить дом', taskDescription='Купить пентхаус в Казани', taskID=4, taskStatus=NEW}
, Epic{taskCollection=[8], taskName='Купить машину', taskDescription='Купить Porsche 911', taskID=6, taskStatus=NEW}
]
```

### После модификации данных
```java
[Task{taskName='Просто отвертка', taskDescription='Купить отвертку', taskID=1, taskStatus=NEW}
, Task{taskName='Просто шуруп', taskDescription='Купить шуруп', taskID=2, taskStatus=IN_PROGRESS}
, Task{taskName='Просто доска', taskDescription='Купить доску', taskID=3, taskStatus=NEW}
]
[SubTask{epicID=4, taskName='Материал для пола', taskDescription='Покупка расходников для пола', taskID=5, taskStatus=DONE}
, SubTask{epicID=4, taskName='Купить материал для потолка', taskDescription='Покупка расходников для потолка', taskID=7, taskStatus=IN_PROGRESS}
, SubTask{epicID=6, taskName='Купить спойлер', taskDescription='Купить спойлер белого цвета', taskID=8, taskStatus=IN_PROGRESS}
]
[Epic{taskCollection=[5, 7], taskName='Купить дом', taskDescription='Купить пентхаус в Казани', taskID=4, taskStatus=IN_PROGRESS}
, Epic{taskCollection=[8], taskName='Купить машину', taskDescription='Купить Porsche 911', taskID=6, taskStatus=IN_PROGRESS}
]
```
### После удаления данных
```java
[Task{taskName='Просто отвертка', taskDescription='Купить отвертку', taskID=1, taskStatus=NEW}
, Task{taskName='Просто шуруп', taskDescription='Купить шуруп', taskID=2, taskStatus=IN_PROGRESS}
]
[SubTask{epicID=4, taskName='Материал для пола', taskDescription='Покупка расходников для пола', taskID=5, taskStatus=DONE}
]
[Epic{taskCollection=[5], taskName='Купить дом', taskDescription='Купить пентхаус в Казани', taskID=4, taskStatus=DONE}
]


All test passed
```

## Спринт №5
- [x] Добавить в интерфейс HistoryManager `void remove(int id)`

## Спринт №7
- [x] Добавить продолжительность и дату старта
  - Duration, ZonedDateTime
  - Изменить конструкторы
  - Добавить getter'ы и setter'ы
  - Добавить подсчет у Epics
  - Добавить сериализацию
  - Добавить десериализацию
- [x] Вывод задач в порядке приоритета
- [x] Проверить пересечения задач
- [ ] Создать автотесты
    1. [x] Для расчёта статуса Epic. Граничные условия:
        1. Пустой список подзадач.+
        2. Все подзадачи со статусом NEW.+
        3. Все подзадачи со статусом DONE.+
        4. Подзадачи со статусами NEW и DONE.+
        5. Подзадачи со статусом IN_PROGRESS.+
    2. [ ] Для двух менеджеров задач `InMemoryTasksManager` и `FileBackedTasksManager`.
        1. +Чтобы избежать дублирования кода, необходим базовый класс с тестами на каждый метод из интерфейса abstract class TaskManagerTest<T extends TaskManager>.
        2. +Для подзадач нужно дополнительно проверить наличие эпика, а для эпика — расчёт статуса.
        3. +Для каждого метода нужно проверить его работу:
            1. +Со стандартным поведением.
            2. +С пустым списком задач.
            3. +С неверным идентификатором задачи (пустой и/или несуществующий идентификатор).
    3. [x] Для `HistoryManager` — тесты для всех методов интерфейса. Граничные условия:
        1. +Пустая история задач.
        2. +Дублирование.
        3. +Удаление из истории: начало, середина, конец.
    4. [x] Дополнительно для FileBackedTasksManager — проверка работы по сохранению и восстановлению состояния. Граничные условия:
        1. +Пустой список задач.
        2. +Эпик без подзадач.
        3. +Пустой список истории.

## Спринт №8
- [x] Реализовать KVServer
  - реализовать метод load
  - перенести его в основной клиент
  - написать под него тесты
- [ ] Проработать логику API
  - Добавление библиотеки GSON
  - Создание класса `HttpTaskServer` в пакете Server
  - Добавление `FileBackedTaskManager` 
  - Реализация мапинга по эндпоинтам для методов интерфейса `TaskManager`
  - [x] GET
  - +GET /tasks/task - getTaskList() ???
  - +GET /tasks/task/?id= - getTaskById(int id)
  - +GET /tasks/epic - getEpicList()
  - +GET /tasks/epic/?id= - getEpicById(int id)
  - +GET /tasks/epic/subTasks/?id= getSubTaskListByEpicID(int id)
  - +GET /tasks/subTask - getSubTaskList()
  - +GET /tasks/subTask/?id= - getSubTaskByID(int id)
  - +GET /tasks/history - getHistory()
  - +GET /tasks/ - getPrioritizedTask()
  - [x] POST
  - +POST /tasks/task/ Body: {task...} createTask(Task newTask) || updateTask(Task new Task)
  - +POST /tasks/subTask/ Body: {subTask...} createSubTask(...) || updateSubTask(...)
  - +POST /tasks/epic/ Body: {epic...} createEpic(...) || updateEpic
  - [ ] DELETE
  - DELETE /tasks/task/?id= - removeTaskById(...)
  - DELETE /tasks/task - clearTasks()
  - DELETE /tasks/subTask/?id= - removeSubTaskById(...)
  - DELETE /tasks/subTask - clearSubTask()
  - DELETE /tasks/epic/?id= - removeEpicById(...)
  - DELETE /tasks/epic - clearEpic()
  - 
- [ ] 
- [ ] 
- [ ] 
- [ ] 