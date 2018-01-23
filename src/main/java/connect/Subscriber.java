package connect;

import java.io.IOException;
import org.apache.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import http.BasicHttpClient;
import http.HttpClient;
import request.Request;

public class Subscriber {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void Subscribe(Request req) {
        HttpClient client = new BasicHttpClient();

        try {
            HttpResponse resp = client.sendPost(req);
            SubscribeResponse sr =  mapper.readValue(resp.getEntity().getContent(), SubscribeResponse.class);

            if (!"true".equalsIgnoreCase(sr.getSuccess())) {
                // log it
                System.exit(-1);
            }

            System.out.println("Connected");

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
