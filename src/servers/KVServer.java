package servers;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

/**
 * �������: <a href="https://www.getpostman.com/collections/a83b61d9e1c81c10575c">...</a>
 */
public class KVServer {
    public static final int PORT = 8070;
    private final String apiToken;
    private final HttpServer server;
    private final Map<String, String> data = new HashMap<>();

    public KVServer() throws IOException {
        apiToken = generateApiToken();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/register", this::register);
        server.createContext("/save", this::save);
        server.createContext("/load", this::load);
    }


    private void save(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/save");
            if (!hasAuth(h)) {
                System.out.println("������ �������������, ����� �������� � query API_TOKEN �� ��������� ���-�����");
                h.sendResponseHeaders(403, 0);
                return;
            }
            if ("POST".equals(h.getRequestMethod())) {
                String key = h.getRequestURI().getPath().substring("/save/".length());
                if (key.isEmpty()) {
                    System.out.println("Key ��� ���������� ������. key ����������� � ����: /save/{key}");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                String value = readText(h);
                if (value.isEmpty()) {
                    System.out.println("Value ��� ���������� ������. value ����������� � ���� �������");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                data.put(key, value);
                System.out.println("�������� ��� ����� " + key + " ������� ���������!");
                h.sendResponseHeaders(200, 0);
            } else {
                System.out.println("/save ��� POST-������, � �������: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    private void load(HttpExchange exchange) throws IOException {
        try{
            System.out.println("\n/load");
            if(!hasAuth(exchange)){
                System.out.println("��� ���������� ��������������, ����������� API_TOKEN");
                exchange.sendResponseHeaders(403, 0);
            }

            if(!"GET".equals(exchange.getRequestMethod())){
                System.out.println("�������� ����� GET, � �����������" + exchange.getRequestMethod());
                exchange.sendResponseHeaders(405, 0);
            } else {
                String key = exchange.getRequestURI().getPath().substring("/load/".length());

                if(key.isEmpty()){
                    System.out.println("���� ��� ��������� ����, /save/API_TOKEN=???");
                    exchange.sendResponseHeaders(400, 0);
                    return;
                }
                String responseAnswer = "[]";

                if(data.containsKey(key)){
                    responseAnswer = data.get(key);
                }

                System.out.println(String.format("���������� � �������: %s", responseAnswer));
                sendText(exchange, responseAnswer);
            }
        } finally {
            exchange.close();
        }
    }

    private void register(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/register");
            if ("GET".equals(h.getRequestMethod())) {
                sendText(h, apiToken);
            } else {
                System.out.println("/register ��� GET-������, � ������� " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    public void start() {
        System.out.println("��������� ������ �� ����� " + PORT);
        System.out.println("������ � �������� http://localhost:" + PORT + "/");
        System.out.println("API_TOKEN: " + apiToken);
        server.start();
    }

    private String generateApiToken() {
        return "" + System.currentTimeMillis();
    }

    protected boolean hasAuth(HttpExchange h) {
        String rawQuery = h.getRequestURI().getRawQuery();
        return rawQuery != null && (rawQuery.contains("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();

        kvServer.start();
    }
}
