package servers;

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
    private HttpServer httpServer;
    private Gson gson;

    public HttpTaskServer() throws IOException, InterruptedException {
        gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .create();
        taskManager = Manager.getDefault();
        httpServer = HttpServer.create(new InetSocketAddress(HOST, PORT), 0);

        httpServer.createContext("/tasks", new TaskHandler());

        httpServer.start();
    }

    public void start() {
        gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .create();
        try {
            taskManager = Manager.getDefault();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Не удалось получить taskManager" + e);
        }
        try {
            httpServer = HttpServer.create(new InetSocketAddress(HOST, PORT), 0);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось запустить сервер через start" + e);
        }

        httpServer.createContext("/tasks", new TaskHandler());

        httpServer.start();
    }

    public void stop(){
        httpServer.stop(1);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpTaskServer server = new HttpTaskServer();
        System.out.println("Сервер запущен на порту: " + server.PORT);
    }

    class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            try {
                switch (method) {
                    case "POST":
                        switch (path) {
                            case "/tasks/task": {
                                String json = readText(exchange);
                                Task newTask = gson.fromJson(json, Task.class);
                                taskManager.createTask(newTask);
                                System.out.println("Задача успешно добавлена!");
                                sendText(exchange, "{\"status\":\"OK\"}");

                                break;
                            }
                            case "/tasks/subTask": {
                                String json = readText(exchange);
                                SubTask newSubTask = gson.fromJson(json, SubTask.class);
                                taskManager.createSubTask(newSubTask);
                                System.out.println("Подзадача успешно добавлена!");
                                sendText(exchange, "{\"status\":\"OK\"}");

                                break;
                            }
                            case "/tasks/epic": {
                                String json = readText(exchange);
                                Epic newEpic = gson.fromJson(json, Epic.class);
                                taskManager.createEpic(newEpic);
                                System.out.println("Эпик успешно добавлен!");
                                sendText(exchange, "{\"status\":\"OK\"}");
                                break;
                            }
                        }
                        break;
                    case "GET":
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
                            switch (path) {
                                case "/tasks/task":
                                    System.out.println("Список задач отправлен на клиент!");
                                    sendText(exchange, gson.toJson(taskManager.getTaskList()));
                                    break;
                                case "/tasks/subTask":
                                    System.out.println("Список подзадач отправлен на клиент!");
                                    sendText(exchange, gson.toJson(taskManager.getSubTaskList()));
                                    break;
                                case "/tasks/epic":
                                    System.out.println("Список эпиков отправлен на клиент!");
                                    sendText(exchange, gson.toJson(taskManager.getEpicList()));
                                    break;
                                case "/tasks":
                                    System.out.println("Задачи отправлены на клиент!");
                                    sendText(exchange, gson.toJson(taskManager.getPrioritizedTask()));
                                    break;
                                case "/tasks/history":
                                    System.out.println("История отправлена на клиент!");
                                    sendText(exchange, gson.toJson(taskManager.getHistory()));
                                    break;
                            }
                        }
                        break;
                    case "DELETE":
                        Optional<String> queryDelete = Optional.ofNullable(exchange.getRequestURI().getQuery());
                        Boolean isQueryDelete = queryDelete.map(q -> q.contains("id")).orElse(false);

                        if (isQueryDelete) {
                            int id = Integer.parseInt(queryDelete.get().substring(queryDelete.get().indexOf('=') + 1));

                            if (path.equals("/tasks/task/")) {
                                taskManager.removeTaskByID(id);
                                sendText(exchange, "{\"status\":\"Task removed\"}");
                            } else if (path.equals("/tasks/subTask/")) {
                                taskManager.removeSubTaskByID(id);
                                sendText(exchange, "{\"status\":\"SubTask removed\"}");
                            } else if (path.equals("/tasks/epic/")) {
                                taskManager.removeEpicByID(id);
                                sendText(exchange, "{\"status\":\"Epic removed\"}");
                            }
                        } else {
                            if (path.equals("/tasks/task")) {
                                taskManager.clearTasks();
                                sendText(exchange, "{\"status\":\"All tasks cleared\"}");
                            } else if (path.equals("/tasks/subTask")) {
                                taskManager.clearSubTasks();
                                sendText(exchange, "{\"status\":\"All subtasks cleared\"}");
                            } else if (path.equals("/tasks/epic")) {
                                taskManager.clearEpics();
                                sendText(exchange, "{\"status\":\"All epics cleared\"}");
                            }
                        }

                        break;

                    default:
                        throw new IllegalStateException("Неизвестный метод");
                }
            } catch (Exception e) {
                System.err.println("Ошибка обработки запроса. Path: " + path + "Method: " + method + ' ' + e);
            }
        }

        protected String readText(HttpExchange h) throws IOException {
            try (InputStream requestBody = h.getRequestBody()) {
                return new String(h.getRequestBody().readAllBytes(), UTF_8);
            }
        }

        protected void sendText(HttpExchange h, String text) throws IOException {
            if(text.equals("[]") || text.equals("{}"))
                text = "Пустой объект";
            byte[] resp = text.getBytes(UTF_8);
            h.getResponseHeaders().add("Content-Type", "application/json");
            h.sendResponseHeaders(200, resp.length);
            try (OutputStream responseBody = h.getResponseBody()) {
                responseBody.write(resp);
            }
        }
    }
}
