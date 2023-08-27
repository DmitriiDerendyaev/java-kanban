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
        System.out.println("Сервер запущен на порту: " + server.PORT);
    }

    class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            try {
                if (path.equals("/tasks/task") && method.equals("POST")) {
                    String json = readText(exchange);
                    Task newTask = gson.fromJson(json, Task.class);
                    taskManager.createTask(newTask);
                    System.out.println("Задача успешно добавлена!");
                    sendText(exchange, "{\"status\":\"OK\"}");

                } else if (path.equals("/tasks/subTask") && method.equals("POST")) {
                    String json = readText(exchange);
                    SubTask newSubTask = gson.fromJson(json, SubTask.class);
                    taskManager.createSubTask(newSubTask);
                    System.out.println("Подзадача успешно добавлена!");
                    sendText(exchange, "{\"status\":\"OK\"}");

                } else if (path.equals("/tasks/epic") && method.equals("POST")) {
                    String json = readText(exchange);
                    Epic newEpic = gson.fromJson(json, Epic.class);
                    taskManager.createEpic(newEpic);
                    System.out.println("Эпик успешно добавлена!");
                    sendText(exchange, "{\"status\":\"OK\"}");
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Возможно, стоит отправить ошибку клиенту
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
            byte[] resp = text.getBytes(UTF_8);
            h.getResponseHeaders().add("Content-Type", "application/json");
            h.sendResponseHeaders(200, resp.length);
            try (OutputStream responseBody = h.getResponseBody()) {
                responseBody.write(resp);
            }
        }
    }
}
