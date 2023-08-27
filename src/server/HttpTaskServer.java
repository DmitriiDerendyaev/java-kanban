package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import deserializer.EpicAdapter;
import deserializer.SubTaskAdapter;
import deserializer.TaskAdapter;
import models.Epic;
import models.SubTask;
import models.Task;
import service.TaskManager;
import util.Manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Optional;


import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private final int PORT = 8080;
    private final String HOST = "localhost";
    private TaskManager taskManager;
    private final HttpServer httpServer;
    private Gson gson;

    public HttpTaskServer() throws IOException {
        gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .create();
        taskManager = Manager.getDefaultFileTaskManager();
        httpServer = HttpServer.create(new InetSocketAddress(HOST, PORT), 0);

        httpServer.createContext("/tasks", new TaskHandler());

        httpServer.start();
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer();
        System.out.println("������ ������� �� �����: " + server.PORT);
    }

    class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            try {
                if(method.equals("POST")){
                    if (path.equals("/tasks/task")) {
                        String json = readText(exchange);
                        Task newTask = gson.fromJson(json, Task.class);
                        taskManager.createTask(newTask);
                        System.out.println("������ ������� ���������!");
                        sendText(exchange, "{\"status\":\"OK\"}");

                    } else if (path.equals("/tasks/subTask")) {
                        String json = readText(exchange);
                        SubTask newSubTask = gson.fromJson(json, SubTask.class);
                        taskManager.createSubTask(newSubTask);
                        System.out.println("��������� ������� ���������!");
                        sendText(exchange, "{\"status\":\"OK\"}");

                    } else if (path.equals("/tasks/epic")) {
                        String json = readText(exchange);
                        Epic newEpic = gson.fromJson(json, Epic.class);
                        taskManager.createEpic(newEpic);
                        System.out.println("���� ������� ��������!");
                        sendText(exchange, "{\"status\":\"OK\"}");
                    }
                } else if (method.equals("GET")) {
                    Optional<String> query = Optional.ofNullable(exchange.getRequestURI().getQuery());
                    Boolean isQuery = query.map(q -> q.contains("id")).orElse(false);
                    if (isQuery) {
                        int id = Integer.parseInt(exchange.getRequestURI().getQuery().substring("id=".length()));

                        if (path.contains("/tasks/task")) {
                            Task task = taskManager.getTaskByID(id);
                            sendText(exchange, gson.toJson(task));
                        } else if (path.contains("/tasks/subTask")) {
                            SubTask subTask = taskManager.getSubTaskByID(id);
                            sendText(exchange, gson.toJson(subTask));
                        } else if (path.contains("/tasks/epic")) {
                            Epic epic = taskManager.getEpicByID(id);
                            sendText(exchange, gson.toJson(epic));
                        } else if (path.contains("/tasks/epic/subTasks")) {
                            List<SubTask> subTasks = taskManager.getSubTaskListByEpicID(id);
                            sendText(exchange, gson.toJson(subTasks));
                        }
                    } else {
                        if (path.equals("/tasks/task")) {
                            System.out.println("������ ����� ��������� �� ������!");
                            sendText(exchange, gson.toJson(taskManager.getTaskList()));
                        } else if (path.equals("/tasks/subTask")) {
                            System.out.println("������ �������� ��������� �� ������!");
                            sendText(exchange, gson.toJson(taskManager.getSubTaskList()));
                        } else if (path.equals("/tasks/epic")) {
                            System.out.println("������ ������ ��������� �� ������!");
                            sendText(exchange, gson.toJson(taskManager.getEpicList()));
                        } else if (path.equals("/tasks")) {
                            System.out.println("������ ���������� �� ������!");
                            sendText(exchange, gson.toJson(taskManager.getPrioritizedTask()));
                        } else if (path.equals("/tasks/history")) {
                            System.out.println("������� ���������� �� ������!");
                            sendText(exchange, gson.toJson(taskManager.getHistory()));
                        }
                    }
                } else if (method.equals("DELETE")) {

                } else {
                    throw new IllegalStateException("����������� �����");
                }
            } catch (Exception e) {
                e.printStackTrace();
                // ��������, ����� ��������� ������ �������
            }
        }



//
//    if(exchange.getRequestURI().getQuery() != null){
//        String query = exchange.getRequestURI().getQuery();
//        if(query.startsWith("id=")){
//            int
//        }
//
//    }

        protected String readText(HttpExchange h) throws IOException {
            try (InputStream requestBody = h.getRequestBody()) {
                return new String(h.getRequestBody().readAllBytes(), UTF_8);
            }
        }

        protected void sendText(HttpExchange h, String text) throws IOException {
            if(text.equals("[]") || text.equals("{}"))
                text = "������ ������";
            byte[] resp = text.getBytes(UTF_8);
            h.getResponseHeaders().add("Content-Type", "application/json");
            h.sendResponseHeaders(200, resp.length);
            try (OutputStream responseBody = h.getResponseBody()) {
                responseBody.write(resp);
            }
        }
    }
}
