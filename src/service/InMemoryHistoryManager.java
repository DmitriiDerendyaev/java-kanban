package service;

import models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private static class Node {
        private final Task task;
        private Node prev;
        private Node next;

        public Node(Task task) {
            this.task = task;
        }
    }

    private Node head;
    private Node tail;
    private final HashMap<Integer, Node> nodeMap = new HashMap<>();

    @Override
    public void remove(int id) {
        Node nodeToRemove = nodeMap.get(id);
        if (nodeToRemove != null) {
            removeNode(nodeToRemove);
            nodeMap.remove(id);
        }
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void linkLast(Node node) {
        if (tail == null) {
            // First node being added
            head = node;
        } else {
            node.prev = tail;
            tail.next = node;
        }
        tail = node;
    }

    private void removeNode(Node node) {
        Node prev = node.prev;
        Node next = node.next;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }
    }


    @Override
    public void add(Task task) {
        int id = task.getTaskID();

        Node existingNode = nodeMap.get(id);
        if (existingNode != null) {
            removeNode(existingNode);
        }

        Node newNode = new Node(task);
        nodeMap.put(id, newNode);

        linkLast(newNode);
    }

}
