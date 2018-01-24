import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Executors;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import connect.Subscriber;
import http.BasicHttpClient;
import request.BotAnswerRequest;
import request.SubscribeRequest;
import request.TranslateRequest;
import response.MessageNotification;
import response.TranslateWords;

public class TranslatorBot {

    private final static ObjectMapper MAPPER = new ObjectMapper();

    private final HttpServer httpServer;
    private final BasicHttpClient httpClient;
    private String endpoint;
    private String sendAnswerUrl;
    private String token;
    private String ya_token;

    {
        Properties props = new Properties();
        props.load(TranslatorBot.class.getResourceAsStream(
                "/app.properties"));
        endpoint = props.getProperty("app_endpoint");
        sendAnswerUrl = props.getProperty("send_message");
        token = props.getProperty("token");
        ya_token = props.getProperty("yandex_token");

        SubscribeRequest req = new SubscribeRequest(
                props.getProperty("subscribeUrl"),
                props.getProperty("token"),
                props.getProperty("app_url")
        );
        Subscriber.Subscribe(req);
    }

    public TranslatorBot() throws IOException, InterruptedException {
        InetSocketAddress addr = new InetSocketAddress(
                InetAddress.getByName("0.0.0.0"), 8080);

        httpClient = new BasicHttpClient();
        httpServer = HttpServer.create(addr, 0);
        httpServer.setExecutor(Executors.newFixedThreadPool(4));
        httpServer.createContext(endpoint, exchange -> {
            try {
                MessageNotification notif = null;
                try (InputStream is = exchange.getRequestBody()) {
                    notif = MAPPER.readValue(is, MessageNotification.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                TranslateRequest translate = new TranslateRequest(
                        ya_token, notif.getMessage().getText()
                );

                TranslateWords tw = MAPPER.readValue(
                        httpClient.sendPostTranslate(translate).getEntity().getContent(),
                        TranslateWords.class
                );

                BotAnswerRequest answer = new BotAnswerRequest(
                        notif.getRecipient().getChatId(),
                        tw.getText().toString(),
                        sendAnswerUrl,
                        token);

                HttpPost post = new HttpPost(answer.getUrl());
                post.setEntity(new ByteArrayEntity(
                        answer.toString().getBytes(),
                        ContentType.APPLICATION_JSON
                ));
                CloseableHttpClient httpClient2 = HttpClients.custom().
                        setDefaultHeaders(Arrays.asList(new BasicHeader("Connection", "close"))).build();
                httpClient2.execute(post);

                writeResponse(exchange, 200, "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        httpServer.start();
    }

    public static void main(String[] args) {
        try {
            new TranslatorBot();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeResponse(HttpExchange exchange, int code, String content) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
        byte[] response = content.getBytes();
        exchange.sendResponseHeaders(code, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

}
