package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import deserializer.EpicAdapter;
import deserializer.SubTaskAdapter;
import deserializer.TaskAdapter;
import models.Epic;
import models.SubTask;
import models.Task;
import models.TaskStatus;
import org.junit.jupiter.api.*;
import servers.HttpTaskServer;
import servers.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {

    private final String baseUrl = "http://localhost:8080";
    private HttpClient httpClient;

    static KVServer kvServer;

    private static Gson gson;

    @BeforeAll
    public static void keyValueServerStart(){
        try {
            kvServer = new KVServer();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка создания KVServer" + e);
        }
        kvServer.start();

        try {
            HttpTaskServer httpTaskServer = new HttpTaskServer();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ошибка запуска сервера из теста" + e);
        }
        gson = new Gson()
                .newBuilder()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
                .create();
    }

    @AfterAll
    public static void keyValueServerStop(){
        kvServer.stop();
    }


    @BeforeEach
    void setUp() {
        httpClient = HttpClient.newHttpClient();
    }

    @AfterEach
    void tearDown(){
        kvServer.clearStorage();
    }
    private HttpResponse<String> doPostRequest(String uri, String jsonField){
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + uri))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonField))
                .build();

        HttpResponse<String> response = null;
        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException("Ошибка отправки POST-запроса " + e);
        }

        return response;
    }

    private HttpResponse<String> doGetRequest(String uri){
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + uri))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = null;
        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException("Ошибка отправки GET-запроса " + e);
        }

        return response;
    }

    @Test
    public void testCreateTask() throws IOException {
        String taskJson = "{\"taskName\":\"Просто отвертка НО ЕЩЕ ОДНА\",\"taskDescription\":\"Купить отвертку\",\"taskStatus\":\"NEW\",\"startTime\":\"2023-07-25T13:00:00+04:00[Europe/Samara]\",\"duration\":5400000}";

        HttpResponse<String> postResponse = doPostRequest("/tasks/task", taskJson);

        assertEquals(200, postResponse.statusCode());

        String responseBody = postResponse.body();
        assertEquals("{\"status\":\"OK\"}", responseBody);

        String taskFromServer = doGetRequest("/tasks/task?id=1").body();

        Task createdTask = gson.fromJson(taskFromServer, Task.class);

        assertEquals("Просто отвертка НО ЕЩЕ ОДНА", createdTask.getTaskName());
        assertEquals("Купить отвертку", createdTask.getTaskDescription());
        assertEquals(TaskStatus.NEW, createdTask.getTaskStatus());

        ZonedDateTime expectedStartTime = ZonedDateTime.parse("2023-07-25T13:00:00+04:00[Europe/Samara]");
        assertEquals(expectedStartTime, createdTask.getStartTime());
    }

    @Test
    public void testCreateSubTask(){
        String epicJSON = "{\"taskName\":\"Купить дом\",\"taskDescription\":\"Купить пентхаус в Казани\",\"taskStatus\":\"NEW\",\"startTime\":\"2023-08-25T14:30:00+04:00[Europe/Samara]\",\"duration\":10800000,\"subTasksList\":[]}";
        String subTaskJSON = "{\"epicID\":1,\"taskName\":\"Материал для пола\",\"taskDescription\":\"Покупка расходников для пола\",\"taskStatus\":\"NEW\",\"startTime\":\"2023-08-25T14:30:00+04:00[Europe/Samara]\",\"duration\":1000}\n";

        HttpResponse<String> httpResponse = doPostRequest("/tasks/epic", epicJSON);

        HttpResponse<String> httpResponseSubTask = doPostRequest("/tasks/subTask", subTaskJSON);

        assertEquals(200, httpResponse.statusCode());
        assertEquals(200, httpResponseSubTask.statusCode());

        String responseBody = httpResponseSubTask.body();
        assertEquals("{\"status\":\"OK\"}", responseBody);

        SubTask createdSubTask = gson.fromJson(subTaskJSON, SubTask.class);

        assertEquals("Материал для пола", createdSubTask.getTaskName());
        assertEquals("Покупка расходников для пола", createdSubTask.getTaskDescription());
        assertEquals(TaskStatus.NEW, createdSubTask.getTaskStatus());

    }

    @Test
    public void testCreateEpic(){
        String taskJson = "{\"taskName\":\"Купить дом\",\"taskDescription\":\"Купить пентхаус в Казани\",\"taskStatus\":\"NEW\",\"startTime\":\"2023-08-25T14:30:00+04:00[Europe/Samara]\",\"duration\":10800000,\"subTasksList\":[]}";

        HttpResponse<String> httpResponse = doPostRequest("/tasks/epic", taskJson);

        assertEquals(200, httpResponse.statusCode());
        String responseBody = httpResponse.body();

        assertEquals("{\"status\":\"OK\"}", responseBody);

        Epic createdEpic = gson.fromJson(taskJson, Epic.class);

        assertEquals("Купить дом", createdEpic.getTaskName());
        assertEquals("Купить пентхаус в Казани", createdEpic.getTaskDescription());
        assertEquals(TaskStatus.NEW, createdEpic.getTaskStatus());

    }




}

