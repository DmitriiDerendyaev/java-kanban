package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class TaskHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if(path.equals("/tasks/task") && method.equals("POST")){

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
}
