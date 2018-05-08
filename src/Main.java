import com.sun.net.httpserver.*;
import summarizer.Algo;
import summarizer.Sentence;
import summarizer.Summarizer;

import javax.net.ssl.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {

        try {
            HttpsServer server = HttpsServer.create(new InetSocketAddress(6789), 0);
            SSLContext sslContext = SSLContext.getInstance("TLS");

           // initialise the keystore
           char[] password = "summery".toCharArray();
           KeyStore ks = KeyStore.getInstance ( "JKS" );
           FileInputStream fis = new FileInputStream ( "key.jks" );
           ks.load ( fis, password );

           // setup the key manager factory
           KeyManagerFactory kmf = KeyManagerFactory.getInstance ( "SunX509" );
           kmf.init ( ks, password );

           // setup the trust manager factory
           TrustManagerFactory tmf = TrustManagerFactory.getInstance ( "SunX509" );
           tmf.init ( ks );

           // setup the HTTPS context and parameters
           sslContext.init ( kmf.getKeyManagers (), tmf.getTrustManagers (), null );
           server.setHttpsConfigurator ( new HttpsConfigurator( sslContext ) {
               public void configure ( HttpsParameters params ) {
                   try {
                       // initialise the SSL context
                       SSLContext c = SSLContext.getDefault ();
                       SSLEngine engine = c.createSSLEngine ();
                       params.setNeedClientAuth ( false );
                       params.setCipherSuites ( engine.getEnabledCipherSuites () );
                       params.setProtocols ( engine.getEnabledProtocols () );

                       // get the default parameters
                       SSLParameters defaultSSLParameters = c.getDefaultSSLParameters ();
                       params.setSSLParameters ( defaultSSLParameters );
                   }
                   catch ( Exception ex ) {
                       ex.printStackTrace();
                   }
               }
           } );
            server.createContext("/test", new MyHandler());
            server.setExecutor(Executors.newFixedThreadPool(50)); // creates a default executor
            server.start();
            System.out.println("Server is running.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // http://194.47.40.14:6789/test?p=userID&t=Love,+oneness,+is+no+separation+between+you+and+life.+It+is+a+progressive+letting+go,+a+progressive+not+fault+finding.&cookie=this_is_a_cookie
    static class MyHandler implements HttpHandler {
        private Connector connector = new Connector();
        @Override
        public void handle(HttpExchange t) throws IOException {


            String response;
            HttpsExchange exchange = (HttpsExchange) t;

            // Get the parameters
            Map<String, Object> parameters = new HashMap<>();
            URI requestedUri = exchange.getRequestURI();
            String query = requestedUri.getRawQuery();

            System.out.println(query);

            parseQuery(query, parameters);
            exchange.setAttribute("parameters", parameters);

            System.out.println(exchange.getRemoteAddress());

            String text = (String) parameters.get("t");
            text = text.replace("percent","%");
            if(!text.contains(".")){
                text+=".";
            }
            // Start the algorithm
            response = Algo.run(text);
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin","*");
            if (exchange.getRequestMethod().equalsIgnoreCase("options")){
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods","GET, OPTIONS");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers","Content-Type, Authorization");
            }
            exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
            // send the response
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

//            if (parameters.containsKey("cookie")&& parameters.containsKey("p")){
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        connector.addCookie((String) parameters.get("p"),exchange.getRemoteAddress().toString(),(String) parameters.get("cookie"));
//                    }
//                }).start();
//            }
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