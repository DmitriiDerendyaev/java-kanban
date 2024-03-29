package clients;

import com.sun.net.httpserver.HttpServer;
import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private final String baseURL;
    private String apiToken;
    private final HttpClient httpClient;
    public KVTaskClient(String baseURL) throws IOException, InterruptedException {
        this.baseURL = baseURL;
        this.httpClient = HttpClient.newHttpClient();
        this.apiToken = generateApiToken();
    }

    public void put(String key, String value) throws IOException, InterruptedException {
        String currentURI = baseURL + "/save/" + key + "?API_TOKEN=" + apiToken;

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(currentURI))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(value))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 200){
            System.out.println("Successful");
        }  else {
            throw new ManagerSaveException("Can't do request, status code: " + response.statusCode());
        }
    }

    public String load(String key) throws IOException, InterruptedException {
        String currentURI = baseURL + "/load/" + key + "?API_TOKEN=" + apiToken;

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(currentURI))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        try {
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new ManagerLoadException("Can't do request, status code: " + response.statusCode());
            }
        } finally {
            response.body();  // This ensures the response body is fully consumed and resources are released
//            response.close(); // Close the response to release the connection
        }
    }

    private String generateApiToken() throws IOException {
        String currentURI = baseURL + "/register";

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(currentURI))
                .GET()
                .build();

        HttpResponse<String> response = null;
        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Неудалось сделать запрос на генерацию токена" + e);
        }

        if(response.statusCode() == 200){
            return response.body();
        } else {
            throw new IOException("Ошибка генерации API_TOKEN");
        }
    }
}
