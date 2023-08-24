package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import deserializer.TaskAdapter;
import models.Task;
import models.TaskStatus;
import service.TaskManager;
import util.Manager;

import java.io.IOException;
import java.lang.reflect.GenericSignatureFormatError;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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

            if(path.equals("/tasks/task") && method.equals("POST")){
                String json = readText(exchange);
                Task newTask = null;
                try {
                    newTask = gson.fromJson(json, Task.class);
                } catch (Exception e){
                    e.printStackTrace();
                }

                taskManager.createTask(newTask);
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
            return new String(h.getRequestBody().readAllBytes(), UTF_8);
        }
    }
}
