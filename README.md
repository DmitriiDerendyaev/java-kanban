# java-kanban
Repository for homework project.

## �������
1. ����������� ������� ������ ���� �����. ��� ����� ��� ����� ������� ���������� ���������.
2. ������ ��� ������� �� ���� �����(������/����/���������):
~~- ��������� ������ ���� �����.~~
~~- �������� ���� �����.~~
~~- ��������� �� ��������������.~~
~~- ��������. ��� ������ ������ ������������ � �������� ���������.~~
~~- ����������. ����� ������ ������� � ������ ��������������� ��������� � ���� ���������.~~
~~- �������� �� ��������������.~~
3. �������������� ������:

- ~~��������� ������ ���� �������� ������������ �����.~~
4. ���������� ��������� �������������� �� ���������� �������:
- �������� ��� �� �������� ������ ��� ������. ���������� � ��� �������� ��������� ������ � ����������� � ����� ������. �� ���� ������ � ����� ������� �� ����� ��������� ������, � ������ ����� ������������.
- ��� ������:
  - ���� � ����� ��� �������� ��� ��� ��� ����� ������ NEW, �� ������ ������ ���� NEW.
  - ���� ��� ��������� ����� ������ DONE, �� � ���� ��������� ����������� � �� �������� DONE.
  - �� ���� ��������� ������� ������ ������ ���� IN_PROGRESS.

## ����� ���������
### ��������� ������
```java

[Task{taskName='������ ��������', taskDescription='������ ��������', taskID=1, taskStatus=NEW}
, Task{taskName='������ �����', taskDescription='������ �����', taskID=2, taskStatus=NEW}
, Task{taskName='������ �����', taskDescription='������ �����', taskID=3, taskStatus=NEW}
]
[SubTask{epicID=4, taskName='�������� ��� ����', taskDescription='������� ����������� ��� ����', taskID=5, taskStatus=NEW}
, SubTask{epicID=4, taskName='������ �������� ��� �������', taskDescription='������� ����������� ��� �������', taskID=7, taskStatus=NEW}
, SubTask{epicID=6, taskName='������ �������', taskDescription='������ ������� ������ �����', taskID=8, taskStatus=NEW}
]
[Epic{taskCollection=[5, 7], taskName='������ ���', taskDescription='������ �������� � ������', taskID=4, taskStatus=NEW}
, Epic{taskCollection=[8], taskName='������ ������', taskDescription='������ Porsche 911', taskID=6, taskStatus=NEW}
]
```

### ����� ����������� ������
```java
[Task{taskName='������ ��������', taskDescription='������ ��������', taskID=1, taskStatus=NEW}
, Task{taskName='������ �����', taskDescription='������ �����', taskID=2, taskStatus=IN_PROGRESS}
, Task{taskName='������ �����', taskDescription='������ �����', taskID=3, taskStatus=NEW}
]
[SubTask{epicID=4, taskName='�������� ��� ����', taskDescription='������� ����������� ��� ����', taskID=5, taskStatus=DONE}
, SubTask{epicID=4, taskName='������ �������� ��� �������', taskDescription='������� ����������� ��� �������', taskID=7, taskStatus=IN_PROGRESS}
, SubTask{epicID=6, taskName='������ �������', taskDescription='������ ������� ������ �����', taskID=8, taskStatus=IN_PROGRESS}
]
[Epic{taskCollection=[5, 7], taskName='������ ���', taskDescription='������ �������� � ������', taskID=4, taskStatus=IN_PROGRESS}
, Epic{taskCollection=[8], taskName='������ ������', taskDescription='������ Porsche 911', taskID=6, taskStatus=IN_PROGRESS}
]
```
### ����� �������� ������
```java
[Task{taskName='������ ��������', taskDescription='������ ��������', taskID=1, taskStatus=NEW}
, Task{taskName='������ �����', taskDescription='������ �����', taskID=2, taskStatus=IN_PROGRESS}
]
[SubTask{epicID=4, taskName='�������� ��� ����', taskDescription='������� ����������� ��� ����', taskID=5, taskStatus=DONE}
]
[Epic{taskCollection=[5], taskName='������ ���', taskDescription='������ �������� � ������', taskID=4, taskStatus=DONE}
]


All test passed
```

## ������ �5
- [ ] �������� � ��������� HistoryManager `void remove(int id)`
- [ ] 