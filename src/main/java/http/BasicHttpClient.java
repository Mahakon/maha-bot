package http;

import java.io.IOException;
import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import com.fasterxml.jackson.databind.ObjectMapper;

import request.Request;

public class BasicHttpClient implements HttpClient  {

    private final CloseableHttpClient httpClient;
    private final ObjectMapper mapper;

    public BasicHttpClient() {
        httpClient = HttpClients.custom().
                setDefaultHeaders(Arrays.asList(new BasicHeader("Connection", "close"))).build();
        mapper = new ObjectMapper();
    }

    @Override
    public HttpResponse sendPost(Request req) throws IOException {
        HttpPost post = new HttpPost(req.getUrl());
        post.setEntity(new ByteArrayEntity(
                req.toString().getBytes(),
                ContentType.APPLICATION_JSON
        ));

        return httpClient.execute(post);
    }

    @Override
    public HttpResponse sendGet(Request req) {
        throw new UnsupportedOperationException("GET is not implemented");
    }

    @Override
    public HttpResponse sendPostTranslate(Request req) throws IOException {
        HttpPost post = new HttpPost("https://translate.yandex.net" + req.getUrl());
        post.addHeader(new BasicHeader("Host", "translate.yandex.net"));
        post.addHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
        post.addHeader(new BasicHeader("Accept", "*/*"));

        post.setEntity(new ByteArrayEntity(req.getData().getBytes()));
        return httpClient.execute(post);
    }
}
