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