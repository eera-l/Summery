import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(6789), 0);
            server.createContext("/test", new MyHandler());
            server.setExecutor(Executors.newFixedThreadPool(50)); // creates a default executor
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // http://localhost:6789/test?p=hello&t=Love,+oneness,+is+no+separation+between+you+and+life.+It+is+a+progressive+letting+go,+a+progressive+not+fault+finding.
    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response;

            // Get the parameters
            Map<String, Object> parameters = new HashMap<>();
            URI requestedUri = t.getRequestURI();
            String query = requestedUri.getRawQuery();
            parseQuery(query, parameters);
            t.setAttribute("parameters", parameters);


            //response +=parameters.get("p");
            String text = (String) parameters.get("t");

            // Start the algorithm
            Algo algo = new Algo(text);
            Thread thread = new Thread(algo);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            response = algo.getText();
            // send the response
            t.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private static void parseQuery(String query, Map<String, Object> parameters)
            throws UnsupportedEncodingException {

        if (query != null) {
            String pairs[] = query.split("[&]");

            for (String pair : pairs) {
                String param[] = pair.split("[=]");

                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0],
                            System.getProperty("file.encoding"));
                }

                if (param.length > 1) {
                    value = URLDecoder.decode(param[1],
                            System.getProperty("file.encoding"));
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if(obj instanceof List<?>) {
                        List<String> values = (List<String>)obj;
                        values.add(value);
                    } else if(obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String)obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }

}